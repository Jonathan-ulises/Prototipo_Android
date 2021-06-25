package com.its_omar.prototipo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.its_omar.prototipo.FirmaActivity;
import com.its_omar.prototipo.R;
import com.its_omar.prototipo.databinding.FragmentVerificacionDatosBinding;
import com.its_omar.prototipo.model.Cliente_por_visitar;
import com.its_omar.prototipo.model.Constantes;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerificacionDatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerificacionDatosFragment extends Fragment {

    private FragmentVerificacionDatosBinding datosBinding;
    private Bitmap fotoCasa;
    private Cliente_por_visitar cl;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VerificacionDatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerificacionDatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerificacionDatosFragment newInstance(String param1, String param2) {
        VerificacionDatosFragment fragment = new VerificacionDatosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        datosBinding = FragmentVerificacionDatosBinding.inflate(inflater, container, false);

        cl = new Cliente_por_visitar();

        datosBinding.rbSVEncontrado.setChecked(true);

        datosBinding.rgEstatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (datosBinding.rbSVAbandonada.isChecked()){
                datosBinding.btnCapturarDatos.setText(R.string.razon_status);
            } else {
                datosBinding.btnCapturarDatos.setText(R.string.btn_capturar);
            }
        });


        datosBinding.btnCapFirma.setOnClickListener(view -> {
            /*FirmaUsuFragment fragment = new FirmaUsuFragment();
            getFragmentManager().beginTransaction().remove(this).commit();
            getFragmentManager().beginTransaction().add(R.id.container_verificacion, fragment).commit();*/
            Intent intent = new Intent(getContext(), FirmaActivity.class);
            startActivity(intent);
        });


        datosBinding.btnCapturarDatos.setOnClickListener(v -> {
            if (datosBinding.rbSVAbandonada.isChecked()){
                RazonFragment fragment = new RazonFragment();
                getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().beginTransaction().add(R.id.container_verificacion, fragment).commit();
            }
        });

        datosBinding.btnCapCasa.setOnClickListener(view -> abrirCamera());


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

                String b64 = bitmapToBase64(fotoCasa);

                cl.setCasa(b64);

                Snackbar.make(datosBinding.getRoot(), "Foto capturada", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setAction("REINTENTAR", view -> abrirCamera()) .show();

            } else {
                Snackbar.make(datosBinding.getRoot(), "Error de captura", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).setAction("REINTENTAR", view -> abrirCamera()) .show();
            }
        }
    }


    /**
     * Convierte Bitmap a formato Base64
     * @param bitmap Foto formato Bitmap {@link Bitmap}
     * @return Base64 {@link String}
     */
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}