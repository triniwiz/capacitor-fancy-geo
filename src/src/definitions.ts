declare global {
    interface PluginRegistry {
        Geo?: GeoPlugin;
    }
}

export interface FenceNotification {
    id: number;
    title: string;
    body: string;
}

export enum FenceTransition {
    ENTER,
    DWELL,
    EXIT,
    ENTER_EXIT,
    ENTER_DWELL,
    DWELL_EXIT,
    ALL,
}

export interface FenceOptions {
    id?: string;
    coordinates: number[];
    radius: number;
    transition: FenceTransition
    notification?: FenceNotification
}

export interface Fence {
    id: string;
    coordinates: number[];
    radius: number;
    transition: FenceTransition
    notification?: FenceNotification
}

export interface Coordinate {
    latitude: number
    longitude: number
}

export interface Location {
    coordinate: Coordinate
    altitude: number
    horizontalAccuracy: number
    verticalAccuracy: number
    speed: number
    direction: number
    timestamp: number
}

export interface LocationOptions {

}

export interface PermissionOptions {
    always?: boolean
}

export interface GeoPlugin {

    setOnMessageListener(listener: Function): void;

    createCircleFence(options: FenceOptions): Promise<string>;

    hasPermission(): Promise<boolean>;

    requestPermission(options: PermissionOptions): Promise<boolean>;

    getCurrentLocation(options: LocationOptions): Promise<Location>;

    getAllFences(): Promise<Fence[]>;

    getFence(id: string): Promise<Fence>;

    removeAllFences(): Promise<any>;

    removeFence(id: string): Promise<any>;

}
