// AUTOGENERATED FILE - DO NOT MODIFY!
// This file was generated by Djinni from polyline_map_object.djinni

#import "NCExport.h"
#import "NCLocationPolyline.h"
#import "NCMapObject.h"
#import <Foundation/Foundation.h>


NAVIGINE_EXPORT
@interface NCPolylineMapObject : NCMapObject

- (BOOL)setPolyLine:(nonnull NCLocationPolyline *)polyline;

- (BOOL)setWidth:(float)width;

- (BOOL)setColor:(float)red
           green:(float)green
            blue:(float)blue
           alpha:(float)alpha;

@end