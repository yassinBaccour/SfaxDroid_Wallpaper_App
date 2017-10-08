package com.sami.rippel.labs.stickers;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.ViewModel;
import com.sami.rippel.model.listner.StickersListner;

public class AddTextFragment extends Fragment {

    StickersListner stickersListner;
    Button mAddText;
    EditText mTxt;
    TextView textViewAddText;

    enum TextStyleEnum {
        BOLD_ITALIC,
        BOLD,
        ITALIC,
        NORMAL
    }
    public boolean ismState() {
        return mState;
    }

    public void setmState(boolean mState) {
        this.mState = mState;
    }

    boolean mState = false;

    public static AddTextFragment newInstance() {
        return new AddTextFragment();
    }

    public String getStringFromEditText() {
        return mTxt.getText().toString();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        mTxt = (EditText) rootView.findViewById(R.id.editText);
        textViewAddText = (TextView) rootView.findViewById(R.id.textViewAddText);
        textViewAddText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "arabicfont5.ttf"));
        mState = true;
        return rootView;
    }

    public void setTextStyle(TextStyleEnum textStyle)
    {
        if (textStyle == TextStyleEnum.BOLD_ITALIC)
            mTxt.setTypeface(null, Typeface.BOLD_ITALIC);
        if (textStyle == TextStyleEnum.BOLD)
            mTxt.setTypeface(null, Typeface.BOLD);
        if (textStyle == TextStyleEnum.ITALIC)
            mTxt.setTypeface(null, Typeface.ITALIC);
        if (textStyle == TextStyleEnum.NORMAL)
            mTxt.setTypeface(null, Typeface.NORMAL);

        ViewModel.Current.dataUtils.SetSetting("TypeFace", textStyle.toString());
    }

    public void setListener(StickersListner stickersListner) {
        this.stickersListner = stickersListner;
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
}
