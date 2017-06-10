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

/**
 * Created by Samuel on 1/06/2017.
 */

public class iconRecyclerViewAdapter extends RecyclerView.Adapter<iconRecyclerViewAdapter.IconViewHolder> {

    private static final String TAG = "iconRecyclerViewAdapter";

    private Context mContext;
    private ArrayList<ActivityIcon> mIcons;
    private String mName;
    private int mActivity;
    private IconBarFragment.OnIconBarInteractionListener miListener;


    public iconRecyclerViewAdapter(Context context, ArrayList<ActivityIcon> icons, IconBarFragment.OnIconBarInteractionListener listener) {
        Log.d(TAG, "iconRecyclerViewAdapter: icons");
        this.mContext = context;
        this.mIcons = icons;
        this.miListener = listener;
    }


    public class IconViewHolder extends RecyclerView.ViewHolder {

        // Initialize View elements
        public final ImageButton mImageButton;
        public final TextView mTextView;

        public IconViewHolder(View view) {
            super(view);
            mImageButton = (ImageButton) view.findViewById(R.id.activity_icons);
            mTextView = (TextView) view.findViewById(R.id.activityicon_name);

            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mIcons.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT).show();
                    mName = mIcons.get(getAdapterPosition()).getName();
                    mActivity = mIcons.get(getAdapterPosition()).getPhoto();
                    miListener.onIconBarFragmentInteraction(mName, mActivity);
                }
            });
        }
    }


    @Override
    public void onBindViewHolder(IconViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + mIcons.get(position).getName() + " loaded");

        // Bind to new data when view is being recycled.
        holder.mImageButton.setImageResource(mIcons.get(position).getPhoto());
        holder.mTextView.setText(mIcons.get(position).getName());


    }


    @Override
    public IconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "OnCreateViewHolder");

        final View view = LayoutInflater.from(this.mContext).inflate(R.layout.horz_gridview_item, parent, false);
        IconViewHolder viewHolder = new IconViewHolder(view);


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
