package com.joaosouza.abastecimento.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.joaosouza.abastecimento.Abastecimentos;
import com.joaosouza.abastecimento.R;
import com.joaosouza.abastecimento.model.AbastecimentoModel;

import java.text.DateFormat;

public class AbastecimentoViewHolder extends RecyclerView.ViewHolder {

    private TextView data;
    private TextView info;
    private ImageView img;
    private ConstraintLayout clPai;
    private String idDoAbastecimento;

    public AbastecimentoViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cast do contexto para activity atual e chamada do método
                ((Abastecimentos) v.getContext()).editaAbastecimento(v, idDoAbastecimento);
            }
        });

        data = itemView.findViewById(R.id.textData);
        info = itemView.findViewById(R.id.textInfo);
        img = itemView.findViewById(R.id.imageView);
        clPai = (ConstraintLayout) itemView;
    }

    public void atualizaGaveta(AbastecimentoModel abastecimento){

        idDoAbastecimento = abastecimento.getId();

        DateFormat formatador = android.text.format.DateFormat.getDateFormat(data.getContext());
        String dataFormatada = formatador.format(abastecimento.getData().getTime());
        data.setText(dataFormatada);

        String valorAbastecido = String.valueOf(abastecimento.getLitrosAbastecidos());
        String valorKm = String.valueOf(abastecimento.getKmAtual());

        SetImage(abastecimento.getPosto());

        info.setText("Abastecidos: " + valorAbastecido + " litros | KM do Veículo: " + valorKm);
    }

    private void SetImage(String posto){

       if(posto.equals("Ipiranga")){
            img.setImageResource(R.mipmap.ic_ipiranga);
        }else if(posto.equals("Texaco")){
            img.setImageResource(R.mipmap.ic_texaco);
        }else if(posto.equals("Petrobras")){
            img.setImageResource(R.mipmap.ic_petrobras);
        }else if(posto.equals("Shell")){
            img.setImageResource(R.mipmap.ic_shell);
        }else{
            img.setImageResource(R.mipmap.ic_outros);
        }
    }
}
