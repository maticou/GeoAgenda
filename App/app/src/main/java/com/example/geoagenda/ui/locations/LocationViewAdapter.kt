package com.example.geoagenda.ui.locations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geoagenda.R
import com.example.geoagenda.ui.addlocation.Location
import com.example.geoagenda.ui.reminder.ReminderViewAdapter
import kotlinx.android.synthetic.main.location_item.view.*

class LocationViewAdapter( val locationList: List<Location>, var clickListener: OnLocationItemClickListener) : RecyclerView.Adapter<LocationViewAdapter.LocationViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)

        return LocationViewHolder(itemView)
    }

    override fun getItemCount() = locationList.size

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val currentItem = locationList[position]

        holder.name.text = currentItem.nombre
        holder.initialize(locationList.get(position), clickListener)
    }

    class LocationViewHolder(locationCard: View) : RecyclerView.ViewHolder(locationCard){
        val name: TextView = locationCard.locationNameText

        fun initialize(item2: Location, action: LocationViewAdapter.OnLocationItemClickListener) {

            itemView.setOnClickListener {
                action.onItemClick(item2, absoluteAdapterPosition)
            }
        }
    }

    interface OnLocationItemClickListener {
        fun onItemClick(location: Location, position: Int){

        }
    }
}