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
#import "KiiOverviewDataBrowseViewController.h"

@interface KiiTutorialOverViewController ()
@property (weak, nonatomic) IBOutlet UITextView *refTextView;
@property TutorialOverview overview;

- (IBAction)browseBtnPressed:(id)sender;

- (IBAction)moveBtnPressed:(id)sender;

@end

@implementation KiiTutorialOverViewController

- (void)viewDidLoad {
    [KiiViewUtilities showSuccessHUD:@"File uploading success" withView:self.view];
    
    
    //[[self refTextView] setText:message];
    //self.refTextView.dataDetectorTypes = UIDataDetectorTypeLink;
}

- (IBAction)mTourAgainButton:(id)sender {
    [KiiUser logOut];
    [self.navigationController popToRootViewControllerAnimated:YES];
}

- (IBAction)browseBtnPressed:(id)sender {
    self.overview = BrowseData;
    [self performSegueWithIdentifier:@"HowToDataBrowse" sender:sender];
    
}

- (IBAction)moveBtnPressed:(id)sender {
    self.overview = MoveForword;
    [self performSegueWithIdentifier:@"HowToDataBrowse" sender:sender];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    if ([[segue identifier] isEqualToString:@"HowToDataBrowse"]) {
        KiiOverviewDataBrowseViewController *viewController = (KiiOverviewDataBrowseViewController *) [segue destinationViewController];
        viewController.overview = self.overview;
    }
}
@end
