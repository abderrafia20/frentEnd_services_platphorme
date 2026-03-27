package com.example.clientcerviceplatphorm.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.ServiceRequest
import com.example.clientcerviceplatphorm.model.ServiceRequestItem
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterServiceRequest(
    private val items: List<ServiceRequestItem>,
    private val onClick: (ServiceRequestItem) -> Unit
) : RecyclerView.Adapter<AdapterServiceRequest.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvNameCF: TextView = itemView.findViewById(R.id.tvNameCF)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvdate: TextView = itemView.findViewById(R.id.tvdate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_request, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvNameCF.text = item.clientName
        holder.tvPrice.text = item.price
        holder.tvDescription.text = item.serviceRequest.decreption
        holder.tvdate.text = formatDate(item.serviceRequest.date)
        holder.tvStatus.text = "Status: ${item.serviceRequest.status}"

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    private fun formatDate(date: java.util.Date): String {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}