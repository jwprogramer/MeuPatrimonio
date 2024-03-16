package com.example.meupatrimnio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> obg_id, nomeObj, localObj, imgObj;

    CustomAdapter(Activity activity, Context context, ArrayList<String> obg_id, ArrayList<String> nomeObj, ArrayList<String> localObj,
                  ArrayList<String> imgObj){
        this.activity = activity;
        this.context = context;
        this.obg_id = obg_id;
        this.nomeObj = nomeObj;
        this.localObj = localObj;
        this.imgObj = imgObj;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_card, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.id_objeto.setText(String.valueOf(obg_id.get(position)));
        holder.nome_objeto.setText(String.valueOf(nomeObj.get(position)));
        holder.local_objeto.setText(String.valueOf(localObj.get(position)));

        // Carregar a imagem usando Picasso
        Picasso.get().load(new File(imgObj.get(position))).into(holder.img_objeto);

        // RecyclerView onClickListener
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(obg_id.get(position)));
                intent.putExtra("nome", String.valueOf(nomeObj.get(position)));
                intent.putExtra("local", String.valueOf(localObj.get(position)));
                intent.putExtra("imagem", String.valueOf(imgObj.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return obg_id.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id_objeto, nome_objeto, local_objeto;
        ImageView img_objeto;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_objeto = itemView.findViewById(R.id.id_objeto);
            nome_objeto = itemView.findViewById(R.id.nomeObj);
            local_objeto = itemView.findViewById(R.id.localObj);
            img_objeto = itemView.findViewById(R.id.imageObj);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}

