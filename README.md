
[![SWUbanner](https://i.ibb.co/DK0WNM7/react-native.jpg)](https://github.com/Navigine/react-native-navigine)

**What is React Native?**

React Native is an open-source UI software framework, used to develop applications for Android, iOS. React Native offers a large amount of inbuilt components and APIs and is well suited for creating location-powered applications with various features, such as indoor wayfinding, real time positioning and geomarketing instruments. 

## Description
The following sections contain a library  for using our SDK (android, iOS) in react-native. Our solution provides a 100% precise on both iOS and Android for react native apps. 

## Useful links 
1. [SDK Documentation](https://github.com/Navigine/Indoor-Navigation-Android-Mobile-SDK-2.0/wiki)
2. Refer to the [Navigine official documentation](https://navigine.com/documentation/) for complete list of downloads, useful materials, information about the company, and so on.
3. [Get started](http://locations.navigine.com/login) with Navigine to get full access to Navigation services, SDKs, and applications.
4. Refer to the Navigine [User Manual](http://docs.navigine.com/) for complete product usage guidelines.
5. Find company contact information at the official website under [Contact](https://navigine.com/contacts/) tab.
6. Find information about Navigine’s Open Source Initiative [here](https://navigine.com/open-source/)


## Values and benefits
React Native is a versatile platform that can be used in many industries, such as retail, healthcare, logistics, and transportation. With the Navigine SDK for React Native, businesses in these industries can take advantage of accurate indoor positioning and tracking to improve their operations and provide a better experience for their customers.

Using the Navigine SDK for React Native provides several benefits, including:

High accuracy: Navigine's indoor positioning technology is highly accurate, with an average accuracy of 2-5 meters, ensuring that businesses can rely on the location data for their operations.

Customizable: The Navigine SDK for React Native is highly customizable, allowing businesses to tailor the technology to their specific needs.

Scalable: The SDK is designed to be scalable, making it suitable for businesses of all sizes.

User-friendly: Navigine's SDK is user-friendly, with a simple and intuitive interface that makes it easy to integrate into any application.

For example, in retail, businesses can use indoor positioning to guide customers to products they are looking for and provide personalized offers based on their location in the store. In healthcare, indoor positioning can be used to track patients and medical equipment, improving efficiency and reducing errors. In logistics and transportation, indoor positioning can be used to track assets, optimize routes, and improve safety.

In summary, the Navigine SDK for React Native provides businesses with an accurate and reliable indoor positioning solution that can be customized to their specific needs, improving their operations and providing a better experience for their customers.

## Getting started - project example

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
