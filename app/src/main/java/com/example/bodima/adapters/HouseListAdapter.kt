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
import com.example.bodima.activities.EditHouseActivity
import com.example.bodima.activities.RecyclerUserHouseActivity
import com.example.bodima.models.House
import com.google.firebase.database.FirebaseDatabase

class HouseListAdapter (private val houselist:ArrayList<House>,private val listener: HouseListAdapter.OnItemClickListener): RecyclerView.Adapter<HouseListAdapter.HouseListHolder>()  {

    interface OnItemClickListener {
        fun onItemClick(item: House)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseListAdapter.HouseListHolder {
        val houseView = LayoutInflater.from(parent.context).inflate(
            R.layout.userhomelist,
            parent, false
        )
        return HouseListHolder(houseView)
    }

    override fun getItemCount(): Int {
        return houselist.size
    }

    override fun onBindViewHolder(holder: HouseListHolder, position: Int) {
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

    inner class HouseListHolder(houseView: View) : RecyclerView.ViewHolder(houseView),
        View.OnClickListener {

        val title: TextView = houseView.findViewById(R.id.housetitle_edit)
        val price: TextView = houseView.findViewById(R.id.houseprice_edit)
        val houseimg: ImageView = houseView.findViewById(R.id.house_img_edit)
        val houselocation: TextView = houseView.findViewById(R.id.houselocation_edit)
        val edibtn: ImageView = houseView.findViewById(R.id.editbtn)
        val delete: ImageView = houseView.findViewById(R.id.deletebtn)


        init {
            houseView.setOnClickListener(this)
            edibtn.setOnClickListener {
                val intent = Intent(houseView.context, EditHouseActivity::class.java)
                intent.putExtra("houseId", houselist[adapterPosition].id)
                houseView.context.startActivity(intent)
            }
            delete.setOnClickListener {
                val dbRef = FirebaseDatabase.getInstance().getReference("House")
                    .child(houselist[adapterPosition].id ?: "")
                val mTask = dbRef.removeValue()

                mTask.addOnSuccessListener {
                    Toast.makeText(houseView.context, "House data deleted", Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(houseView.context, RecyclerUserHouseActivity::class.java)

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
            listener.onItemClick(houselist[adapterPosition])
        }

    }


    }