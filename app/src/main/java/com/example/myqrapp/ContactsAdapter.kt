package com.example.myqrapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactsAdapter(private val mDataset: ArrayList<String>) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val txtHeader: TextView = v.findViewById(R.id.tvContactName)
        val txtFooter: TextView = v.findViewById(R.id.tvContactPhone)

        fun remove(item: String) {
            val position = mDataset.indexOf(item)
            mDataset.remove(item)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.contacts_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = mDataset[position]
        holder.txtHeader.text = mDataset[position]
        holder.txtHeader.setOnClickListener {
            holder.remove(name)
        }
        holder.txtFooter.text = "Nombre: ${mDataset[position]}"
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }
}