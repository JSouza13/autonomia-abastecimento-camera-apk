package com.joaosouza.abastecimento.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.joaosouza.abastecimento.R;
import com.joaosouza.abastecimento.model.AbastecimentoDAO;
import com.joaosouza.abastecimento.model.AbastecimentoModel;

public class AbastecimentoAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout elementoPrincipalXML = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.abastecimento_item, parent, false
        );

        AbastecimentoViewHolder gaveta = new AbastecimentoViewHolder(elementoPrincipalXML);

        return gaveta;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AbastecimentoModel abastecimento = AbastecimentoDAO.obterInstancia().obterLista().get(position);
        AbastecimentoViewHolder gaveta = (AbastecimentoViewHolder) holder;

        gaveta.atualizaGaveta(abastecimento);
    }

    @Override
    public int getItemCount() {
        return AbastecimentoDAO.obterInstancia().obterLista().size();
    }
}
