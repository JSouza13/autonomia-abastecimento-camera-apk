package com.joaosouza.abastecimento.model;

import com.joaosouza.abastecimento.Formulario;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AbastecimentoDAO {
    private ArrayList<AbastecimentoModel> bancoDeDados;

    public ArrayList<AbastecimentoModel> obterLista(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults lista = realm.where(AbastecimentoModel.class).findAll();
        bancoDeDados.clear();
        bancoDeDados.addAll(realm.copyFromRealm(lista));
        return bancoDeDados;
    }

    public void addNaLista(AbastecimentoModel a){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(a);
        realm.commitTransaction();
    }

    public int atualizaNaLista(AbastecimentoModel abastecimento){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(abastecimento);
        realm.commitTransaction();

        for(int i = 0; i < bancoDeDados.size(); i++){
            if(bancoDeDados.get(i).getId().equals(abastecimento.getId())){
                bancoDeDados.set(i, abastecimento);
                return i;
            }
        }
        return -1;
    }

    public int excluiDaLista(AbastecimentoModel abastecimento){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(AbastecimentoModel.class).equalTo("id", abastecimento.getId()).findFirst().deleteFromRealm();
        realm.commitTransaction();

        for(int i = 0; i < bancoDeDados.size(); i++){
            if(bancoDeDados.get(i).getId().equals(abastecimento.getId())){
                bancoDeDados.remove(i);
                return i;
            }
        }
        return -1;
    }

    public AbastecimentoModel obterObjetoPeloId(String id){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        AbastecimentoModel abastecimento = realm.copyFromRealm(realm.where(AbastecimentoModel.class).equalTo("id", id).findFirst());
        realm.commitTransaction();
        return abastecimento;
    }

    private static AbastecimentoDAO INSTANCIA;

    public static AbastecimentoDAO obterInstancia(){
        if (INSTANCIA == null){
            INSTANCIA = new AbastecimentoDAO();
        }
        return INSTANCIA;
    }

    private AbastecimentoDAO(){
        bancoDeDados = new ArrayList<AbastecimentoModel>();
    }
}
