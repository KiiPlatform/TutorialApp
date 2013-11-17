//
//  KiiTutorialOverViewController.m
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/15/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import <KiiSDK/KiiUser.h>
#import "KiiTutorialOverViewController.h"
#import "KiiViewUtilities.h"

@interface KiiTutorialOverViewController ()

@end

@implementation KiiTutorialOverViewController

- (void)viewDidLoad {
    [KiiViewUtilities showSuccessHUD:@"File uploading success" withView:self.view];
}

- (IBAction)mTourAgainButton:(id)sender {
    [KiiUser logOut];
    [self.navigationController popToRootViewControllerAnimated:YES];
}

@end
