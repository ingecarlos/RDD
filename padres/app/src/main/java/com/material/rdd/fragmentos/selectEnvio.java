package com.material.rdd.fragmentos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.material.rdd.R;
import com.material.rdd.commons;
import com.material.rdd.modelos.Reporte;

public class selectEnvio extends Fragment {
    public static int STEP = 5;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_envio, container, false);
        commons.pushFragment(this);
        initComponents();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        commons.reportarDia.setCurrentStep(this.STEP);
    }


    private void initComponents() {
        setButtons();
    }

    private void setButtons() {
        //INTERNET
        view.findViewById(R.id.btn_internet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commons.reporte.setTipo_envio(Reporte.INTERNET);
                enviar();
            }
        });

        //SMS
        view.findViewById(R.id.btn_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSmsPermission()) {
                    commons.reporte.setTipo_envio(Reporte.SMS);
                    enviar();
                }
            }
        });
    }

    public void enviar() {
        getActivity().finish();
        commons.reporte.enviar(getActivity());
        commons.reporte = null;
    }

    private boolean checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.SEND_SMS}, 0);
            }
            return false;
        }
        return true;
    }

}
