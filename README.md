
[![SWUbanner](https://i.ibb.co/DK0WNM7/react-native.jpg)](https://github.com/Navigine/react-native-navigine)

## Description
The following sections contain a library  for using our SDK (android, iOS) in react-native. Our solution provides a 100% precise on both iOS and Android for react native apps. 

## Useful links 
- [SDK Documentation](https://github.com/Navigine/Indoor-Navigation-Android-Mobile-SDK-2.0/wiki)

- Refer to the [Navigine official documentation](https://docs.navigine.com/)  for a complete list of downloads, useful materials, information about the company, and so on.
- [Get started](https://client.navigine.com/login)  with Navigine to get full access to Navigation services, SDKs, and applications.
- Refer to the Navigine - [User Manual](https://docs.navigine.com/) for complete product usage guidelines.
- Find company contact information at the official website under the [Contacts tab](https://navigine.com/contacts/).
## Example project

- [Navigine basic example](https://github.com/Navigine/react-native-navigine-example)

## Basic Usage

```
// js
import {
    IconMapObject,
    PolylineMapObject,
    CircleMapObject,
    Animation,
    Position,
    Point,
    LocationPoint,
    RoutePath,
    LocationPolyline,
    Polyline
} from 'react-native-navigine'

import LocationView from 'react-native-navigine';
```

### Components usage
```typescript jsx
import React from 'react';
import LocationView from 'react-native-navigine';

class Map extends React.Component {
  render() {
    return (
      <LocationView
        source={{ uri: 'you_image_url' }}
        style={{ flex: 1 }}
        size={{
            width: 22,
            height: 22,
        }}
        visible={true}
        interactive={true}
      />
    );
  }
}
```

#### Common types
```typescript
export interface Point {
    x: number,
    y: number,
}

export interface Polyline {
    points: Point[]
}

export interface LocationPoint {
  locationId: number,
  sublocationId: number,
  point: Point,
}

export interface LocationPolyline {
    locationId: number,
    sublocationId: number,
    polyline: Polyline,
}

export interface Position {
  point: Point;
  locationId: number;
  sublocationId: number;
  accuracy: number;
  azimuth: number;
}

export enum RouteEventType {
    TURNLEFT,
    TURNRIGHT,
    TRANSITION,
}

export interface RouteEvent {
    type: RouteEventType;
    value: number;
    distance: number;
  }

export interface RoutePath {
    length: number;
    events: RouteEvent[];
    points: LocationPoint[];
  }

export enum Animation {
    NONE,
    LINEAR,
    CUBIC,
    QUINT,
    SINE,
}
```
### LocationView - main view with sublocation content

#### LocationView `props`
- `onPositionUpated?: (event: NativeSyntheticEvent<Position>) => void;`
- `onPathsUpdated?: (event: NativeSyntheticEvent<RoutePath>) => void;`
- `onMapPress?: (event: NativeSyntheticEvent<Point>) => void;`
- `onMapLongPress?: (event: NativeSyntheticEvent<Point>) => void;`

#### LocationView methods (avaliable via ref)
- `public setLocationId(locationId: number)`
- `public setSublocationId(sublocationId: number)`
- `public setTarget(locationPoint: LocationPoint)`
- `public clearTargets()`
- `public screenPositionToMeters(screenPosition: Point, callback: (position: Point) => void)`

### IconMapObject - map object created by user with bitmap image inside

#### Example of usage
```
import { IconMapObject } from 'react-native-navigine'

<LocationView
    ref={this.view}
    style={styles.locationView}
    onPositionUpated={this.onPositionUpated}
    onPathsUpdated={this.onPathsUpdated}
    onMapPress={this.onMapPress}
    onMapLongPress={this.onMapLongPress}>
    <IconMapObject
        locationPoint={this.state.userPosition}
        source={USER}
        size={{
            width: 22,
            height: 22,
        }}
        styling={'{ order: 1, collide: false}'}
        visible={true}
        interactive={true}
    />
</LocationView>
```
#### IconMapObject `props`
```
  locationPoint: LocationPoint;
  source?: ImageSourcePropType;
  size?: { width: number, height: number };
  styling?: string;
  visible?: boolean;
  interactive?: boolean;
```
