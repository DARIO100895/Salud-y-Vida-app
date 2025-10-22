package com.example.salud_y_vida.ui.p_cita.adapter

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
        tvNombrePaciente.text = "${c.pacienteid}${c.medicoid}"
            tvEstado.text = "Estado: ${c.estadoCita}"
            root.setOnClickListener { onClick(c) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCitaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(lista[position])




    override fun getItemCount() = lista.size

    fun actualizar (nuevaLista : List<Cita>) {

        lista = nuevaLista
        notifyDataSetChanged()
    }
}