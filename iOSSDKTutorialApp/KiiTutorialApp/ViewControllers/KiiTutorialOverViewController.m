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
#import "KiiCommonUtilities.h"

@interface KiiTutorialOverViewController ()
@property (weak, nonatomic) IBOutlet UITextView *refTextView;

- (IBAction)browseBtnPressed:(id)sender;


@end

@implementation KiiTutorialOverViewController

- (void)viewDidLoad {
    [KiiViewUtilities showSuccessHUD:@"File uploading success" withView:self.view];
    
    NSString* data = @"We have guides,references, samples and tutorials describes the details of developing Apps with KiiCloud.You can get these from http://developer.kii.com/%@/guides/ios/starts";
    
    NSString* message = [NSString stringWithFormat:data, [KiiCommonUtilities kiidocsLocalePath]];
    [[self refTextView] setText:message];
    self.refTextView.dataDetectorTypes = UIDataDetectorTypeLink;
}

- (IBAction)mTourAgainButton:(id)sender {
    [KiiUser logOut];
    [self.navigationController popToRootViewControllerAnimated:YES];
}

- (IBAction)browseBtnPressed:(id)sender {
    [self performSegueWithIdentifier:@"HowToDataBrowse" sender:sender];
    
}
@end
