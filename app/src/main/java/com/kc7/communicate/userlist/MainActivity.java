package com.kc7.communicate.userlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kc7.communicate.Login_Signup;
import com.kc7.communicate.R;
import com.kc7.communicate.communicationpack.AdapterCommunication;
import com.kc7.communicate.communicationpack.ModelCommunication;

public class MainActivity extends AppCompatActivity {

    TextView name;
    ImageView logout;

    RecyclerView recyclerView;

    FirebaseAuth mauth;
    FirebaseUser mUser;
    FirebaseFirestore Db;

    AdapterUserList adapter;

    CollectionReference collref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
    logout = findViewById(R.id.logout);

        recyclerView = findViewById(R.id.recy2);
    mauth = FirebaseAuth.getInstance();
    mUser = mauth.getCurrentUser();
    Db = FirebaseFirestore.getInstance();

    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            mauth.signOut();
            startActivity(new Intent(MainActivity.this, Login_Signup.class));

            finish();



        }
    });

        collref = Db.collection("UserChat");

        DocumentReference docref = Db.collection("UserChat")
                .document(mauth.getCurrentUser().getUid());

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){

                    name.setText(documentSnapshot.getString("name"));


                }
                else{

                    Toast.makeText(MainActivity.this, "" +
                            "Unable to get name", Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, "" +
                        e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        setupRecyclerView();

    }

    private void setupRecyclerView() {
        Query query = collref.orderBy("uid", Query.Direction.DESCENDING)
                .whereNotEqualTo("uid", mUser.getUid());
        FirestoreRecyclerOptions<ModelUserList> options = new FirestoreRecyclerOptions.Builder<ModelUserList>()
                .setQuery(query, ModelUserList.class)
                .build();

        adapter = new AdapterUserList(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);



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