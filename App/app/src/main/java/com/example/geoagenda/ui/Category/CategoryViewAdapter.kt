package com.example.geoagenda.ui.Category

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geoagenda.R
import kotlinx.android.synthetic.main.category_card.view.*
import java.io.File

class CategoryViewAdapter(val categoryList :List<Category>, var clickListener: OnCategoryItemClickListener): RecyclerView.Adapter<CategoryViewAdapter.CategoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder{
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.category_card, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        /*val currentItem = reminderList[position]

        holder.title.text = currentItem.title
        holder.note.text = currentItem.note*/
        holder.initialize(categoryList.get(position), clickListener)
    }

    override fun getItemCount() = categoryList.size

    class CategoryViewHolder(categoryCard: View) : RecyclerView.ViewHolder(categoryCard){
        val image: ImageView = categoryCard.category_image
        val title: TextView = categoryCard.category_title
        fun initialize(item: Category, action: OnCategoryItemClickListener) {
            image.setImageURI(Uri.parse(item.image))
            title.text = item.title

            itemView.setOnClickListener {
                action.onItemClick(item, absoluteAdapterPosition)
            }
        }
    }

    interface OnCategoryItemClickListener {
        fun onItemClick(categories: Category, position: Int){

        }
    }


}