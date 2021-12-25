package com.kc7.communicate.userlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kc7.communicate.R;
import com.kc7.communicate.communicationpack.AdapterCommunication;
import com.kc7.communicate.communicationpack.ModelCommunication;
import com.kc7.communicate.communicationpack.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterUserList extends FirestoreRecyclerAdapter<ModelUserList, AdapterUserList.UserList_AdapterViewHolder> {

    FirebaseFirestore db;

    FirebaseUser user;
    FirebaseAuth auth;


    public AdapterUserList(@NonNull FirestoreRecyclerOptions<ModelUserList> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull UserList_AdapterViewHolder holder, int position, @NonNull final ModelUserList model) {

        holder.name.setText(model.getname());
holder.time.setText(model.getlatestchat());

        final Context context = holder.name.getContext();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        Intent intent = new Intent(context, communication.class);
        intent.putExtra("uid", model.getuid());
        context.startActivity(intent);


            }
        });

    }

    @NonNull
    @Override
    public UserList_AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem2, parent, false);
        return new UserList_AdapterViewHolder(view);

    }

    class UserList_AdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView name, time;

        public UserList_AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
time = itemView.findViewById(R.id.time);

        }
    }

}
