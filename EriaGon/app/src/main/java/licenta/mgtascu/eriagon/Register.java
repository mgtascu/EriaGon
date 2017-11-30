package licenta.mgtascu.eriagon;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Register extends AppCompatActivity {

    EditText etUser, etPass1, etRePass1, etEmail;
    Button btnReg, btnclose;
    RadioButton rMal, rFem;
    RadioGroup rgGender;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        etUser = (EditText) findViewById(R.id.etUserName);
        etPass1 = (EditText) findViewById(R.id.etPass1);
        etRePass1 = (EditText) findViewById(R.id.etRePass1);
        etEmail = (EditText) findViewById(R.id.etEmail1);

        btnReg = (Button) findViewById(R.id.btnRegDb);
        btnclose = (Button) findViewById(R.id.btnClose);

        rMal = (RadioButton) findViewById(R.id.rMal);
        rFem = (RadioButton) findViewById(R.id.rFem);

        rgGender = (RadioGroup) findViewById(R.id.rgGender);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), LogIn.class));
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {

        String email = etEmail.getText().toString();
        String pass = etPass1.getText().toString();
        String rePass = etRePass1.getText().toString();
        String name = etUser.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter an user name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(rePass)) {
            Toast.makeText(this, "Please re-enter the same password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(rePass)) {
            Toast.makeText(this, "Passwords must match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(rMal.isChecked() || rFem.isChecked())) {
            Toast.makeText(this, "Please select a gender...", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "Unable to register, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
