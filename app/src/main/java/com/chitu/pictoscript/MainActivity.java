package com.chitu.pictoscript;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button next, downloadbtn2;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    String[] seltype = {"Image","PDF"};
    String inptype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar actionBar = findViewById(R.id.actionbar);
        setSupportActionBar(actionBar);

        next = findViewById(R.id.button);
        downloadbtn2=findViewById(R.id.downloadbtn2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inptype == seltype[0]){
                    Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                    startActivity(intent);
                }else if(inptype == seltype[1]){
                    Toast.makeText(MainActivity.this, "PDF is under progress", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Select the input type first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        downloadbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, Downloads.class);
                startActivity(intent2);
            }
        });

        autoCompleteTextView = findViewById(R.id.auto_complete_textview);
        adapterItems = new ArrayAdapter<String>(this,R.layout.drop_down_list,seltype);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               inptype  = adapterView.getItemAtPosition(i).toString();
            }
        });
    }
}