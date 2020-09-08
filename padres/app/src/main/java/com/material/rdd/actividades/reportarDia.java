package com.material.rdd.actividades;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.material.rdd.R;
import com.material.rdd.commons;
import com.material.rdd.fragmentos.selectCentro;
import com.material.rdd.fragmentos.selectFecha;
import com.material.rdd.fragmentos.selectMaterias;
import com.material.rdd.modelos.Reporte;

public class reportarDia extends AppCompatActivity {

    private static final int MAX_STEP = 5;
    private int current_step = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_dia);

        initComponents();
     }


    private void initComponents(){
        commons.initReporte(this);
        initComponent();
        setUpViewer();
        crearReporte();
    }
    private void crearReporte(){
        commons.reporte = new Reporte();
    }


    private void setUpViewer(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment cur = new selectCentro();
        commons.pushFragment(cur);
        fragmentTransaction.add(R.id.pager,cur);
        fragmentTransaction.commit();
    }


    private void initComponent() {
        //status = (TextView) findViewById(R.id.status);

        ((LinearLayout) findViewById(R.id.lyt_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ((LinearLayout) findViewById(R.id.lyt_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawNextStep();
            }
        });

        bottomProgressDots(current_step);
    }

    private void drawNextStep(){

        switch (current_step){
            case 1:
                //SELECT CENTRO
                Snackbar.make(this.findViewById(android.R.id.content),"Seleccione un centro educativo para continuar",Snackbar.LENGTH_LONG).show();
                break;
            case 2:
                //SELECT FECHA
                selectFecha.next();
                break;
            case 4:
                //SELECT MATERIAS
                selectMaterias.next();
                break;
        }
    }


    private void nextStep(int progress) {
        if (progress < MAX_STEP) {
            progress++;
            current_step = progress;
        }
    }

    private void backStep(int progress) {
        if (progress > 1) {
            progress--;
            current_step = progress;
        }
    }

    private void bottomProgressDots(int current_index) {
        current_index--;
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.white_transparency), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.red_500), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
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

    public void setCurrentStep(int step){
        current_step = step;
        bottomProgressDots(current_step);
    }
}
