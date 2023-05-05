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
import com.example.bodima.activities.EditFurnitureActivity
import com.example.bodima.activities.RecyclerUserFurnitureActivity
import com.example.bodima.models.Furniture
import com.google.firebase.database.FirebaseDatabase

class FurnitureListAdapter (private val furniturelist:ArrayList<Furniture>,private val listener: FurnitureListAdapter.OnItemClickListener): RecyclerView.Adapter<FurnitureListAdapter.FurnitureListHolder>()  {

    interface OnItemClickListener {
        fun onItemClick(item: Furniture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureListAdapter.FurnitureListHolder {
        val furnitureView = LayoutInflater.from(parent.context).inflate(
            R.layout.userfurniturelist,
            parent, false
        )
        return FurnitureListHolder(furnitureView)
    }

    override fun getItemCount(): Int {
        return furniturelist.size
    }

    override fun onBindViewHolder(holder: FurnitureListHolder, position: Int) {
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

    inner class FurnitureListHolder(furnitureView: View) : RecyclerView.ViewHolder(furnitureView),
        View.OnClickListener {

        val title: TextView = furnitureView.findViewById(R.id.furnituretitle_edit)
        val price: TextView = furnitureView.findViewById(R.id.furnitureprice_edit)
        val furnitureimg: ImageView = furnitureView.findViewById(R.id.furniture_img_edit)
        val furniturelocation: TextView = furnitureView.findViewById(R.id.furniturelocation_edit)
        val edibtn: ImageView = furnitureView.findViewById(R.id.editbtn)
        val delete: ImageView = furnitureView.findViewById(R.id.deletebtn)


        init {
            furnitureView.setOnClickListener(this)
            edibtn.setOnClickListener {
                val intent = Intent(furnitureView.context, EditFurnitureActivity::class.java)
                intent.putExtra("furnitureId", furniturelist[adapterPosition].id)
                furnitureView.context.startActivity(intent)
            }
            delete.setOnClickListener {
                val dbRef = FirebaseDatabase.getInstance().getReference("Furniture")
                    .child(furniturelist[adapterPosition].id ?: "")
                val mTask = dbRef.removeValue()

                mTask.addOnSuccessListener {
                    Toast.makeText(furnitureView.context, "Furniture data deleted", Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(furnitureView.context, RecyclerUserFurnitureActivity::class.java)

                    furnitureView.context.startActivity(intent)


                }.addOnFailureListener { error ->
                    Toast.makeText(
                        furnitureView.context,
                        "Deleting Err ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        }

        override fun onClick(v: View) {
            listener.onItemClick(furniturelist[adapterPosition])
        }

    }


    }