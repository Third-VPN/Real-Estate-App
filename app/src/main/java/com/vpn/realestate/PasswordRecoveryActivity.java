package com.vpn.realestate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordRecoveryActivity extends AppCompatActivity {
    EditText etNewPassword, etNewConPassword;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //removing action bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_password_recovery);

        etNewPassword = findViewById(R.id.etNewPassword);
        etNewConPassword = findViewById(R.id.etNewConPassword);

        btnSubmit = findViewById(R.id.btnSubmit);

        //password recovery btn click event
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPassword, newConPassword;
                newPassword = etNewPassword.getText().toString();
                newConPassword = etNewConPassword.getText().toString();

                if (!newPassword.equals("") && !newConPassword.equals("")
                        && newPassword.equals(newConPassword)) {

                    Toast.makeText(PasswordRecoveryActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(PasswordRecoveryActivity.this, "Enter valid details", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}