package com.material.rdd.dialogs;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.material.rdd.R;

public class DialogAccept extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_accept);
        this.setFinishOnTouchOutside(false);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String mensaje = intent.getStringExtra("mensaje");
        int iconId = intent.getIntExtra("icon",-1);
        String boton = intent.getStringExtra("boton");

        Button btn = findViewById(R.id.bt_close);

        if (mensaje != null && iconId!=-1) {
            //TITULO
            TextView titleView = findViewById(R.id.title);
            titleView.setText(title);
            //CONTENT
            TextView content = findViewById(R.id.content);
            content.setText(mensaje);
            //ICONO
            ImageView icon = findViewById(R.id.icon);
            icon.setImageResource(iconId);
            //TEXTO DEL BOTON
            if(boton!=null){
                btn.setText(boton);
            }
            //SI ES UN MENSAJE DE ALERTA
            if(intent.getBooleanExtra("alert",false)){
                findViewById(R.id.header).setBackgroundResource(R.color.red_300);
                findViewById(R.id.bt_close).setBackgroundResource(R.drawable.btn_rounded_red);
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
