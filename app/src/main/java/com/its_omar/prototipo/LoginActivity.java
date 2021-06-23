package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Toast;

import com.its_omar.prototipo.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setContentView(loginBinding.getRoot());

        asignarTipografia(face);

        loginBinding.btnLogin.setOnClickListener(view -> {
            //Toast.makeText(this, "MSJ -> " + validarCampos(), Toast.LENGTH_SHORT).show();
            if(validarCampos()){
                Intent intent = new Intent(this, ClientesActivity.class);
                startActivity(intent);
            }
        });
    }

//https://itsmarts.fortidns.com:4600
    /**
     * Comprueba que los TextInputEditText no esten vacios
     * @return boolean si esta validado
     */
    private boolean validarCampos(){

        boolean isValidated = false;

        String nombre_usuario = loginBinding.etNomUsuario.getText().toString();
        String password_usuario = loginBinding.etPassword.getText().toString();

        if(nombre_usuario.isEmpty() && password_usuario.isEmpty()){
            loginBinding.tlNombreUsuario.setError(getString(R.string.text_input_empty_error_login));
            loginBinding.tlNombreUsuario.requestFocus();
            loginBinding.tlContraseA.setError(getString(R.string.text_input_empty_error_login));
            loginBinding.tlContraseA.requestFocus();

        } else if (nombre_usuario.isEmpty() || password_usuario.isEmpty()) {
            if (nombre_usuario.isEmpty()){
                loginBinding.tlNombreUsuario.setError(getString(R.string.text_input_empty_error_login));
                loginBinding.tlNombreUsuario.requestFocus();
                loginBinding.tlContraseA.setError(null);
            } else {
                loginBinding.tlContraseA.setError(getString(R.string.text_input_empty_error_login));
                loginBinding.tlContraseA.requestFocus();
                loginBinding.tlNombreUsuario.setError(null);
            }
        } else {
            loginBinding.tlNombreUsuario.setError(null);
            loginBinding.tlContraseA.setError(null);

            isValidated = true;
        }

        return isValidated;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Cerrar", Toast.LENGTH_SHORT).show();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(startMain);
    }

    private void asignarTipografia(Typeface face){
        loginBinding.tvTitle.setTypeface(face);
    }
}