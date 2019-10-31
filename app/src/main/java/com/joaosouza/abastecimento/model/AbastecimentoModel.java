package com.joaosouza.abastecimento.model;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class AbastecimentoModel extends RealmObject {

    @PrimaryKey
    private String id;
    private double kmAtual;
    private double litrosAbastecidos;
    private Date dataPura;
    private String posto;
    private String caminhoDaFotografia;

    @Ignore
    private Calendar data;

    public AbastecimentoModel() {
        id = UUID.randomUUID().toString();


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getKmAtual() {
        return kmAtual;
    }

    public void setKmAtual(double kmAtual) {
        this.kmAtual = kmAtual;
    }

    public double getLitrosAbastecidos() {
        return litrosAbastecidos;
    }

    public void setLitrosAbastecidos(double litrosAbastecidos) {
        this.litrosAbastecidos = litrosAbastecidos;
    }

    public String getPosto() {
        return posto;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public Calendar getData() {
        if (dataPura != null){
            data = Calendar.getInstance();
            data.setTime(dataPura);
        }
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
        this.dataPura = data.getTime();
    }

    public String getCaminhoDaFotografia() {
        return caminhoDaFotografia;
    }

    public void setCaminhoDaFotografia(String caminhoDaFotografia){
        this.caminhoDaFotografia = caminhoDaFotografia;
    }
}
