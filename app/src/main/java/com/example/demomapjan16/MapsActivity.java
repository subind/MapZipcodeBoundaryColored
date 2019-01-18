package com.example.demomapjan16;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;

import java.io.IOException;

/**
 * 1. Initialise GeoJsonLayer with parameters of GoogleMap & resource file.
 * 2. Use "GeoJsonFeature feature : layer.getFeatures()" to loop through GeoJson data
 * 3. Style the map. (Polygon, LineString, Point) [Here polygon is implemented]
 * 4. Add GeoJsonLayer to the map
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private final static String mLogTag = "GeoJsonDemo";

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Move the camera
        LatLng northCarolina = new LatLng(38.8760098, -77.0253225);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(northCarolina));
        retrieveFileFromResource();
    }

    private void retrieveFileFromResource() {
        try {
            GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.dc_district_of_columbia_zip_codes_geo_code, this);
            addGeoJsonLayerToMap(layer);
        } catch (IOException e) {
            Log.e(mLogTag, "GeoJSON file could not be read");
        } catch (JSONException e) {
            Log.e(mLogTag, "GeoJSON file could not be converted to a JSONObject");
        }
    }

    private void addGeoJsonLayerToMap(GeoJsonLayer layer) {
        addColorsToMarkers(layer);
        layer.addLayerToMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(38.8760098, -77.0253225)));

        // Get the icon for the feature
        BitmapDescriptor pointIcon = BitmapDescriptorFactory.defaultMarker();
        // Create a new point style
        GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();

        layer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(Feature feature) {
                Toast.makeText(MapsActivity.this,
                        "Coordinates at: " + feature.getProperty("INTPTLAT10") + " " + feature.getProperty("INTPTLON10"),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addColorsToMarkers(GeoJsonLayer layer) {
        // Iterate over all the features stored in the layer
        for (GeoJsonFeature feature : layer.getFeatures()) {

            //Initialise polygon style
            GeoJsonPolygonStyle polygonStyle = new GeoJsonPolygonStyle();
            polygonStyle.setStrokeColor(Color.BLACK);

            switch (feature.getProperty("ZCTA5CE10")) {
                case "20004":
                    polygonStyle.setFillColor(Color.RED);
                    feature.setPolygonStyle(polygonStyle);
                    break;
                case "20535":
                    polygonStyle.setFillColor(Color.BLUE);
                    feature.setPolygonStyle(polygonStyle);
                    break;
                case "20551":
                    polygonStyle.setFillColor(Color.GREEN);
                    feature.setPolygonStyle(polygonStyle);
                    break;
                case "20011":
                    polygonStyle.setFillColor(Color.CYAN);
                    feature.setPolygonStyle(polygonStyle);
                    break;
                case "20018":
                    polygonStyle.setFillColor(Color.MAGENTA);
                    feature.setPolygonStyle(polygonStyle);
                    break;
                case "20037":
                    polygonStyle.setFillColor(Color.GREEN);
                    feature.setPolygonStyle(polygonStyle);
                    break;
                default:
                    polygonStyle.setFillColor(Color.YELLOW);
                    feature.setPolygonStyle(polygonStyle);
                    break;
            }
        }
    }

}
