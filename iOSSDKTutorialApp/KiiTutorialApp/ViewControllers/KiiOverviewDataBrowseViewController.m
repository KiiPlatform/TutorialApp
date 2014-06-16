//
//  KiiOverviewDataBrowseViewController.m
//  KiiTutorialApp
//
//  Created by Moshiur on 6/2/14.
//  Copyright (c) 2014 Kii Corporation. All rights reserved.
//

#import "KiiOverviewDataBrowseViewController.h"
#import "KiiCommonUtilities.h"

@interface KiiOverviewDataBrowseViewController ()
@property (weak, nonatomic) IBOutlet UIWebView *descView;
- (IBAction)okPressed:(id)sender;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;

@end

@implementation KiiOverviewDataBrowseViewController

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
    NSString* browseDataTitle = @"How to browse data";
    NSString* browseData = @"To check the details of created object: \
     <ul> \
     <li>Go to http://developer.kii.com </li> \
     <li>Login using KiiCloud account</li> \
     <li>Open App console.</li> \
     <li>From left side pane click to the 'Objects'</li> \
     <li>Click to 'Data Browser' shows search box</li> \
     <li>Search by typing 'tutorial' shows the list of objects under the bucket</li> \
     </ul>";
    
    NSString* moveTitle = @"How to move forward";
    
    NSString* moveData = @"<br><br>We have guides,references, samples and tutorials which describes the details of developing Apps with KiiCloud.<br><br>You can access these resources from <a href=\"http://documentation.kii.com/%@/starts\">here</a>";
    
    if (self.overview == MoveForword) {
        self.titleLabel.text = moveTitle;
        [[self descView] loadHTMLString:[NSString stringWithFormat:moveData, [KiiCommonUtilities kiidocsLocalePath]] baseURL:nil];
    } else if (self.overview == BrowseData) {
        [self titleLabel].text = browseDataTitle;
        [[self descView] loadHTMLString:[NSString stringWithFormat:browseData, [KiiCommonUtilities kiidocsLocalePath]] baseURL:nil];
    }
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)okPressed:(id)sender {
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
