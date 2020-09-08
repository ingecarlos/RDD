package com.material.rdd.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.material.rdd.R;
import com.material.rdd.adapter.AdapterCentroConfiguracion;
import com.material.rdd.commons;
import com.material.rdd.modelos.Establecimiento;

import java.util.List;

public class configuracion extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        setLista();
        setButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLista();
    }

    private void setButtons(){
        /*BUSCAR POR DEPARTAMENTO*/
        (findViewById(R.id.btn_buscar_centro)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.startActivity(getApplicationContext(), busquedaCentro.class);
            }
        });
    }
    private void setLista() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        List<Establecimiento> items = commons.getEstablecimientosFavoritos(this);
        if(items==null || items.size()==0){
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.view_empty).setVisibility(View.VISIBLE);
            return;
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.view_empty).setVisibility(View.GONE);
        }

        //set data and list adapter
        AdapterCentroConfiguracion mAdapter = new AdapterCentroConfiguracion(items);
        mAdapter.setOnDeleteClick(onDeleteClick);
        recyclerView.setAdapter(mAdapter);

    }


    AdapterCentroConfiguracion.OnDeleteClick onDeleteClick = new AdapterCentroConfiguracion.OnDeleteClick() {
        @Override
        public void onDeleteClick(View view, Establecimiento obj, int position) {
            commons.removeEstablecimientoFavorito(getApplicationContext(),obj.getCodigo());
            setLista();
            Toast.makeText(getApplicationContext(),obj.getNombre(),Toast.LENGTH_SHORT).show();
        }
    };
}
