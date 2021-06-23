package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.databinding.ActivityLoginBinding;
import com.its_omar.prototipo.model.Veri_Con;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                /*Intent intent = new Intent(this, ClientesActivity.class);
                startActivity(intent);*/
                String nomU = loginBinding.etNomUsuario.getText().toString();
                String pass = loginBinding.etPassword.getText().toString();
                verificarDatosLogin(nomU, pass, this);
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

    private void verificarDatosLogin(String nombreUsuario, String password, Context ctx){
        WebService geoService = ServiceRetrofit.getInstance().getSevices();

        geoService.loginApp(nombreUsuario, password).enqueue(new Callback<Veri_Con>() {
            @Override
            public void onResponse(Call<Veri_Con> call, Response<Veri_Con> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), "Error de respuesta", Toast.LENGTH_SHORT).show();
                } else {
                    if(response.body().isOk()){
                        Intent intent = new Intent(getApplication(), ClientesActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        /*AlertDialog.Builder buider = new AlertDialog.Builder(ctx);
                        buider.setTitle(response.body().getMensaje());
                        buider.setPositiveButton(R.string.alert_dialog_confirm_login, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        limpiarCampos();
                                    }
                                });

                        AlertDialog dialog = buider.create();
                        dialog.show();*/
                        new MaterialAlertDialogBuilder(ctx, R.style.ThemeOverlay_MaterialComponents_Dialog)
                                .setTitle(response.body().getMensaje())
                                .setPositiveButton(R.string.alert_dialog_confirm_login, (dialogInterface, i) -> limpiarCampos())
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Veri_Con> call, Throwable t) {
                Toast.makeText(getApplication(), "Error -> " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void limpiarCampos(){
        loginBinding.etNomUsuario.setText("");
        loginBinding.etPassword.setText("");
    }

    private void asignarTipografia(Typeface face){
        loginBinding.tvTitle.setTypeface(face);
    }
}