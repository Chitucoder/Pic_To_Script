package com.chitu.pictoscript;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.chitu.pictoscript.adapter.RecyclerDownloadsAdapter;

import java.util.ArrayList;
import java.util.List;

public class Downloads extends AppCompatActivity {

    private List<FilesData> filesData=new ArrayList<>();
    private RecyclerView recyclerView;
    private String fileName;
    private boolean recordExists;
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);


        recyclerView = findViewById(R.id.recycler_view_downloads_list);

        String F_name = getIntent().getStringExtra("F_name");
        filesData.add(new FilesData(F_name));



        RecyclerDownloadsAdapter recyclerDownloadsAdapter = new RecyclerDownloadsAdapter(filesData, this);
        recyclerView.setAdapter(recyclerDownloadsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}