package io.example.peanutbutter.prototype;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Tile;

import io.example.peanutbutter.prototype.DiscoverTileFragment.OnListFragmentInteractionListener;
import io.example.peanutbutter.prototype.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DTRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnMapReadyCallback {

    private static final String TAG = "DTRecyclerViewAdapter";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private View mbackView;
    private Integer toUnflip;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final CardView cv;
        public final ImageView locPhoto;
        public final GridLayout activityIconsGridLayout;
        public final View mView;
        public final Button exploreButton;
        public final TextView locName;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            cv = (CardView) itemView.findViewById(R.id.discover_CardViewFragment);
            locPhoto = (ImageView) itemView.findViewById(R.id.locationImageFragment);
            activityIconsGridLayout = (GridLayout) itemView.findViewById(R.id.activity_icons_GridLayoutFragment);
            exploreButton = (Button) itemView.findViewById(R.id.tileExplore_Button);
            locName = (TextView) itemView.findViewById(R.id.locationTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }


    }

    public class ViewHolderBack extends RecyclerView.ViewHolder {

        public final CardView cv;
        public final ImageButton mImageButton;


        public ViewHolderBack(View itemView) {
            super(itemView);
            mbackView = itemView;
            cv = (CardView) itemView.findViewById(R.id.discover_CardView_back);
            mImageButton = (ImageButton) itemView.findViewById(R.id.back_back);
            mMapView = (MapView) itemView.findViewById(R.id.back_mapviewlite);
        }
    }

    private List<DiscoverTile> Tiles;
    private Context mContext;
    public MapView mMapView;
    private int width;
    private LatLng coordinate;
    private final OnListFragmentInteractionListener mListener;
    private final int FRONT = 0, BACK = 1;


    DTRecyclerViewAdapter(Context context, List<DiscoverTile> Tiles, OnListFragmentInteractionListener listener) {
        //Log.d(TAG, "RecyclerViewAdapter1: ");
        this.Tiles = Tiles;
        this.mContext = context;
        mListener = listener;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case FRONT:
                View view1 = inflater.inflate(R.layout.fragment_discovertile, parent, false);
                viewHolder = new ViewHolder(view1);
                break;
            case BACK:
                View view2 = inflater.inflate(R.layout.fragment_discovertile_back, parent, false);
                viewHolder = new ViewHolderBack(view2);
                break;
            default:
                View view = inflater.inflate(R.layout.fragment_discovertile_list, parent, false);
                viewHolder = new ViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: current position " + position);
        // This part for when it's front.
        switch (holder.getItemViewType()) {
            case FRONT:
                ViewHolder vh1 = (ViewHolder) holder;
                configureViewHolderFront(vh1, position);
                break;
            case BACK:
                ViewHolderBack vh2 = (ViewHolderBack) holder;
                configureViewHolderBack(vh2, position);
                // Keep track of position to potentially unflip when new position selected.
                toUnflip = position;
                break;

        }
    }

    @Override
    public int getItemCount() {
        //Log.d(TAG, "getItemCount: ");
        return Tiles.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Log.d(TAG, "getItemViewType: ");
        if (Tiles.get(position).getOrientation() == 0) {
            return FRONT;
        } else if (Tiles.get(position).getOrientation() == 1) {
            return BACK;
        }
        return -1;
    }

    private void configureViewHolderFront(ViewHolder holder, final int position) {
        //Log.d(TAG, "ConfigureViewHolderFront: ");

        // Set ImageView to photo
        holder.locPhoto.setImageResource(Tiles.get(position).getPhoto());
        Log.d(TAG, "configureViewHolderFront: For " + Tiles.get(position).getName());

        // Set TextView to name
        holder.locName.setText(Tiles.get(position).getName());

        // Display Activity Icons (Only needs to be done on initialization)
        int c = 0;
        holder.activityIconsGridLayout.removeAllViews();
        for (int y = 0; y < Tiles.get(position).noOfActivities(); y++) {
            //Log.d(TAG, "configureViewHolderFront: Adding activity icon " + y);
            holder.activityIconsGridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
            holder.activityIconsGridLayout.setColumnCount(8);
            //holder.activityIconsGridLayout.setRowCount(1);
            ImageView iconImageView = new ImageView(mContext);
            iconImageView.setImageResource(Tiles.get(position).getActivities().get(y));
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayoutManager.LayoutParams.WRAP_CONTENT;
            param.width = GridLayoutManager.LayoutParams.WRAP_CONTENT;
            //param.columnSpec = GridLayout.spec(c);
            //param.rowSpec = GridLayout.spec(0);
            Resources r = mContext.getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, r.getDisplayMetrics());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(px, px);
            iconImageView.setLayoutParams(layoutParams);

            holder.activityIconsGridLayout.addView(iconImageView);
            c++;
        }
        c = 0;

        //Explore button setOnClickListener
        holder.exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    Log.d(TAG, "onClick: " + Tiles.get(position).getName() + " explore has been clicked.");
                    if (toUnflip != null) {
                        Log.d(TAG, "onClick: Tile to unflip: " + Tiles.get(toUnflip).getName());
                    }
                    // Pass data to activity (through DiscoverTileFragment)
                    mListener.onListFragmentInteraction((ArrayList<DiscoverTile>) Tiles, position, toUnflip);
                }
            }
        });


    }


    private void configureViewHolderBack(ViewHolderBack holder, final int position) {

        Log.d(TAG, "configureViewHolderBack: For " + Tiles.get(position).getName());

        //Start mapLite
        Bundle mapViewBundle = new Bundle();
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        coordinate = new LatLng(Tiles.get(position).getLat(), Tiles.get(position).getLng());


        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    Log.d(TAG, "onClick: " + Tiles.get(position).getName() + " has been selected.");

                    // Clicked therefore transform.
                    mListener.onListFragmentInteraction((ArrayList<DiscoverTile>) Tiles, position, null);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map view Ready for ");

        GoogleMap mMap;

        mMap = googleMap;

        //Add marker
        mMap.addMarker(new MarkerOptions().position(coordinate).title("Test"));
        // Set camera center and zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, (float) 13));

        // Resize views to maintain ratios as defined by guidelines.
        mMapView.requestLayout();
        width = mMapView.getWidth();
        Log.d(TAG, "MapView width is " + width);
        mMapView.getLayoutParams().height = 2 * (width / 3); // Maintain a 3:2 ratio.
    }


}
