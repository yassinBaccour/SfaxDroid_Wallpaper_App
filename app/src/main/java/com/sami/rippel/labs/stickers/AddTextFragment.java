package com.sami.rippel.labs.stickers;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.listner.StickersListener;

public class AddTextFragment extends Fragment {

    private EditText mTxt;
    private boolean mState = false;

    public static AddTextFragment newInstance() {
        return new AddTextFragment();
    }

    public boolean ismState() {
        return mState;
    }

    public void setState(boolean mState) {
        this.mState = mState;
    }

    public String getStringFromEditText() {
        return mTxt.getText().toString();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        mTxt = rootView.findViewById(R.id.editText);
        TextView textViewAddText = (TextView) rootView.findViewById(R.id.textViewAddText);
        if (getActivity() != null) {
            textViewAddText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "arabicfont5.ttf"));
        }
        mState = true;
        return rootView;
    }

    public void setTextStyle(TextStyleEnum textStyle) {
        if (textStyle == TextStyleEnum.BOLD_ITALIC)
            mTxt.setTypeface(null, Typeface.BOLD_ITALIC);
        if (textStyle == TextStyleEnum.BOLD)
            mTxt.setTypeface(null, Typeface.BOLD);
        if (textStyle == TextStyleEnum.ITALIC)
            mTxt.setTypeface(null, Typeface.ITALIC);
        if (textStyle == TextStyleEnum.NORMAL)
            mTxt.setTypeface(null, Typeface.NORMAL);

        ViewModel.Current.sharedPrefsUtils.SetSetting("TypeFace", textStyle.toString());
    }

    public void setListener(StickersListener stickersListner) {
        StickersListener stickersListner1 = stickersListner;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTxt.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    enum TextStyleEnum {
        BOLD_ITALIC,
        BOLD,
        ITALIC,
        NORMAL
    }
}
