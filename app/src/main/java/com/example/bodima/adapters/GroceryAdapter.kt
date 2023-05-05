package com.example.bodima.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.interfaces.OnItemClickListener
import com.example.bodima.models.Grocery

class GroceryAdapter(private val grocerylist:ArrayList<Grocery>, private val listener: OnItemClickListener): RecyclerView.Adapter<GroceryAdapter.GroceryHolder>() {

   interface OnItemClickListener{
       fun onItemClick(item:Grocery)
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryHolder {
        val groceryView = LayoutInflater.from(parent.context).inflate(
            R.layout.grocery_show,
            parent, false
        )
        return GroceryHolder(groceryView)
    }

    override fun getItemCount(): Int {
        return grocerylist.size
    }

    override fun onBindViewHolder(holder: GroceryHolder, position: Int) {
        val currentgrocery = grocerylist[position]
        holder.title.setText(currentgrocery.title.toString())
        holder.price.setText(currentgrocery.price.toString())
        holder.grocerylocation.setText(currentgrocery.locate.toString())
        val bytes = android.util.Base64.decode(
            currentgrocery.groceryimg,
            android.util.Base64.DEFAULT
        )

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.groceryimg.setImageBitmap(bitmap)

    }

    inner class GroceryHolder(grocerView: View) : RecyclerView.ViewHolder(grocerView),
        View.OnClickListener {

        val title: TextView = grocerView.findViewById(R.id.grocerytitle)
        val price: TextView = grocerView.findViewById(R.id.groceryprice)
        val groceryimg: ImageView = grocerView.findViewById(R.id.grocery_img)
        val grocerylocation: TextView = grocerView.findViewById(R.id.grocerylocation)

        init {
            grocerView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener.onItemClick(grocerylist[adapterPosition])
        }

    }

}