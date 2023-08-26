package com.example.ma2023.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ma2023.R;


public class PrijateljiFragment extends Fragment {

    Button dodajPrijatelja;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_prijatelji, container, false);

        dodajPrijatelja = view.findViewById(R.id.dodajPrijatelje);

        dodajPrijatelja.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Fragment dodajPrijateljaf = new DodajPrijateljaFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_prijatelji,dodajPrijateljaf).commit();
            }
        });

        return view;
    }
}