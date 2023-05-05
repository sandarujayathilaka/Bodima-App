package com.example.bodima.adapters

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.activities.EditGroceryActivity
import com.example.bodima.activities.RecyclerUserGroceryActivity
import com.example.bodima.models.Grocery
import com.google.firebase.database.FirebaseDatabase

class GroceryListAdapter (private val grocerylist:ArrayList<Grocery>,private val listener: GroceryListAdapter.OnItemClickListener): RecyclerView.Adapter<GroceryListAdapter.GroceryListHolder>()  {

    interface OnItemClickListener {
        fun onItemClick(item: Grocery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListAdapter.GroceryListHolder {
        val houseView = LayoutInflater.from(parent.context).inflate(
            R.layout.usergrocerylist,
            parent, false
        )
        return GroceryListHolder(houseView)
    }

    override fun getItemCount(): Int {
        return grocerylist.size
    }

    override fun onBindViewHolder(holder: GroceryListHolder, position: Int) {
        val currentgrocery = grocerylist[position]
        holder.title.setText(currentgrocery.title.toString())
        holder.price.setText(currentgrocery.price.toString())
        holder.houselocation.setText(currentgrocery.locate.toString())
        val bytes = android.util.Base64.decode(
            currentgrocery.groceryimg,
            android.util.Base64.DEFAULT
        )

        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.houseimg.setImageBitmap(bitmap)

    }

    inner class GroceryListHolder(houseView: View) : RecyclerView.ViewHolder(houseView),
        View.OnClickListener {

        val title: TextView = houseView.findViewById(R.id.grocerytitle_edit)
        val price: TextView = houseView.findViewById(R.id.groceryprice_edit)
        val houseimg: ImageView = houseView.findViewById(R.id.grocery_img_edit)
        val houselocation: TextView = houseView.findViewById(R.id.grocerylocation_edit)
        val edibtn: ImageView = houseView.findViewById(R.id.Gro_editbtn)
        val delete: ImageView = houseView.findViewById(R.id.Gro_deletebtn)


        init {
            houseView.setOnClickListener(this)
            edibtn.setOnClickListener {
                val intent = Intent(houseView.context, EditGroceryActivity::class.java)
                intent.putExtra("groceryId", grocerylist[adapterPosition].id)
                houseView.context.startActivity(intent)
            }
            delete.setOnClickListener {
                val dbRef = FirebaseDatabase.getInstance().getReference("Grocery")
                    .child(grocerylist[adapterPosition].id ?: "")
                val mTask = dbRef.removeValue()

                mTask.addOnSuccessListener {
                    Toast.makeText(houseView.context, "Grocery data deleted", Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(houseView.context, RecyclerUserGroceryActivity::class.java)

                    houseView.context.startActivity(intent)


                }.addOnFailureListener { error ->
                    Toast.makeText(
                        houseView.context,
                        "Deleting Err ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        }

        override fun onClick(v: View) {
            listener.onItemClick(grocerylist[adapterPosition])
        }

    }


}