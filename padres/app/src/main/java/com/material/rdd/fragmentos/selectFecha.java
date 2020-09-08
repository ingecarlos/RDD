package com.material.rdd.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.material.rdd.R;
import com.material.rdd.commons;

import afu.org.checkerframework.checker.nullness.qual.Nullable;


public class selectFecha extends Fragment {
    public static int STEP=2;
    public View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable  ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_fecha, container, false);
        commons.pushFragment(this);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        commons.reportarDia.setCurrentStep(this.STEP);
    }



    public static void next(){
        /*store date*/
        View view = ((selectFecha)commons.getCurrentFragment()).view;

        DatePicker datePicker =  view.findViewById(R.id.date_picker);
        commons.reporte.setFecha(datePicker.getDayOfMonth()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getYear());

        /*do next*/
        FragmentTransaction ft = commons.getCurrentFragment().getFragmentManager().beginTransaction();
        ft.replace(R.id.pager, new selectCaso()).addToBackStack("caso");
        ft.commit();
    }


}
