package io.example.peanutbutter.prototype;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Samuel on 5/06/2017.
 */

public class miniRecyclerViewAdapter extends RecyclerView.Adapter<miniRecyclerViewAdapter.ViewHolder>
        implements ItemTouchHelperAdapter, View.OnTouchListener, View.OnDragListener {

    private static final String TAG = "miniRecyclerViewAdapter";
    private static final String KEY = TAG;

    private Context mContext;
    private ArrayList<DiscoverTile> mTiles;
    private MiniTilesFragment.OnMiniTilesInteractionListener mListener;


    public miniRecyclerViewAdapter(Context context, ArrayList<DiscoverTile> tiles, MiniTilesFragment.OnMiniTilesInteractionListener listener) {
        Log.d(TAG, "iconRecyclerViewAdapter: icons");
        this.mContext = context;
        this.mTiles = tiles;
        this.mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize View elements
        public final ImageView mImageView;
        public final TextView mTextView;
        public final View mView;

        public ViewHolder(final View view) {
            super(view);
            mView = view;

            // Assign view elements.
            mImageView = (ImageView) view.findViewById(R.id.mini_locphoto);
            mTextView = (TextView) view.findViewById(R.id.mini_locname);


        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + mTiles.get(position).getName() + " loaded");

        // Bind to new data when view is being recycled.
        holder.mImageView.setImageResource(mTiles.get(position).getPhoto());
        holder.mTextView.setText(mTiles.get(position).getName());


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "OnCreateViewHolder");

        final View view = LayoutInflater.from(this.mContext).inflate(R.layout.mini_tiles_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.mTiles.size();

    }


    // Logic for moving items.
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mTiles, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mTiles, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mTiles.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemSwiped(int position) {
        mListener.onMiniTilesFragmentInteraction(mTiles.get(position));
        mTiles.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        return false;
    }
}

