//
//  KiiCreateObjectViewController.m
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/15/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import <KiiSDK/KiiBucket.h>
#import <KiiSDK/Kii.h>
#import "KiiCreateObjectViewController.h"
#import "KiiFileUploadViewController.h"
#import "KiiViewUtilities.h"
#import "KiiAppConstants.h"

@interface KiiCreateObjectViewController ()

@end

@implementation KiiCreateObjectViewController

- (void)viewDidLoad {
    [KiiViewUtilities showSuccessHUD:@"Login success" withView:self.view];
}

- (IBAction)mCreateKiiObjectButton:(id)sender {
    KiiBucket *bucket = [Kii bucketWithName:KII_APP_BUCKET_NAME];
    KiiObject *object = [bucket createObject];
    [object setObject:[KiiUser currentUser].username forKey:@"username"];
    [object saveWithBlock:^(KiiObject *retObject, NSError *retError) {
        [KiiViewUtilities hideProgressHUD:self.view];
        if (retError) {
            [KiiViewUtilities showFailureHUD:@"KiiObject creation is failed." withView:self.view];
        } else {
            self.kiiObject = retObject;
            [self performSegueWithIdentifier:@"CreateObjectCompleted" sender:self];
        }
    }];
    [KiiViewUtilities showProgressHUD:@"Create KiiObject..." withView:self.view];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([[segue identifier] isEqualToString:@"CreateObjectCompleted"]) {
        KiiFileUploadViewController *viewController = (KiiFileUploadViewController *) [segue destinationViewController];
        viewController.kiiObject = self.kiiObject;
    }
}

@end