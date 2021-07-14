package com.its_omar.prototipo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.its_omar.prototipo.R;
import com.its_omar.prototipo.controller.SharedPreferencesApp;
import com.its_omar.prototipo.databinding.FragmentVerificarEstatusBinding;
import com.its_omar.prototipo.model.Constantes;

import static com.its_omar.prototipo.model.Constantes.ESTATUS_NO_ENCONTRADO;
import static com.its_omar.prototipo.model.Constantes.ESTATUS_VISITA_RECHAZADA;
//import com.its_omar.prototipo.databinding.FragmentVerificarEstatusBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerificarEstatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerificarEstatusFragment extends Fragment {

    private FragmentVerificarEstatusBinding verificarBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String NOMBRE_CLIENTE = "nombreCl";
    private static final String ID_CLIENTE_SELECCIONADO = "idC";

    // TODO: Rename and change types of parameters
    private String mNombre;
    private int mIdC;

    public VerificarEstatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param nombre Parameter 1.
     * @return A new instance of fragment VerificarEstatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerificarEstatusFragment newInstance(String nombre, int id) {
        VerificarEstatusFragment fragment = new VerificarEstatusFragment();
        Bundle args = new Bundle();
        args.putString(NOMBRE_CLIENTE, nombre);
        args.putInt(ID_CLIENTE_SELECCIONADO, id);
        Log.i(Constantes.TAG_ERROR_ARGUMENTS_FRAGMENT, "recibe -> " + nombre + " " + id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNombre = getArguments().getString(NOMBRE_CLIENTE);
            mIdC = getArguments().getInt(ID_CLIENTE_SELECCIONADO);
        } else {
            mNombre = "Verificacion de visita";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        verificarBinding = FragmentVerificarEstatusBinding.inflate(inflater, container, false);
        //verificarBinding.btnVerificar.setEnabled(true);

        verificarBinding.btnVerificar.setText(R.string.verificar_button);

        SharedPreferencesApp sp = SharedPreferencesApp.getInstance(getContext());

        verificarBinding.toolbar.setTitle(mNombre);
        ((AppCompatActivity)getActivity()).setSupportActionBar(verificarBinding.toolbar);

        verificarBinding.rgEstatus.setOnCheckedChangeListener((radioGroup, i) -> {

            if (verificarBinding.rbSEncontrador.isChecked()){
                //verificarBinding.btnVerificar.setEnabled(verificarBinding.rbSEncontrador.isChecked());
                verificarBinding.btnVerificar.setText(R.string.verificar_button);
            } else {
                verificarBinding.btnVerificar.setText(R.string.especificar_razon);
            }
        });

        verificarBinding.btnVerificar.setOnClickListener(view -> {

            if (verificarBinding.rbSEncontrador.isChecked()){
                VerificacionDatosFragment fragment = VerificacionDatosFragment.newInstance(mIdC);
                getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().beginTransaction().add(R.id.container_verificacion, fragment).commit();

            } else if(verificarBinding.rbSNoEncontrado.isChecked()){
                RazonFragment fragment = RazonFragment.newInstance(mIdC, sp.getUsuarioLogeado().getId_empleado(), ESTATUS_NO_ENCONTRADO, mNombre);
                getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().beginTransaction().add(R.id.container_verificacion, fragment).commit();
            } else if(verificarBinding.rbSVisitaRechazada.isChecked()){
                RazonFragment fragment = RazonFragment.newInstance(mIdC, sp.getUsuarioLogeado().getId_empleado(), ESTATUS_VISITA_RECHAZADA, mNombre);
                getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().beginTransaction().add(R.id.container_verificacion, fragment).commit();
            }
        });


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_verificar_estatus, container, false);
        return verificarBinding.getRoot();
    }
}