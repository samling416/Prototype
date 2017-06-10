package io.example.peanutbutter.prototype;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnMiniTilesInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MiniTilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiniTilesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<DiscoverTile> mTiles;
    private miniRecyclerViewAdapter miniTilesAdapter;
    private OnMiniTilesInteractionListener mListener;

    public MiniTilesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MiniTilesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MiniTilesFragment newInstance(String param1, String param2) {
        MiniTilesFragment fragment = new MiniTilesFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.fragment_verticalminitiles, container, false);
        Bundle bundle = getArguments();
        mTiles = bundle.getParcelableArrayList("KEY");


        // Set the adapter
        if (view instanceof RecyclerView) {
            //Log.d(TAG, "onCreateView: Instance of RecyclerView");
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            //add ItemDecoration
            //recyclerView.addItemDecoration(new DividerItemDecoration(HORIZONTAL_ITEM_SPACING, horizontal));

            miniTilesAdapter = new miniRecyclerViewAdapter(context, mTiles, mListener);
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(miniTilesAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(miniTilesAdapter);
        }


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMiniTilesInteractionListener) {
            mListener = (OnMiniTilesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMiniTilesInteractionListener");
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
    public interface OnMiniTilesInteractionListener {
        // TODO: Update argument type and name
        void onMiniTilesFragmentInteraction(DiscoverTile tile);
    }
}
