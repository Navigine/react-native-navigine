#pragma once

#import <React/RCTComponent.h>

#import <Navigine/Navigine.h>

@class RCTBridge;

@interface RNNCLocationView: NCLocationView<RCTComponent, NCPositionListener, NCRouteListener, NCGestureRecognizerDelegate>

- (void) setLocationId: (int)locationId;
- (void) setSublocationId: (int)sublocationId;

- (void) screenPositionToMeters: (NSString * _Nonnull)_id position:(NSDictionary * _Nonnull) position;
- (void) setTarget:(NCLocationPoint * _Nonnull) targetPoint;
- (void) clearTargets;

@property (nonatomic, copy) RCTBubblingEventBlock _Nullable onPositionUpdated;
@property (nonatomic, copy) RCTBubblingEventBlock _Nullable onPathsUpdated;
@property (nonatomic, copy) RCTBubblingEventBlock _Nullable onMapPress;
@property (nonatomic, copy) RCTBubblingEventBlock _Nullable onMapLongPress;
@property (nonatomic, copy) RCTBubblingEventBlock _Nullable onScreenPositionToMetersReceived;

@end
