//
//  KiiCreateUserViewController.h
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/15/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface KiiCreateUserViewController : UIViewController <UITextViewDelegate>

@property (nonatomic, strong) IBOutlet UITextField *usernameField;
@property (nonatomic, strong) IBOutlet UITextField *passwordField;

@end
