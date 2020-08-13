package com.example.geoagenda.ui.reminder

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geoagenda.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.reminder_card.view.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


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
        val image: ImageView = reminderCard.reminder_image
        val time: TextView = reminderCard.reminder_time
        val title: TextView = reminderCard.reminder_title
        val note: TextView = reminderCard.reminder_note
        val alarmIcon: ImageView = reminderCard.alarm_icon
        val recordingIcon: ImageView = reminderCard.recording_icon
        val locationIcon: ImageView = reminderCard.location_icon

        fun initialize(item: Reminder, action: OnReminderItemClickListener) {
            //image.setImageURI(Uri.parse(getBitmapFromURL(item.image)))
            //image.setImageURI(Uri.parse(item.image))
            Picasso.get()
                .load(item.image)
                .into(image, object : Callback {
                    override fun onSuccess() {
                        Log.d(TAG, "success")
                    }

                    override fun onError(e: Exception?) {
                        Log.d(TAG, "error")
                    }
                })
            title.text = item.title
            note.text = item.note
            if (item.month != "0") {
                alarmIcon.setImageResource(R.drawable.ic_alarm_on)
                val text: String = "Fecha: " + item.day + "/" + item.month + "/" + item.year + "   Hora: " + item.hour + ":" + item.minute
                time.text = text
                time.visibility = View.VISIBLE
            }

            if(item.recording != "null"){
                recordingIcon.setImageResource(R.drawable.ic_keyboard_voice_green)
            }

            if(item.location != ""){
                locationIcon.setImageResource(R.drawable.ic_location_green)
            }

            itemView.setOnClickListener {
                action.onItemClick(item, absoluteAdapterPosition)
            }
        }

        private fun getBitmapFromURL(image: String): String? {
            return try {
                val url = URL(image)
                val connection: HttpURLConnection = url
                    .openConnection() as HttpURLConnection
                connection.setDoInput(true)
                connection.connect()
                val input: InputStream = connection.getInputStream()
                BitmapFactory.decodeStream(input).toString()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }


    }

    interface OnReminderItemClickListener {
        fun onItemClick(reminders: Reminder, position: Int){

        }
    }

}