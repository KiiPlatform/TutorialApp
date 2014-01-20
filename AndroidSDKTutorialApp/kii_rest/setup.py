
import ConfigParser
import logging
import httplib
import json
import time

CONFIG_FILE = 'setting.ini'
# set to DEBUG to see details
LOG_LEVEL = logging.INFO

def getLogger():
    logger = logging.getLogger('info')
    ch = logging.StreamHandler();
    ch.setLevel(LOG_LEVEL)
    logger.addHandler(ch)
    logger.setLevel(LOG_LEVEL)
    return logger

class ApiHelper(object):

    def __init__(self):
        conf = ConfigParser.SafeConfigParser()
        conf.read(CONFIG_FILE)
        self.appId = conf.get('app', 'app-id')
        self.appKey = conf.get('app', 'app-key')
        self.host = conf.get('app', 'host')
        self.clientId = conf.get('app', 'client-id')
        self.clientSecret = conf.get('app', 'client-secret')
 
        self.logger = getLogger()
        self.logger.debug('app id: ' + self.appId)
        self.logger.debug('app key: ' + self.appKey)
        self.logger.debug('base uri: ' + self.host)
        self.logger.debug('client id: ' + self.clientId)
        self.logger.debug('client secret: ' + self.clientSecret)
        self.getAppAdminToken()


    def createTotalSessionAggregationRule(self):
        self.logger.debug('create aggregation rule.')
        conn = httplib.HTTPConnection(self.host)
        path = '/api/apps/{0}/aggregation-rules'.format(self.appId)
        self.logger.debug('path: ' + path)
        body = {
            'name': 'totalSessions',
            'source':'event',
            'eventType': 'KiiSession',
            'aggregate' : { 'valueOf' : 'sessionLength', 'type': 'bigint', 'with': 'sum'},
            'groupBy': [
                        { 'name': 'appVersion', 'label': 'AppVersion', 'type': 'string'},
                        { 'name': 'deviceModel', 'label': 'DeviceModel', 'type': 'string'},
                        { 'name': 'resolution', 'label': 'Resolution', 'type': 'string'},
                        { 'name': 'locale', 'label': 'Locale', 'type': 'string'},
                        { 'name': 'platform', 'label': 'Platform', 'type': 'string'},
                        { 'name': 'osVersion', 'label': 'OSVersion', 'type': 'string'},
                        { 'name': 'packageName','label': 'PackageName', 'type': 'string'}
                        ]
        }
        self.logger.debug('request body: %s', body)
        headers = {'x-kii-appid': self.appId, 'x-kii-appkey': self.appKey,
            'authorization': 'Bearer ' + self.token,
            'content-type':
            'application/vnd.kii.AggregationRule+json'}
        jsonBody = json.dumps(body);
        conn.request('POST', path, jsonBody, headers)
        response = conn.getresponse()
        respDict = json.load(response)
        ruleId = respDict['_id']
        self.ruleId = ruleId
        self.logger.info('ruleId: %s', self.ruleId)
        self.logger.info('Use ruleId to retrieve analytics data')
        self.logger.debug('status: %d', response.status)
        self.logger.debug('body: %s', response.read())

    def createAverageSessionLengthAggregationRule(self):
        self.logger.debug('create aggregation rule.')
        conn = httplib.HTTPConnection(self.host)
        path = '/api/apps/{0}/aggregation-rules'.format(self.appId)
        self.logger.debug('path: ' + path)
        body = {
            'name': 'averageSessionLength',
            'source':'event',
            'eventType': 'KiiSession',
            'aggregate' : { 'valueOf' : 'sessionLength', 'type': 'float', 'with': 'avg'},
            'groupBy': [
                    { 'name': 'appVersion', 'label': 'AppVersion', 'type': 'string'},
                    { 'name': 'deviceModel', 'label': 'DeviceModel', 'type': 'string'},
                    { 'name': 'resolution', 'label': 'Resolution', 'type': 'string'},
                    { 'name': 'locale', 'label': 'Locale', 'type': 'string'},
                    { 'name': 'platform', 'label': 'Platform', 'type': 'string'},
                    { 'name': 'osVersion', 'label': 'OSVersion', 'type': 'string'},
                    { 'name': 'packageName','label': 'PackageName', 'type': 'string'}
                    ]
        }
        self.logger.debug('request body: %s', body)
        headers = {'x-kii-appid': self.appId, 'x-kii-appkey': self.appKey,
        'authorization': 'Bearer ' + self.token,
        'content-type':
        'application/vnd.kii.AggregationRule+json'}
        jsonBody = json.dumps(body);
        conn.request('POST', path, jsonBody, headers)
        response = conn.getresponse()
        respDict = json.load(response)
        ruleId = respDict['_id']
        self.ruleId = ruleId
        self.logger.info('ruleId: %s', self.ruleId)
        self.logger.info('Use ruleId to retrieve analytics data')
        self.logger.debug('status: %d', response.status)
        self.logger.debug('body: %s', response.read())


    def getAppAdminToken(self):
        self.logger.debug('get token')
        conn = httplib.HTTPConnection(self.host)
        path = '/api/oauth2/token'
        body = {'client_id': self.clientId, 'client_secret': self.clientSecret}
        jsonBody = json.dumps(body)
        headers = {'x-kii-appid': self.appId, 'x-kii-appkey': self.appKey,
                'content-type': 'application/json'}
        conn.request('POST', path, jsonBody, headers)
        response = conn.getresponse()
        respDict = json.load(response)
        self.logger.debug('status: %d', response.status)
        self.logger.debug('body: %s', respDict)
        token = respDict['access_token']
        self.logger.debug('access-token: ' + token)
        self.token = token


if __name__ == '__main__':
    helper = ApiHelper()
    helper.createTotalSessionAggregationRule()
    helper.createAverageSessionLengthAggregationRule()


