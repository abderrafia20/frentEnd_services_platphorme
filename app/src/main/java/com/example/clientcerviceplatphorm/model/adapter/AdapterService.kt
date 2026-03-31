package com.example.clientcerviceplatphorm.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.Service

class AdapterService(private val services: List<Service>, private val onItemClick: (Service) -> Unit) :
    RecyclerView.Adapter<AdapterService.ServiceViewHolder>() {


    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvNameFournin: TextView = itemView.findViewById(R.id.tvNameF)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_services, parent, false)
        return ServiceViewHolder(view)
    }


    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]
        holder.tvTitle.text = service.title
        holder.tvPrice.text = "${service.price} DH"
        holder.tvNameFournin.text = "By: ${service.nameFournisseur}"
        holder.itemView.setOnClickListener {
            onItemClick(service)
        }

    }


    override fun getItemCount(): Int {
        return services.size
    }
}