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
    NSString* data = @"To check the details of created object: \
     <ul> \
     <li>Go to http://developer.kii.com </li> \
     <li>Login using KiiCloud account</li> \
     <li>Open App console.</li> \
     <li>From left side pane click to the 'Objects'</li> \
     <li>Click to 'Data Browser' shows search box</li> \
     <li>Search by typing 'tutorial' shows the list of objects under the bucket</li> \
     </ul>";
    
    NSString* message = [NSString stringWithFormat:data, [KiiCommonUtilities kiidocsLocalePath]];
    [[self descView] loadHTMLString:message baseURL:nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)okPressed:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}
@end
