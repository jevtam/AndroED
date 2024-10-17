package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val context: Context,
    private val list: ArrayList<ColorData>,
    private val cellClickListener: CellClickListener
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view)
        val viewColor: View = view.findViewById(R.id.view_color)

        fun bind(colorData: ColorData, clickListener: CellClickListener) {
            textView.text = colorData.colorName
            viewColor.setBackgroundColor(colorData.colorHex)

            itemView.setOnClickListener {
                clickListener.onCellClickListener(colorData.colorName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colorData = list[position]
        holder.bind(colorData, cellClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}