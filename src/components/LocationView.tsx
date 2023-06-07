import {Component, createRef} from 'react';
import React from 'react';
import {
  Platform,
  requireNativeComponent,
  NativeModules,
  UIManager,
  findNodeHandle,
  ViewProps,
  NativeSyntheticEvent,
} from 'react-native';
// @ts-ignore

import CallbacksManager from '../utils/CallbacksManager';
import { LocationPoint, Position, Point, RoutePath } from '../interfaces';

const { NavigineModule: NativeNavigineModule, LocationViewModule: NavigineLocationView } = NativeModules;

export interface NavigineViewProps extends ViewProps {
    onPositionUpdated?: (event: NativeSyntheticEvent<Position>) => void;
    onPathsUpdated?: (event: NativeSyntheticEvent<RoutePath>) => void;
    onMapPress?: (event: NativeSyntheticEvent<Point>) => void;
    onMapLongPress?: (event: NativeSyntheticEvent<Point>) => void;
}

const NativeViewModule = requireNativeComponent<NavigineViewProps>('NavigineLocationView');

export class LocationView extends Component<NavigineViewProps> {

  // @ts-ignore
  view = createRef<NativeViewModule>();

  public static init(userHash: string, server: string): Promise<void> {
    return NativeNavigineModule.init(userHash, server);
  }

  public setLocationId(locationId: number) {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      this.getCommand('setLocationId'),
      [locationId],
    );
  }

  public setSublocationId(locationId: number) {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      this.getCommand('setSublocationId'),
      [locationId],
    );
  }

  public setTarget(locationPoint: LocationPoint) {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      this.getCommand('setTarget'),
      [locationPoint],
    );
  }

  public clearTargets() {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      this.getCommand('clearTargets')
    );
  }

  public screenPositionToMeters(screenPosition: Point, callback: (position: Point) => void) {
    const cbId = CallbacksManager.addCallback(callback);
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this),
      this.getCommand('screenPositionToMeters'),
      [cbId, screenPosition],
    );
  }

  private getCommand(cmd: string): any {
    if (Platform.OS === 'ios') {
      return UIManager.getViewManagerConfig('NavigineLocationView').Commands[cmd];
    } else {
      return cmd;
    }
  }

  private processScreenPositionToMeters(event: any) {
    const { id, ...position } = event.nativeEvent;
    CallbacksManager.call(id, position);
  }

  private getProps() {
    const props = {
      ...this.props,
      onScreenPositionToMetersReceived: this.processScreenPositionToMeters,
    };
    return props;
  }

  render() {
    return (
      <NativeViewModule
        {...this.getProps()}
        ref={this.view}
      />
    );
  }
}
