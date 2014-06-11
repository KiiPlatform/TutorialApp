//
//  KiiFileCreateDescViewController.m
//  KiiTutorialApp
//
//  Created by Moshiur on 5/30/14.
//  Copyright (c) 2014 Kii Corporation. All rights reserved.
//

#import "KiiFileCreateDescViewController.h"
#import "KiiCommonUtilities.h"

@interface KiiFileCreateDescViewController ()
- (IBAction)btnPressed:(id)sender;

@end

@implementation KiiFileCreateDescViewController

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
    NSString* data = @"Creating object means storing arbitrary key/value pairs as JSON-style objects in kiicloud.<br><br> On completion, an \'app\' scope object will be created in \'tutorial\' bucket.\n\n To learn more about bucket and object, visit <a href=\"http://documentation.kii.com/%@/guides/ios/managing-data/buckets\">docs.</a>";
    
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
