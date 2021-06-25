package com.its_omar.prototipo.fragments.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.its_omar.prototipo.databinding.ItemClienteListBinding;
import com.its_omar.prototipo.model.Cliente_por_visitar;
import com.its_omar.prototipo.model.Constantes;

public class ClientesVisitaAdapter extends ListAdapter<Cliente_por_visitar, ClientesVisitaAdapter.ClientesViewHolder> {


    public static final DiffUtil.ItemCallback<Cliente_por_visitar> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Cliente_por_visitar>() {
                //Compara si los items de la lista son iguales
                @Override
                public boolean areItemsTheSame(
                        @NonNull Cliente_por_visitar oldCliente_por_visitar, @NonNull  Cliente_por_visitar newCliente_por_visitar) {
                    return oldCliente_por_visitar.getIdCliente() == newCliente_por_visitar.getIdCliente();
                }

                //Compara si el contenido de un item es el mismo que otro
                @Override
                public boolean areContentsTheSame(
                        @NonNull Cliente_por_visitar oldCliente_por_visitar, @NonNull Cliente_por_visitar newCliente_por_visitar) {
                    return oldCliente_por_visitar.equals(newCliente_por_visitar);
                }
            };


    public ClientesVisitaAdapter() {
        super(DIFF_CALLBACK);
    }

    private OnItemClickListener onItemClickListener;

    /**
     * Evento de click del item de la lista
     */
    public interface OnItemClickListener {
        /**
         * Muestra informacion del cliente seleccionado
         * @param cliente
         */
        void onItemClickListener(Cliente_por_visitar cliente);
    }

    /**
     * Asigna un nuevo listener
     * @param onItemClickListener listenerClick {@link OnItemClickListener}
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ClientesViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        ItemClienteListBinding binding = ItemClienteListBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ClientesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull  ClientesVisitaAdapter.ClientesViewHolder holder, int position) {
        Cliente_por_visitar cliente = getItem(position);
        holder.bind(cliente);
    }

    public class ClientesViewHolder extends RecyclerView.ViewHolder{
        private ItemClienteListBinding itemBinding;

        public ClientesViewHolder(@NonNull  ItemClienteListBinding binding) {
            super(binding.getRoot());

            this.itemBinding = binding;
        }

        /**
         * Conecta los datos con los componentes del item de la lista
         * @param cliente Cliente por visitar {@link Cliente_por_visitar}
         */
        protected void bind(Cliente_por_visitar cliente) {
            itemBinding.tvNombreCliente.setText(Constantes.generarNombreCompleto(cliente));

            itemBinding.cvItemCliente.setOnClickListener(view -> {
                onItemClickListener.onItemClickListener(cliente);
            });

            //Permite que renderice correctamente cada item de la lista
            itemBinding.executePendingBindings();
        }


    }
}
