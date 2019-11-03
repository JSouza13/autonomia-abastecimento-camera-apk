package com.joaosouza.abastecimento;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.joaosouza.abastecimento.model.AbastecimentoModel;
import com.joaosouza.abastecimento.model.AbastecimentoDAO;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.UUID;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Formulario extends AppCompatActivity  {

    private AbastecimentoModel objetoAbastecimento;
    private String idDoAbastecimento;
    private EditText editKm;
    private EditText editLitros;
    private TextInputEditText etData;
    private Spinner postos;

    private MaterialSpinner spPrioridade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        editKm = findViewById(R.id.editKM);
        editLitros = findViewById(R.id.editLitros);
        etData = findViewById(R.id.etData);
        postos = findViewById(R.id.spPostos);
        etData.setKeyListener(null);

        String[] opcoesPostos = getResources().getStringArray(R.array.opcoes_posto);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcoesPostos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        postos.setAdapter(adapter);

        idDoAbastecimento = getIntent().getStringExtra("abastecimentoId");
        if(idDoAbastecimento == null){
            objetoAbastecimento = new AbastecimentoModel();
            Button btnExcluir = findViewById(R.id.btnExcluir);
            btnExcluir.setVisibility(View.INVISIBLE);
        }else{
            objetoAbastecimento = AbastecimentoDAO.obterInstancia().obterObjetoPeloId(idDoAbastecimento);

            atualizaFotografiaNaTela();

            editKm.setText(String.valueOf(objetoAbastecimento.getKmAtual()));
            editLitros.setText(String.valueOf(objetoAbastecimento.getLitrosAbastecidos()));


            for(int i = 0; i < postos.getAdapter().getCount(); i++){
                if (postos.getAdapter().getItem(i).equals(objetoAbastecimento.getPosto())){
                    postos.setSelection(i);
                    break;
                }
            }

            DateFormat formatador = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            String dataSelecionadaFormatada = formatador.format(objetoAbastecimento.getData().getTime());
            etData.setText(dataSelecionadaFormatada);
        }

    }

    public void salvar(View v){
        objetoAbastecimento.setKmAtual(Double.parseDouble(editKm.getText().toString()));
        objetoAbastecimento.setLitrosAbastecidos(Double.parseDouble(editLitros.getText().toString()));
        objetoAbastecimento.setPosto(postos.getSelectedItem().toString());

        if(idDoAbastecimento == null) {
            AbastecimentoDAO.obterInstancia().addNaLista(objetoAbastecimento);
            setResult(201);
        }else{
            int posicaoDoObjeto = AbastecimentoDAO.obterInstancia().atualizaNaLista(objetoAbastecimento);
            Intent intencaoDeFechamentoDaActivityFormulario = new Intent();
            intencaoDeFechamentoDaActivityFormulario.putExtra("posicaoDoObjetoEditado", posicaoDoObjeto);
            setResult(200, intencaoDeFechamentoDaActivityFormulario);
        }
        finish();
    }

    public void selecionarData(View v){
        Calendar dataPadrao = objetoAbastecimento.getData();;
        if(dataPadrao == null){
            dataPadrao = Calendar.getInstance();
        }

        DatePickerDialog dialogoParaPegarData = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar dataSelecionada = Calendar.getInstance();
                        dataSelecionada.set(year,month,dayOfMonth);
                        objetoAbastecimento.setData(dataSelecionada);

                        DateFormat formatador = android.text.format.DateFormat.getDateFormat( getApplicationContext() );
                        String dataSelecionadaFormatada = formatador.format( dataSelecionada.getTime() );
                        etData.setText( dataSelecionadaFormatada );
                    }
                },
                dataPadrao.get(Calendar.YEAR),
                dataPadrao.get(Calendar.MONTH),
                dataPadrao.get(Calendar.DAY_OF_MONTH)
        );
        dialogoParaPegarData.show();
    }

    public void excluir(){
        new AlertDialog.Builder(this)
                .setTitle("Você tem certeza?")
                .setMessage("Você quer mesmo excluir?")
                .setPositiveButton("Excluir", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int posicaoDoObjeto = AbastecimentoDAO.obterInstancia().excluiDaLista(objetoAbastecimento);
                        Intent intencaoDeFechamentoDaActivityFormulario = new Intent();
                        intencaoDeFechamentoDaActivityFormulario.putExtra("posicaoDoObjetoExcluido", posicaoDoObjeto);
                        setResult(202, intencaoDeFechamentoDaActivityFormulario);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    String caminhoDaFoto = null;

    private File criarArquivoParaSalvarFoto() throws IOException {
        String nomeFoto = UUID.randomUUID().toString();
        //getExternalStoragePublicDirectory()
        //    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
        File diretorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fotografia = File.createTempFile(nomeFoto,".jpg",diretorio);
        caminhoDaFoto = fotografia.getAbsolutePath();
        return fotografia;
    }


    public void abrirCamera(View v){
        Intent intecaoAbrirCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File arquivoDaFoto = null;
        try {
            arquivoDaFoto = criarArquivoParaSalvarFoto();
        } catch (IOException ex) {
            Toast.makeText(this, "Não foi possível criar arquivo para foto", Toast.LENGTH_LONG).show();
        }
        if (arquivoDaFoto != null) {
            Uri fotoURI = FileProvider.getUriForFile(this,
                    "com.example.a02_listas.fileprovider",
                    arquivoDaFoto);
            intecaoAbrirCamera.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
            startActivityForResult(intecaoAbrirCamera, 1);
        }

    }


// avisar o SO para atualizar o índice da galeria
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if(resultCode == RESULT_OK){
//                Bundle extras = data.getExtras();
//                Bitmap bitmapaFoto = (Bitmap) extras.get("data");

                objetoAbastecimento.setCaminhoDaFotografia( caminhoDaFoto );
                atualizaFotografiaNaTela();

            }
        }
    }

    private void atualizaFotografiaNaTela(){
        if(objetoAbastecimento.getCaminhoDaFotografia() != null){
            ImageView ivFotografia = findViewById(R.id.btnFoto);
            ivFotografia.setImageURI(Uri.parse(objetoAbastecimento.getCaminhoDaFotografia()));
        }
    }
}
