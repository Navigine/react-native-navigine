#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import "NavigineLocationView.h"
#import <Navigine/NCNavigineSdk.h>
#import <React/RCTEventEmitter.h>

@interface NavigineModule : RCTEventEmitter <RCTBridgeModule>

@property (nonatomic, strong) NavigineLocationView *view;
@property (nonatomic, strong) NCNavigineSdk *sdk;

@end
