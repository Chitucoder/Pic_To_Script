package com.chitu.pictoscript;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.chitu.pictoscript.adapter.RecyclerDownloadsAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
;import java.security.cert.PKIXRevocationChecker;

public class Downloads extends AppCompatActivity {


    private RecyclerView recyclerView;
    private String fileName,UID;
    RecyclerDownloadsAdapter adapter;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads);

        Toolbar actionBar = findViewById(R.id.actionbar);
        setSupportActionBar(actionBar);


//      FB
        user= FirebaseAuth.getInstance().getCurrentUser();
        UID= user.getUid();
        recyclerView = findViewById(R.id.recycler_view_downloads_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        FirebaseRecyclerOptions<model> options=
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("/Document/"+UID), model.class)
                        .build();
        adapter = new RecyclerDownloadsAdapter(options);
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

}