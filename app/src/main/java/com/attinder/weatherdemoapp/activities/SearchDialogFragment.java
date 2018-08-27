package com.attinder.weatherdemoapp.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.attinder.weatherdemoapp.R;

public class SearchDialogFragment extends android.support.v4.app.DialogFragment {


    private EditText etSearch;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public static SearchDialogFragment newInstance(String param1, String param2) {
        SearchDialogFragment fragment = new SearchDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SearchDialog);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View myView = inflater.inflate(R.layout.fragment_search, container, false);
        try{
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        catch (NullPointerException ex) {
            Log.d("google", ex.toString());
        }

        etSearch = myView.findViewById(R.id.et_search);





        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        || i == EditorInfo.IME_ACTION_DONE) {

                    String search = etSearch.getText().toString();
                    Intent intent =  new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("search",search);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        return myView;

    }



    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);



        try{
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        catch (NullPointerException ex) {
            Log.d("google", ex.toString());
        }
    }


}
