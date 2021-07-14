package com.its_omar.prototipo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.its_omar.prototipo.ClientesActivity;
import com.its_omar.prototipo.api.ServiceRetrofit;
import com.its_omar.prototipo.api.WebService;
import com.its_omar.prototipo.databinding.FragmentRazonBinding;
import com.its_omar.prototipo.model.Result;
import com.its_omar.prototipo.model.bodyJSONCliente.BodyJSONCliente;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.its_omar.prototipo.controller.ConsultasComunes.registrarAccionBitacora;
import static com.its_omar.prototipo.model.Constantes.ESTATUS_NO_ENCONTRADO;
import static com.its_omar.prototipo.model.Constantes.ESTATUS_VISITA_ABANDONADA;
import static com.its_omar.prototipo.model.Constantes.ESTATUS_VISITA_RECHAZADA;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RazonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RazonFragment extends Fragment {

    private FragmentRazonBinding razonBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ID_CLIENTE = "idC";
    private static final String NOMBRE_CLIENTE = "nombreC";
    private static final String ID_EMPLEADO = "idE";
    private static final String ESTATUS_VISITA_FINAL = "estatus_visita";

    // TODO: Rename and change types of parameters
    private int mIdCliente;
    private int mIdEmpleado;
    private int mEstatusV;
    private String nombreC;

    public RazonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param idC Parameter 1.
     * @param idE Parameter 2.
     * @return A new instance of fragment RazonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RazonFragment newInstance(int idC, int idE, int est, String nombreC) {
        RazonFragment fragment = new RazonFragment();
        Bundle args = new Bundle();
        args.putInt(ID_CLIENTE, idC);
        args.putInt(ID_EMPLEADO, idE);
        args.putInt(ESTATUS_VISITA_FINAL, est);
        args.putString(NOMBRE_CLIENTE, nombreC);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdCliente = getArguments().getInt(ID_CLIENTE);
            mIdEmpleado = getArguments().getInt(ID_EMPLEADO);
            mEstatusV = getArguments().getInt(ESTATUS_VISITA_FINAL);
            nombreC = getArguments().getString(NOMBRE_CLIENTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        razonBinding = FragmentRazonBinding.inflate(inflater, container, false);

        razonBinding.toolbarRazon.setTitle(nombreC);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(razonBinding.toolbarRazon);

        initRadioButtons();

        razonBinding.btnVerificar.setOnClickListener(v -> {
            subirRazon();
        });

        // Inflate the layout for this fragment
        return razonBinding.getRoot();
    }

    /**
     * Inicializa la vizualizacion e interaccion de los radioButtons dependiendo el estatus recibido
     */
    private void initRadioButtons(){
        switch (mEstatusV){
            case ESTATUS_VISITA_ABANDONADA:
                radioButtonsVisitaAbandonada();
                break;
            case ESTATUS_NO_ENCONTRADO:
                radioButtonsNoEncontrado();
                break;
            case ESTATUS_VISITA_RECHAZADA:
                radioButtonsVisitaRechazada();
                break;
        }

    }

    /**
     * Preparacion de radiobutton si el estatus es una Visita Abandonada
     */
    private void radioButtonsVisitaAbandonada(){
        razonBinding.rbSNoEncontrado.setChecked(false);
        razonBinding.rbSNoEncontrado.setEnabled(false);
        razonBinding.rbSNoEncontrado.setVisibility(View.GONE);
        razonBinding.rbSVisitaRechazada.setChecked(false);
        razonBinding.rbSVisitaRechazada.setEnabled(false);
        razonBinding.rbSVisitaRechazada.setVisibility(View.GONE);
        razonBinding.rbSVisitaAbandonada.setChecked(true);
        razonBinding.rbSVisitaAbandonada.setEnabled(true);
    }

    /**
     * Preparacion de radiobutton si el estatus es No Encontrado
     */
    private void radioButtonsNoEncontrado(){
        razonBinding.rbSNoEncontrado.setChecked(true);
        razonBinding.rbSNoEncontrado.setEnabled(true);
        razonBinding.rbSVisitaRechazada.setChecked(false);
        razonBinding.rbSVisitaRechazada.setEnabled(false);
        razonBinding.rbSVisitaRechazada.setVisibility(View.GONE);
        razonBinding.rbSVisitaAbandonada.setChecked(false);
        razonBinding.rbSVisitaAbandonada.setEnabled(false);
        razonBinding.rbSVisitaAbandonada.setVisibility(View.GONE);
    }

    /**
     * Preparacion de radiobutton si el estatus es una Visita Rechazada
     */
    private void radioButtonsVisitaRechazada(){
        razonBinding.rbSNoEncontrado.setChecked(false);
        razonBinding.rbSNoEncontrado.setEnabled(false);
        razonBinding.rbSNoEncontrado.setVisibility(View.GONE);
        razonBinding.rbSVisitaRechazada.setChecked(true);
        razonBinding.rbSVisitaRechazada.setEnabled(true);
        razonBinding.rbSVisitaAbandonada.setChecked(false);
        razonBinding.rbSVisitaAbandonada.setEnabled(false);
        razonBinding.rbSVisitaAbandonada.setVisibility(View.GONE);
    }

    /**
     * Sube los datos de la razon al webservice
     */
    private void subirRazon(){
        BodyJSONCliente body = new BodyJSONCliente();
        body.setFirma("");
        body.setFotoCasa("");
        body.setIdCliente(mIdCliente);
        body.setLatitudReal(0d);
        body.setLongitudReal(0d);
        body.setIdEstatusVisita(mEstatusV);
        String razon = razonBinding.etRazonV.getText().toString();
        body.setComentario(razon);

        WebService api = ServiceRetrofit.getInstance().getSevices();

        api.asignarVisita(body).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if(response.code() == 200){
                    if(response.body().isOk()){
                        registrarAccionBitacora("Verificacion", "Asignacion", mIdEmpleado);

                        Snackbar.make(razonBinding.getRoot(), "Datos Capturados", Snackbar.LENGTH_SHORT)
                                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();

                        Intent intent = new Intent(getActivity(), ClientesActivity.class);
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
}