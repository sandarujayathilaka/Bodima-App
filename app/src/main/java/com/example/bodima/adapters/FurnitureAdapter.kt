package com.example.bodima.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.models.Furniture

class FurnitureAdapter(private val furniturelist:ArrayList<Furniture>, private val listener: OnItemClickListener): RecyclerView.Adapter<FurnitureAdapter.FurnitureHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Furniture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureHolder {
        val furnitureView = LayoutInflater.from(parent.context).inflate(
            R.layout.furniture,
            parent, false
        )
        return FurnitureHolder(furnitureView)
    }

    override fun getItemCount(): Int {
        return furniturelist.size
    }

    override fun onBindViewHolder(holder: FurnitureHolder, position: Int) {
        val currentfurniture = furniturelist[position]
        holder.title.setText(currentfurniture.title.toString())
        holder.price.setText(currentfurniture.price.toString())
        holder.furniturelocation.setText(currentfurniture.location.toString())
        val bytes = android.util.Base64.decode(
            currentfurniture.furnitureimg,
            android.util.Base64.DEFAULT
        )

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.furnitureimg.setImageBitmap(bitmap)

    }

    inner class FurnitureHolder(furnitureView: View) : RecyclerView.ViewHolder(furnitureView),
        View.OnClickListener {

        val title: TextView = furnitureView.findViewById(R.id.furnituretitle)
        val price: TextView = furnitureView.findViewById(R.id.furnitureprice)
        val furnitureimg: ImageView = furnitureView.findViewById(R.id.furniture_img)
        val furniturelocation: TextView = furnitureView.findViewById(R.id.furniturelocation)

        init {
            furnitureView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener.onItemClick(furniturelist[adapterPosition])
        }

    }

}