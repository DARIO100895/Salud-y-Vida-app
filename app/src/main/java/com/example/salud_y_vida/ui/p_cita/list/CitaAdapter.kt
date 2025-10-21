package com.example.salud_y_vida.ui.p_cita.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.salud_y_vida.data.model.Cita
import com.example.salud_y_vida.databinding.ItemCitaBinding

class CitaAdapter (
    private var lista : List<Cita>,
    private val onClick : (Cita) -> Unit
) : RecyclerView.Adapter<CitaAdapter.VH>() {

    inner class VH(private val binding: ItemCitaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(c: Cita) = with(binding) {
            val paciente = c.paciente
            tvNombrePaciente.text = if (paciente != null) {
                "${paciente.nombrePaciente} ${paciente.apellidoPaciente}"
            } else {
                "Paciente ID: ${c.pacienteid}"
            }
            tvFecha.text = "Fecha: ${c.fechaCita?.toString() ?: "Sin fecha"}"
            tvEstado.text = "Estado: ${c.estadoCita ?: "Sin estado"}"
            tvHora.text = "Hora: ${c.horaCita?.toString() ?: "Sin Hora"}"

            root.setOnClickListener { onClick(c) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCitaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Log.d("CITA_ADAPTER", "Bind posición $position: ${lista[position]}")
        holder.bind(lista[position])
    }



    override fun getItemCount() = lista.size

    fun actualizar (nuevaLista : List<Cita>) {

        lista = nuevaLista
        notifyDataSetChanged()
    }
}

