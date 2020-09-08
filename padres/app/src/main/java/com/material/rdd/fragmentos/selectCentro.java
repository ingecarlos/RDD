package com.material.rdd.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.material.rdd.R;
import com.material.rdd.actividades.configuracion;
import com.material.rdd.actividades.menuReportes;
import com.material.rdd.actividades.reportarDia;
import com.material.rdd.adapter.AdapterCentroSelect;
import com.material.rdd.commons;
import com.material.rdd.modelos.Establecimiento;

import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.Nullable;

public class selectCentro extends Fragment {
    RecyclerView recyclerView;
    public static int STEP=1;
    //ArrayList<Establecimiento> establecimientos;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_centro, container, false);
        commons.pushFragment(this);
        initComponents();
        return view;
    }

    private void initComponents(){
        setButtons();
        setLista();
    }

    @Override
    public void onResume() {
        super.onResume();
        setLista();
        commons.reportarDia.setCurrentStep(this.STEP);
    }

    private void setButtons(){
        /*CONFIGURACION*/
        (view.findViewById(R.id.btn_configuracion)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.startActivity(getContext(), configuracion.class);
            }
        });
    }



    public static void next() {
        commons.delay(150);
        FragmentTransaction ft = commons.getCurrentFragment().getFragmentManager().beginTransaction();
        ft.replace(R.id.pager, new selectFecha()).addToBackStack("fecha");
        ft.commit();
    }

    private void setLista() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        List<Establecimiento> items = commons.getEstablecimientosFavoritos(getActivity());
        if (items == null || items.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            view.findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
            return;
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            view.findViewById(R.id.view_empty).setVisibility(View.GONE);
        }

        AdapterCentroSelect mAdapter = new AdapterCentroSelect(getActivity(), items, R.layout.cardview_establecimiento_select);
        mAdapter.setOnItemClickListener(onItemClickListener);

        recyclerView.setAdapter(mAdapter);

    }

    AdapterCentroSelect.OnItemClickListener onItemClickListener = new AdapterCentroSelect.OnItemClickListener() {
        @Override
        public void onItemClick(View view, Establecimiento obj, int position) {
            commons.reporte.setCentro(obj.getCodigo());
            next();
        }
    };


}
