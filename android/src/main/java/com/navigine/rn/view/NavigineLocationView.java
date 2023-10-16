package com.navigine.rn.view;

import android.content.Context;
import android.graphics.PointF;
import android.view.View;

import com.navigine.idl.java.InputListener;
import com.navigine.rn.models.ReactMapObject;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.navigine.idl.java.CircleMapObject;
import com.navigine.idl.java.IconMapObject;
import com.navigine.idl.java.LocationManager;
import com.navigine.idl.java.LocationPoint;
import com.navigine.idl.java.MapObject;
import com.navigine.idl.java.NavigationManager;
import com.navigine.idl.java.NavigineSdk;
import com.navigine.idl.java.Point;
import com.navigine.idl.java.PolylineMapObject;
import com.navigine.idl.java.Position;
import com.navigine.idl.java.PositionListener;
import com.navigine.idl.java.RouteEvent;
import com.navigine.idl.java.RouteListener;
import com.navigine.idl.java.RouteManager;
import com.navigine.idl.java.RoutePath;
import com.navigine.view.LocationView;
import com.navigine.view.TouchInput;

import java.util.ArrayList;
import java.util.List;

public class NavigineLocationView extends LocationView {
    private final List<ReactMapObject> childs = new ArrayList<>();
    private final LocationManager mLocationManager;
    private NavigationManager mNavigationManager;
    private RouteManager mRouteManager;

    public NavigineLocationView(Context context) {
        super(context);
        mLocationManager = NavigineSdk.getInstance().getLocationManager();
        mNavigationManager = NavigineSdk.getInstance().getNavigationManager(mLocationManager);

        mNavigationManager.addPositionListener(new PositionListener() {
            @Override
            public void onPositionUpdated(Position position) {
                WritableMap point = Arguments.createMap();
                point.putDouble("x", position.getLocationPoint().getPoint().getX());
                point.putDouble("y", position.getLocationPoint().getPoint().getY());

                WritableMap locationPoint = Arguments.createMap();
                locationPoint.putMap("point", point);
                locationPoint.putInt("locationId", position.getLocationPoint().getLocationId());
                locationPoint.putInt("sublocationId", position.getLocationPoint().getSublocationId());

                WritableMap globalPoint = Arguments.createMap();
                globalPoint.putDouble("latitude", position.getPoint().getLatitude());
                globalPoint.putDouble("longitude", position.getPoint().getLongitude());


                WritableMap data = Arguments.createMap();
                data.putMap("point", globalPoint);
                data.putDouble("accuracy", position.getAccuracy());
                data.putDouble("heading", position.getHeading());
                data.putMap("locationPoint", locationPoint);
                data.putDouble("heading", position.getLocationHeading());

                ReactContext reactContext = (ReactContext) getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onPositionUpdated", data);
            }

            @Override
            public void onPositionError(Error error) {

            }
        });

        mRouteManager = NavigineSdk.getInstance().getRouteManager(mLocationManager, mNavigationManager);
        mRouteManager.addRouteListener(new RouteListener() {
            @Override
            public void onPathsUpdated(ArrayList<RoutePath> arrayList) {
                if (arrayList.size() != 0) {
                    RoutePath routePath = arrayList.get(0);
                    WritableMap result = Arguments.createMap();
                    result.putDouble("length", routePath.getLength());

                    WritableArray locationPoints = Arguments.createArray();
                    for (LocationPoint locationPoint : routePath.getPoints()) {
                        WritableMap locationPointMap = Arguments.createMap();
                        locationPointMap.putInt("locationId", locationPoint.getLocationId());
                        locationPointMap.putInt("sublocationId", locationPoint.getSublocationId());
                        WritableMap pointMap = Arguments.createMap();
                        pointMap.putDouble("x", locationPoint.getPoint().getX());
                        pointMap.putDouble("y", locationPoint.getPoint().getY());
                        locationPointMap.putMap("point", pointMap);
                        locationPoints.pushMap(locationPointMap);
                    }
                    result.putArray("points", locationPoints);

                    WritableArray routeEvents = Arguments.createArray();
                    for (RouteEvent routeEvent : routePath.getEvents()) {
                        WritableMap routeEventMap = Arguments.createMap();
                        routeEventMap.putInt("type", routeEvent.getType().ordinal());
                        routeEventMap.putDouble("value", routeEvent.getValue());
                        routeEventMap.putDouble("distance", routeEvent.getDistance());
                        routeEvents.pushMap(routeEventMap);
                    }
                    result.putArray("events", routeEvents);
                    ReactContext reactContext = (ReactContext) getContext();
                    reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onPathsUpdated", result);
                }
            }
        });

        getLocationWindow().addInputListener(new InputListener() {
            @Override
            public void onViewTap(PointF pointF) {
                WritableMap data = Arguments.createMap();
                data.putDouble("x", pointF.x);
                data.putDouble("y", pointF.y);
                ReactContext reactContext = (ReactContext) getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onMapPress", data);
            }

            @Override
            public void onViewDoubleTap(PointF pointF) {

            }

            @Override
            public void onViewLongTap(PointF pointF) {
                WritableMap data = Arguments.createMap();
                data.putDouble("x", pointF.x);
                data.putDouble("y", pointF.y);
                ReactContext reactContext = (ReactContext) getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onMapLongPress", data);
            }
        });
    }

    // ref methods
    public void setLocationId(int locationId) {
        mLocationManager.setLocationId(locationId);
    }

    public void setSublocationId(int sublocationId) {
        super.getLocationWindow().setSublocationId(sublocationId);
    }

    public void screenPositionToMeters(String id, ReadableMap pointMap) {
        float x = pointMap.getInt("x");
        float y = pointMap.getInt("y");
        Point point = getLocationWindow().screenPositionToMeters(new PointF(x, y));
        WritableMap metersPoint = Arguments.createMap();
        metersPoint.putDouble("x", point.getX());
        metersPoint.putDouble("y", point.getY());
        metersPoint.putString("id", id);
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "screenPositionToMeters", metersPoint);
    }

    public void setTarget(ReadableMap locationPointMap) {
        ReadableMap pointMap = locationPointMap.getMap("point");
        float x = pointMap.getInt("x");
        float y = pointMap.getInt("y");
        int locationId = locationPointMap.getInt("locationId");
        int sublocationId = locationPointMap.getInt("sublocationId");
        Point point = new Point(x, y);

        mRouteManager.setTarget(new LocationPoint(point, locationId, sublocationId));
    }

    public void clearTargets() {
        mRouteManager.clearTargets();
    }

    public void addFeature(View child, int index) {
        if (child instanceof NavigineIconMapObject){
            NavigineIconMapObject _child = (NavigineIconMapObject) child;
            IconMapObject obj = getLocationWindow().addIconMapObject();
            _child.setMapObject(obj);
            childs.add(_child);
         }else if(child instanceof NavigineCircleMapObject){
             NavigineCircleMapObject _child = (NavigineCircleMapObject) child;
             CircleMapObject obj = getLocationWindow().addCircleMapObject();
             _child.setMapObject(obj);
             childs.add(_child);
        }else if(child instanceof NaviginePolylineMapObject){
            NaviginePolylineMapObject _child = (NaviginePolylineMapObject) child;
            PolylineMapObject obj = getLocationWindow().addPolylineMapObject();
            _child.setMapObject(obj);
            childs.add(_child);
        }
    }

    public void removeChild(int index) {
        if (getChildAt(index) instanceof ReactMapObject) {
            final ReactMapObject child = (ReactMapObject) getChildAt(index);
            if (child == null) return;

                final MapObject mapObject = child.getMapObject();
                if (mapObject == null) return;
                if (mapObject instanceof IconMapObject) {
                    final IconMapObject iconMapObject = (IconMapObject) mapObject;
                    getLocationWindow().removeIconMapObject(iconMapObject);
                } else if (mapObject instanceof PolylineMapObject) {
                    final PolylineMapObject polylineMapObject = (PolylineMapObject) mapObject;
                    getLocationWindow().removePolylineMapObject(polylineMapObject);
                } else if (mapObject instanceof CircleMapObject) {
                    final CircleMapObject circleMapObject = (CircleMapObject) mapObject;
                    getLocationWindow().removeCircleMapObject(circleMapObject);
                }
        }
    }
}