package com.material.rdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.material.rdd.R;
import com.material.rdd.modelos.Reporte;

import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.NonNull;

public class AdapterReporte extends RecyclerView.Adapter<AdapterReporte.ReporteViewHolder>
{
private List<Reporte> reportesList;

public AdapterReporte(List<Reporte> reportesList)
{
    this.reportesList=reportesList;
}

public void clearReportesList()
{
    this.reportesList.clear();

}


    public void setReportesList(List<Reporte> reportesList)
    {
        this.reportesList.clear();
        this.reportesList.addAll(reportesList);
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.cardview_reporte,viewGroup,false);
        return new ReporteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReporteViewHolder reporteViewHolder, int i)
    {
        Reporte reporte=reportesList.get(i);
        reporteViewHolder.centro.setText(reporte.getCentro());
        reporteViewHolder.nombre.setText(reporte.getNombre());
        reporteViewHolder.reportado.setText(reporte.getActividad());
        reporteViewHolder.fecha.setText(reporte.getFecha());

    }

    @Override
    public int getItemCount() {
        return reportesList.size();
    }



    public static  class ReporteViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView centro;
        protected TextView nombre;
        protected TextView reportado;
        protected TextView fecha;


        public ReporteViewHolder(final View itemView)
        {
            super(itemView);
            itemView.setClickable(true);


            centro = itemView.findViewById(R.id.centro);
            nombre= itemView.findViewById(R.id.nombre);
            reportado=itemView.findViewById(R.id.reportado);
            fecha= itemView.findViewById(R.id.fecha);

        }
    }
}
