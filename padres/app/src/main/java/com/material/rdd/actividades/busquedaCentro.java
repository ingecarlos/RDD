package com.material.rdd.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.material.rdd.R;
import com.material.rdd.adapter.AdapterCentroSelect;
import com.material.rdd.commons;
import com.material.rdd.modelos.Establecimiento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class busquedaCentro extends AppCompatActivity {
    RecyclerView recyclerView;

    Map<String, String> departamentosMap;
    Map<String, municipio> municipiosMap;
    Map<String, String> nivelesMap;

    ArrayList<Establecimiento> listaEstablecimientos;

    Spinner departamentoSpinner;
    Spinner municipioSpinner;
    Spinner nivelSpinner;

    String muniSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_centro);

        initComponents();
    }

    private void initComponents(){
        initRecyclerView();
        setSpinners();
        setListeners();
        cleanSpinners(0);
        setDepartamentos();
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setSpinners(){
            departamentoSpinner = findViewById(R.id.departamentoSpinner);
            municipioSpinner = findViewById(R.id.municipioSpinner);
            nivelSpinner = findViewById(R.id.nivelSpinner);
    }

    private void setListeners() {
        departamentoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = ((TextView) selectedItemView).getText().toString();
                if (!selected.equals("Departamento")) {
                    cleanSpinners(1);
                    setMunicipios(departamentosMap.get(selected));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        municipioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = ((TextView) selectedItemView).getText().toString();
                if (!selected.equals("Municipio")) {
                    cleanSpinners(2);
                    muniSelected = selected;
                    municipio m = municipiosMap.get(selected);
                    setNiveles(m.depto,m.municipio);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        nivelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = ((TextView) selectedItemView).getText().toString();
                if (!selected.equals("Nivel")) {
                    municipio m = municipiosMap.get(muniSelected);
                    String nivel = nivelesMap.get(selected);
                    setCentros(m.depto,m.municipio,nivel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void setLista() {
        AdapterCentroSelect ca;

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        registerForContextMenu(recyclerView);

        ca = new AdapterCentroSelect(this,listaEstablecimientos,R.layout.cardview_establecimiento_select_api);
        ca.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(ca);
    }

    /*CLICK LISTENER*/
    AdapterCentroSelect.OnItemClickListener onItemClickListener = new AdapterCentroSelect.OnItemClickListener() {
        @Override
        public void onItemClick(View view, Establecimiento obj, int position) {
            addEstablecimientoToFavoritos(obj);
        }
    };

    private void addEstablecimientoToFavoritos(Establecimiento e){
        commons.addEstablecimientoFavorito(this,e);
        Toast.makeText(this,"El establecimiento \"" + e.getNombre() + "\" fue añadido a la lista de establecimientos favoritos",Toast.LENGTH_LONG).show();
        finish();
    }

    private void setDepartamentos() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = commons.RDDAPI_URL + "getDepartamentos";
        JSONObject jsonBody = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        try {
                            JSONObject deptos = new JSONObject(JSONString);
                            if (!deptos.isNull("departamentos")) {
                                //Llenar departamentos
                                JSONArray deptosArray = deptos.getJSONArray("departamentos");
                                int len = deptosArray.length();
                                departamentosMap = new HashMap<>();
                                String deptosArreglo[] = new String[len + 1];
                                deptosArreglo[0] = "Departamento";
                                for (int i = 0; i < len; i++) {
                                    JSONObject JSONDepto = (JSONObject) deptosArray.get(i);
                                    /*meter al arreglo para tener diccionario*/
                                    departamentosMap.put(
                                            JSONDepto.getString("departamento"),
                                            JSONDepto.getString("id")
                                    );
                                    deptosArreglo[i + 1] = JSONDepto.getString("departamento");
                                }
                                /*meter al spinner*/
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                        R.layout.spinner_item_white_text, deptosArreglo);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                departamentoSpinner.setAdapter(adapter);
                            }
                        } catch (Throwable tx) {
                            tx.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(request);

    }

    private void setMunicipios(String departamento) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = commons.RDDAPI_URL + "getMunicipios";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("departamento", departamento);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        try {
                            JSONObject munis = new JSONObject(JSONString);
                            if (!munis.isNull("municipios")) {
                                //Llenar municipios
                                JSONArray munisArray = munis.getJSONArray("municipios");
                                int len = munisArray.length();
                                municipiosMap = new HashMap<>();
                                String munisArreglo[] = new String[len + 1];
                                munisArreglo[0] = "Municipio";
                                for (int i = 0; i < len; i++) {
                                    JSONObject JSONMuni = (JSONObject) munisArray.get(i);
                                    /*meter al arreglo para tener diccionario*/
                                    municipiosMap.put(
                                            JSONMuni.getString("municipio"),
                                            new municipio(JSONMuni.getString("id_depto"), JSONMuni.getString("id_municipio"))
                                    );
                                    munisArreglo[i + 1] = JSONMuni.getString("municipio");
                                }
                                /*meter al spinner*/
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                        R.layout.spinner_item_white_text, munisArreglo);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                municipioSpinner.setAdapter(adapter);
                            }
                        } catch (Throwable tx) {
                            tx.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(request);

    }

    private void setNiveles(String departamento,String municipio) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = commons.RDDAPI_URL + "getNiveles";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("departamento", departamento);
            jsonBody.put("municipio",municipio);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        try {
                            JSONObject niveles = new JSONObject(JSONString);
                            if (!niveles.isNull("niveles")) {
                                //Llenar departamentos
                                JSONArray nivelesArray = niveles.getJSONArray("niveles");
                                int len = nivelesArray.length();
                                nivelesMap = new HashMap<>();
                                String nivelesArreglo[] = new String[len + 1];
                                nivelesArreglo[0] = "Nivel";
                                for (int i = 0; i < len; i++) {
                                    JSONObject JSONNivel = (JSONObject) nivelesArray.get(i);
                                    /*meter al arreglo para tener diccionario*/
                                    nivelesMap.put(
                                            JSONNivel.getString("nivel"),
                                            JSONNivel.getString("id_nivel")
                                    );
                                    nivelesArreglo[i + 1] = JSONNivel.getString("nivel");
                                }
                                /*meter al spinner*/
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                        R.layout.spinner_item_white_text, nivelesArreglo);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                nivelSpinner.setAdapter(adapter);

                            }
                        } catch (Throwable tx) {
                            tx.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO error en response

                    }
                });

        queue.add(request);

    }

    private void setCentros(String departamento, String municipio,String nivel) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = commons.RDDAPI_URL + "getCentros";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("departamento", departamento);
            jsonBody.put("municipio", municipio);
            jsonBody.put("nivel", nivel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String JSONString = response.toString();
                        try {
                            JSONObject centros = new JSONObject(JSONString);
                            if (!centros.isNull("centros")) {
                                listaEstablecimientos = new ArrayList<>();

                                //Llenar departamentos
                                JSONArray centrosArray = centros.getJSONArray("centros");
                                int len = centrosArray.length();
                                for (int i = 0; i < len; i++) {
                                    JSONObject JSONCentro = (JSONObject) centrosArray.get(i);
                                    String codigo = JSONCentro.getString("codigo");
                                    String nombre = JSONCentro.getString("nombre");
                                    String dir = JSONCentro.getString("direccion");
                                    Double latitud = 0.0;
                                    if (!JSONCentro.isNull("latitud"))
                                        latitud = JSONCentro.getDouble("latitud");
                                    Double longitud = 0.0;
                                    if (!JSONCentro.isNull("longitud"))
                                        longitud = JSONCentro.getDouble("longitud");
                                    listaEstablecimientos.add(new Establecimiento(codigo, nombre, dir,"jornada",latitud, longitud,false));
                                }
                                setLista();
                            }
                        } catch (Throwable tx) {
                            tx.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO error en response

                    }
                });


        queue.add(request);

    }

    private void cleanSpinners(int option){
        /*
        * option = 0; desde departamento
        * option = 1; desde municipio
        * option = 2; desde nivel
        */
        ArrayList<String> array;
        ArrayAdapter<String> adapter;

        switch(option){
            case 0:
                array = new ArrayList<>();
                array.add("Departamento");
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item_white_text, array);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                departamentoSpinner.setAdapter(adapter);
            case 1:
                array = new ArrayList<>();
                array.add("Municipio");
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item_white_text, array);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                municipioSpinner.setAdapter(adapter);
            case 2:
                array = new ArrayList<>();
                array.add("Nivel");
                adapter = new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item_white_text, array);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                nivelSpinner.setAdapter(adapter);
        }
    }
}



class municipio {
    public String depto;
    public String municipio;

    public municipio(String depto, String municipio) {
        this.depto = depto;
        this.municipio = municipio;
    }

    public String getDepto() {
        return depto;
    }

    public String getMunicipio() {
        return municipio;
    }
}