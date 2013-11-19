//
//  KiiViewUtilities.m
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/15/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import "KiiViewUtilities.h"

@implementation KiiViewUtilities

+ (void)showProgressHUD:(NSString *)labelText withView:(id)view {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.labelText = labelText;
    hud.dimBackground = YES;
}

+ (MBProgressHUD *)showProgressHUD:(NSString *)labelText withMode:(MBProgressHUDMode)mode andView:(id)view {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.labelText = labelText;
    hud.dimBackground = YES;
    hud.mode = mode;
    return hud;
}

+ (void)showSuccessHUD:(NSString *)labelText withView:(id)view {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.labelText = labelText;
    hud.dimBackground = YES;
    hud.mode = MBProgressHUDModeCustomView;
    hud.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"success-indicator"]];
    [hud hide:YES afterDelay:2];
}

+ (void)showFailureHUD:(NSString *)labelText withView:(id)view {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.labelText = labelText;
    hud.dimBackground = YES;
    hud.mode = MBProgressHUDModeCustomView;
    hud.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"failure-indicator"]];
    [hud addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(hideHUD:)]];
}

+ (void)showFailureHUD:(NSString *)labelText withDetailsText:(NSString *)detailsText andView:(id)view {
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.labelText = labelText;
    hud.detailsLabelText = detailsText;
    hud.dimBackground = YES;
    hud.delegate = view;
    hud.mode = MBProgressHUDModeCustomView;
    hud.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"failure-indicator"]];
    [hud addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(hideHUD:)]];
}

+ (void)hideHUD:(UIGestureRecognizer *)gestureRecognizer {
    MBProgressHUD *hud = (MBProgressHUD *) [gestureRecognizer view];
    [hud hide:YES];
}

+ (void)hideProgressHUD:(id)view {
    [MBProgressHUD hideHUDForView:view animated:YES];
}

@end
