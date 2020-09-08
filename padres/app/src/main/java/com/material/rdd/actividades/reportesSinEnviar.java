package com.material.rdd.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.material.rdd.R;
import com.material.rdd.adapter.AdapterReporte;
import com.material.rdd.commons;
import com.material.rdd.modelos.Reporte;

import java.util.ArrayList;

public class reportesSinEnviar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_sin_enviar);
        setLista();
    }

    private void setLista(){
        ArrayList<Reporte> reportes = getSinEnviar();
        if(reportes==null || reportes.size()==0){
            commons.showAlertDialog(this,"Alerta","No cuenta con reportes sin enviar",R.drawable.ic_error_outline,null);
            finish();
        }

        RecyclerView recList;
        AdapterReporte ca;

        recList = findViewById(R.id.Lista);
        //recList.setOnClickListener(this);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        registerForContextMenu(recList);



        ca = new AdapterReporte(reportes);
        recList.setAdapter(ca);
    }


    private ArrayList<Reporte> getSinEnviar(){
        String sinEnviar = commons.getShared(this,commons.SIN_ENVIAR,"[]");
        return Reporte.reportesFromJson(sinEnviar);
    }
}
