//
//  KiiOverviewDataBrowseViewController.h
//  KiiTutorialApp
//
//  Created by Moshiur on 6/2/14.
//  Copyright (c) 2014 Kii Corporation. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSUInteger, TutorialOverview) {
    MoveForword,
    BrowseData
};
@interface KiiOverviewDataBrowseViewController : UIViewController<UIWebViewDelegate>
@property  TutorialOverview overview;
@end
