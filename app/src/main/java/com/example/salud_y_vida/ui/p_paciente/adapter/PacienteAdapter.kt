package com.example.salud_y_vida.ui.p_paciente.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.salud_y_vida.data.model.Paciente
import com.example.salud_y_vida.databinding.ItemPacienteBinding

class PacienteAdapter(
    private var lista: List<Paciente>,
    private val onClick: (Paciente) -> Unit
) : RecyclerView.Adapter<PacienteAdapter.VH>() {

    inner class VH(private val binding: ItemPacienteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(p: Paciente) = with(binding) {
            tvNombre.text = "${p.nombrePaciente} ${p.apellidoPaciente}"
            tvDni.text = "DNI: ${p.dniPaciente}"
            root.setOnClickListener { onClick(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemPacienteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(lista[position])

    override fun getItemCount() = lista.size

    // Actualizar lista despues del CRUD
    fun actualizar(nuevaLista: List<Paciente>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}