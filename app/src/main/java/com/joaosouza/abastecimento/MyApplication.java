package com.joaosouza.abastecimento;

import android.app.Application;

import com.joaosouza.abastecimento.model.GerenciadorMigracoesRealm;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        //Realm.deleteRealm(Realm.getDefaultConfiguration());

        RealmConfiguration configuracaoRealm = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration( new GerenciadorMigracoesRealm() )
                .build();

        Realm.setDefaultConfiguration( configuracaoRealm );
        Realm.getInstance( configuracaoRealm );
    }
}
