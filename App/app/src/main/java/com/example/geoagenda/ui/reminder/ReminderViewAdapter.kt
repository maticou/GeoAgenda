package com.example.geoagenda.ui.reminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geoagenda.R
import kotlinx.android.synthetic.main.reminder_card.view.*

class ReminderViewAdapter(val reminderList: List<Reminder>, var clickListener: OnReminderItemClickListener) : RecyclerView.Adapter<ReminderViewAdapter.ReminderViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.reminder_card, parent, false)

        return ReminderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        /*val currentItem = reminderList[position]

        holder.title.text = currentItem.title
        holder.note.text = currentItem.note*/
        holder.initialize(reminderList.get(position), clickListener)
    }

    override fun getItemCount() = reminderList.size

    class ReminderViewHolder(reminderCard: View) : RecyclerView.ViewHolder(reminderCard){
        val title: TextView = reminderCard.reminder_title
        val note: TextView = reminderCard.reminder_note

        fun initialize(item: Reminder, action: OnReminderItemClickListener) {
            title.text = item.title
            note.text = item.note

            itemView.setOnClickListener {
                action.onItemClick(item, absoluteAdapterPosition)
            }
        }
    }

    interface OnReminderItemClickListener {
        fun onItemClick(reminders: Reminder, position: Int){

        }
    }
}