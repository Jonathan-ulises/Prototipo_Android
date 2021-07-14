package com.its_omar.prototipo.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.its_omar.prototipo.R;
import com.its_omar.prototipo.databinding.FragmentDialogINEBinding;

import java.util.Objects;


public class DialogINEFragment extends DialogFragment {

    private FragmentDialogINEBinding dialogINEBinding;
    public static final String TAG = "full_screen_dialog_ine";

    private static final String FOTO_INE = "INE";

    //Parametros del fragment
    private String fotoIne;

    public DialogINEFragment() {
    }

    /**
     * Crea nuevas instancias de este fragment con el parametro de la base 64 para a foto del ine
     *
     * @param Base64INE Parameter 1.
     * @return Una nueva instancia de DialogINEFragment {@link DialogINEFragment}
     */
    // TODO: Rename and change types and number of parameters
    public static DialogINEFragment newInstance(String Base64INE) {
        DialogINEFragment fragment = new DialogINEFragment();
        Bundle args = new Bundle();
        args.putString(FOTO_INE, Base64INE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fotoIne = getArguments().getString(FOTO_INE);
        }

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dialogINEBinding = FragmentDialogINEBinding.inflate(inflater, container, false);

        dialogINEBinding.btniCerrearDialog.setOnClickListener(v -> dismiss());
        showINE();

        return dialogINEBinding.getRoot();
    }

    /**
     * Muestra la fotografia del INE, identificacion si es una base 64
     */
    private void showINE() {
        if(fotoIne.length() > 100) {
            Glide.with(requireContext())
                    .load("data:image/png|jpg|jpeg|gif;base64," + fotoIne)
                    .into(dialogINEBinding.ivFotoINE);
        }else {
            dialogINEBinding.ivErrorImage.setVisibility(View.VISIBLE);
        }
    }
}