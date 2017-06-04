package io.example.peanutbutter.prototype;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscoverTileFragmentBack.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscoverTileFragmentBack#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverTileFragmentBack extends DiscoverTileFragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private MapView mMapView;
    private ImageButton mBackButton;

    private DiscoverTile mTile;
    private GoogleMap mMap;
    private int width;
    private LatLng coordinate;

    private OnFragmentInteractionListener mListener;

    public DiscoverTileFragmentBack() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverTileFragmentBack.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverTileFragmentBack newInstance(String param1, String param2) {
        DiscoverTileFragmentBack fragment = new DiscoverTileFragmentBack();
        Bundle args = new Bundle();
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
        final View view = inflater.inflate(R.layout.fragment_discovertile_back, container, false);

        // Get Data sent by activity.
        Bundle bundle = getArguments();
        mTile = bundle.getParcelable("KEY");
        coordinate = new LatLng(mTile.getLat(), mTile.getLng());

        //MapView requires that the Bundle you pas contain ONLY MapView SDK objects or sub-bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        // Set the adapter
        /*if (view instanceof RecyclerView) {
            Log.d(TAG, "onCreateView: Instance of RecyclerView" );
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager llm = new LinearLayoutManager(context);

            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(new DTRecyclerViewAdapter(context,Test, mListener));
        }*/

        return view;
    }

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        /*if (context instanceof DiscoverTileFragment.OnListFragmentInteractionListener) {
            mListener = (DiscoverTileFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMapFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
