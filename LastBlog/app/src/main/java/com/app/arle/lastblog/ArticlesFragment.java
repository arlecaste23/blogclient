package com.app.arle.lastblog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class ArticlesFragment extends Fragment {
    private Connection connection;
    final static String url = "http://192.168.0.4:8989/api/articles/android/7";
    private OnFragmentInteractionListener mlistener;
    private String result = "";
    public TextView view;
    private boolean check = false;
    public ArticlesFragment() {
        // Required empty public constructor

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_articles, container, false);
    }

    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);
        view = new TextView(getContext());
        try {
            this.connection = new Connection(getContext(), url, view);
        } catch(Exception e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        this.connection.execute();
       //while (connection.getCheck() == false);
        LinearLayout mainlayout = (LinearLayout) getView().findViewById(R.id.allarticles);

        mainlayout.addView(view);


        TextView c0;
        String text = "";
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
/*
        for(int i = 0; i<100; i++) {
            c0 = new TextView(getContext());
            c0.setGravity(Gravity.CENTER);
            text = "Article " + i;
            c0.setText(text);
            mainlayout.addView(c0);
        }*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mlistener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mlistener = null;

    }
}
