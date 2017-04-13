package io.example.peanutbutter.prototype;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupListActivity extends AppCompatActivity {
    private static final String TAG = "GroupListActivity";
    private ArrayList<Peanut> mPeanutButter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        mPeanutButter = new ArrayList<Peanut>();

        // Test case. 5 jars of Peanut Butter
        for (int i = 5; i >= 0; i--) {
            Peanut peanut = new Peanut();
            peanut.setName("Swagling");
            mPeanutButter.add(peanut);
        }

        ListView listView = (ListView) findViewById(R.id.group_List_ListView);
        listView.setAdapter(new groupListAdapter(mPeanutButter));


    }

    // ListView Adapter
    private class groupListAdapter extends ArrayAdapter<Peanut>{
        groupListAdapter(ArrayList<Peanut> PeanutButter){
            super(GroupListActivity.this, R.layout.activity_group_list_row,R.id.group_List_RowTextView, PeanutButter);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Peanut peanut = getItem(position);
            // Display in textview name of person
            TextView rowTextView =(TextView)convertView.findViewById(R.id.group_List_RowTextView);
            //Log.d(TAG,"Name is "+peanut.getName());
            rowTextView.setText(peanut.getName());
            return convertView;
        }
    }
}
