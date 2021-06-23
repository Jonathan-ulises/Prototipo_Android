package com.its_omar.prototipo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.its_omar.prototipo.R;
import com.its_omar.prototipo.databinding.FragmentVerificacionDatosBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerificacionDatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerificacionDatosFragment extends Fragment {

    private FragmentVerificacionDatosBinding datosBinding;

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

        datosBinding.rbSVEncontrado.setChecked(true);

        datosBinding.rgEstatus.setOnCheckedChangeListener((group, checkedId) -> {
            if (datosBinding.rbSVAbandonada.isChecked()){
                datosBinding.btnCapturarDatos.setText(R.string.razon_status);
            } else {
                datosBinding.btnCapturarDatos.setText(R.string.btn_capturar);
            }
        });

        datosBinding.btnCapturarDatos.setOnClickListener(v -> {
            if (datosBinding.rbSVAbandonada.isChecked()){
                RazonFragment fragment = new RazonFragment();
                getFragmentManager().beginTransaction().remove(this).commit();
                getFragmentManager().beginTransaction().add(R.id.container_verificacion, fragment).commit();
            }
        });

        // Inflate the layout for this fragment
        return datosBinding.getRoot();
    }
}