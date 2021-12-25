package com.kc7.communicate.communicationpack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kc7.communicate.R;
import com.kc7.communicate.userlist.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class communication extends AppCompatActivity {
TextView username;
EditText writemessage;
Button sendmessage;
ImageView home;

        private RecyclerView recyclerView;
        private AdapterCommunication adapterCommunication;
    RecyclerView recyclerView1;
        AdapterCommunication adapter;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
FirebaseAuth mauth;
FirebaseUser mUser;

        String uid2;
        CollectionReference notebookref;
    CollectionReference notebookref2;
DocumentReference userref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);

        Bundle extras = getIntent().getExtras();
        String extras1 = extras.getString("uid");


        recyclerView1 = findViewById(R.id.recy1);
username = findViewById(R.id.username);
writemessage = findViewById(R.id.writemessage);
sendmessage = findViewById(R.id.sendmessage);
home = findViewById(R.id.home);

        mauth = FirebaseAuth.getInstance();

        mUser = mauth.getCurrentUser();


        notebookref = db.collection("UserChat")
                .document(mUser.getUid())
                .collection(extras1);

        notebookref2 = db.collection("UserChat")
                .document(extras1)
                .collection(mUser.getUid());

        userref = db.collection("UserChat")
                .document(extras1);

        userref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    username.setText(documentSnapshot.getString("name"));


                }
                else{

                    Toast.makeText(communication.this, "" +
                            "Unable to get name", Toast.LENGTH_SHORT).show();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(communication.this, "" +
                        e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(communication.this, MainActivity.class));
                finish();


            }
        });

        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(writemessage.getText().toString().trim().isEmpty()){

                    Toast.makeText(communication.this, "" +
                            "Can't send empty message", Toast.LENGTH_SHORT).show();

                }
                else{

                    sendmessagenowhere(writemessage.getText().toString().trim());


                }
            }
        });

setupRecyclerView();

    }

    private void latestchat(String timedate){

        DocumentReference docref2 = db.collection("UserChat")
                .document(mUser.getUid());

        String latestchat = timedate;


        Map<String, Object> newmap = new HashMap<>();

        newmap.put("latestchat", latestchat);

        docref2.update(newmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(communication.this, "" +
                            "Updation done", Toast.LENGTH_SHORT).show();


                }
                else{

                    Toast.makeText(communication.this, "" +
                            "Not updated", Toast.LENGTH_SHORT).show();


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(communication.this, "" +
                        e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void putuser2(String message, String timedate){


        Map<String, Object> newMap = new HashMap<>();
        newMap.put("message", message);
        newMap.put("send", false);
        newMap.put("time", timedate);
        notebookref2.document(timedate).set(newMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(communication.this, "" +
                            "message sent", Toast.LENGTH_SHORT).show();


                }
                else{

                    Toast.makeText(communication.this, "" +
                            "unable to send message", Toast.LENGTH_SHORT).show();


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(communication.this, "" +
                        e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void sendmessagenowhere(String communicationmessage){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd_HH mm ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String year = currentDateandTime.substring(0, 4);
        String month = currentDateandTime.substring(5, 7);
        String date = currentDateandTime.substring(8, 10);
        String hour = currentDateandTime.substring(11, 13);
        String minute = currentDateandTime.substring(14, 16);
String second = currentDateandTime.substring(17);

        String lateschat = date + "-" + month + "-" + year + "    " + hour + ":" + minute + ":"
                + second;

        latestchat(lateschat);

        putuser2(communicationmessage, lateschat);


Map<String, Object> newMap = new HashMap<>();
newMap.put("message", communicationmessage);
newMap.put("send", true);
newMap.put("time", lateschat);
notebookref.document(lateschat).set(newMap).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {

        if(task.isSuccessful()){

            Toast.makeText(communication.this, "" +
                    "message sent", Toast.LENGTH_SHORT).show();


        }
        else{

            Toast.makeText(communication.this, "" +
                    "unable to send message", Toast.LENGTH_SHORT).show();


        }

    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {

        Toast.makeText(communication.this, "" +
                e.getMessage(), Toast.LENGTH_SHORT).show();

    }
});

    }

        private void setupRecyclerView() {
            Query query = notebookref.orderBy("time", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<ModelCommunication> options = new FirestoreRecyclerOptions.Builder<ModelCommunication>()
                    .setQuery(query, ModelCommunication.class)
                    .build();

            adapter = new AdapterCommunication(options);

            recyclerView1.setHasFixedSize(true);
            recyclerView1.setLayoutManager(new LinearLayoutManager(recyclerView1.getContext()));
            recyclerView1.setAdapter(adapter);
            Toast.makeText(recyclerView1.getContext(), "recyclerviewsetup", Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onBackPressed() {
// super.onBackPressed();
// Not calling **super**, disables back button in current screen.
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}