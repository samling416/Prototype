package io.example.peanutbutter.prototype;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DiscoverTileFragment extends Fragment {

    private static final String TAG = "DiscoverTileFragment";
    private static final int vertical = 0, horizontal = 1;


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int VERTICAL_ITEM_SPACE = 250;
    // TODO: Customize parameters
    private OnListFragmentInteractionListener mListener;
    private ArrayList<DiscoverTile> Test;
    public DTRecyclerViewAdapter tilesAdapter;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DiscoverTileFragment() {
        super();
        // Just to be an empty bundle which can be used later.
        setArguments(new Bundle());
    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DiscoverTileFragment newInstance(int columnCount) {
        Log.d(TAG, "NewInstance");

        DiscoverTileFragment fragment = new DiscoverTileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_discovertile_list, container, false);
        final DiscoverActivity discoverActivity = (DiscoverActivity) getActivity(); // COntext;
        Bundle bundle = getArguments();
        Test = bundle.getParcelableArrayList("KEY");


        // Set the adapter
        if (view instanceof RecyclerView) {
            Log.d(TAG, "onCreateView: Instance of RecyclerView");
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager llm = new LinearLayoutManager(context);

            //add ItemDecoration
            recyclerView.addItemDecoration(new DividerItemDecoration(VERTICAL_ITEM_SPACE, vertical));

            recyclerView.setLayoutManager(llm);
            tilesAdapter = new DTRecyclerViewAdapter(context, Test, mListener);
            recyclerView.setAdapter(tilesAdapter);

        }


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    // Interfaces.

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ArrayList<DiscoverTile> Test, int position, Integer toUnflip);
    }


}
