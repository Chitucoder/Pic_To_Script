package com.chitu.pictoscript.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chitu.pictoscript.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<String> File_Names;


    public RecyclerViewAdapter(Context context, ArrayList<String> file_name) {

        this.context = context;
        this.File_Names = File_Names;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerViewAdapter.ViewHolder viewHolder, int i) {
        String fileName = File_Names.get(i);

        viewHolder.files.setText(fileName);

    }

    @Override
    public int getItemCount() {

        return File_Names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView files;
        public ImageView iconButtons;


         public ViewHolder(@androidx.annotation.NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            files = itemView.findViewById(R.id.file_name);
            iconButtons = itemView.findViewById(R.id.file_icon);

            iconButtons.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("ClickFromViewHolder", "Clicked");
        }
    }
}
