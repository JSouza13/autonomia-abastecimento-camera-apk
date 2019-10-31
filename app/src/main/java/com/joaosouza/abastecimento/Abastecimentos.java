package com.joaosouza.abastecimento;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.joaosouza.abastecimento.adapter.AbastecimentoAdapter;
import com.joaosouza.abastecimento.model.AbastecimentoDAO;

public class Abastecimentos extends AppCompatActivity {

    private AbastecimentoAdapter adaptador;
    private RecyclerView rvAbastecimentos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimentos);

        rvAbastecimentos = findViewById(R.id.rvAbastecimentos);

        adaptador = new AbastecimentoAdapter();
        rvAbastecimentos.setLayoutManager(new LinearLayoutManager(this));
        rvAbastecimentos.setAdapter(adaptador);
    }

    public void editaAbastecimento(View v, String abastecimentoId){
        Intent intencao= new Intent(this, Abastecimento.class);
        intencao.putExtra("abastecimentoId", abastecimentoId);
        startActivityForResult(intencao, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == 200){
                int posicao = data.getIntExtra("posicaoDoObjetoEditado", -1);
                adaptador.notifyItemChanged(posicao);
                rvAbastecimentos.smoothScrollToPosition(posicao);
            }else if(resultCode == 201){
                Toast.makeText(this,"Abastecimento Inserido com Sucesso", Toast.LENGTH_LONG).show();
                int posicao = AbastecimentoDAO.obterInstancia().obterLista().size()-1;
                adaptador.notifyItemInserted(posicao);
                rvAbastecimentos.smoothScrollToPosition(posicao);
            }else if(resultCode == 202){
                Toast.makeText(this, "Abastecimento Excluído com Sucesso", Toast.LENGTH_LONG).show();
                int posicao = data.getIntExtra("posicaoDoObjetoExcluido", -1);
                adaptador.notifyItemRemoved(posicao);

            }
        }
    }
    public void adicionarAbastecimento(View v){
        Intent intencao = new Intent(this, Abastecimento.class);
        startActivityForResult(intencao, 1);
    }
}
