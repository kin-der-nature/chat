package com.example.mychatktk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static int SIGN_IN_CODE =1;
    private RelativeLayout activity_main;
    private FirebaseListAdapter<Message> adapter;
    private FloatingActionButton sendbth;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE)
        {
            if(requestCode == RESULT_OK)
            {
                Snackbar.make(activity_main,"Вы авторизованы",Snackbar.LENGTH_SHORT).show();
                displayALlMessages();
            }
            else{
                Snackbar.make(activity_main,"Вы авторизованы",Snackbar.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main =findViewById(R.id.activity_main);
        sendbth =findViewById(R.id.bthSend);
        sendbth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField =findViewById(R.id.messageField);
                if (textField.getText().toString()== "")
                    return;
                FirebaseDatabase.getInstance().getReference().push().setValue(
                  new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),textField.getText().toString())

                );
                textField.setText("");
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser()== null)
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_CODE);
        else
            Snackbar.make(activity_main,"Вы авторизованы",Snackbar.LENGTH_SHORT).show();
        displayALlMessages();
    }

    private void displayALlMessages() {
        ListView listofMessages = findViewById(R.id.list_of_messages);
        adapter = new FirebaseListAdapter<Message>(this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView mess_user,mess_time,mess_text;
                mess_user =v.findViewById(R.id.message_user);
                mess_time =v.findViewById(R.id.message_time);
                mess_text =v.findViewById(R.id.message_text);
                mess_user.setText(model.getTextMessage());
                mess_time.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss",model.getMessageTime()));
            }
        };
        listofMessages.setAdapter(adapter);

    }


}
