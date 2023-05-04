package com.example.bodima.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.interfaces.OnItemClickListener
import com.example.bodima.models.House

class HouseAdapter(private val houselist:ArrayList<House>,private val listener: OnItemClickListener): RecyclerView.Adapter<HouseAdapter.HouseHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item:House)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseHolder {
        val houseView = LayoutInflater.from(parent.context).inflate(
            R.layout.house,
            parent, false
        )
        return HouseHolder(houseView)
    }

    override fun getItemCount(): Int {
        return houselist.size
    }

    override fun onBindViewHolder(holder: HouseHolder, position: Int) {
        val currenthouse = houselist[position]
        holder.title.setText(currenthouse.title.toString())
        holder.price.setText(currenthouse.price.toString())
        holder.houselocation.setText(currenthouse.location.toString())
        val bytes = android.util.Base64.decode(
            currenthouse.houseimg,
            android.util.Base64.DEFAULT
        )

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.houseimg.setImageBitmap(bitmap)

    }

    inner class HouseHolder(houseView: View) : RecyclerView.ViewHolder(houseView),
        View.OnClickListener {

        val title: TextView = houseView.findViewById(R.id.housetitle)
        val price: TextView = houseView.findViewById(R.id.houseprice)
        val houseimg: ImageView = houseView.findViewById(R.id.house_img)
        val houselocation: TextView = houseView.findViewById(R.id.houselocation)

        init {
            houseView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener.onItemClick(houselist[adapterPosition])
        }

    }

}