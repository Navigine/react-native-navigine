export interface Point {
    x: number,
    y: number,
}

export interface GlobalPoint {
  latitude: number,
  longitude: number,
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
  point: GlobalPoint;
  accuracy: number;
  heading: number;
  locationPoint: LocationPoint;
  locationHeading: number;
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
