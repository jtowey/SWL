package com.jc2e.bestgift.ui.sections;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc2e.bestgift.ui.EditFriendsActivity;
import com.jc2e.bestgift.ui.R;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

public class ListsFragment extends Fragment {


    public ListsFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_lists, container, false);
         
        return rootView;
    }
}
