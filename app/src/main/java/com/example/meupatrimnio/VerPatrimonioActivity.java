package com.example.meupatrimnio;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerPatrimonioActivity extends AppCompatActivity {

    private PatrimonioDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_patrimonio);

        dbHelper = new PatrimonioDBHelper(this);

        ListView listView = findViewById(R.id.listViewPatrimonio);
        exibirPatrimonio(listView);
    }

    private void exibirPatrimonio(ListView listView) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "patrimonio",
                new String[]{"_id", "nome", "local", "descricao", "imagem"},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            Toast.makeText(this, "Erro ao obter dados do patrimônio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Não há itens no patrimônio", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.ver_patrimonio,
                cursor,
                new String[]{"nome", "local", "descricao", "imagem"},
                new int[]{R.id.NomeObjeto, R.id.LocalObjeto, R.id.DescObjeto, R.id.imageObjeto},
                0
        );

        listView.setAdapter(adapter);
    }
}
