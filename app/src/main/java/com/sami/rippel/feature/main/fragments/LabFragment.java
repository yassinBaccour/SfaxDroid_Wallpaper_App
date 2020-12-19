package com.sami.rippel.feature.main.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sami.rippel.allah.R;
import com.sami.rippel.feature.main.activity.HomeActivity;

public class LabFragment extends Fragment {

    public static LabFragment newInstance() {
        return new LabFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView stickers = view.findViewById(R.id.buttonTakeImageActivityStikers);
        stickers.setOnClickListener(views -> {
            HomeActivity.isAdsShow = true;
            try {
                Intent intent = new Intent(
                        getActivity(),
                        Class.forName("com.sfaxdroid.detail.StickersLabActivity"));
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lab, container, false);
    }

}
