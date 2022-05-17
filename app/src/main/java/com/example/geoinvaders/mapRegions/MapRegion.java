package com.example.geoinvaders.mapRegions;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapRegion {
    private ArrayList<LatLng> points = new ArrayList<>();
    private LatLng center;

    public MapRegion(LatLng p1, LatLng p2, LatLng p3, LatLng p4) {
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);

        center = getCenterOfPolygon(points);
    }

    public ArrayList<LatLng> getPoints() {
        return points;
    }

    public LatLng getCenter() {
        return center;
    }

    private static LatLng getCenterOfPolygon(List<LatLng> latLngList) {
        double[] centroid = {0.0, 0.0};
        for (int i = 0; i < latLngList.size(); i++) {
            centroid[0] += latLngList.get(i).latitude;
            centroid[1] += latLngList.get(i).longitude;
        }
        int totalPoints = latLngList.size();
        return new LatLng(centroid[0] / totalPoints, centroid[1] / totalPoints);
    }
}
