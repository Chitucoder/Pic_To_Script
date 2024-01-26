package com.chitu.pictoscript.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chitu.pictoscript.R;
import com.chitu.pictoscript.model;
import com.chitu.pictoscript.view_document;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecyclerDownloadsAdapter extends FirebaseRecyclerAdapter<model,RecyclerDownloadsAdapter.myviewholder>
{

    public RecyclerDownloadsAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {

        holder.header.setText(model.getFilename());

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.img1.getContext(), view_document.class  );
                intent.putExtra("filename",model.getFilename());
                intent.putExtra("fileurl",model.getFileurl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.img1.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.new_downloads_list_item,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {

        ImageView img1;
        TextView header;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.imageView2);
            header=itemView.findViewById(R.id.name_view);
        }
    }

}
