//
//  KiiViewUtilities.h
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/15/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MBProgressHUD.h"

@class MBProgressHUD;

@interface KiiViewUtilities : NSObject

+ (void)showProgressHUD:(NSString *)labelText withView:(id)view;

+ (MBProgressHUD *)showProgressHUD:(NSString *)labelText withMode:(MBProgressHUDMode)mode andView:(id)view;

+ (void)showSuccessHUD:(NSString *)labelText withView:(id)view;

+ (void)showFailureHUD:(NSString *)labelText withView:(id)view;

+ (void)showFailureHUD:(NSString *)labelText withDetailsText:(NSString *)detailsText andView:(id)view;

+ (void)hideHUD:(id)sender;

+ (void)hideProgressHUD:(id)view;

@end
