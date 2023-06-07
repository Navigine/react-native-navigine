#import <React/RCTComponent.h>
#import <React/UIView+React.h>

#import "RNNCLocationView.h"
#import "NavigineIconMapObjectView.h"
#import "NaviginePolylineMapObjectView.h"
#import "NavigineCircleMapObjectView.h"

#ifndef MAX
#import <NSObjCRuntime.h>
#endif

@implementation RNNCLocationView {
    NSMutableArray<UIView*>* _reactSubviews;
    NCLocationManager* _locationManager;
    NCNavigationManager* _navigationManager;
    NCRouteManager* _routeManager;
}

- (instancetype)init {
    self = [super init];
    _reactSubviews = [[NSMutableArray alloc] init];
    _locationManager = [[NCNavigineSdk getInstance] getLocationManager];
    _navigationManager = [[NCNavigineSdk getInstance] getNavigationManager:_locationManager];
    _routeManager = [[NCNavigineSdk getInstance] getRouteManager:_locationManager navigationManager:_navigationManager];

    [_navigationManager addPositionListener:self];
    [_routeManager addRouteListener:self];
    super.gestureDelegate = self;
    return self;
}

// children
- (void)addSubview:(UIView *) view {
    [super addSubview:view];
}

- (void) setLocationId: (int)locationId {
    [_locationManager setLocationId:locationId];
}

- (void) setSublocationId: (int)sublocationId {
    [super setSublocationId:sublocationId];
}

- (void) screenPositionToMeters: (NSString * _Nonnull)_id position:(NSDictionary * _Nonnull) position {
    NCPoint* point = [super screenPositionToMeters:CGPointMake([position[@"x"] floatValue], [position[@"y"] floatValue])];
    NSDictionary* metersPoint = @{
        @"x": @(point.x),
        @"y": @(point.y)
    };
    NSMutableDictionary *response = [NSMutableDictionary dictionaryWithDictionary:metersPoint];
    [response setValue:_id forKey:@"id"];
    if (self.onScreenPositionToMetersReceived) {
        self.onScreenPositionToMetersReceived(response);
    }
}

- (void)insertReactSubview:(UIView<RCTComponent>*) subview atIndex:(NSInteger) atIndex {
  if ([subview isKindOfClass:[NaviginePolylineMapObjectView class]]) {
    NaviginePolylineMapObjectView* polyline = (NaviginePolylineMapObjectView*) subview;
    NCPolylineMapObject* obj = [self addPolylineMapObject];
    [polyline setMapObject:obj];
  } else if ([subview isKindOfClass:[NavigineIconMapObjectView class]]) {
    NavigineIconMapObjectView* marker = (NavigineIconMapObjectView *) subview;
    NCIconMapObject* obj = [self addIconMapObject];
    [marker setMapObject:obj];
  } else if ([subview isKindOfClass:[NavigineCircleMapObjectView class]]) {
    NavigineCircleMapObjectView* circle = (NavigineCircleMapObjectView*) subview;
    NCCircleMapObject* obj = [self addCircleMapObject];
    [circle setMapObject:obj];
  } else {
    NSArray<id<RCTComponent>> *childSubviews = [subview reactSubviews];
    for (int i = 0; i < childSubviews.count; i++) {
      [self insertReactSubview:(UIView *)childSubviews[i] atIndex:atIndex];
    }
  }
  [_reactSubviews insertObject:subview atIndex:atIndex];
  [super insertReactSubview:subview atIndex:atIndex];
}

- (void)removeReactSubview:(UIView<RCTComponent>*) subview {
  if ([subview isKindOfClass:[NaviginePolylineMapObjectView class]]) {
    NaviginePolylineMapObjectView* polyline = (NaviginePolylineMapObjectView*) subview;
    [self removePolylineMapObject:[polyline getMapObject]];
  } else if ([subview isKindOfClass:[NavigineIconMapObjectView class]]) {
    NavigineIconMapObjectView* marker = (NavigineIconMapObjectView*) subview;
    [self removeIconMapObject:[marker getMapObject]];
  } else if ([subview isKindOfClass:[NavigineCircleMapObjectView class]]) {
    NavigineCircleMapObjectView* circle = (NavigineCircleMapObjectView*) subview;
    [self removeCircleMapObject:[circle getMapObject]];
  } else {
    NSArray<id<RCTComponent>> *childSubviews = [subview reactSubviews];
    for (int i = 0; i < childSubviews.count; i++) {
      [self removeReactSubview:(UIView *)childSubviews[i]];
    }
  }
  [_reactSubviews removeObject:subview];
  [super removeReactSubview: subview];
}

- (void)onPositionError:(nullable NSError *)error {
//  NSLog(error);
}

- (void)onPositionUpdated:(nonnull NCPosition *)position {
  if (self.onPositionUpdated) {
    NSDictionary* point = @{
      @"x": @(position.locationPoint.point.x),
      @"y": @(position.locationPoint.point.y)
    };

    NSDictionary* locationPoint = @{
      @"point": point,
      @"locationId": @(position.locationPoint.locationId),
      @"sublocationId": @(position.locationPoint.sublocationId)
    };

    NSDictionary* globalPoint = @{
      @"latitude": @(position.point.latitude),
      @"longitude": @(position.point.longitude)
    };

    NSDictionary* data = @{
        @"point": globalPoint,
        @"accuracy": @(position.accuracy),
        @"heading": @(position.heading.doubleValue),
        @"locationPoint": locationPoint,
        @"locationHeading": @(position.locationHeading.doubleValue)
    };

    self.onPositionUpdated(data);
  }
}

- (void)onPathsUpdated:(nonnull NSArray<NCRoutePath *> *)paths
{
  if (self.onPathsUpdated && paths.count) {
    NCRoutePath *routePath = paths.firstObject;
    NSMutableArray* pointsArray = [[NSMutableArray alloc] init];
    for(NCLocationPoint* locationPoint in routePath.points) {
      NSDictionary* point = @{
        @"x": @(locationPoint.point.x),
        @"y": @(locationPoint.point.y)
      };
      NSDictionary* lp = @{
        @"locationId": @(locationPoint.locationId),
        @"sublocationId": @(locationPoint.sublocationId),
        @"point": point
      };
      [pointsArray addObject:lp];
    }

    NSMutableArray* eventsArray = [[NSMutableArray alloc] init];
    for(NCRouteEvent* routeEvent in routePath.events) {
      [eventsArray addObject:@{
        @"type" : @(routeEvent.type),
        @"value" : @(routeEvent.value),
        @"distance" : @(routeEvent.distance),
      }];
    }
    NSDictionary* data = @{
      @"length" : @(routePath.length),
      @"points" : pointsArray,
      @"events" : eventsArray,
    };
    self.onPathsUpdated(data);
  }
}

- (void) setTarget:(NCLocationPoint * _Nonnull) targetPoint
{
  [_routeManager setTarget:targetPoint];
}

- (void) clearTargets
{
  [_routeManager clearTargets];
}

- (void)locationView:(NCLocationView *)view
          recognizer:(UIGestureRecognizer *)recognizer
didRecognizeSingleTapGesture:(CGPoint)location {
    if (self.onMapPress) {
        NSDictionary* data = @{
            @"x": [NSNumber numberWithDouble:location.x],
            @"y": [NSNumber numberWithDouble:location.y],
        };
        self.onMapPress(data);
    }
}

- (void)locationView:(NCLocationView *)view
     recognizer:(UIGestureRecognizer *)recognizer
didRecognizeLongPressGesture:(CGPoint)location {
  if (self.onMapLongPress) {
        NSDictionary* data = @{
            @"x": [NSNumber numberWithDouble:location.x],
            @"y": [NSNumber numberWithDouble:location.y],
        };
        self.onMapLongPress(data);
    }
}

@synthesize reactTag;

@end
