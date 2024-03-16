package com.example.meupatrimnio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AddActivity extends AppCompatActivity {

    EditText nome, local, descricao;
    Button salvar, img_button;

    ImageView image;

    String imagePath;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String DIRECTORY_NAME = "AppImagens";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);

        image = findViewById(R.id.imgItem);
        nome = findViewById(R.id.nomeObjeto);
        local = findViewById(R.id.localObjeto);
        descricao = findViewById(R.id.descObjeto);
        salvar = findViewById(R.id.salvarObjeto);
        img_button = findViewById(R.id.adicionarImage);

        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarFoto();
            }
        });
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addItem(nome.getText().toString().trim(),
                        local.getText().toString().trim(),
                        descricao.getText().toString().trim(),
                        imagePath);
            }
        });
    }

    private void checkPermissionAndSaveImage() {
        // Verifica se a permissão de escrita externa foi concedida
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            saveImageToDirectory(Uri.parse(imagePath));
        }
    }
    private void saveImageToDirectory(Uri imageUri) {
        // Verifique se a URI é nula ou vazia
        if (imageUri == null) {
            Toast.makeText(this, "URI da imagem inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obter o caminho real do arquivo a partir da URI
        String realPath = getRealPathFromUri(imageUri);
        if (realPath == null) {
            Toast.makeText(this, "Falha ao obter o caminho real da imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        // Copiar o arquivo para o diretório de destino
        File sourceFile = new File(realPath);
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), DIRECTORY_NAME);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File destFile = new File(directory, System.currentTimeMillis() + ".jpg");
        try {
            FileInputStream in = new FileInputStream(sourceFile);
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            Toast.makeText(this, "Imagem salva com sucesso", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String realPath = cursor.getString(columnIndex);
            cursor.close();
            return realPath;
        }
        return null;
    }


    public void selecionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            imagePath = uri.toString();
            image.setImageURI(uri);
            checkPermissionAndSaveImage(); // Chamada da função aqui
        }
    }
}

