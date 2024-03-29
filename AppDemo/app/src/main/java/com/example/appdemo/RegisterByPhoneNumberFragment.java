package com.example.appdemo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterByPhoneNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterByPhoneNumberFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterByPhoneNumberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterByPhoneNumberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterByPhoneNumberFragment newInstance(String param1, String param2) {
        RegisterByPhoneNumberFragment fragment = new RegisterByPhoneNumberFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_register_by_phone_number, container, false);

        // Set margins programmatically
//        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rootView.getLayoutParams();
//        layoutParams.setMargins(16, 16, 16, 16); // Replace these values with your desired margins

        Button captchaBtn = rootView.findViewById(R.id.btn_captcha);
        if(captchaBtn != null) {
            captchaBtn.setOnClickListener(view -> {
                onClickCaptchaButton(view);
            });
        }


        return rootView;
    }


    public void onClickCaptchaButton(View view) {
        Log.d("####", "onClickCaptchaButton");
    }

    public void onClickSubmitButton(View view) {
        Log.d("####", "onClickSubmitButton");
    }
}