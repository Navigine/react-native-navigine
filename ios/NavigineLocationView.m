#import <React/RCTViewManager.h>

#import "NavigineLocationView.h"
#import "RNNavigineModule.h"
#import "View/RNNCLocationView.h"

#import "Converter/RCTConvert+RNNavigineView.m"

#ifndef MAX
#import <NSObjCRuntime.h>
#endif

@interface NavigineLocationView()
@property (nonatomic, strong) NCLocationManager* locationManager;
@end

@implementation NavigineLocationView

RCT_EXPORT_MODULE()

- (NSArray<NSString *> *)supportedEvents {
    return @[@"onPositionUpdated", @"onPathsUpdated", @"onMapPress", @"onMapLongPress", @"onScreenPositionToMetersReceived"];
}

- (instancetype)init {
    self = [super init];
    return self;
}
+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (UIView *_Nullable)view {
    RNNCLocationView* view = [[RNNCLocationView alloc] init];
    return view;
}

RCT_EXPORT_VIEW_PROPERTY(onPositionUpdated, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onPathsUpdated, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onMapPress, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMapLongPress, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onScreenPositionToMetersReceived, RCTBubblingEventBlock)

RCT_EXPORT_METHOD(setLocationId:(nonnull NSNumber*) reactTag locationId:(NSNumber*_Nonnull) locationId )
{
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
      RNNCLocationView *view = (RNNCLocationView*) viewRegistry[reactTag];
        if (!view || ![view isKindOfClass:[RNNCLocationView class]]) {
            RCTLogError(@"Cannot find NativeView with tag #%@", reactTag);
            return;
        }
        [view setLocationId: [locationId floatValue]];
    }];
}

RCT_EXPORT_METHOD(setSublocationId:(nonnull NSNumber*) reactTag locationId:(NSNumber*_Nonnull) locationId )
{
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
      RNNCLocationView *view = (RNNCLocationView*) viewRegistry[reactTag];
        if (!view || ![view isKindOfClass:[RNNCLocationView class]]) {
            RCTLogError(@"Cannot find NativeView with tag #%@", reactTag);
            return;
        }
        [view setSublocationId: [locationId floatValue]];
    }];
}

RCT_EXPORT_METHOD(screenPositionToMeters:(nonnull NSNumber*) reactTag _id:(NSString*_Nonnull) _id position:(NSDictionary*_Nonnull) position) {
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
      RNNCLocationView *view = (RNNCLocationView*) viewRegistry[reactTag];
        if (!view || ![view isKindOfClass:[RNNCLocationView class]]) {
            RCTLogError(@"Cannot find NativeView with tag #%@", reactTag);
            return;
        }
      [view screenPositionToMeters:_id position:position];
    }];
}


RCT_EXPORT_METHOD(setTarget:(nonnull NSNumber*) reactTag targetPoint:(NSDictionary*_Nonnull) targetPoint )
{
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
      RNNCLocationView *view = (RNNCLocationView*) viewRegistry[reactTag];
        if (!view || ![view isKindOfClass:[RNNCLocationView class]]) {
            RCTLogError(@"Cannot find NativeView with tag #%@", reactTag);
            return;
        }
        [view setTarget: [RCTConvert NCLocationPoint:targetPoint]];
    }];
}

RCT_EXPORT_METHOD(clearTargets:(nonnull NSNumber*) reactTag)
{
    [self.bridge.uiManager addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *,UIView *> *viewRegistry) {
      RNNCLocationView *view = (RNNCLocationView*) viewRegistry[reactTag];
        if (!view || ![view isKindOfClass:[RNNCLocationView class]]) {
            RCTLogError(@"Cannot find NativeView with tag #%@", reactTag);
            return;
        }
        [view clearTargets];
    }];
}

@end
