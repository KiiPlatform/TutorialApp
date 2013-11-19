//
//  KiiCommonUtilities.m
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/19/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import "KiiCommonUtilities.h"

@implementation KiiCommonUtilities

+ (NSString *)errorDetailsMessage:(NSError *)error {
    if (error == nil || [error userInfo] == nil) {
        return nil;
    }
    NSDictionary *userInfo = [error userInfo];
    NSString *detailsMessage = userInfo[@"description"];
    if (detailsMessage == nil || [detailsMessage isEqualToString:@""]) {
        detailsMessage = userInfo[@"server_message"];
    }
    return detailsMessage;
}

@end
