package com.example.a91p;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a91p.data.DatabaseHelper;
import com.example.a91p.data.Advert;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsFragment newInstance(String param1, String param2) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);

        //access bundle and check for contents
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            //define and link elements to id
            TextView fragPostTypeText = rootView.findViewById(R.id.fragPostTypeTextView);
            TextView fragDescriptionText = rootView.findViewById(R.id.fragDescriptionTextView);
            TextView fragDateText = rootView.findViewById(R.id.fragDateTextView);
            TextView fragLocationText = rootView.findViewById(R.id.fragLocationTextView);
            TextView fragNameText = rootView.findViewById(R.id.fragNameTextView);
            TextView fragPhoneText = rootView.findViewById(R.id.fragPhoneTextView);
            Button fragRemoveButton = rootView.findViewById(R.id.fragRemoveButton);

            //access database
            DatabaseHelper db = new DatabaseHelper(getContext());
            List<Advert> advertList = db.getData();

            //save selection
            int position = bundle.getInt("position");

            //display elements of data model selection
            fragPostTypeText.setText(advertList.get(position).getPostType());
            fragDescriptionText.setText(advertList.get(position).getDescription());
            fragDateText.setText(advertList.get(position).getDate());
            fragLocationText.setText(advertList.get(position).getLocation());
            fragNameText.setText("Posted by " + advertList.get(position).getName());
            fragPhoneText.setText("Phone: " + advertList.get(position).getPhone());

            //remove button
            fragRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //pass position to delete method
                    db.delete(advertList.get(position).getName());

                    //confirm delete
                    Toast.makeText(getContext(), "Delete successful", Toast.LENGTH_LONG).show();
                }
            });
        }

        return rootView;
    }
}