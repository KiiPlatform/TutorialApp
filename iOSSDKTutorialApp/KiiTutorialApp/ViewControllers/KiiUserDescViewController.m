//
//  KiiUserDescViewController.m
//  KiiTutorialApp
//
//  Created by Moshiur on 5/30/14.
//  Copyright (c) 2014 Kii Corporation. All rights reserved.
//

#import "KiiUserDescViewController.h"
#import "KiiCommonUtilities.h"

@interface KiiUserDescViewController ()
@property (weak, nonatomic) IBOutlet UIWebView *descView;
- (IBAction)btnPressed:(id)sender;

@end

@implementation KiiUserDescViewController

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
    self.descView.delegate = self;
	// Do any additional setup after loading the view.
    
    NSString* data = @"Signup/login to KiiCloud with user credentials.<br> \
    Note it is different from developer account you're using to login to developer.kii.com<br><br> \
    When signup succeeded, a new user registered with your application. \
    Once registered, they will be able to login with their username and password.<br><br> \
    To learn more about user management, visit <a href=\"http://documentation.kii.com/%@/guides/ios/managing-users\">docs.</a>";
    
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
