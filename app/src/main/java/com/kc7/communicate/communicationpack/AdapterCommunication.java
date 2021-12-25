package com.kc7.communicate.communicationpack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterCommunication extends FirestoreRecyclerAdapter<ModelCommunication, AdapterCommunication.Communication_AdapterViewHolder> {

    FirebaseFirestore db;

    FirebaseUser user;
    FirebaseAuth auth;


    public AdapterCommunication(@NonNull FirestoreRecyclerOptions<ModelCommunication> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull Communication_AdapterViewHolder holder, int position, @NonNull ModelCommunication model) {

holder.message.setText(model.getmessage());
holder.time.setText(model.gettime());

if(model.getsendorrecieve()){

    //show here that this message was sent by me

holder.layout.setBackgroundColor(holder.layout.getResources().getColor(R.color.colorPrimaryDark));
holder.message.setTextColor(holder.message.getResources().getColor(R.color.text));

    holder.time.setTextColor(holder.time.getResources().getColor(R.color.text));

}
else{

    //show here that this message was sent by the user

    holder.layout.setBackgroundColor(holder.layout.getResources().getColor(R.color.mymessage));
    holder.message.setTextColor(holder.message.getResources().getColor(R.color.colorPrimaryDark));

    holder.time.setTextColor(holder.time.getResources().getColor(R.color.colorPrimaryDark));



}

    }

    @NonNull
    @Override
    public Communication_AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleritem, parent, false);
        return new Communication_AdapterViewHolder(view);

    }

    class Communication_AdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView message, time;
ConstraintLayout layout;

            public Communication_AdapterViewHolder(@NonNull View itemView) {
                super(itemView);

                message = itemView.findViewById(R.id.message);
                time = itemView.findViewById(R.id.time);
layout = itemView.findViewById(R.id.constraintlayoout2);


            }
        }

    }
