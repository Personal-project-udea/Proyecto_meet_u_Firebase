package com.martinez.steven.practica_2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {


    public CheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);


        ((PrincipalActivity) getActivity())
                .setActionBarTitle(toolbar);

        return view;

    }

}
