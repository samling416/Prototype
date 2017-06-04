package io.example.peanutbutter.prototype;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samuel on 1/06/2017.
 */

public class IconRecyclerViewAdapter extends RecyclerView.Adapter<IconRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "IconRecyclerViewAdapter";

    private Context mContext;
    private ArrayList<ActivityIcon> mIcons;
    private String mName;
    private int mActivity;
    private IconBarFragment.OnIconBarInteractionListener mListener;



    public IconRecyclerViewAdapter(Context context, ArrayList<ActivityIcon> icons, IconBarFragment.OnIconBarInteractionListener listener) {
        Log.d(TAG, "IconRecyclerViewAdapter: ");
        this.mContext = context;
        this.mIcons = icons;
        this.mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize View elements
        public final ImageButton mImageButton;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mImageButton = (ImageButton) view.findViewById(R.id.activity_icons);
            mTextView = (TextView) view.findViewById(R.id.activityicon_name);

            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mIcons.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    mName = mIcons.get(getAdapterPosition()).getName();
                    mActivity = mIcons.get(getAdapterPosition()).getPhoto();
                    mListener.onIconBarFragmentInteraction(mName, mActivity);
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + mIcons.get(position).getName() + " loaded");

        // Bind to new data when view is being recycled.
        holder.mImageButton.setImageResource(mIcons.get(position).getPhoto());
        holder.mTextView.setText(mIcons.get(position).getName());


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "OnCreateViewHolder");

        final View view = LayoutInflater.from(this.mContext).inflate(R.layout.horz_gridview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return this.mIcons.size();
    }

    public String getName() {
        return mName;
    }

    public int getActivity() {
        return mActivity;
    }
}
