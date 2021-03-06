package com.its_omar.prototipo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.its_omar.prototipo.ClientesActivity;
import com.its_omar.prototipo.FirmaActivity;
import com.its_omar.prototipo.R;
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.controller.CapturarUbicacion;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.FragmentVerificacionDatosBinding;
import com.its_omar.prototipo.model.Cliente_por_visitar;
import com.its_omar.prototipo.model.Constantes;
import com.its_omar.prototipo.model.Result;
import com.its_omar.prototipo.model.bodyJSONCliente.BodyJSONCliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.its_omar.prototipo.controller.ConsultasComunes.registrarAccionBitacora;
import static com.its_omar.prototipo.model.Constantes.DATO_IMCOMPLETO;
import static com.its_omar.prototipo.model.Constantes.ESTATUS_VERIFICACION;
import static com.its_omar.prototipo.model.Constantes.ESTATUS_VISITA_ABANDONADA;
import static com.its_omar.prototipo.model.Constantes.FLAG_FIRMA_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.FLAG_FOTO_CASA;
import static com.its_omar.prototipo.model.Constantes.FLAG_LAT_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.FLAG_LONG_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.FLAG_VALIDACION;
import static com.its_omar.prototipo.model.Constantes.FOTO_CASA_KEY;
import static com.its_omar.prototipo.model.Constantes.LATITUDE_UBI_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.LONGITUD_UBI_CLIENTE;
import static com.its_omar.prototipo.model.Constantes.TAG_INFO_DATOS_VERIFICACION;
import static com.its_omar.prototipo.model.Constantes.checkIfLocationOpened;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerificacionDatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerificacionDatosFragment extends Fragment {

    private FragmentVerificacionDatosBinding datosBinding;
    private Bitmap fotoCasa;
    private Cliente_por_visitar cl;
    private SharedPreferences.Editor editor;
    private SharedPreferencesApp sp;
    private Map<String, Integer> datosVerificados;
    private List<String> datosFaltantes; //Srive para mostrar en el alert lo que falta

    //POCISIONAMIENTO
    private CapturarUbicacion capturarUbicacion;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ID_CLIENTE = "idCliente";

    // TODO: Rename and change types of parameters
    private int mIdCliente;

    public VerificacionDatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idC Parameter 1.
     * @return A new instance of fragment VerificacionDatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerificacionDatosFragment newInstance(int idC) {
        VerificacionDatosFragment fragment = new VerificacionDatosFragment();
        Bundle args = new Bundle();
        args.putInt(ID_CLIENTE, idC);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdCliente = getArguments().getInt(ID_CLIENTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        datosBinding = FragmentVerificacionDatosBinding.inflate(inflater, container, false);

        capturarUbicacion = new CapturarUbicacion(getActivity());

        cl = new Cliente_por_visitar(); //Objeto a enviar al servicio

        datosFaltantes = new ArrayList<>();

        initEditorVerificacion(); //Inicializa el editor

        sp = SharedPreferencesApp.getInstance(getContext());



        datosBinding.rbSVEncontrado.setChecked(true);

        //EVENTO de abandonada la visita
        datosBinding.rgEstatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (datosBinding.rbSVAbandonada.isChecked()){
                datosBinding.btnCapturarDatos.setText(R.string.razon_status);
            } else {
                datosBinding.btnCapturarDatos.setText(R.string.btn_capturar);
            }
        });

        //EVENTO OBTENER UBICACION TODO: IMPLEMENTAR HERE MAPS
        datosBinding.btnObtenerUbicacion.setOnClickListener(view -> {

            //Comprueba que se tenga el GPS encendido
            if(checkIfLocationOpened(requireContext())){
                datosBinding.bacgrPr.setVisibility(View.VISIBLE);
                requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                //datosBinding.prBar.setVisibility(View.VISIBLE);  //Progress bar por defecto
                datosBinding.prBar.playAnimation();
                datosBinding.prBar.setVisibility(View.VISIBLE);

                capturarUbicacion.starLocating(new CapturarUbicacion.PlatformLocationListener() {
                    @Override
                    public void onLocationUpdate(Location location, int status) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        Log.i("location" , "la: " + lat + "||--||" + " lon: " + lon);

                        datosBinding.tvDireccionCap.setText("latitude: " + lat + " - " + " longitud: " + lon);

                        editor.putString(LONGITUD_UBI_CLIENTE, Double.toString(lon));
                        editor.putString(LATITUDE_UBI_CLIENTE, Double.toString(lat));
                        editor.apply();

                        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        datosBinding.bacgrPr.setVisibility(View.GONE);
                        //datosBinding.prBar.setVisibility(View.GONE); //Progress bar por defecto
                        datosBinding.prBar.setVisibility(View.GONE);
                        datosBinding.prBar.pauseAnimation();
                    }
                });

            } else {

                new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_MaterialComponents_Dialog)
                        .setTitle(R.string.alert_mapa_title_error)
                        .setIcon(R.drawable.ic_warning)
                        .setMessage(R.string.alert_mapa_message_error)
                        .show();
            }
        });

        //EVENTO CAPTURAR FIRMA
        datosBinding.btnCapFirma.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), FirmaActivity.class);
            startActivity(intent);
        });


        //EVENTO CAPTURAR FOTO CASA
        datosBinding.btnCapCasa.setOnClickListener(view -> abrirCamera());


        //EVENTO VALIDACION DEL CLIENTE
        datosBinding.rgValidacion.setOnCheckedChangeListener((radioGroup, i) -> {
            if (datosBinding.rbValidado.isChecked()){
                editor.putInt(ESTATUS_VERIFICACION, 7);
                editor.apply();
            }else if (datosBinding.rbNoValidado.isChecked()) {
                editor.putInt(ESTATUS_VERIFICACION, 8);
                editor.apply();
            } else {
                editor.putInt(ESTATUS_VERIFICACION, 2);
                editor.apply();
            }
        });


        //EVENTO CAPTURAR DATOS
        datosBinding.btnCapturarDatos.setOnClickListener(v -> {
            if(datosBinding.rbSVAbandonada.isChecked()){
                sp.borrarPreferencesDatos(editor);
                RazonFragment fragment = RazonFragment.newInstance(mIdCliente, sp.getUsuarioLogeado().getId_empleado(), ESTATUS_VISITA_ABANDONADA);
                getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().beginTransaction().add(R.id.container_verificacion, fragment).commit();

            } else {
                boolean res = validarCampos();


                if(res){
                    new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_MaterialComponents_Dialog)
                            .setTitle(R.string.alert_datos_title)
                            .setMessage(R.string.alert_datos_message)
                            .setPositiveButton(R.string.alert_datos_positive_btn, (dialogInterface, i) -> {
                                //Toast.makeText(getContext(), "REGISTRADO", Toast.LENGTH_SHORT).show();
                                subirDatos(mIdCliente);
                            })
                            .setNegativeButton(R.string.alert_datos_negative_btn, (dialogInterface, i) -> {
                                Toast.makeText(getContext(), "SE LIMPIARAN LOS DATOS", Toast.LENGTH_SHORT).show();
                            })
                            .show();
                    Log.i(TAG_INFO_DATOS_VERIFICACION, "Todo bien");
                } else {

                    //Genera la lista de acciones faltantes para mostrarla en un Dialog
                    StringBuilder faltantes = new StringBuilder();

                    for (String dt: datosFaltantes) {
                        faltantes.append(dt).append("\n");
                    }

                    new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_MaterialComponents_Dialog)
                            .setTitle(R.string.alert_datos_faltantes_title)
                            .setMessage(faltantes.toString())
                            .setPositiveButton(R.string.alert_datos_faltantes_positive_btn, null)
                            .show();


                    Toast.makeText(getContext(), "Faltan datos", Toast.LENGTH_SHORT).show();

                    Log.i(TAG_INFO_DATOS_VERIFICACION, "Algo salio mal");
                }
            }


        });

        // Inflate the layout for this fragment
        return datosBinding.getRoot();
    }

    /**
     * Abre la camara del telefono
     */
    private void abrirCamera(){
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camaraIntent, Constantes.REQUEST_CODE_CAMERA_INTENT);
    }


    /**
     * Evento de escucha de la respuesta de la activity de la camara
     * @param requestCode Codigo de Intent a que pertenece el resultado
     * @param resultCode Codigo de resultado
     * @param data Datos resultantes
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && requestCode == Constantes.REQUEST_CODE_CAMERA_INTENT) {
            if(data != null) {

                fotoCasa = data.getExtras().getParcelable("data");

                String b64 = Constantes.bitmapToBase64(fotoCasa);

                cl.setCasa(b64);

                if(editor != null){
                    editor.putString(FOTO_CASA_KEY, b64);
                    editor.apply();
                }

                Snackbar.make(datosBinding.getRoot(), "Foto capturada", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setAction("REINTENTAR", view -> abrirCamera()) .show();

            } else {
                Snackbar.make(datosBinding.getRoot(), "Error de captura", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setAction("REINTENTAR", view -> abrirCamera()) .show();
            }
        }
    }

    /**
     * Inicializa el Editor del SharedPreferences para escribir datos
     */
    private void initEditorVerificacion(){
        SharedPreferencesApp sp = SharedPreferencesApp.getInstance(getActivity());
        editor = sp.getEditorForDatosVerificacion();
    }


    /**
     * Verifica que todas las acciones disponibles del formulario se han ejecutado, se??ala
     * los apartados restantes por llenar
     * @return esta completo el formulario {@link boolean}
     */
    private boolean validarCampos(){
        datosVerificados = sp.getFlagValidacion();

        int flagDatos = 1; //Contador de datos correctos
        boolean isComplete = false;

        try {
            //Foto Casa
            if(datosVerificados.get(FLAG_FOTO_CASA) == DATO_IMCOMPLETO){
                datosBinding.tpCasa.setVisibility(View.VISIBLE);
                datosFaltantes.add("Capturar Casa");
            } else {
                datosBinding.tpCasa.setVisibility(View.GONE);
                flagDatos++;
            }

            //Firma cliente
            if (datosVerificados.get(FLAG_FIRMA_CLIENTE) == DATO_IMCOMPLETO){
                datosBinding.tpFirma.setVisibility(View.VISIBLE);
                datosFaltantes.add("Capturar Firma");
            } else {
                datosBinding.tpFirma.setVisibility(View.GONE);
                flagDatos++;
            }

            //UBICACION
            double lon = datosVerificados.get(FLAG_LONG_CLIENTE);
            double lat = datosVerificados.get(FLAG_LAT_CLIENTE);

            if(lon == 0d && lat == 0d){
                datosBinding.tpUbi.setVisibility(View.VISIBLE);
                datosFaltantes.add("Obtener Ubicacion");
            } else {
                datosBinding.tpUbi.setVisibility(View.GONE);
                flagDatos++;
            }

            //Validacion
            if(datosVerificados.get(FLAG_VALIDACION) != 2) {
                datosBinding.tpVeri.setVisibility(View.GONE);
                flagDatos++;
            } else {
                datosBinding.tpVeri.setVisibility(View.VISIBLE);
                datosFaltantes.add("Validacion de Identidad");
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        if (flagDatos == 5) isComplete = true;

        return isComplete;
    }

    /**
     * Sube los datos de la verificacion al servicio web de asignarvisita
     * @param iDCl
     */
    private void subirDatos(int iDCl){
        int idE = sp.getUsuarioLogeado().getId_empleado();

        BodyJSONCliente body = sp.generateBodyRequestVisita(iDCl);

        WebService api = ServiceRetrofit.getInstance().getSevices();

        api.asignarVisita(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.code() == 200) {
                    if(response.body().isOk()) {
                        registrarAccionBitacora("Verificacion", "Asignacion", idE);

                        Snackbar.make(datosBinding.getRoot(), "Datos Capturados", Snackbar.LENGTH_SHORT)
                                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();

                        Intent intent = new Intent(getActivity(), ClientesActivity.class);
                        sp.borrarPreferencesDatos(editor);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        sp.borrarPreferencesDatos(editor);
        //Log.i("final", "xd");
    }

}