package com.sami.rippel.labs.stickers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sami.rippel.allah.R;
import com.sami.rippel.model.entity.HelpTypeEnum;
import com.sami.rippel.model.listner.StickersListner;

public class HelpFragment extends Fragment {

    private boolean mState = false;
    private TextView mTitleTxt;
    private TextView mTextViewDesc1;
    private TextView mTextViewDesc2;
    private TextView mTextViewDesc3;
    private TextView mTextViewDesc4;
    private ImageView mImageView;
    private String mhelpType = "RESIZE_HELPS";

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    public void setMhelpType(String mhelpType) {
        this.mhelpType = mhelpType;
    }

    public boolean ismState() {
        return mState;
    }

    public void setmState(boolean mState) {
        this.mState = mState;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mState = true;
        mImageView.setVisibility(View.GONE);
        mTitleTxt.setVisibility(View.GONE);
        mTextViewDesc1.setVisibility(View.GONE);
        mTextViewDesc2.setVisibility(View.GONE);
        mTextViewDesc3.setVisibility(View.GONE);
        mTextViewDesc4.setVisibility(View.GONE);
        if (mhelpType.equals("HELP1X"))
            LoadView(HelpTypeEnum.HELP1);
        else if (mhelpType.equals("HELP2X"))
            LoadView(HelpTypeEnum.HELP2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        mTitleTxt = (TextView) rootView.findViewById(R.id.titleTxt);
        mTextViewDesc1 = (TextView) rootView.findViewById(R.id.textViewDesc1);
        mTextViewDesc2 = (TextView) rootView.findViewById(R.id.textViewDesc2);
        mTextViewDesc3 = (TextView) rootView.findViewById(R.id.textViewDesc3);
        mTextViewDesc4 = (TextView) rootView.findViewById(R.id.textViewDesc4);
        mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        return rootView;
    }

    public void LoadView(HelpTypeEnum helpTypeEnum) {
        if (helpTypeEnum == HelpTypeEnum.HELP1) {
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImageResource(R.mipmap.ic_help1);
            mTitleTxt.setVisibility(View.VISIBLE);
            mTitleTxt.setText(getString(R.string.swipeleftright));
        }

        if (helpTypeEnum == HelpTypeEnum.HELP2) {
            mImageView.setVisibility(View.VISIBLE);
            mImageView.setImageResource(R.mipmap.ic_help2);
            mTitleTxt.setVisibility(View.VISIBLE);
            mTitleTxt.setText(getString(R.string.resizestickers));
        }
    }

    public void setTextFromType(HelpType helpType) {
        switch (helpType) {
            case RESIZE_HELPS:
                SetTextToEditText(
                        getString(R.string.stickersHelpTitleResize),
                        getString(R.string.stickersHelpDesc1Resize),
                        getString(R.string.stickersHelpDesc2Resize),
                        getString(R.string.stickersHelpDesc3Resize),
                        getString(R.string.stickersHelpDesc4Resize));
                break;
            case ADD_TEXT_HELP:
                SetTextToEditText(
                        getString(R.string.stickersHelpTitleAddText),
                        getString(R.string.stickersHelpDesc1AddText),
                        getString(R.string.stickersHelpDesc2AddText),
                        getString(R.string.stickersHelpDesc3AddText),
                        getString(R.string.stickersHelpDesc4AddText));
                break;
            case ADD_STICKERS_HELPS:
                SetTextToEditText(
                        getString(R.string.stickersHelpTitleAddSickers),
                        getString(R.string.stickersHelpDesc1AddSickers),
                        getString(R.string.stickersHelpDesc2AddSickers),
                        getString(R.string.stickersHelpDesc3AddSickers),
                        getString(R.string.stickersHelpDesc4AddSickers));
                break;
            case DELETE_STICKERS_HELPS:
                SetTextToEditText(
                        getString(R.string.stickersHelpTitleDeleteSickers),
                        getString(R.string.stickersHelpDesc1DeleteSickers),
                        getString(R.string.stickersHelpDesc2DeleteSickers),
                        getString(R.string.stickersHelpDesc3DeleteSickers),
                        getString(R.string.stickersHelpDesc4DeleteSickers));
                break;

        }
    }

    public void SetTextToEditText(String title, String textViewDesc1, String textViewDesc2, String textViewDesc3, String textViewDesc4) {
        mTitleTxt.setText(title);
        mTextViewDesc1.setText(textViewDesc1);
        mTextViewDesc2.setText(textViewDesc2);
        mTextViewDesc3.setText(textViewDesc4);
        mTextViewDesc4.setText(textViewDesc4);
    }

    public void setListener(StickersListner stickersListner) {
        StickersListner stickersListner1 = stickersListner;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    enum HelpType {
        RESIZE_HELPS,
        ADD_TEXT_HELP,
        ADD_STICKERS_HELPS,
        DELETE_STICKERS_HELPS,
    }
}
