//
//  KiiCommonUtilities.h
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/19/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface KiiCommonUtilities : NSObject

+ (NSString *)errorDetailsMessage:(NSError *)error;
+ (NSString *)kiidocsLocalePath;

@end
