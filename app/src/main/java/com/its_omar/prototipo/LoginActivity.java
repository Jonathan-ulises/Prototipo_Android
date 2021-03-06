package com.its_omar.prototipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.controller.ConsultasComunes;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.ActivityLoginBinding;
import com.its_omar.prototipo.model.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding loginBinding;
    public static final String SESION_KEY = "login_ok";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        setContentView(loginBinding.getRoot());


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        asignarTipografia(face);


        loginBinding.btnLogin.setOnClickListener(view -> {
            if(validarCampos()){
                String nomU = loginBinding.etNomUsuario.getText().toString();
                String pass = loginBinding.etPassword.getText().toString();
                verificarDatosLogin(nomU, pass, this);
            }
        });

    }


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

    /**
     * Metodo que regresa al menu Home del dispositivo si el usuario presiona el boton back del
     * navegador del telefono
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Cerrar", Toast.LENGTH_SHORT).show();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(startMain);
    }

    /**
     * Verifica si el usuario existe en la bd
     * @param nombreUsuario Nombre de usuario del login
     * @param password password Nombre de usuario del login
     * @param ctx contexto de ejecucion de la activity
     */
    protected void verificarDatosLogin(String nombreUsuario, String password, Context ctx){
        WebService geoService = ServiceRetrofit.getInstance().getSevices();

        geoService.loginApp(nombreUsuario, password).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), "Error de respuesta", Toast.LENGTH_SHORT).show();
                } else {
                    if(response.body().isOk()){
                        SharedPreferencesApp sharedPreferencesApp = SharedPreferencesApp.getInstance(ctx);
                        sharedPreferencesApp.saveSharePreferencesLogin(nombreUsuario, response.body().getFk_empleado());

                        //Toast.makeText(ctx, "" + response.body().getFk_empleado(), Toast.LENGTH_SHORT).show();
                        ConsultasComunes.registrarAccionBitacora("Login", "Inicio Sesion", response.body().getFk_empleado());

                        Intent intent = new Intent(getApplication(), ClientesActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        new MaterialAlertDialogBuilder(ctx, R.style.ThemeOverlay_MaterialComponents_Dialog)
                                .setIcon(R.drawable.ic_error)
                                .setTitle(R.string.alert_login_error)
                                .setMessage(response.body().getMensaje())
                                .setPositiveButton(R.string.alert_dialog_confirm_login, (dialogInterface, i) -> limpiarCampos())
                                .show();

                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getApplication(), "Error -> " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Limpia los campos del login
     */
    private void limpiarCampos(){
        loginBinding.etNomUsuario.setText("");
        loginBinding.etPassword.setText("");
    }


    private void asignarTipografia(Typeface face){
        loginBinding.tvTitle.setTypeface(face);
    }
}