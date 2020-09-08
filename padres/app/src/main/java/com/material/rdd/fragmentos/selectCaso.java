package com.material.rdd.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.material.rdd.R;
import com.material.rdd.commons;
import com.material.rdd.modelos.Reporte;

public class selectCaso extends Fragment {
    public static int STEP=3;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_caso, container, false);
        commons.pushFragment(this);
        setButtons();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        commons.reportarDia.setCurrentStep(this.STEP);
    }

    private void setButtons(){
        //CENTRO CERRADO
        view.findViewById(R.id.btn_cerrado).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.delay(150);
                commons.reporte.setActividad(Reporte.CERRADO);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pager, new selectEnvio()).addToBackStack("envio");
                ft.commit();
            }
        });
        //NORMALES
        view.findViewById(R.id.btn_normales).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.delay(150);
                commons.reporte.setActividad(Reporte.NORMALES);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pager, new selectEnvio()).addToBackStack("envio");
                ft.commit();
            }
        });
        //CIERTAS MATERIAS
        view.findViewById(R.id.btn_ciertas_materias).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.delay(150);
                commons.reporte.setActividad(Reporte.CIERTAS_MATERIAS);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pager, new selectMaterias()).addToBackStack("Ciertas materias");
                ft.commit();
            }
        });

    }


}
