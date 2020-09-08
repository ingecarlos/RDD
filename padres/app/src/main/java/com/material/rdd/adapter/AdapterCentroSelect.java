package com.material.rdd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

import com.material.rdd.R;
import com.material.rdd.modelos.Establecimiento;

import java.util.ArrayList;
import java.util.List;

public class AdapterCentroSelect extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Establecimiento> items = new ArrayList<>();

    private Context ctx;

    @LayoutRes
    private int layout_id;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Establecimiento obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterCentroSelect(Context context, List<Establecimiento> items, @LayoutRes int layout_id) {
        this.items = items;
        ctx = context;
        this.layout_id = layout_id;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView subtitle;
        public TextView codigo;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            subtitle = v.findViewById(R.id.subtitle);
            codigo = v.findViewById(R.id.codigo);
            lyt_parent = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout_id, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Establecimiento e = items.get(position);
            view.title.setText(e.getNombre());
            view.subtitle.setText(e.getJornada()+", " + e.getDireccion());
            view.codigo.setText(e.getCodigo());

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener == null) return;
                    mOnItemClickListener.onItemClick(view, items.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}