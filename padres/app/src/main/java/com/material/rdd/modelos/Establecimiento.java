package com.material.rdd.modelos;

import org.json.JSONException;
import org.json.JSONObject;

public class Establecimiento
{
    private String Codigo;
    private String Nombre;
    private String Direccion;
    private String Jornada;
    private double Latitud;
    private double Longitud;

    private boolean selected;

    public Establecimiento(String Codigo, String nombre, String Direccion, String Jornada)
    {
        this.Codigo=Codigo;
        this.Nombre= nombre;
        this.Direccion=Direccion;
        this.Jornada=Jornada;
        this.selected=false;
    }

    /*HAY QUE BORRARLO*/
    public Establecimiento(String Codigo, String nombre, String Direccion,String Jornada, double Latitud, double Longitud)
    {
    }

    public Establecimiento(String Codigo, String nombre, String Direccion,String Jornada, double Latitud, double Longitud,boolean Selected)
    {
        this.Codigo=Codigo;
        this.Nombre= nombre;
        this.Direccion=Direccion;
        this.Jornada = Jornada;
        this.Latitud=Latitud;
        this.Longitud=Longitud;
        this.selected=Selected;
    }

    public Establecimiento(){};

    public String getDireccion() {
        return Direccion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setCodigo(String codigo) { Codigo = codigo; }

    public String getCodigo() { return Codigo; }

    public void setLatitud(double latitud) { Latitud = latitud; }

    public void setLongitud(double longitud) { Longitud = longitud; }

    public double getLatitud() { return Latitud; }

    public double getLongitud() { return Longitud; }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getJornada() {
        return Jornada;
    }

    public void setJornada(String jornada) {
        Jornada = jornada;
    }


    public Establecimiento clone() {
        Establecimiento nuevo = new Establecimiento();
        nuevo.Codigo = this.Codigo;
        nuevo.Nombre = this.Nombre;
        nuevo.Direccion = this.Direccion;
        nuevo.Latitud = this.Latitud;
        nuevo.Longitud = this.Longitud;
        nuevo.selected = this.selected;
        nuevo.Jornada = this.Jornada;
        return nuevo;
    }

    public String toJSONString(){
        return toJSON().toString();
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("codigo",this.Codigo);
            jsonObject.put("nombre",this.Nombre);
            jsonObject.put("direccion",this.Direccion);
            jsonObject.put("latitud",this.Latitud);
            jsonObject.put("longitud",this.Longitud);
            jsonObject.put("selected",this.selected);
            jsonObject.put("jornada",this.Jornada);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    public static Establecimiento newFromJSON(JSONObject jsonObject){
        try {
            String codigo = jsonObject.getString("codigo");
            String nombre = jsonObject.getString("nombre");
            String direccion = jsonObject.getString("direccion");
            Double latitud = jsonObject.getDouble("latitud");
            Double longitud = jsonObject.getDouble("longitud");
            Boolean selected = jsonObject.getBoolean("selected");
            String jornada = jsonObject.getString("jornada");
            return new Establecimiento(codigo,nombre,direccion,jornada,latitud,longitud,selected);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
