package com.example.bodima.adapters


import com.example.bodima.models.Foods
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bodima.R
import com.example.bodima.activities.EditFoodActivity
import com.example.bodima.activities.RecyclerUserFood
import com.google.firebase.database.FirebaseDatabase
import com.example.bodima.interfaces.OnItemClickListener

class FoodListAdapter( private val foodList: ArrayList<Foods>,
                       private val listener: FoodListAdapter.OnItemClickListener
): RecyclerView.Adapter<FoodListAdapter.FoodListHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Foods)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListHolder {
        val foodView = LayoutInflater.from(parent.context).inflate(R.layout.userfoodlist, parent, false)
        return FoodListHolder(foodView)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: FoodListHolder, position: Int) {
        val currentFood = foodList[position]
        holder.addFood.text = currentFood.foodName.toString()
        holder.addPrice.text = currentFood.foodPrice.toString()
        holder.addAddress.text = currentFood.foodAddress.toString()
        // holder.edit.text = currentFood.foodAddress.toString()
        // holder.delete.text = currentFood.foodAddress.toString()
        val bytes = android.util.Base64.decode(currentFood.foodImage, android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        holder.foodImg.setImageBitmap(bitmap)
    }

    inner class FoodListHolder(foodView: View) : RecyclerView.ViewHolder(foodView), View.OnClickListener {
        val addFood: TextView = foodView.findViewById(R.id.foodname)
        val addPrice: TextView = foodView.findViewById(R.id.foodprice)
        val addAddress: TextView = foodView.findViewById(R.id.foodaddress)
        val foodImg: ImageView = foodView.findViewById(R.id.food_img)
        val edit: ImageView = foodView.findViewById(R.id.editbtn)
        val delete: ImageView = foodView.findViewById(R.id.deletebtn)
        init {
            foodView.setOnClickListener(this)
            edit.setOnClickListener {
                val intent = Intent(foodView.context, EditFoodActivity::class.java)
                intent.putExtra("foodId", foodList[adapterPosition].foodId)
                foodView.context.startActivity(intent)
            }
            delete.setOnClickListener {
                val dbRef = FirebaseDatabase.getInstance().getReference("Foods").child(foodList[adapterPosition].foodId?:"")
                val mTask = dbRef.removeValue()

                mTask.addOnSuccessListener {
                    Toast.makeText(foodView.context, "Food data deleted", Toast.LENGTH_LONG).show()

                    val intent = Intent(foodView.context, RecyclerUserFood::class.java)

                    foodView.context.startActivity(intent)


                }.addOnFailureListener{ error ->
                    Toast.makeText(foodView.context, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
                }

            }
        }


        override fun onClick(v: View) {
            listener.onItemClick(foodList[adapterPosition])
        }
    }
}