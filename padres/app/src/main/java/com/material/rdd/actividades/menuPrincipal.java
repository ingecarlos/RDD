package com.material.rdd.actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.material.rdd.R;
import com.material.rdd.commons;
import com.material.rdd.utils.Tools;

public class menuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        setButtons();
    }


    @Override
    public void onResume() {
        super.onResume();
        //debugShared();
    }

    private void debugShared(){
        commons.log("favoritos: " + commons.getShared(this,commons.FAVORITOS,""));
        commons.log("enviados: " + commons.getShared(this,commons.ENVIADOS,""));
        commons.log("sin enviar: " + commons.getShared(this,commons.SIN_ENVIAR,""));
    }


    private void setButtons(){
        /*REPORTAR*/
        (findViewById(R.id.btn_reportar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.startActivity(getApplicationContext(), reportarDia.class);
            }
        });

        /*REPORTES LLENADOS*/
        (findViewById(R.id.btn_llenados)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.startActivity(getApplicationContext(), menuReportes.class);
            }
        });

        /*CONFIGURACION*/
        (findViewById(R.id.btn_configuracion)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.startActivity(getApplicationContext(), configuracion.class);
            }
        });

        /*SALIR*/
        (findViewById(R.id.btn_salir)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        Tools.changeMenuIconColor(menu, Color.WHITE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
