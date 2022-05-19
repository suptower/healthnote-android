package de.othregensburg.healthnote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.othregensburg.healthnote.data.Medicament

class ListAdapter: RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var medList = emptyList<Medicament>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameItem: TextView = view.findViewById(R.id.medName)
        val idItem: TextView = view.findViewById(R.id.medId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = medList[position]
        holder.nameItem.text = currentItem.name
        holder.idItem.text = currentItem.id.toString()
    }

    override fun getItemCount(): Int {
        return medList.size
    }

    fun setData(meds: List<Medicament>) {
        this.medList = meds
        notifyDataSetChanged()
    }
}