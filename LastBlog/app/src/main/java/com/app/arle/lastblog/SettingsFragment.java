package com.app.arle.lastblog;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mlistener;

    public SettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    Button accountbutton, loginbutton, registerbutton, backbutton;
    FragmentManager fragmentManager;
    Fragment tmpfragment;
    @Override
    public void onActivityCreated(Bundle b) {
            super.onActivityCreated(b);

            backbutton = (Button) getView().findViewById(R.id.backbutton);
            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragmentManager = getActivity().getSupportFragmentManager();
                    tmpfragment = fragmentManager.findFragmentByTag("settings");
                    getActivity().getSupportFragmentManager().beginTransaction().remove(tmpfragment).commit();
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.articleslayout, mlistener.getArticlesFragment()).commit();

                }
            });
        loginbutton = (Button) getView().findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(mlistener.getSettingsFragment()).commit();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.settingslayout, mlistener.getCreateFragment()).commit();

            }
        });
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
