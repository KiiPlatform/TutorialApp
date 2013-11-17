//
//  KiiFileUploadViewController.h
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/15/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <KiiSDK/KiiObject.h>

@interface KiiFileUploadViewController : UIViewController <UIImagePickerControllerDelegate, UIActionSheetDelegate, UINavigationControllerDelegate>

@property (nonatomic, strong) IBOutlet UIImageView *selectedImageView;
@property (nonatomic, strong) UIImage *selectedImage;
@property (nonatomic, strong) NSString *selectedImagePath;
@property (nonatomic, strong) UIImagePickerController *mediaPicker;
@property (nonatomic, strong) KiiObject *kiiObject;

@end
