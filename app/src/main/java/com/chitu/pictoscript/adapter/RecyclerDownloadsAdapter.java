package com.chitu.pictoscript.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chitu.pictoscript.FilesData;
import com.chitu.pictoscript.R;

import java.util.List;

public class RecyclerDownloadsAdapter extends RecyclerView.Adapter<RecyclerDownloadsAdapter.DownloadsViewHolder> {

    private List<FilesData> filesData;
    private Context context;

    public RecyclerDownloadsAdapter(List<FilesData> filesData, Context context) {
        this.filesData = filesData;
        this.context = context;
    }

    @NonNull
    @Override
    public DownloadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.new_downloads_list_item, parent, false);
        DownloadsViewHolder downloadsViewHolder = new DownloadsViewHolder(view);
        return downloadsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadsViewHolder holder, int position) {
        FilesData fData = filesData.get(position);
        holder.nameView.setText(fData.getName());
    }

    @Override
    public int getItemCount() {
        return filesData.size();
    }

    class DownloadsViewHolder extends RecyclerView.ViewHolder{

        private TextView nameView;
        public DownloadsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView=itemView.findViewById(R.id.name_view);
        }
    }
}
