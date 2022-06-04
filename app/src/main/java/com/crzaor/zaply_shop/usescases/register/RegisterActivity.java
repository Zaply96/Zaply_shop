package com.crzaor.zaply_shop.usescases.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.crzaor.zaply_shop.usescases.login.LoginActivity;
import com.crzaor.zaply_shop.util.SessionManager;
import com.crzaor.zaply_shop.databinding.ActivityRegisterBinding;
import com.crzaor.zaply_shop.usescases.main.MainActivity;
import com.crzaor.zaply_shop.provider.DBController;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    DBController dbController = new DBController();
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);

        binding.imageButton.setOnClickListener(v ->{
            finish();
        });

        binding.btnConfirm.setOnClickListener(view -> {
            register();
        });
    }

    private void register(){
        String name = binding.editTextName.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPwd.getText().toString().trim();
        String confirmPassword = binding.editTextConfirmPwd.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            binding.editTextName.setError("Nombre necesario");
        }else if(TextUtils.isEmpty(email)) {
            binding.editTextEmail.setError("Email necesario");
        }else if(!emailValidation(email)){
            binding.editTextEmail.setError("Email no válido");
        }else if(TextUtils.isEmpty(password)){
            binding.editTextPwd.setError("Contraseña necesaria");
        }else if(TextUtils.isEmpty(confirmPassword)){
            binding.editTextConfirmPwd.setError("Repite la contraseña");
        }else if(!password.equals(confirmPassword)){
            binding.editTextPwd.setError("La contraseña no coincide");
            binding.editTextConfirmPwd.setError("La contraseña no coincide");
        }else{
            if(dbController.insertUser(name,email,password)){
                sessionManager.createSession(email);
                Toast.makeText(this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "El email ya está asociado a una cuenta", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean emailValidation(String emailAddress){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}