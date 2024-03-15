package com.example.meupatrimnio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editNome;
    private EditText editLocal;
    private EditText editDescricao;
    private Button salvarButton;
    private Button verPatrimonioButton;

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private String imagePath;

    private PatrimonioDBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editNome);
        editLocal = findViewById(R.id.editLocal);
        editDescricao = findViewById(R.id.editDescricao);
        imageView = findViewById(R.id.ImagemItem);
        salvarButton = findViewById(R.id.Salvar);
        verPatrimonioButton = findViewById(R.id.verPatrimonio);

        dbHelper = new PatrimonioDBHelper(this);

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarPatrimonio();
            }
        });

        verPatrimonioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VerPatrimonioActivity.class);
                startActivity(intent);
            }
        });
    }
    public void selecionarFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            imagePath = uri.toString();
            imageView.setImageURI(uri);
        }
    }

    private void salvarPatrimonio() {
        String nome = editNome.getText().toString().trim();
        String local = editLocal.getText().toString().trim();
        String descricao = editDescricao.getText().toString().trim();

        if (nome.isEmpty() || local.isEmpty() || descricao.isEmpty() || imagePath == null) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("local", local);
        values.put("descricao", descricao);
        values.put("imagem", imagePath); //

        long newRowId = db.insert("patrimonio", null, values);


        if (newRowId != -1) {
            Toast.makeText(this, "Patrimônio salvo com sucesso", Toast.LENGTH_SHORT).show();
            editNome.setText("");
            editLocal.setText("");
            editDescricao.setText("");
            imageView.setImageResource(android.R.color.transparent); //
            imagePath = null; //
        } else {
            Toast.makeText(this, "Erro ao salvar patrimônio", Toast.LENGTH_SHORT).show();
        }

        db.close();

    }

}
