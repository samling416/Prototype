package io.example.peanutbutter.prototype;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExpandedDiscoverTile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExpandedDiscoverTile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpandedDiscoverTile extends Fragment implements OnMapReadyCallback{

    private static final String TAG = "ExpandedDiscoverTile";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    // TODO: Rename and change types of parameters
    private MapView mMapView;
    private DiscoverTile mTile;
    private GoogleMap mMap;
    private int width;
    private LatLng coordinate;

    private OnFragmentInteractionListener mListener;

    public ExpandedDiscoverTile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpandedDiscoverTile.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpandedDiscoverTile newInstance(String param1, String param2) {
        ExpandedDiscoverTile fragment = new ExpandedDiscoverTile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_expanded_discover_tile, container, false);

        //Tracking debug statement
        //Log.d(TAG, "onCreateView: ");




        // Set layout with correct data.
        TextView textView = (TextView) v.findViewById(R.id.primary_Title);
        textView.setText(mTile.getName());
        ImageView imageView = (ImageView)v.findViewById(R.id.expanded_Imageview);
        imageView.setImageResource(mTile.getPhoto());

        //MapView requires that the Bundle you pas contain ONLY MapView SDK objects or sub-bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null){
            mapViewBundle =  savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        // Initializes MapView on Fragment. OnMapReady() determines what is displayed.
        mMapView = (MapView) v.findViewById(R.id.expanded_MapView);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Ensures Mapview only has a mapview bundle
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null){
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMapFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    // OnMapReady is called whe mapView is ready. Modify mapView here.
   @Override
    public void onMapReady(GoogleMap googleMap) {


       mMap = googleMap;

       // Adjust settings of map
       mMap.getUiSettings().setTiltGesturesEnabled(false);
       mMap.getUiSettings().setRotateGesturesEnabled(false);
       mMap.getUiSettings().setScrollGesturesEnabled(false);

       mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

       //Add marker
       mMap.addMarker(new MarkerOptions().position(coordinate).title("Test"));
       // Set camera center and zoom
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, (float) 13));

       // Resize views to maintain ratios as defined by guidelines.
       mMapView.requestLayout();
       width = mMapView.getWidth();
       //Log.d(TAG, "MapView width is " + width);
       mMapView.getLayoutParams().height = (width / 3); // Maintain a 3:1 ratio.
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
