package com.example.meupatrimnio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class UpdateActivity extends AppCompatActivity {

    EditText nomeObjeto, localObjeto, descObjeto;

    ImageView imageObjeto;

    String imagePath;
    private static final int PICK_IMAGE_REQUEST = 1;

    Button update_button, delete_button;

    String id, nome, local, descricao, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        nomeObjeto = findViewById(R.id.nomeObjeto2);
        localObjeto = findViewById(R.id.localObjeto2);
        descObjeto = findViewById(R.id.descObjeto2);
        imageObjeto = findViewById(R.id.imgItem2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);

        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(nome);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                nome = nomeObjeto.getText().toString().trim();
                local = localObjeto.getText().toString().trim();
                descricao = descObjeto.getText().toString().trim();
                image = imagePath;
                myDB.updateItem(id, nome, local, descricao, image);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }

    public void selecionarFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            imagePath = uri.toString();
            imageObjeto.setImageURI(uri);
        }
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("nome") &&
                getIntent().hasExtra("local") && getIntent().hasExtra("descricao")) {
            // Getting Data from Intent
            id = getIntent().getStringExtra("id");
            nome = getIntent().getStringExtra("nome");
            local = getIntent().getStringExtra("local");
            descricao = getIntent().getStringExtra("descricao");

            if(getIntent().hasExtra("imagem")) {
                image = getIntent().getStringExtra("imagem");
                Picasso.get().load(image).into(imageObjeto);
            }

            // Setting Intent Data
            nomeObjeto.setText(nome);
            localObjeto.setText(local);
            descObjeto.setText(descricao);
            // Image setting might be needed here
        } else {
            Toast.makeText(this, "Vazio.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir " + nome + " ?");
        builder.setMessage("Deseja excluir o item " + nome + " ?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
