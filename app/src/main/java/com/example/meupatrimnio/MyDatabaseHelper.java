package com.example.meupatrimnio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Patrimonio.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "inventario";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_LOCAL = "local";
    private static final String COLUMN_DESC = "descricao";
    private static final String COLUMN_IMAGE = "imagem";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT, " +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_LOCAL + " TEXT, " +
                COLUMN_DESC + " TEXT" +
               ")";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addItem(String nome, String local, String descricao, String imagem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_IMAGE, imagem);
        values.put(COLUMN_LOCAL, local);
        values.put(COLUMN_DESC, descricao);
        long result = db.insert(TABLE_NAME,null, values);
        if(result == -1){
            Toast.makeText(context, "Erro. Não foi possível realizar o cadastro!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateItem(String id, String nome, String local, String descricao, String imagem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_IMAGE, imagem);
        values.put(COLUMN_LOCAL, local);
        values.put(COLUMN_DESC, descricao);

        long result = db.update(TABLE_NAME, values, "_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Erro. Não foi possível atualizar o cadastro!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Cadastro atualizado!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Erro. Não foi possível excluir o cadastro!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Cadastro excluído!", Toast.LENGTH_SHORT).show();
        }
    }



}