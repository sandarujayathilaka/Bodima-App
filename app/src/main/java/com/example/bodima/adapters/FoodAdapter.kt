package com.example.bodima.adapters

import com.example.bodima.models.Foods
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R

class FoodAdapter(
    private val foodList: ArrayList<Foods>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<FoodAdapter.FoodHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Foods)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val foodView = LayoutInflater.from(parent.context).inflate(R.layout.food, parent, false)
        return FoodHolder(foodView)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {
        val currentFood = foodList[position]
        holder.addFood.text = currentFood.foodName.toString()
        holder.addPrice.text = currentFood.foodPrice.toString()
        holder.addAddress.text = currentFood.foodAddress.toString()

        val bytes = android.util.Base64.decode(currentFood.foodImage, android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.foodImg.setImageBitmap(bitmap)
    }

    inner class FoodHolder(foodView: View) : RecyclerView.ViewHolder(foodView), View.OnClickListener {
        val addFood: TextView = foodView.findViewById(R.id.foodname)
        val addPrice: TextView = foodView.findViewById(R.id.foodprice)
        val addAddress: TextView = foodView.findViewById(R.id.foodaddress)
        val foodImg: ImageView = foodView.findViewById(R.id.food_img)

        init {
            foodView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener.onItemClick(foodList[adapterPosition])
        }
    }
}
