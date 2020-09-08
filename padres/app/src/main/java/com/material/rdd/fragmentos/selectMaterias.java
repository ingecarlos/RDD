package com.material.rdd.fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.material.rdd.R;
import com.material.rdd.commons;

public class selectMaterias extends Fragment {
    public static int STEP = 4;
    View view;

    private static final int[] switches = {R.id.cnb1,R.id.cnb2,R.id.cnb3,R.id.cnb4,R.id.cnb5,R.id.cnb6,R.id.cnb7,R.id.cnb8,R.id.cnb9,R.id.cnb10};
    private static final String[] cnbLabels ={"Matemáticas","Culturas e Idiomas Mayas, Garífuna o Xinca","Comunicación y Lenguaje, Idioma Español","Comunicación y Lenguaje, Idioma Extranjero","Ciencias Naturales","Ciencias Sociales","Educación Artística","Emprendimiento para la Productividad","Tecnologías del Aprendizaje y la Comunicación","Educación Física"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_materias, container, false);
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
        (view.findViewById(R.id.et_grado)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGradoDialog(v);
            }
        });
        (view.findViewById(R.id.et_periodos)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPeriodosDialog(v);
            }
        });

        toogleView();
    }

    private void showGradoDialog(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Grado");
        builder.setSingleChoiceItems(getGradosNivel(), -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(getGradosNivel()[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showPeriodosDialog(final View v) {
        final String[] array = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Periodos perdidos");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private static String[] getGradosNivel() {
        String nivel = commons.reporte.getCentro().split("-")[3];
        if (nivel.equals("45")) {
            return new String[]{
                    "Primero básico", "Segundo básico", "Tercero básico"
            };
        } else if (nivel.equals("43")) {
            return new String[]{
                    "Primero primaria", "Segundo primaria", "Tercero primaria", "Cuarto primaria", "Quinto primaria", "Sexto primaria"
            };
        } else if (nivel.equals("46")) {
            return new String[]{
                    "Cuarto diversificado", "Quinto diversificado", "Sexto diversificado"
            };
        }
        return null;
    }

    private static boolean esBasico() {
        return commons.reporte.esBasico();
    }

    private void toogleView() {
        boolean materias = esBasico();
        if (materias) {
            ((TextView) view.findViewById(R.id.tbToogle)).setText("Seleccione TODAS las materias que no se impartieron");
            view.findViewById(R.id.cardview_materias).setVisibility(View.VISIBLE);
            view.findViewById(R.id.cardview_periodos).setVisibility(View.GONE);
        } else {
            ((TextView) view.findViewById(R.id.tbToogle)).setText("Seleccione cuantos periodos se perdieron");
            view.findViewById(R.id.cardview_materias).setVisibility(View.GONE);
            view.findViewById(R.id.cardview_periodos).setVisibility(View.VISIBLE);
        }

    }

    public static void next() {
        /*store data*/
        View view = ((selectMaterias)commons.getCurrentFragment()).view;
        {
            //grado
            String grado = ((EditText)view.findViewById(R.id.et_grado)).getText().toString();
            if (grado.equals("Seleccionar grado")){
                Snackbar.make(view,"Seleccione un grado para continuar",Snackbar.LENGTH_LONG).show();
                return;
            }
            commons.reporte.setGrado(grado);
            if(esBasico()){
                //GET CNB
                boolean seleccionado = false;
                commons.reporte.initMateria();
                for (int i = 0; i<switches.length; i++) {
                    int switchId = switches[i];
                    if( ((SwitchCompat)view.findViewById(switchId)).isChecked() ){
                        commons.reporte.addMateria(cnbLabels[i]);
                        seleccionado = true;
                    }
                }
                if(!seleccionado){
                    Snackbar.make(view,"Seleccione al menos una materia para continuar",Snackbar.LENGTH_LONG).show();
                    return;
                }
            }else{
                //GET PERIODOS
                String periodos = ((EditText)view.findViewById(R.id.et_periodos)).getText().toString();
                commons.reporte.setPerdidos(Integer.parseInt(periodos));
            }
        }


        /*do next*/
        FragmentTransaction ft = commons.getCurrentFragment().getFragmentManager().beginTransaction();
        ft.replace(R.id.pager, new selectEnvio()).addToBackStack("envio");
        ft.commit();
    }

}
