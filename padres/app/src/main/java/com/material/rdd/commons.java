package com.material.rdd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.material.rdd.actividades.menuPrincipal;
import com.material.rdd.dialogs.DialogAccept;
import com.material.rdd.fragmentos.selectCentro;
import com.material.rdd.modelos.Establecimiento;
import com.material.rdd.modelos.Reporte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Stack;

import afu.org.checkerframework.checker.nullness.qual.Nullable;

public class commons {

    /*UTILITIES*/
    public static void startActivity(Context context, Class c) {
        Intent i = new Intent(context, c);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    public static void log(String msg) {
        Log.d("DEBUG", msg);
    }
    public static void toMenu(Context activity) {
        Intent intent = new Intent(activity, menuPrincipal.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    public static void delay(int del){
        try {
            Thread.sleep(del);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /*END UTILITIES*/

    /*ESTABLECIMIENTOS FAVORITOS*/
    private static ArrayList<Establecimiento> establecimientos_favoritos;
    public static Establecimiento getEstablecimientoFavorito(String codigo) {
        for (Establecimiento e : establecimientos_favoritos) {
            if (codigo.equals(e.getCodigo())) {
                return e;
            }
        }
        return null;
    }
    public static ArrayList<Establecimiento> getEstablecimientosFavoritos(Context activity) {
        establecimientos_favoritos = new ArrayList<>();
        String favoritos = getShared(activity, FAVORITOS, null);
        if (favoritos == null || favoritos.equals("")) return null;

        try {
            JSONArray jsonEstablecimientos = new JSONArray(favoritos);
            int len = jsonEstablecimientos.length();
            for (int i = 0; i < len; i++) {
                JSONObject jsonEstablecimiento = jsonEstablecimientos.getJSONObject(i);
                establecimientos_favoritos.add(Establecimiento.newFromJSON(jsonEstablecimiento));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return establecimientos_favoritos;
    }
    public static void addEstablecimientoFavorito(Context activity, Establecimiento e) {
        String establecimientosString = "[]";
        String establecimientos = getShared(activity, FAVORITOS, null);
        if (establecimientos == null || establecimientos.equals("")) {
            establecimientosString = "[" + e.toJSONString() + "]";
        } else {
            try {
                JSONArray jsonEstablecimientos = new JSONArray(establecimientos);
                jsonEstablecimientos.put(e.toJSON());
                establecimientosString = jsonEstablecimientos.toString();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        putShared(activity, FAVORITOS, establecimientosString);
    }
    public static void removeEstablecimientoFavorito(Context activity, String codigo) {
        int targetIndex = 0;
        for (Establecimiento e : establecimientos_favoritos) {
            if (codigo.equals(e.getCodigo())) {
                break;
            }
            targetIndex++;
        }

        establecimientos_favoritos.remove(targetIndex);

        /*COMIT CHANGES TO SHARED PREFERENCES*/
        JSONArray jsonEstablecimientos = new JSONArray();
        for (Establecimiento e : establecimientos_favoritos) {
            jsonEstablecimientos.put(e.toJSON());
        }
        String establecimientosString = jsonEstablecimientos.toString();

        putShared(activity, FAVORITOS, establecimientosString);
    }
    /*END FAVORITOS*/

    /*REPORTE*/
    public static com.material.rdd.actividades.reportarDia reportarDia;
    private static Stack<Fragment> currentFragment;
    public static Reporte reporte;
    public static void pushFragment(Fragment fragment) {
        currentFragment.push(fragment);
    }
    public static Fragment getCurrentFragment(){
        return currentFragment.get(currentFragment.size()-1);
    }
    public static void initReporte(com.material.rdd.actividades.reportarDia instance){
        commons.reportarDia = instance;
        currentFragment = new Stack<>();
    }
    /*END REPORTE*/

    public static final String RDDAPI_URL = "http://odk.exitoescolar.org:5100/";


    public static void showAlertDialog(Context activity,String title, String mensaje, int icon, String textoBoton) {
        Intent intent = new Intent(activity.getApplicationContext(), DialogAccept.class);
        intent.putExtra("title",title);
        intent.putExtra("mensaje", mensaje);
        intent.putExtra("icon", icon);
        intent.putExtra("alert", true);
        intent.putExtra("boton",textoBoton);
        activity.startActivity(intent);
    }
    public static void showMessageDialog(Context activity,String title, String mensaje, int icon, String textoBoton) {
        Intent intent = new Intent(activity.getApplicationContext(), DialogAccept.class);
        intent.putExtra("title",title);
        intent.putExtra("mensaje", mensaje);
        intent.putExtra("icon", icon);
        intent.putExtra("alert", false);
        intent.putExtra("boton",textoBoton);
        activity.startActivity(intent);
    }

    /*SHARED PREFERENCES*/
    public static final String FAVORITOS = "favoritos";
    public static final String ENVIADOS = "enviados";
    public static final String SIN_ENVIAR = "encolados";
    public static String getShared(Context activity, String key, String defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        return sp.getString(key, defValue);
    }
    public static void putShared(Context activity, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static void clearEnviados(Context activity) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("enviados", "[]");
        editor.apply();
    }
    /* END SHARED PREFERENCES*/


}
