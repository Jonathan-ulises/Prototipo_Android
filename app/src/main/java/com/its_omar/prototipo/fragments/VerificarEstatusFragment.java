package com.its_omar.prototipo.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.its_omar.prototipo.R;
import com.its_omar.prototipo.VerificacionVisitaActivity;
import com.its_omar.prototipo.databinding.FragmentVerificarEstatusBinding;
import com.its_omar.prototipo.model.Constantes;

import java.util.Objects;
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
        verificarBinding.btnVerificar.setEnabled(false);

        Toast.makeText(getContext(), mNombre, Toast.LENGTH_SHORT).show();

        verificarBinding.toolbar.setTitle(mNombre);
        ((AppCompatActivity)getActivity()).setSupportActionBar(verificarBinding.toolbar);

        verificarBinding.rgEstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                verificarBinding.btnVerificar.setEnabled(verificarBinding.rbSEncontrador.isChecked());
            }
        });

        verificarBinding.btnVerificar.setOnClickListener(view -> {
            //VerificacionVisitaActivity activity = new VerificacionVisitaActivity();
            VerificacionDatosFragment fragment = VerificacionDatosFragment.newInstance(mIdC);
            getFragmentManager().beginTransaction().remove(this).commit();
            getFragmentManager().beginTransaction().add(R.id.container_verificacion, fragment).commit();
            //VerificacionDatosFragment fragment = new VerificacionDatosFragment();
            //requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_verificacion, fragment);
        });


        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_verificar_estatus, container, false);
        return verificarBinding.getRoot();
    }
}