package com.example.geoinvaders.mapRegions;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RegionsHelp {
    double EARTH_RADIUS = 6371.009;

    public LatLng destination(LatLng point, double point_bearing, double distance) {
        double lat1 = Math.toRadians(point.latitude);
        double lng1 = Math.toRadians(point.longitude);
        double bearing = Math.toRadians(point_bearing);

        double d_div_r = distance / EARTH_RADIUS;

        double lat2 = Math.asin(
                Math.sin(lat1) * Math.cos(d_div_r) +
                        Math.cos(lat1) * Math.sin(d_div_r) * Math.cos(bearing)
        );

        double lng2 = lng1 + Math.atan2(
                Math.sin(bearing) * Math.sin(d_div_r) * Math.cos(lat1),
                Math.cos(d_div_r) - Math.sin(lat1) * Math.sin(lat2)
        );

        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lng2));
    }

    public ArrayList<ArrayList<MapRegion>> calculatePolygon() {

        ArrayList<ArrayList<MapRegion>> regions = new ArrayList<>();

        double distance = 100.0 / 1000.0;

        double beginLatitude = 55.608357720913865;
        double beginLongitude = 37.35292325439079;

        double endLatitude = 55.774548;
        double endLongitude = 37.721793;

        double i = beginLatitude;
        double j = beginLongitude;

        LatLng p1;
        LatLng p2;
        LatLng p3;
        LatLng p4;

        while (i < endLatitude) {
            p1 = new LatLng(i, j);
            p2 = destination(p1, 0, distance);
            p3 = destination(p2, 90, distance);
            p4 = destination(p3, 180, distance);

            ArrayList<MapRegion> regionArrayList = new ArrayList<>();
            regionArrayList.add(new MapRegion(p1, p2, p3, p4));
            while (j < endLongitude) {
                p1 = new LatLng(p4.latitude, p4.longitude);
                p2 = destination(p1, 0, distance);
                p3 = destination(p2, 90, distance);
                p4 = destination(p3, 180, distance);

                regionArrayList.add(new MapRegion(p1, p2, p3, p4));
                j = p4.longitude;
            }
            regions.add(regionArrayList);
            i = p2.latitude;
            j = beginLongitude;
        }

        return regions;
    }
}
