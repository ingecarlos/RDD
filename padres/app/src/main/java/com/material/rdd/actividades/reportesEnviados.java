package com.material.rdd.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.material.rdd.R;
import com.material.rdd.adapter.AdapterReporte;
import com.material.rdd.commons;
import com.material.rdd.modelos.Reporte;

import java.util.ArrayList;

public class reportesEnviados extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_enviados);

        initComponents();
    }
    private void initComponents(){
        setLista();
        setButtons();
    }

    private void setButtons(){
        /*boton liberar espacio*/
        ((TextView) this.findViewById(R.id.btn_liberar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liberarEspacio();
            }
        });

        /*boton regresar al menú principal*/
        ((TextView) this.findViewById(R.id.btn_regresar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commons.toMenu(getApplicationContext());
            }
        });
    }

    private void liberarEspacio(){
        commons.clearEnviados(this);
        commons.showMessageDialog(this,"Tarea completada!","Se ha limpiado el espacio de su teléfono",R.drawable.ic_trash,null);
        finish();
    }


    private void setLista(){
        ArrayList<Reporte> reportes = getEnviados();
        if(reportes==null || reportes.size()==0){
            commons.showAlertDialog(this,"Alerta","No cuenta con reportes previamente enviados",R.drawable.ic_error_outline,null);
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


    private ArrayList<Reporte> getEnviados(){
        String enviados = commons.getShared(this,"enviados","[]");
        return Reporte.reportesFromJson(enviados);
    }
}
