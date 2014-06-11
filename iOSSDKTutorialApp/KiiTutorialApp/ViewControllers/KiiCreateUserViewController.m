//
//  KiiCreateUserViewController.m
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/15/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import "KiiCreateUserViewController.h"
#import "KiiUserDescViewController.h"
#import "KiiViewUtilities.h"
#import "KiiCommonUtilities.h"
#import <KiiSDK/KiiUser.h>

@interface KiiCreateUserViewController ()
@property (weak, nonatomic) IBOutlet UIImageView *descView;

@end

@implementation KiiCreateUserViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(descViewTaped:)];
    singleTap.numberOfTapsRequired = 1;
    singleTap.numberOfTouchesRequired = 1;
    [self.descView addGestureRecognizer:singleTap];
    [self.descView setUserInteractionEnabled:YES];
}

- (IBAction)mSignUpButton:(id)sender {
    // Hide keyboard
    [self.view endEditing:YES];

    // Get username and password from text field
    NSString *userIdentifier = [self.usernameField text];
    NSString *password = [self.passwordField text];

    // Create KiiUser from username and password
    KiiUser *user = [KiiUser userWithUsername:userIdentifier andPassword:password];
    // Do register with Blocks
    [user performRegistrationWithBlock:^(KiiUser *retUser, NSError *retError) {
        [KiiViewUtilities hideProgressHUD:self.view];

        // Check returning error
        if (retError) {
            NSString *errorMessage = @"Registration failed.";
            NSString *detailedMessage = [KiiCommonUtilities errorDetailsMessage:retError];
            [KiiViewUtilities showFailureHUD:errorMessage withDetailsText:detailedMessage andView:self.view];
        } else {
            [self performSegueWithIdentifier:@"LoginCompleted" sender:self];
        }
    }];

    [KiiViewUtilities showProgressHUD:@"Processing..." withView:self.view];
}

- (IBAction)mSignInButton:(id)sender {
    // Hide keyboard
    [self.view endEditing:YES];

    // Get username and password from text field
    NSString *userIdentifier = [self.usernameField text];
    NSString *password = [self.passwordField text];

    // Do authenticate with Blocks
    [KiiUser authenticate:userIdentifier withPassword:password andBlock:^(KiiUser *retUser, NSError *retError) {
        [KiiViewUtilities hideProgressHUD:self.view];

        // Check returning error
        if (retError) {
            NSString *errorMessage = @"SignIn failed.";
            NSString *detailedMessage = [KiiCommonUtilities errorDetailsMessage:retError];
            [KiiViewUtilities showFailureHUD:errorMessage withDetailsText:detailedMessage andView:self.view];
        } else {
            [self performSegueWithIdentifier:@"LoginCompleted" sender:self];
        }
    }];

    [KiiViewUtilities showProgressHUD:@"Processing..." withView:self.view];
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}

- (IBAction)descriptionFieldGestureHandler:(id)sender {
    [self.view endEditing:YES];
}

- (void)singleTapGestureCaptured:(UITapGestureRecognizer *)gesture {
    [self.view endEditing:YES];
}

- (void)descViewTaped:(UIGestureRecognizer *)gestureRecognizer {
    
    [self performSegueWithIdentifier:@"UserDesc" sender:gestureRecognizer];
}
@end
