package com.material.rdd.modelos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.material.rdd.R;
import com.material.rdd.commons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Reporte {
    private String centro;
    private String nombre;
    private String actividad;
    private String fecha;
    private String grado;
    private List<String> materias;


    private int tipo_envio;
    //private int actividad;
    private int perdidos;

    /*CONSTANTES*/
    public static final int INTERNET = 530;
    public static final int SMS = 155;

    public static final String NORMALES = "Las actividades fueron normales";
    public static final String CERRADO = "El centro educativo Estaba cerrado";
    public static final String CIERTAS_MATERIAS = "No se impartieron ciertas materias";


    public static final int NORMALES_CODE = 1;
    public static final int CERRADO_CODE = 2;
    public static final int CIERTAS_MATERIAS_CODE = 3;

    public static final String PRIMERO_PRIMARIA = "Primero primaria";
    public static final String SEGUNDO_PRIMARIA = "Segundo primaria";
    public static final String TERCERO_PRIMARIA = "Tercero primaria";
    public static final String CUARTO_PRIMARIA = "Cuarto primaria";
    public static final String QUINTO_PRIMARIA = "Quinto primaria";
    public static final String SEXTO_PRIMARIA = "Sexto primaria";

    public static final String PRIMERO_BASICO = "Primero básico";
    public static final String SEGUNDO_BASICO = "Segundo básico";
    public static final String TERCERO_BASICO = "Tercero básico";

    public static final String CUARTO_DIVER = "Cuarto diversificado";
    public static final String QUINTO_DIVER = "Quinto diversificado";
    public static final String SEXTO_DIVER = "Sexto diversificado";

    public static final int PRIMERO_PRIMARIA_CODE =1;
    public static final int SEGUNDO_PRIMARIA_CODE =2;
    public static final int TERCERO_PRIMARIA_CODE =3;
    public static final int CUARTO_PRIMARIA_CODE =4;
    public static final int QUINTO_PRIMARIA_CODE =5;
    public static final int SEXTO_PRIMARIA_CODE =6;

    public static final int PRIMERO_BASICO_CODE =7;
    public static final int SEGUNDO_BASICO_CODE =8;
    public static final int TERCERO_BASICO_CODE =9;

    public static final int CUARTO_DIVER_CODE =10;
    public static final int QUINTO_DIVER_CODE =11;
    public static final int SEXTO_DIVER_CODE =12;


    public static final String MAT = "Matemáticas";
    public static final String MAYAS = "Culturas e Idiomas Mayas, Garífuna o Xinca";
    public static final String ESPANOL = "Comunicación y Lenguaje, Idioma Español";
    public static final String EXTRANJERO = "Comunicación y Lenguaje, Idioma Extranjero";
    public static final String NATURALES = "Ciencias Naturales";
    public static final String SOCIALES = "Ciencias Sociales";
    public static final String ARTISTICA = "Educación Artística";
    public static final String EMPRENDIMIENTO = "Emprendimiento para la Productividad";
    public static final String TAC = "Tecnologías del Aprendizaje y la Comunicación";
    public static final String FISICA = "Educación Física";

    public static final int MAT_CODE =1;
    public static final int MAYAS_CODE =2;
    public static final int ESPANOL_CODE =3;
    public static final int EXTRANJERO_CODE =4;
    public static final int NATURALES_CODE =5;
    public static final int SOCIALES_CODE =6;
    public static final int ARTISTICA_CODE =7;
    public static final int EMPRENDIMIENTO_CODE =8;
    public static final int TAC_CODE =9;
    public static final int FISICA_CODE =10;

    public Reporte(String centro, String nombre, String actividad, String fecha) {
        this.centro = centro;
        this.nombre = nombre;
        this.actividad = actividad;
        this.fecha = fecha;
    }

    public Reporte() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getTipo_envio() {
        return tipo_envio;
    }

    public void setTipo_envio(int tipo_envio) {
        this.tipo_envio = tipo_envio;
    }

    public int getPerdidos() {
        return perdidos;
    }

    public void setPerdidos(int perdidos) {
        this.perdidos = perdidos;
    }

    public void initMateria() {
        materias = new ArrayList<String>();
    }

    public void addMateria(String materia) {
        materias.add(materia);
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getActividad() {
        return actividad;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    private JSONObject getJsonFromReporte(boolean tipo_envio) {
        JSONObject message = new JSONObject();
        try {
            message.put("centro", centro);
            message.put("fecha", fecha);
            message.put("actividad", actividad);
            if (actividad == CIERTAS_MATERIAS) {
                message.put("grado", grado);
                if (commons.reporte.esBasico()) {
                    JSONArray materiasJSON = new JSONArray();
                    for (String materia : materias) {
                        materiasJSON.put(materia);
                    }
                    message.put("materias", (Object) materiasJSON);
                } else {
                    message.put("perdidos", perdidos);
                }
            }

            /*tipo de envío*/
            if (tipo_envio) {
                message.put("tipo_envio", this.tipo_envio);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return message;
    }

    private String getSmsFromReporte() {
        String sms = "fhi360";
        sms += "$" + centro;
        sms += "$" + fecha;
        sms += "$" + actividadToCode(actividad);

        if (actividad == CIERTAS_MATERIAS) {
            sms += "$" + gradoToCode(grado);
            if (commons.reporte.esBasico()) {
                sms+="$";
                for (String materia : materias) {
                    sms+=materiaToCode(materia)+"#";
                }
                sms = sms.substring(0,sms.length()-1);
            } else {
                sms+="$"+perdidos;
            }
        }
        return sms;
    }

    private static Reporte reporteFromJson(String json) {
        Reporte reporte = new Reporte();
        try {
            JSONObject jsonReporte = new JSONObject(json);
            /*centro*/
            String codigo = jsonReporte.getString("centro");
            reporte.setCentro(codigo);
            {
                /*set valores de nombre y direccion*/
                Establecimiento estInfo = commons.getEstablecimientoFavorito(codigo);
                reporte.setNombre(estInfo.getNombre() + ", " + estInfo.getDireccion());

            }
            /*fecha*/
            reporte.setFecha(jsonReporte.getString("fecha"));
            /*actividad*/
            String actividad = jsonReporte.getString("actividad");
            reporte.setActividad(actividad);
            /*tipo de envio*/
            if (jsonReporte.has("tipo_envio")) {
                reporte.setTipo_envio(jsonReporte.getInt("tipo_envio"));
            }

            if (actividad.equals(Reporte.CIERTAS_MATERIAS)) {
                /*grado*/
                reporte.setGrado(jsonReporte.getString("grado"));
                if (esBasico(codigo)) {
                    /*materias*/
                    JSONArray materias = jsonReporte.getJSONArray("materias");
                    /*materias del CNB*/
                    reporte.initMateria();
                    int len = materias.length();
                    for (int i = 0; i < len; i++) {
                        String materia = materias.getString(i);
                        reporte.addMateria(materia);
                    }
                } else {
                    /*perdidos*/
                    reporte.setPerdidos(jsonReporte.getInt("perdidos"));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return reporte;
    }

    public static ArrayList<Reporte> reportesFromJson(String json) {
        ArrayList<Reporte> reportes = new ArrayList<>();

        try {
            JSONArray root = new JSONArray(json);
            int len = root.length();
            for (int i = 0; i < len; i++) {
                JSONObject reporte = root.getJSONObject(i);
                reportes.add(reporteFromJson(reporte.toString()));
                commons.log(reporte.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return reportes;
    }


    private String getFormatedFecha() {
        String formatedFecha;
        formatedFecha = fecha.split("-")[0] + "-" + (Integer.parseInt(fecha.split("-")[1]) + 1) + "-" + fecha.split("-")[2];
        return formatedFecha;
    }

    public void enviar(Context activity) {
        switch (tipo_envio) {
            case INTERNET:
                enviar_internet(activity);
                break;
            case SMS:
                enviar_sms(activity);
                break;
        }
    }

    private boolean validateInternetConnection(Context activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnected())) {
            return false;
        }
        return true;
    }

    private void enviar_internet(Context activity) {
        if (validateInternetConnection(activity)) {
            rdd_addReporte(activity);
        } else {
            addEncolados(activity);
            commons.showAlertDialog(activity, "Sin conexión!", "El dispositivo no cuenta con una conexión a internet en este momento. Su reporte se enviara cuando se cuente con conexión a internet", R.drawable.ic_signal_wifi_off, null);
        }

    }

    private void enviar_sms(Context activity) {
        try{
            commons.log("ENVIAR SMS");
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("47703741", null, getSmsFromReporte(), null, null);
            commons.log(getSmsFromReporte());
            commons.log("MENSAJE ENVIADO");
        }catch(Exception e){
            commons.log("Message not sent!");
        }

        addEncolados(activity);
        commons.showMessageDialog(activity, "Reporte almacenado!", "Su reporte será envíado cuando se alcance el periodo de envío de mensajes de texto. Puede visualizar los reportes en espera de ser enviados en el botón \"Ver Reportes Llenados\" del menú principal", R.drawable.ic_save, null);

    }

    private void addEnviados(Context activity) {
        String enviados = commons.getShared(activity, commons.ENVIADOS, "[]");

        if (enviados.equals("[]")) {
            enviados = "[" + getJsonFromReporte(false).toString() + "]";
        } else {
            enviados = "[" + enviados.substring(1, enviados.length() - 1) + "," + getJsonFromReporte(false).toString() + "]";
        }

        commons.putShared(activity, commons.ENVIADOS, enviados);
    }

    private void addEncolados(Context activity) {
        String encolados = commons.getShared(activity, commons.SIN_ENVIAR, "[]");

        if (encolados.equals("[]")) {
            encolados = "[" + getJsonFromReporte(true).toString() + "]";
        } else {
            encolados = "[" + encolados.substring(1, encolados.length() - 1) + "," + getJsonFromReporte(true).toString() + "]";
        }

        commons.putShared(activity, commons.SIN_ENVIAR, encolados);
    }

    public Boolean esBasico() {
        return this.getCentro().split("-")[3].equals("45");
    }

    public static Boolean esBasico(String codigo) {
        return codigo.split("-")[3].equals("45");
    }


    private void rdd_addReporte(final Context activity) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = commons.RDDAPI_URL + "addReporte";
        JSONObject jsonBody = this.getJsonFromReporte(false);
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        responseFunction(activity, JSONString);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(request);

    }

    private void responseFunction(Context activity, String JSONString) {
        try {
            addEnviados(activity);
            commons.showMessageDialog(activity, "Reporte enviado!", "Su reporte fue enviado exitosamente. Puede visualizar los reportes enviados en el botón \"Ver Reportes Llenados\" del menú principal", R.drawable.ic_email, null);

            JSONObject res = new JSONObject(JSONString);
            Toast.makeText(activity, res.getString("mensaje").toString(), Toast.LENGTH_LONG).show();
        } catch (Throwable tx) {
            tx.printStackTrace();
        }

    }

    private int actividadToCode(String actividad) {
        switch (actividad) {
            case NORMALES:
                return NORMALES_CODE;
            case CERRADO:
                return CERRADO_CODE;
            case CIERTAS_MATERIAS:
                return CIERTAS_MATERIAS_CODE;
        }
        return -1;
    }

    private int gradoToCode(String grado) {
        switch (grado) {
            case PRIMERO_PRIMARIA:
                return PRIMERO_PRIMARIA_CODE;
            case SEGUNDO_PRIMARIA:
                return SEGUNDO_PRIMARIA_CODE;
            case TERCERO_PRIMARIA:
                return TERCERO_PRIMARIA_CODE;
            case CUARTO_PRIMARIA:
                return CUARTO_PRIMARIA_CODE;
            case QUINTO_PRIMARIA:
                return QUINTO_PRIMARIA_CODE;
            case SEXTO_PRIMARIA:
                return SEXTO_PRIMARIA_CODE;

            case PRIMERO_BASICO:
                return PRIMERO_BASICO_CODE;
            case SEGUNDO_BASICO:
                return SEGUNDO_BASICO_CODE;
            case TERCERO_BASICO:
                return TERCERO_BASICO_CODE;

            case CUARTO_DIVER:
                return CUARTO_DIVER_CODE;
            case QUINTO_DIVER:
                return QUINTO_DIVER_CODE;
            case SEXTO_DIVER:
                return SEXTO_DIVER_CODE;
        }
        return -1;
    }

    private int materiaToCode(String materia) {
        switch (materia) {
            case MAT:
                return MAT_CODE;
            case MAYAS:
                return MAYAS_CODE;
            case ESPANOL:
                return ESPANOL_CODE;
            case EXTRANJERO:
                return EXTRANJERO_CODE;
            case NATURALES:
                return NATURALES_CODE;
            case SOCIALES:
                return SOCIALES_CODE;
            case ARTISTICA:
                return ARTISTICA_CODE;
            case EMPRENDIMIENTO:
                return EMPRENDIMIENTO_CODE;
            case TAC:
                return TAC_CODE;
            case FISICA:
                return FISICA_CODE;
        }
        return -1;
    }

}
