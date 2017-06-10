package io.example.peanutbutter.prototype;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.maps.android.PolyUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static io.example.peanutbutter.prototype.DiscoverActivity.calculateInSampleSize;

public class PlanActivity extends AppCompatActivity
        implements OnMapReadyCallback, MiniTilesFragment.OnMiniTilesInteractionListener {

    private static final String TAG = "PlanActivity";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private ArrayList<DiscoverTile> Test;
    private ArrayList<LatLng> mCoordinatelist;
    private GoogleMap mMap;
    private PolylineOptions mPolylineOptions;
    private LatLng coordinates;
    private int noOfMarkers = 0;

    private MiniTilesFragment mMiniTilesFragment;
    private Button route;
    private MapView mMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Initialize mapview
        mMapView = (MapView) findViewById(R.id.plan_mapview);

        //MapView requires that the Bundle you pas contain ONLY MapView SDK objects or sub-bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);


        // Set floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Set Route button
        mCoordinatelist = new ArrayList<LatLng>();
        route = (Button) findViewById(R.id.route_button);


        // Setting database
        Test = new ArrayList<DiscoverTile>();
        ArrayList<Integer> item1activities = new ArrayList<>();
        ArrayList<Integer> item2activities = new ArrayList<>();
        ArrayList<Integer> item3activities = new ArrayList<>();


        item1activities.add(R.drawable.ic_kayaking);
        item1activities.add(R.drawable.ic_swimming);
        item1activities.add(R.drawable.ic_tent);
        item2activities.add(R.drawable.ic_hiking);
        item2activities.add(R.drawable.ic_running);
        item3activities.add(R.drawable.ic_running);
        item3activities.add(R.drawable.ic_kayaking);


        Test.add(new DiscoverTile("Rangitikei River", compressPhoto(R.drawable.one), item1activities, -39.92, 175.5814));
        Test.add(new DiscoverTile("Item_2", compressPhoto(R.drawable.two), item2activities, -44.7, 169.1321));
        Test.add(new DiscoverTile("Item_3", compressPhoto(R.drawable.three), item3activities, -37.7, 176.1321));
        Test.add(new DiscoverTile("Item_4", compressPhoto(R.drawable.four), item1activities, -39.29, 174.06));
        // Test.add(new DiscoverTile("Item_5", compressPhoto(R.drawable.five), item1activities));

        // Set up vertical minilist fragment
        mMiniTilesFragment = new MiniTilesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("KEY", Test);

        mMiniTilesFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.tiles_list, mMiniTilesFragment);

        fragmentTransaction.commit();
    }

    @Override
    public void onMiniTilesFragmentInteraction(DiscoverTile tile) {
        coordinates = new LatLng(tile.getLat(), tile.getLng());
        //Bitmap compress = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), tile.getPhoto()), 10, 10, true);

        //Add marker
        mMap.addMarker(new MarkerOptions()
                .position(coordinates)
                .title(tile.getName()));
        noOfMarkers++;

        // Store coordinate of Tile for routing.
        mCoordinatelist.add(coordinates);

        // Set camera center and zoom
        if (noOfMarkers < 2) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, (float) 13));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, (float) 5));
        }

        route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mPolylineOptions = new PolylineOptions().geodesic(true).width(10)
                        .add(new LatLng(-36.88, 174.76));
                for (int i = 0; i < mCoordinatelist.size(); i++) {
                    mPolylineOptions.add(mCoordinatelist.get(i));
                }*/

                // ArrayList of destinations have been created.

                if (mCoordinatelist.size() > 1) {
                    LatLng origin = new LatLng(-36.88, 174.76);

                    // Sort out array for waypoints.
                    ArrayList<LatLng> waypoints = new ArrayList<LatLng>();
                    waypoints = arrangeLatLng(mCoordinatelist); // Last index in waypoint is destination.
                    LatLng dest = waypoints.get(waypoints.size()-1);


                    //Get URL to Google Directions API
                    String url = getDirectionsUrl(origin, dest, waypoints);
                    Log.d(TAG, "URL FETCHED: "+ url);
                    FetchURL fetchURL = new FetchURL();

                    // Start download json data from Google Directions API
                    fetchURL.execute(url);

                }


                //TODO: LEARN GOOGLE DIRECTIONS API.

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.addMarker(new MarkerOptions().position(new LatLng(-36.88, 174.76)).title("Home")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home)));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Ensures Mapview only has a mapview bundle
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest, ArrayList<LatLng> waypoints) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Waypoints within the route
        String str_way = "waypoints=";
        for (int i = 0; i < waypoints.size() - 1; i++) {
            Double lat = waypoints.get(i).latitude;
            Double lng = waypoints.get(i).longitude;
            if (i != waypoints.size() - 2) {
                str_way = str_way.concat(lat.toString() + "," + lng.toString() + "|");
            } else {
                str_way = str_way.concat(lat.toString() + "," + lng.toString());
            }

        }
        Log.d(TAG, str_way);

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest +"&" + str_way + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public ArrayList<LatLng> arrangeLatLng(ArrayList<LatLng> coordinates) {
        ArrayList<Location> holder = new ArrayList<Location>();
        ArrayList<Location> locations = new ArrayList<Location>();
        ArrayList<LatLng> result = new ArrayList<LatLng>();

        // Convert arraylist of coordinates to arraylist of locations
        for (Integer i = 0; i < coordinates.size(); i++) {
            String count = i.toString();
            locations.add(i, new Location(count));
            locations.get(i).setLatitude(coordinates.get(i).latitude);
            locations.get(i).setLongitude(coordinates.get(i).longitude);
            //Log.d(TAG, "arrangeLatLng: coordinates " + locations.get(i).getLatitude() + ", " +
            //        locations.get(i).getLongitude() + " @ " + i);
        }

        float shortestdistance;
        // Loop through locations arraylist, and rearrange in terms of distance
        for (int i = 0; i < locations.size() - 1; i++) { // Solely for accessing latest coordinates
            //Log.d(TAG, "arrangeLatLng: i is " + i);
            shortestdistance = locations.get(i).distanceTo(locations.get(i + 1));
            for (int y = i + 1; y < locations.size(); y++) {
                //Log.d(TAG, "arrangeLatLng: y is " + y);
                //Log.d(TAG, "arrangeLatLng: shortest d is "+shortestdistance);
                float dist = locations.get(i).distanceTo(locations.get(y));
                //Log.d(TAG, "arrangeLatLng: d is "+dist);
                if (shortestdistance > dist) {
                    shortestdistance = dist;
                    Collections.swap(locations, i, y);

                }
            }
        }

        holder.addAll(locations);
        Log.d(TAG, "arrangeLatLng: holder= " + holder.toString());

        // Holder contains rearranged arraylist conver to arraylist <LatLng> and return
        for (int i = 0; i < holder.size(); i++) {
            result.add(new LatLng(holder.get(i).getLatitude(), holder.get(i).getLongitude()));
        }

        return result;
    }


    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onStart() {
        mMapView.onStart();
        super.onStart();
    }

    @Override
    public void onStop() {
        mMapView.onStop();
        super.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }


    // Functions for handling image processing.
    public int compressPhoto(int photoID) {
        // This method compresses a photo to a suitable size to be displayed on cards.

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), photoID, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        // Load a Scaled Down Version into Memory
        options.inSampleSize = calculateInSampleSize(options, 1000, 750);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return photoID;


    }

    // Classes
    // Fetches data from URL passed.
    private class FetchURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            //'data' stores data from web service
            String data = "";

            try {
                // Fetch data rom web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for pasing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);
                lineOptions.color(Color.BLACK);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }
}
