package com.example.studybank;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ImexFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent myIntent = new Intent(getActivity(), ImExActivity.class);
        startActivity(myIntent);
        return inflater.inflate(R.layout.fragment_imex, container, false);
    }
}