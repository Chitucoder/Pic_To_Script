package com.chitu.pictoscript;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.List;



    class DataFormat{
        int left,right,top,bottom;
        String data;
        public DataFormat(String d,int l,int r,int t,int b){
            left = l;
            right = r;
            top = t;
            bottom = b;
            data = d;
        }

        @NonNull
        public String toString() {
            return data+" "+left+" "+right+" "+top+" "+bottom;
        }

    }


    public class MainActivity3 extends AppCompatActivity{


        String []listDoc = {"Word File", "Excel File", "Text File", "Copy text"};
        String item=null,content,type;
        StringBuilder arrtxt;
        List<com.chitu.pictoscript.DataFormat> datatowrite = new ArrayList<>();
        int count=0;
        int CREATE_TXTFILE_REQUEST_CODE=1,REQUEST_CODE_SAVE_DOCX=2, REQUEST_CODE_SAVE_SHEET = 3;
        AutoCompleteTextView autoCompleteTextView;
        EditText F_name;
        ArrayAdapter<String> adapterItems;
        TextRecognizer recognizer;
    Uri resUri,fileUri;
    Bitmap bitmap;
    Intent intent;
    Task<Text> result;
    Button savebtn,sharebtn,downloadbtn;

//    FB
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String filetitletype,UID;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Toolbar actionBar = findViewById(R.id.actionbar);
        setSupportActionBar(actionBar);


        savebtn = findViewById(R.id.savebtn);
        sharebtn = findViewById(R.id.sharebtn);
        downloadbtn = findViewById(R.id.downloadbtn);
        F_name = (EditText) findViewById(R.id.F_Name);

//      FB
        user= FirebaseAuth.getInstance().getCurrentUser();
        UID= user.getUid();
        createTxtRecg();

        txtExtraction();

        selectDoc();


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item == null){
                    Toast.makeText(MainActivity3.this, "Select required file", Toast.LENGTH_SHORT).show();
                }else{
                    createDoc();
                }
            }
        });

        //        Firebase initialization
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("/Document/"+UID);


        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileUri!=null){
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    shareIntent.setType(type);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(Intent.createChooser(shareIntent, "Share File"));
                }
                else {
                    Toast.makeText(MainActivity3.this, "Save the file before sharing", Toast.LENGTH_SHORT).show();
                }
            }
        });

        downloadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent downloadintent = new Intent(MainActivity3.this, Downloads.class);
                downloadintent.putExtra("F_name", F_name.getText().toString());
                startActivity(downloadintent);
            }
        });


    }


    public void selectDoc(){
        autoCompleteTextView = findViewById(R.id.auto_complete_textview);
        adapterItems = new ArrayAdapter<String>(this,R.layout.drop_down_list,listDoc);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
            }
        });
    }

    public void createDoc(){
        if(item.equals(listDoc[0])){

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            intent.putExtra(Intent.EXTRA_TITLE, F_name.getText()+".docx");
            filetitletype=".docx";
            startActivityForResult(intent, REQUEST_CODE_SAVE_DOCX);

        } else if (item.equals(listDoc[1])) {


            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            intent.putExtra(Intent.EXTRA_TITLE,F_name.getText()+".xlsx");
            filetitletype=".xlsx";
            startActivityForResult(intent, REQUEST_CODE_SAVE_SHEET);


        } else if (item.equals(listDoc[2])) {

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE,F_name.getText()+".txt");
            filetitletype=".txt";
            startActivityForResult(intent, CREATE_TXTFILE_REQUEST_CODE);

        } else if (item.equals(listDoc[3])) {

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", content);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Text Copied Successfully", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Select Appropriate Format from list", Toast.LENGTH_SHORT).show();
        }
    }

    private void createTxtRecg(){
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        intent = getIntent();
        resUri = (Uri) intent.getParcelableExtra("img");

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void txtExtraction(){
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                String resultText = text.getText();
                for (Text.TextBlock block : text.getTextBlocks()) {
                    count++;
                    String blockText = block.getText();
                    Point[] blockCornerPoints = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    //Log.d("difficult",blockText);
                    datatowrite.add(new DataFormat(blockText,blockFrame.left,blockFrame.right,blockFrame.top,blockFrame.bottom));
                }
                arrangeText();

                content = result.getResult().getText().toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity3.this, "Errror occured", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void arrangeText(){


        Collections.sort(datatowrite, Comparator.comparingInt((DataFormat df)->df.top).thenComparing(df->df.left));
        arrtxt = new StringBuilder(datatowrite.get(0).data);
        int min = datatowrite.get(0).right;
        for(int i=1;i < datatowrite.size();i++){
            if(datatowrite.get(i).top>datatowrite.get(i-1).bottom){
                arrtxt.append("\n");
                if(datatowrite.get(i).left>min){
                    arrtxt.append("(empty)" + "\t");
                }
                arrtxt.append(datatowrite.get(i).data);
            }
            else if(datatowrite.get(i).left>datatowrite.get(i-1).right){
                arrtxt.append("\t"+datatowrite.get(i).data);
            }
        }
        Log.d("Text arranged", arrtxt.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == REQUEST_CODE_SAVE_DOCX && resultCode==RESULT_OK){
            Uri uri = data.getData();
            writeToDoc(uri);
            Toast.makeText(this, "file saved", Toast.LENGTH_SHORT).show();
            fileUri = uri;
            type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            processupload(fileUri);
        }
        else if(requestCode==REQUEST_CODE_SAVE_SHEET && resultCode==RESULT_OK){
            Uri uri = data.getData();
            putTable(uri);
            Toast.makeText(this, "File saved", Toast.LENGTH_SHORT).show();
            fileUri = uri;
            type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            processupload(fileUri);
        }
        else if(requestCode==CREATE_TXTFILE_REQUEST_CODE && resultCode == RESULT_OK){
                Uri uri = data.getData();
                ContentResolver resolver = getContentResolver();
                try(OutputStream out = resolver.openOutputStream(uri)){
                    out.write(content.getBytes());
                }catch (IOException e){
                    e.printStackTrace();
                }
                fileUri = uri;
                type = "text/plain";
                processupload(fileUri);
        }
    }

//    FB
        private void processupload(Uri fileUri) {

            final ProgressDialog pd= new ProgressDialog(this);
            pd.setTitle("File Uploading....!!!");
            pd.show();

            final StorageReference reference=storageReference.child("uploads/"+F_name.getText().toString()+"_"+System.currentTimeMillis()+filetitletype);
            reference.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    fileinfomodel obj=new fileinfomodel(F_name.getText().toString()+filetitletype, uri.toString());

                                    databaseReference.child(databaseReference.push().getKey()).setValue(obj);

                                    pd.dismiss();
                                    Toast.makeText(getApplicationContext(),"File Uploaded", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                            pd.setMessage("Uploading: "+(int)percent+"%");
                        }
                    });
        }

//        FB



        public void writeToDoc(Uri uri){
        try (OutputStream out = getContentResolver().openOutputStream(uri)){
            if(out!=null){
                XWPFDocument document = new XWPFDocument();
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();

                String[] lines = content.split("\n");
                for(int i=0;i<lines.length;i++){
                    run.setText(lines[i]);
                    run.addBreak();
                }
                document.write(out);

                out.close();
                document.close();

            }
        } catch (IOException e) {
            Toast.makeText(this, "Something went wrong.....", Toast.LENGTH_SHORT).show();
        }
    }

    public void putTable(Uri uri){
        String formattxt = arrtxt.toString();
        Log.d("difficult",formattxt);
        try (OutputStream out = getContentResolver().openOutputStream(uri)) {
            if (out != null) {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet();
                String[] rowdata = formattxt.split("\n");
                for(int i=0;i<rowdata.length;i++){
                    //Log.d("difficult",rowdata[i]);
                    String[] coldata = rowdata[i].split("\t");
                    Row row = sheet.createRow(i);
                    for(int j=0;j<coldata.length;j++){
                        //Log.d("difficult",coldata[j]);
                        Cell cell = row.createCell(j);
                        cell.setCellValue(coldata[j]);
                    }

                }
                workbook.write(out);

                out.close();
                workbook.close();
            }
        }catch (IOException e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }
}