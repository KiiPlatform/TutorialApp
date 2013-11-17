//
//  KiiFirstViewController.m
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/15/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import "KiiFirstViewController.h"

@interface KiiFirstViewController ()

@end

@implementation KiiFirstViewController

- (IBAction)mTutorialStartButton:(id)sender {
    [self performSegueWithIdentifier:@"TutorialStarted" sender:self];
}

@end
