package com.material.rdd.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.material.rdd.R;
import com.material.rdd.commons;

public class menuReportes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_reportes);

        setButtons();
    }

    private  void setButtons(){
        /*ENVIADOS*/
        (findViewById(R.id.btn_enviados)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.startActivity(getApplicationContext(), reportesEnviados.class);
            }
        });

        /*SIN ENVIAR*/
        (findViewById(R.id.btn_sin_enviar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.startActivity(getApplicationContext(), reportesSinEnviar.class);
            }
        });
    }
}
