package com.example.salud_y_vida.ui.p_especialidad.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.salud_y_vida.data.model.Especialidad
import com.example.salud_y_vida.databinding.ItemEspecialidadBinding

class EspecialidadAdapter (

    private var lista : List<Especialidad>,
    private val onClick : (Especialidad) -> Unit

) : RecyclerView.Adapter<EspecialidadAdapter.VH>() {

    inner class VH(private val binding: ItemEspecialidadBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(e : Especialidad) = with(binding) {

            tvNombre.text = "${e.nombre}"
            root.setOnClickListener { onClick(e) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemEspecialidadBinding.inflate(
            LayoutInflater.from(parent.context), parent,false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.bind(lista[position])

    override fun getItemCount() = lista.size

    fun actualizar(nuevaLista : List<Especialidad>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}