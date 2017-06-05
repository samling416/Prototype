package io.example.peanutbutter.prototype;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IconBarFragment.OnIconBarInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IconBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IconBarFragment extends Fragment {

    private static final String TAG = "IconBarFragment";
    private static final int vertical = 0, horizontal = 1;
    private static final int HORIZONTAL_ITEM_SPACING = 10;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<ActivityIcon> mIcons;
    public IconRecyclerViewAdapter iconAdapter;

    private OnIconBarInteractionListener mListener;

    public IconBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IconBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IconBarFragment newInstance(String param1, String param2) {
        IconBarFragment fragment = new IconBarFragment();
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
        View view = inflater.inflate(R.layout.fragment_horizontalgridview, container, false);
        Bundle bundle = getArguments();
        mIcons = bundle.getParcelableArrayList("KEY");


        // Set the adapter
        if (view instanceof RecyclerView) {
            //Log.d(TAG, "onCreateView: Instance of RecyclerView");
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            //add ItemDecoration
            //recyclerView.addItemDecoration(new DividerItemDecoration(HORIZONTAL_ITEM_SPACING, horizontal));

            iconAdapter = new IconRecyclerViewAdapter(context, mIcons, mListener);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(iconAdapter);
        }


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnIconBarInteractionListener) {
            mListener = (OnIconBarInteractionListener) context;
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
    public interface OnIconBarInteractionListener {
        // TODO: Update argument type and name
        void onIconBarFragmentInteraction(String mName, int mActivity);
    }
}
