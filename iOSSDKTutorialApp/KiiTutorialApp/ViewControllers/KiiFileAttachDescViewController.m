//
//  KiiFileAttachDescViewController.m
//  KiiTutorialApp
//
//  Created by Moshiur on 5/30/14.
//  Copyright (c) 2014 Kii Corporation. All rights reserved.
//

#import "KiiFileAttachDescViewController.h"
#import "KiiCommonUtilities.h"

@interface KiiFileAttachDescViewController ()
- (IBAction)btnPressed:(id)sender;
@property (weak, nonatomic) IBOutlet UIWebView *descView;

@end

@implementation KiiFileAttachDescViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    self.descView.delegate = self;
    
    NSString* data = @"The file will be uploaded to kiicloud associating it with the object as object body.<br><br>To learn more about body attachment, visit <a href=\"http://documentation.kii.com/%@/guides/ios/managing-data/buckets\">docs.</a>";
    
    NSString* message = [NSString stringWithFormat:data, [KiiCommonUtilities kiidocsLocalePath]];
    [[self descView] loadHTMLString:message baseURL:nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)btnPressed:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    if (navigationType == UIWebViewNavigationTypeLinkClicked) {
        [[UIApplication sharedApplication] openURL:request.URL];
        return false;
    }
    return true;
}
@end
