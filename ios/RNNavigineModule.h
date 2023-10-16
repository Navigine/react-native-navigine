#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import "NavigineLocationView.h"
#import "NCNavigineSdk.h"

@interface NavigineModule : NSObject <RCTBridgeModule>

@property (nonatomic, strong) NavigineLocationView *view;
@property (nonatomic, strong) NCNavigineSdk *sdk;

@end
