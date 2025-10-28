package com.example.salud_y_vida.ui.p.medico.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.salud_y_vida.data.model.Medico
import com.example.salud_y_vida.databinding.ItemMedicoBinding


class MedicoAdapter (
    private var lista : List<Medico>,
    private val onClick : (Medico) -> Unit

) : RecyclerView.Adapter<MedicoAdapter.VH>() {

    inner class VH(private val binding: ItemMedicoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(m: Medico) = with(binding) {
            tvNombre.text = "${m.nombreMed} ${m.apellidoMed}"
            tvEspecialidad.text = "Especialidad: ${m.especialidad?.nombre ?: "Sin asignar"}"
            root.setOnClickListener { onClick(m) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMedicoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(lista[position])

    override fun getItemCount() = lista.size

    //Actualizar lista despues del CRUD
    fun actualizar(nuevaLista : List<Medico>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}