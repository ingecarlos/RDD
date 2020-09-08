package com.material.rdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.material.rdd.R;
import com.material.rdd.modelos.Establecimiento;

import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.NonNull;
import butterknife.OnClick;

public class AdapterCentroConfiguracion extends RecyclerView.Adapter<AdapterCentroConfiguracion.EstablecimientoViewHolder>
{
    private List<Establecimiento> establecimientosList;

    private OnDeleteClick mOnDeleteClick;

    public interface OnDeleteClick {
        void onDeleteClick(View view, Establecimiento obj, int position);
    }

    public void setOnDeleteClick(final OnDeleteClick mOnDeleteClick) {
        this.mOnDeleteClick = mOnDeleteClick;
    }

    public AdapterCentroConfiguracion(List<Establecimiento> establecimientosList)
    {
        this.establecimientosList=establecimientosList;
    }

    public void clearEstablecimientosList()
    {
        this.establecimientosList.clear();

    }

    public void setEstablecimientosList(List<Establecimiento> establecimientosList)
    {
        this.establecimientosList.clear();
        this.establecimientosList.addAll(establecimientosList);
    }

    @NonNull
    @Override
    public EstablecimientoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cardview_establecimiento_configuracion,viewGroup,false);
        return new EstablecimientoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final EstablecimientoViewHolder establecimientoViewHolder, final int i)
    {
        Establecimiento est=establecimientosList.get(i);
        establecimientoViewHolder.vTitle.setText(est.getNombre());
        establecimientoViewHolder.vSubtitle.setText(est.getJornada() + ", " + est.getDireccion());
        establecimientoViewHolder.vCodigo.setText(est.getCodigo());
        establecimientoViewHolder.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnDeleteClick == null) return;
                mOnDeleteClick.onDeleteClick(view, establecimientosList.get(i),i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return establecimientosList.size();
    }



    public static  class EstablecimientoViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView vTitle;
        protected TextView vSubtitle;
        protected TextView vCodigo;
        protected ImageView icDelete;

        public EstablecimientoViewHolder(final View itemView)
        {
            super(itemView);
            itemView.setClickable(true);
            vTitle= itemView.findViewById(R.id.title);
            vSubtitle= itemView.findViewById(R.id.subtitle);
            vCodigo = itemView.findViewById(R.id.codigo);
            icDelete = itemView.findViewById(R.id.ic_delete);
        }
    }
}
