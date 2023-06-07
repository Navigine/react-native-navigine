package com.navigine.rn;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.navigine.rn.view.NavigineLocationView;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

import java.util.Map;

public class NavigineViewManager extends ViewGroupManager<NavigineLocationView> {

    public static final String REACT_CLASS = "NavigineLocationView";

    private static final int SET_LOCATION_ID = 1;
    private static final int SET_SUBLOCATION_ID = 2;
    private static final int SCREEN_POSITION_TO_METERS = 3;
    private static final int SET_TARGET_POINT = 4;
    private static final int CLEAR_TARGETS = 5;

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .build();
    }

    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("onPositionUpdated", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onPositionUpdated")))
                .put("onPathsUpdated", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onPathsUpdated")))
                .put("onMapPress", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMapPress")))
                .put("onMapLongPress", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onMapLongPress")))
                .put("screenPositionToMeters", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onScreenPositionToMetersReceived")))
                .build();
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        Map<String, Integer> map = MapBuilder.newHashMap();
        map.put("setLocationId", SET_LOCATION_ID);
        map.put("setSublocationId", SET_SUBLOCATION_ID);
        map.put("screenPositionToMeters", SCREEN_POSITION_TO_METERS);
        map.put("setTarget", SET_TARGET_POINT);
        map.put("clearTargets", CLEAR_TARGETS);

        return map;
    }

    @Override
    public void receiveCommand(
            @NonNull NavigineLocationView view,
            String commandType,
            @Nullable ReadableArray args) {
        Assertions.assertNotNull(view);
        Assertions.assertNotNull(args);
        switch (commandType) {
            case "setLocationId":
                if (args != null) {
                    view.setLocationId(args.getInt(0));
                }
                return;
            case "setSublocationId":
                if (args != null) {
                    view.setSublocationId(args.getInt(0));
                }
                return;
            case "screenPositionToMeters":
                if (args != null) {
                    view.screenPositionToMeters(args.getString(0), args.getMap(1));
                }
                return;
            case "setTarget":
                if (args != null) {
                    view.setTarget(args.getMap(0));
                }
                return;
            case "clearTargets":
                if (args != null) {
                    view.clearTargets();
                }
                return;
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandType,
                        getClass().getSimpleName()));
        }
    }

    private NavigineLocationView castToNavigineLocationView(View view) {
        return (NavigineLocationView) view;
    }

    @Override
    protected NavigineLocationView createViewInstance(ThemedReactContext reactContext) {
        NavigineLocationView locationView = new NavigineLocationView(reactContext);
        return locationView;
    }
    @Override
    public void addView(NavigineLocationView parent, View child, int index) {
        parent.addFeature(child, index);
        super.addView(parent, child, index);
    }

    @Override
    public void removeViewAt(NavigineLocationView parent, int index) {
        parent.removeChild(index);
        super.removeViewAt(parent, index);
    }
}
