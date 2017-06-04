package io.example.peanutbutter.prototype;

import android.content.Intent;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.transition.Transition;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.logicalAnd;

public class DiscoverActivity extends AppCompatActivity implements DiscoverTileFragment.OnListFragmentInteractionListener, BottomListFragment.Listener,
        ExpandedDiscoverTile.OnFragmentInteractionListener, MapDialogFragment.OnFragmentInteractionListener, IconBarFragment.OnIconBarInteractionListener {

    private static final String TAG = "DiscoverActivity";
    private static final String MapDialogkey = "MapDialogKey";
    private static final String CoordinatesKey = "mCoordinatesKey";


    // Request Codes
    static final int PLACE_PICKER_REQUEST = 1;
    public static final int useDTF = 0, useFTF = 1;

    private TextView locationChosen;
    private ImageButton adjustRadius;

    private String mPlaceSelected;
    private double mSelectedLat;
    private double mSelectedLng;
    private LatLng mCoordinates;

    private ArrayList<DiscoverTile> Test;
    private ArrayList<ActivityIcon> Icons;

    private ArrayList<Boolean> mShowingBack;
    private DiscoverTileFragment discoverTileFragment;
    private DiscoverTileFragment filteredTileFragment;
    private int TFmode;

    private IconBarFragment iconBarFragment;

    private BottomSheetBehavior mBottomSheetBehavior;
    private Boolean BottomSheetState = false;
    private Bundle coordinatesBundle;
    private double rad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);


        // Toolbar
        Toolbar discoverToolbar = (Toolbar) findViewById(R.id.discover_Toolbar);
        setSupportActionBar(discoverToolbar);

        // Toolbar TextView
        locationChosen = (TextView) findViewById(R.id.toolbar_location_indicator);

        // Toolbar Radius Adjust
        adjustRadius = (ImageButton) findViewById(R.id.toolbar_search_radius);
        adjustRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Button only works if a place has been selected on the place picker.
                if (mPlaceSelected != null) {
                    // Open up map fragment.
                    FragmentManager manager = getSupportFragmentManager();
                    MapDialogFragment mapDialog = new MapDialogFragment();
                    mapDialog.setArguments(coordinatesBundle);
                    mapDialog.show(manager, MapDialogkey);
                }
            }
        });

        // Toolbar icon Map
        final ImageButton mapIcon = (ImageButton) findViewById(R.id.map_toolbar_icon);
        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Construct Intent to launch place picker activity.
                try {
                    PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(DiscoverActivity.this);
                    // Start the Intent by requesting a result, identified by a request code.
                    startActivityForResult(intent, PLACE_PICKER_REQUEST); // Result should be placename and coordinates
                    // Hide the pick option in the UI to prevent users from starting the picker
                    // multiple times.
                    //showPickAction(false);

                } catch (GooglePlayServicesRepairableException e) {
                    GooglePlayServicesUtil
                            .getErrorDialog(e.getConnectionStatusCode(), DiscoverActivity.this, 0);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Toast.makeText(DiscoverActivity.this, "Google Play Services is not available.",
                            Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        // Setting up icons
        Icons = new ArrayList<ActivityIcon>();

        Icons.add(new ActivityIcon("ALL", R.drawable.ic_all));
        Icons.add(new ActivityIcon("Camp", R.drawable.ic_tent));
        Icons.add(new ActivityIcon("Run", R.drawable.ic_running));
        Icons.add(new ActivityIcon("Swim", R.drawable.ic_swimming));
        Icons.add(new ActivityIcon("Kayak", R.drawable.ic_kayaking));
        Icons.add(new ActivityIcon("Climb", R.drawable.ic_climbing));
        Icons.add(new ActivityIcon("Cycle", R.drawable.ic_cycling));
        Icons.add(new ActivityIcon("SUP", R.drawable.ic_sup));

        // Horizontal gridview will display activity icons to filter tiles based on activities icon.

        iconBarFragment = new IconBarFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("KEY", Icons);

        iconBarFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.icon_barfragment, iconBarFragment);


        // Bottom Sheet fragment
        /*FrameLayout bottomSheet = (FrameLayout) findViewById(R.id.bottom_sheet);
        bottomSheet.bringToFront();
        Button bottomsheetbutton = (Button) findViewById(R.id.bottom_sheet_button);
        bottomsheetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open bottomsheet fragment.
                //BottomListFragment bottomListFragment = new BottomListFragment();
                //FragmentManager manager = getSupportFragmentManager();
                //bottomListFragment.show(manager, "Bottom List Fragment");
            }
        });*/

        // Bottom Sheet gridview
        /*final View bottomsheet = findViewById(R.id.bottom_sheet);
        bottomsheet.bringToFront();
        Button bottomsheetbutton = (Button) findViewById(R.id.bottom_sheet_button);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        mBottomSheetBehavior.setPeekHeight(100);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        bottomsheetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BottomSheetState) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    BottomSheetState = true;
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    BottomSheetState = false;
                }
            }
        });*/


        // Potential lighting up code for Map
        /*mapIcon.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        // Your action here on button click

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });*/


        // Darken background image
        //ImageView backgroundImageView = (ImageView)findViewById(R.id.discover_Background);
        //backgroundImageView.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);


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
        Test.add(new DiscoverTile("Item_3", compressPhoto(R.drawable.three), item3activities));
        Test.add(new DiscoverTile("Item_4", compressPhoto(R.drawable.four), item1activities));
        Test.add(new DiscoverTile("Item_5", compressPhoto(R.drawable.five), item1activities));

        mShowingBack = new ArrayList<Boolean>();
        for (int i = 0; i < Test.size(); i++) {
            mShowingBack.add(i, false);
        }


        // Tile code
        /*discoverAdapter adapter = new discoverAdapter(DiscoverActivity.this,Test);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
            }
        });*/

        // Start and send data to DiscoverTileFragment
        /**/
        discoverTileFragment = new DiscoverTileFragment();
        TFmode = useDTF;        // Set mode for fragment to use.

        Bundle bundle_DT = new Bundle();
        bundle_DT.putParcelableArrayList("KEY", Test);

        discoverTileFragment.setArguments(bundle_DT);
        fragmentTransaction.add(R.id.discover_tilefragment, discoverTileFragment);
        fragmentTransaction.commit();


        // TODO: Fix picture compression when picture is too big.


    }


    // Occurs when fragments are resumed(visible on screen)
    @Override
    protected void onResume() {
        super.onResume();
        if (rad == 50000) {
            filterLocations(50000, mSelectedLat, mSelectedLng); //Filter to show locations in 50 km radi
        }
    }

    // When expecting a result from calling another activity. Note that fragments cannot be replaced
    // in here.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Provide results based on location picked in placepicker.
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastPlaceNameMsg = String.format("Place selected: %s", place.getName());
                Toast.makeText(this, toastPlaceNameMsg, Toast.LENGTH_LONG).show();

                /*String toastCoordinateNameMsg = String.format("Coordinates selected: %s", place.getLatLng());
                Toast.makeText(this, toastCoordinateNameMsg, Toast.LENGTH_LONG).show();*/

                // Store information based on place picked.
                mPlaceSelected = (String) place.getName();
                mSelectedLat = place.getLatLng().latitude;
                mSelectedLng = place.getLatLng().longitude;
                mCoordinates = place.getLatLng();

                // Update TextView showing location
                locationChosen.setText(mPlaceSelected);

                // Store Coordinates in a bundle so other fragments can use if necessary.
                coordinatesBundle = new Bundle();
                coordinatesBundle.putParcelable(CoordinatesKey, mCoordinates);
                rad = 50000;
            }
        }
    }

    public void onBottomListClicked(int position) {
        return;
    }

    // Fragment Interface with DiscoverTileFragment
    public void onListFragmentInteraction(ArrayList<DiscoverTile> Test, int position, Integer toUnflip) {
        Log.d(TAG, "onListFragmentInteraction: " + Test.get(position).getName() + " has been clicked to flip");

        // Expand or flip card
        flipCard(Test, position, toUnflip);
        return;
    }

    // Fragment Interface with MapDialogFragmentInteraction
    public void onMapDialogFragmentInteraction(double mrad) {
        this.rad = mrad;
        filterLocations(rad, mSelectedLat, mSelectedLng);
    }

    // Fragment Interface with IconBarFragmentInteraction
    public void onIconBarFragmentInteraction(String mName, int mActivity) {
        filterActivities(mActivity);
    }

    public void filterActivities(int mActivity) {
        // Loop through tiles and only filter via activity icons.
        // New arraylist of filtered things.
        ArrayList<DiscoverTile> newTest = new ArrayList<DiscoverTile>();

        // Check to see if all activities have been selected.
        if (mActivity != R.drawable.ic_all) {
            // Loop through to filter out activities.
            for (int i = 0; i < Test.size(); i++) {
                for (int j = 0; j < Test.get(i).noOfActivities(); j++) {
                    int activity = Test.get(i).getActivities().get(j);
                    if (activity == mActivity) {
                        //Log.d(TAG, "Yes");
                        newTest.add(Test.get(i));
                        break;
                    }
                    //Log.d(TAG, "No");
                }
            }
        } else { // Show all tiles if 'all' has been selected.
            newTest = Test;
        }


        // Based on the TFmode, the process of restarting the fragment which show tiles will differ.
        switch (TFmode) {
            case useDTF:
                if (filteredTileFragment == null) {
                    // Create filteredTileFragment.
                    filteredTileFragment = new DiscoverTileFragment();
                    TFmode = useFTF;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("KEY", newTest);
                    filteredTileFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.discover_tilefragment, filteredTileFragment);
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    // May not need to exist.
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(filteredTileFragment);
                    //fragmentTransaction.addToBackStack(null);

                    // Edit new fragment.
                    filteredTileFragment = new DiscoverTileFragment();
                    TFmode = useFTF;
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("KEY", newTest);
                    filteredTileFragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.discover_tilefragment, filteredTileFragment);

                    fragmentTransaction.commit();
                }
                break;
            case useFTF:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(filteredTileFragment);
                //fragmentTransaction.addToBackStack(null);

                // Edit new fragment.
                filteredTileFragment = new DiscoverTileFragment();
                TFmode = useFTF;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("KEY", newTest);
                filteredTileFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.discover_tilefragment, filteredTileFragment);

                fragmentTransaction.commit();
                break;

        }

    }

    public void filterLocations(double rad, double centerlat, double centerlng) {
        // Radius and center coordinates received.
        Location center = new Location("Center");
        center.setLatitude(centerlat);
        center.setLongitude(centerlng);
        Location point = new Location("Point");

        // New arraylist of filter things.
        ArrayList<DiscoverTile> newTest = new ArrayList<DiscoverTile>();

        // For loop calculate straight line distance between each tile object in database and center.
        for (int i = 0; i < Test.size(); i++) {
            //Log.d(TAG, "onMapDialogFragmentInteraction: Rad is " + rad);
            point.setLatitude(Test.get(i).getLat());
            point.setLongitude(Test.get(i).getLng());
            float distance = center.distanceTo(point);
            //Log.d(TAG, "onMapDialogFragmentInteraction: distance is " + distance);
            if (distance <= rad) {
                // Test.get(i) is in the radius.  Show selection.
                newTest.add(Test.get(i));
            }
        }

        // Create a new tile fragment (separate from disoverTileFragment) called filteredTileFragment,
        // which contains the filtered tiles.
        filteredTileFragment = new DiscoverTileFragment();
        TFmode = useFTF;

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("KEY", newTest);

        filteredTileFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.discover_tilefragment, filteredTileFragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


    }


    public void onFragmentInteraction(Uri uri) {
        return;
    }

    public void expandCard(ArrayList<DiscoverTile> Test, int position) {
        //Create second fragment to transition to.
        ExpandedDiscoverTile expandedDiscoverTile = new ExpandedDiscoverTile();
        Bundle bundle = new Bundle();
        bundle.putParcelable("KEY", Test.get(position));
        expandedDiscoverTile.setArguments(bundle);


        // Inflate transition to apply
        android.transition.Transition changeTransform = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform);
        android.transition.Transition explodeTransform = TransitionInflater.from(this).inflateTransition(android.R.transition.explode);

        // Set up exit transition on initial fragment.
        discoverTileFragment.setSharedElementReturnTransition(changeTransform); //Transition used during a pop to a back stack.
        discoverTileFragment.setExitTransition(explodeTransform); // View exiting.

        // Set up entry transition on new fragment.
        expandedDiscoverTile.setSharedElementEnterTransition(changeTransform);
        expandedDiscoverTile.setEnterTransition(explodeTransform);

        // Find the shared image.
        ImageView sharedimage = (ImageView) findViewById(R.id.locationImageFragment);

        // Add second fragment by replacing first.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .replace(R.id.coordinatorlayout, expandedDiscoverTile)
                .addToBackStack(null)
                .addSharedElement(sharedimage, "LocImageTransition");

        ft.commit();


    }


    public void flipCard(ArrayList<DiscoverTile> Test, int position, Integer toUnflip) {


        // Only one card should show a back at any time. Auto inflip.
        if (mShowingBack.get(position)) {
            Log.d(TAG, "onListFragmentInteraction: " + Test.get(position).getName() + " flipped to show front");
            Test.get(position).setOrientation(0); // Set back to front.
            mShowingBack.set(position, false);
        } else {
            // Flip to the back.
            mShowingBack.set(position, true);
            Test.get(position).setOrientation(1);
            Log.d(TAG, "onListFragmentInteraction: " + Test.get(position).getName() + " flipped to show back");

        }

        // If another tile is flipped, unflip previously flipped tile.
        if (toUnflip != null) {
            if (toUnflip != position) {
                // Reset previously flipped card
                Test.get(toUnflip).setOrientation(0); // Set back to front.
                mShowingBack.set(toUnflip, false);
                Log.d(TAG, "onListFragmentInteraction: " + Test.get(position).getName() + " gonna get flipped to show back");
            }
        }


        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getSupportFragmentManager()
                .beginTransaction()

                // Replace the default fragment animations with animator resources representing
                // rotations when switching to the back of the card, as well as animator
                // resources representing rotations when flipping back to the front (e.g. when
                // the system Back button is pressed).
                /*.setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)*/

                // Replace any fragments currently in the container view with a fragment
                // representing the next page (indicated by the just-incremented currentPage
                // variable).

                //.replace(R.id.discover_tilefragment, new DiscoverTileFragmentBack())

                // Add this transaction to the back stack, allowing users to press Back
                // to get to the front of the card.
                //.addToBackStack(null)

                // Commit the transaction.
                .commit();

        // Update the respective fragment adapter based on TFmode.
        if (TFmode == useDTF) {
            Log.d(TAG, "Flipping in discoverTileFragment...");
            discoverTileFragment.tilesAdapter.notifyDataSetChanged();
        } else if (TFmode == useFTF) {
            Log.d(TAG, "Flipping in filteredTileFragment ...");
            filteredTileFragment.tilesAdapter.notifyDataSetChanged();
        }

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

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //Activity Icon code.


}
