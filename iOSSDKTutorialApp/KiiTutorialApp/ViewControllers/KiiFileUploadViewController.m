//
//  KiiFileUploadViewController.m
//  KiiTutorialApp
//
//  Created by Ryuji OCHI on 11/15/13.
//  Copyright (c) 2013 Kii Corporation. All rights reserved.
//

#import "KiiFileUploadViewController.h"
#import "KiiViewUtilities.h"
#import "KiiCommonUtilities.h"
#import <AssetsLibrary/ALAssetsLibrary.h>
#import <AssetsLibrary/ALAssetRepresentation.h>
#import <KiiSDK/Kii.h>

@interface KiiFileUploadViewController ()
@property (weak, nonatomic) IBOutlet UIImageView *descView;
@end

@implementation KiiFileUploadViewController

- (void)viewDidLoad {
    // Set border line to image view
    self.selectedImageView.layer.cornerRadius = 5.0;
    self.selectedImageView.layer.masksToBounds = YES;
    self.selectedImageView.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.selectedImageView.layer.borderWidth = 1.0;

    [KiiViewUtilities showSuccessHUD:@"KiiObject creation success" withView:self.view];
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(descViewTaped:)];
    singleTap.numberOfTapsRequired = 1;
    singleTap.numberOfTouchesRequired = 1;
    [self.descView addGestureRecognizer:singleTap];
    [self.descView setUserInteractionEnabled:YES];
}

- (IBAction)mChoosePhotoButton:(id)sender {
    self.mediaPicker = [[UIImagePickerController alloc] init];
    self.mediaPicker.delegate = self;
    self.mediaPicker.allowsEditing = NO;
    UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                                             delegate:self
                                                    cancelButtonTitle:@"Cancel"
                                               destructiveButtonTitle:nil
                                                    otherButtonTitles:@"Take photo", @"Choose existing", nil];
    [actionSheet showInView:self.view];
}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {
        if (![UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
            return;
        }
        self.mediaPicker.sourceType = UIImagePickerControllerSourceTypeCamera;
    } else if (buttonIndex == 1) {
        self.mediaPicker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    } else {
        return;
    }
    [self presentViewController:self.mediaPicker animated:YES completion:nil];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [self dismissViewControllerAnimated:YES completion:nil];

    [KiiViewUtilities showProgressHUD:@"Processing..." withView:self.view];

    UIImagePickerControllerSourceType sourceType = picker.sourceType;
    ALAssetsLibrary *assetsLibrary = [[ALAssetsLibrary alloc] init];

    if (sourceType == UIImagePickerControllerSourceTypeCamera) {
        UIImage *image = [info objectForKey:UIImagePickerControllerOriginalImage];

        // Request to save the image to camera roll
        [assetsLibrary writeImageToSavedPhotosAlbum:[image CGImage] orientation:(ALAssetOrientation) [image imageOrientation] completionBlock:^(NSURL *assetURL, NSError *error) {
            if (error) {
                [KiiViewUtilities hideProgressHUD:self.view];
                [KiiViewUtilities showFailureHUD:@"Selected photo can not be used" withView:self.view];
            } else {
                NSLog(@"url %@", assetURL);
                [self saveImageDataToTemporaryArea:assetsLibrary withAssetUrl:assetURL];
            }
        }];
    } else {
        NSURL *url = (NSURL *) [info valueForKey:UIImagePickerControllerReferenceURL];
        [self saveImageDataToTemporaryArea:assetsLibrary withAssetUrl:url];
    }
}

- (void)saveImageDataToTemporaryArea:(ALAssetsLibrary *)assetsLibrary withAssetUrl:(NSURL *)url {
    [assetsLibrary assetForURL:url
                   resultBlock:^(ALAsset *asset) {
                       ALAssetRepresentation *representation = [asset defaultRepresentation];
                       NSString *fileUTI = [representation UTI];
                       NSString *fileName = [representation filename];
                       ALAssetOrientation orientation = (ALAssetOrientation) [[asset valueForProperty:@"ALAssetPropertyOrientation"] intValue];
                       CGImageRef cgImg = [representation fullResolutionImage];
                       self.selectedImage = [UIImage imageWithCGImage:cgImg scale:1.0 orientation:(UIImageOrientation) orientation];
                       NSData *data = nil;
                       if ([fileUTI isEqualToString:@"public.png"]) {
                           data = UIImagePNGRepresentation(self.selectedImage);
                       } else {
                           data = UIImageJPEGRepresentation(self.selectedImage, 95);
                       }

                       NSError *error = nil;
                       self.selectedImagePath = [NSTemporaryDirectory() stringByAppendingPathComponent:fileName];
                       [data writeToFile:self.selectedImagePath options:NSDataWritingAtomic error:&error];
                       if (error != nil) {
                           self.selectedImage = nil;
                           self.selectedImagePath = nil;
                           self.selectedImageView.image = nil;
                           NSLog(@"Failed to save : %i - %s", errno, strerror(errno));
                           [KiiViewUtilities hideProgressHUD:self.view];
                           [KiiViewUtilities showFailureHUD:@"Selected photo can not be used" withView:self.view];
                           return;
                       }
                       self.selectedImageView.contentMode = UIViewContentModeScaleAspectFit;
                       self.selectedImageView.image = self.selectedImage;

                       [KiiViewUtilities hideProgressHUD:self.view];
                       [KiiViewUtilities showSuccessHUD:@"Photo selected" withView:self.view];
                   }
                  failureBlock:^(NSError *error) {
                      [KiiViewUtilities hideProgressHUD:self.view];
                      [KiiViewUtilities showFailureHUD:@"Selected photo can not be used" withView:self.view];
                  }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)mFileUploadButton:(id)sender {
    if (self.selectedImagePath == nil) {
        [KiiViewUtilities showFailureHUD:@"Image is not selected!" withView:self.view];
        return;
    }

    MBProgressHUD *hud = [KiiViewUtilities showProgressHUD:@"Uploading..."
                                                  withMode:MBProgressHUDModeAnnularDeterminate
                                                   andView:self.view];

    KiiUploader *uploader = [self.kiiObject uploader:self.selectedImagePath];

    // Create a progress block.
    KiiRTransferBlock progressBlock = ^(id <KiiRTransfer> transferObject, NSError *retError) {
        KiiRTransferInfo *info = [transferObject info];
        float progress = (float) [info completedSizeInBytes] / [info totalSizeInBytes];
        hud.progress = progress;
        NSLog(@"Progress : %f", progress);
    };

    KiiRTransferBlock completionBlock = ^(id <KiiRTransfer> transferObject, NSError *retError) {
        [KiiViewUtilities hideProgressHUD:self.view];
        if (retError != nil) {
            // Something went wrong...
            NSLog(@"Transfer error! : %@", retError);
            NSString *errorMessage = @"File uploading failed.";
            NSString *detailedMessage = [KiiCommonUtilities errorDetailsMessage:retError];
            [KiiViewUtilities showFailureHUD:errorMessage withDetailsText:detailedMessage andView:self.view];
        } else {
            [self performSegueWithIdentifier:@"FileUploadCompleted" sender:self];
        }
    };

    // Start uploading.
    [uploader transferWithProgressBlock:progressBlock andCompletionBlock:completionBlock];
}

- (void)descViewTaped:(UIGestureRecognizer *)gestureRecognizer {
   [self performSegueWithIdentifier:@"FileAttachDesc" sender:gestureRecognizer]; 
}

@end
