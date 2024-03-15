package com.example.meupatrimnio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PatrimonioDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "patrimonio.db";
    private static final int DATABASE_VERSION = 1;

    public PatrimonioDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PATRIMONIO_TABLE =
                "CREATE TABLE patrimonio (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nome TEXT, " +
                        "local TEXT, " +
                        "descricao TEXT, " +
                        "imagem TEXT)";

        db.execSQL(SQL_CREATE_PATRIMONIO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Backup dos dados existentes
            db.execSQL("CREATE TABLE patrimonio_backup AS SELECT * FROM patrimonio");

            // Criação da nova tabela com a estrutura atualizada
            String SQL_CREATE_NEW_PATRIMONIO_TABLE =
                    "CREATE TABLE patrimonio_new (" +
                            "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "nome TEXT, " +
                            "local TEXT, " +
                            "descricao TEXT, " +
                            "imagem TEXT, " +
                            "nova_coluna TEXT)"; // Adicione novas colunas conforme necessário

            db.execSQL(SQL_CREATE_NEW_PATRIMONIO_TABLE);

            // Copiar dados da tabela antiga para a nova tabela
            db.execSQL("INSERT INTO patrimonio_new SELECT _id, nome, local, descricao, imagem, null FROM patrimonio_backup");

            // Excluir tabela antiga
            db.execSQL("DROP TABLE patrimonio");

            // Renomear a nova tabela para o nome original
            db.execSQL("ALTER TABLE patrimonio_new RENAME TO patrimonio");

            // Atualize a versão do banco de dados
            db.setVersion(2);
        }
    }

}
