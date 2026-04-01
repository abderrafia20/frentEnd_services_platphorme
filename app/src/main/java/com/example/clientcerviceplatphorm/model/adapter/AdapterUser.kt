package com.example.clientcerviceplatphorm.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clientcerviceplatphorm.R
import com.example.clientcerviceplatphorm.model.User

class AdapterUser(
    private val users: List<User>,
    private val onClick: (User) -> Unit
) : RecyclerView.Adapter<AdapterUser.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvUserEmail: TextView = itemView.findViewById(R.id.tvUserEmail)
        val tvUserPhone: TextView = itemView.findViewById(R.id.tvUserPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.tvUserName.text = user.getName()
        holder.tvUserEmail.text = user.getEmail()
        holder.tvUserPhone.text = user.getPhone()

        holder.itemView.setOnClickListener {
            onClick(user)
        }
    }
}