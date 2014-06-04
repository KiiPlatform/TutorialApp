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
    NSLog(@"error description: %@", [error description]);
    if (error == nil || [error userInfo] == nil) {
        return nil;
    }
    NSDictionary *userInfo = [error userInfo];
    NSString *detailsMessage = userInfo[@"ios_sdk_message"];
    if (detailsMessage == nil || [detailsMessage isEqualToString:@""]) {
        detailsMessage = userInfo[@"server_message"];
    }
    return detailsMessage;
}

+ (NSString *)kiidocsLocalePath {
    NSString* lan = [[NSLocale preferredLanguages] objectAtIndex:0];
    NSString* path = nil;
    if ([lan isEqualToString:@"jp"] || [lan isEqualToString:@"cn"]) {
        path = lan;
    } else {
        path = @"en";
    }
    return path;
}

@end
