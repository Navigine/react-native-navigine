#import "RNNavigineModule.h"
#import <React/RCTLog.h>

#import <Navigine/Navigine.h>

@implementation NavigineModule

@synthesize view;

- (instancetype) init {
    self = [super init];
    if (self) {
        view = [[NavigineLocationView alloc] init];
    }

    return self;
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

- (dispatch_queue_t)methodQueue{
    return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(init: (NSString *) userHash server:(NSString *) server
          resolver:(RCTPromiseResolveBlock)resolve
          rejecter:(RCTPromiseRejectBlock)reject) {
    @try {
        _sdk = [NCNavigineSdk getInstance];
        [_sdk setUserHash: userHash];
        [_sdk setServer: server];

        resolve(nil);
    } @catch (NSException *exception) {
        NSError *error = nil;
        if (exception.userInfo.count > 0) {
            error = [NSError errorWithDomain:NSCocoaErrorDomain code:0 userInfo:exception.userInfo];
        }
        reject(exception.name, exception.reason ?: @"Error initiating NavigineSDK", error);
    }
}

RCT_EXPORT_MODULE();
@end
