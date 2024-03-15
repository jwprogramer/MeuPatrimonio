package com.example.meupatrimnio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ObjetoAdaptador extends ArrayAdapter<Objeto> {

    public ObjetoAdaptador(Context context, ArrayList<Objeto> objetos) {
        super(context, 0, objetos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Objeto objeto = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_ver_patrimonio, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageObjeto);
        TextView nomeTextView = convertView.findViewById(R.id.NomeObjeto);
        TextView localTextView = convertView.findViewById(R.id.LocalObjeto);
        TextView descricaoTextView = convertView.findViewById(R.id.DescObjeto);
        Button editarButton = convertView.findViewById(R.id.editButton);
        Button excluirButton = convertView.findViewById(R.id.deleteButton);

        imageView.setImageResource(objeto.getImagem());
        nomeTextView.setText(objeto.getNome());
        localTextView.setText(objeto.getLocal());
        descricaoTextView.setText(objeto.getDescricao());


        editarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(getContext(), "Editar " + objeto.getNome(), Toast.LENGTH_SHORT).show();
            }
        });


        excluirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(getContext(), "Excluir " + objeto.getNome(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
