package com.crzaor.zaply_shop.usescases.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.crzaor.zaply_shop.databinding.ActivityLoginBinding;
import com.crzaor.zaply_shop.model.User;
import com.crzaor.zaply_shop.usescases.register.RegisterActivity;
import com.crzaor.zaply_shop.util.SessionManager;
import com.crzaor.zaply_shop.usescases.main.MainActivity;
import com.crzaor.zaply_shop.provider.DBController;

import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    DBController dbController = new DBController();
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        binding.textViewNoAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        binding.btnConfirm.setOnClickListener(v ->{
            login();
        });

    }

    private void login(){
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            binding.editTextEmail.setError("Email necesario");
        }else if(!emailValidation(email)){
            binding.editTextEmail.setError("Email no válido");

        }else if(TextUtils.isEmpty(password)){
            binding.editTextPassword.setError("Contraseña necesaria");
        }else{
            User user = dbController.getUser(email);
            if(user != null && user.getPassword().equals(password)){
                sessionManager.createSession(email);
                startActivity(new Intent(this, MainActivity.class));
            }else{
                Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean emailValidation(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}