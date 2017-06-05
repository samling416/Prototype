package io.example.peanutbutter.prototype;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback {

    private static final String TAG = "MapDialogFragment";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String CoordinatesKey = "mCoordinatesKey";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";


    // TODO: Rename and change types of parameters
    private MapView mMapView;
    private SeekBar mSeekbar;
    private EditText mRadiusText;
    private GoogleMap mMap;
    private Button mSet;
    private Button mCancel;
    private int width;
    private LatLng coordinate;
    private Circle mRadius;
    private double rad;
    private Bundle coordinatesBundle;
    private OnFragmentInteractionListener mListener;

    public MapDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapDialogFragment newInstance(String param1, String param2) {
        MapDialogFragment fragment = new MapDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 1st");

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_dialog, container, false);

        // Get data sent from activity
        coordinatesBundle = getArguments();
        coordinate = coordinatesBundle.getParcelable(CoordinatesKey);


        //MapView requires that the Bundle you pas contain ONLY MapView SDK objects or sub-bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        //Set up mapView
        mMapView = (MapView) v.findViewById(R.id.dialog_mapview);

        // Remove darkoverlay from MapView that is naturally applied by dialogfragment.
        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        //Set up radius slider
        mSeekbar = (SeekBar) v.findViewById(R.id.radius_slider);
        mRadiusText = (EditText) v.findViewById(R.id.radius_text);
        int maxLength = 2;
        mRadiusText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        // Set up set and cancel buttons
        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: 2nd");
        super.onViewCreated(view, savedInstanceState);
    }

    // OnMapReady is called whe mapView is ready. Modify mapView here.
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady: 3rd");

        mMap = googleMap;

        // Adjust settings of map
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Add marker
        mMap.addMarker(new MarkerOptions().position(coordinate).title("Test"));
        // Set camera center and zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, (float) 13));
        // Add a radius circle
        CircleOptions circleOptions = new CircleOptions()
                .center(coordinate)
                .radius(1000)//in meters
                .strokeWidth(2)
                .fillColor(Color.TRANSPARENT);

        mRadius = mMap.addCircle(circleOptions);

        // Resize views to maintain ratios as defined by guidelines.
        mMapView.requestLayout();
        width = mMapView.getWidth();
        Log.d(TAG, "MapView width is " + width);
        mMapView.getLayoutParams().height = 2 * (width / 3); // Maintain a 3:2 ratio.

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {

                //float zoom = (float) (13.586 + (-0.04545) * progress);
                //Log.d(TAG, "onProgressChanged: Zoom is " + zoom);
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));

                rad = (242 * progress) + 758;
                Log.d(TAG, "onProgressChanged: Radius is " + rad);
                mRadius.setRadius(rad);
                mRadiusText.setText(Integer.toString((int) (rad / 1000)));

                if (rad <= 4000) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 12));
                } else if ((rad > 4000) & (rad <= 8000)) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 11));
                } else if ((rad > 8000) & (rad <= 12000)) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 10));
                } else if ((rad > 12000) & (rad <= 20000)) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 9));
                } else if ((rad > 20000)) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 8));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Allow for edit text to manipulate slider
        // Issue: Glitches when strings input.
        // TODO: FIX Limiting character input and glitches when characters input.
        mRadiusText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) & (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Double checkrad = Double.parseDouble(mRadiusText.getText().toString());
                    if ((checkrad < 25) & (checkrad > 0)) {
                        rad = checkrad * 1000; //Convert km to m
                    }else {
                        //Input is out of range.
                        rad = 24000;
                    }
                    //Log.d(TAG, "onKey: Rad set to " + rad);
                    double newProgress = (rad - 758) / 242;
                    //Log.d(TAG, "onKey: newP set to " + newProgress);
                    mSeekbar.setProgress((int) (newProgress + 1));
                    //Log.d(TAG, "onKey: Prog set to " + mSeekbar.getProgress());

                    //Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mRadiusText.getWindowToken(), 0);

                    return true;
                } else {
                    return false;
                }
            }
        });

        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send data to activity.
                mListener.onMapDialogFragmentInteraction(rad);
                dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dismiss DialogFragment oncancel click.
                dismiss();
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
                    + " must implement OnPlanMapFragmentInteractionListener");
        }
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
        void onMapDialogFragmentInteraction(double radius);
    }
}
