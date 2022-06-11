package de.othregensburg.healthnote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.othregensburg.healthnote.fragments.list.ListFragmentDirections
import de.othregensburg.healthnote.model.Medicament

class ListAdapter: RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var medList = emptyList<Medicament>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameItem: TextView = view.findViewById(R.id.medName)
        val rowLayout: ConstraintLayout = view.findViewById(R.id.rowlayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.text_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = medList[position]
        holder.nameItem.text = currentItem.name

        holder.rowLayout.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return medList.size
    }

    fun setData(meds: List<Medicament>) {
        this.medList = meds
        notifyDataSetChanged()
    }
}