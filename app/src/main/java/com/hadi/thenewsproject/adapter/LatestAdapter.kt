package com.hadi.thenewsproject.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hadi.thenewsproject.R
import com.hadi.thenewsproject.model.ArticlesItem
import kotlinx.android.synthetic.main.item_latest.view.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class LatestAdapter(
    val context: Context,
    val list: List<ArticlesItem>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<LatestAdapter.LatestViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_latest, parent, false)
        return LatestViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: LatestViewHolder, position: Int) {
        Glide.with(context).load(list[position].urlToImage).into(holder.image)
        holder.title.text = list[position].title


//        val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val instant = Instant.parse(list[position].publishedAt)
//            instant
//        } else {
//            holder.date.text = list[position].publishedAt.substring(0,10)
//        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dtf = DateTimeFormatter.ISO_DATE_TIME
            val zdt = ZonedDateTime.parse(list[position].publishedAt,dtf)

            val output = "${zdt.year} - ${zdt.month} - ${zdt.dayOfMonth} - ${zdt.dayOfWeek}"
            holder.date.text =output
        }


        holder.itemView.setOnClickListener {
            listener.onItemClick(list[position])
        }

    }

    class LatestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.imgNewsLatest as ImageView
        val title = itemView.tvNewsTitleLatest as TextView
        val date = itemView.tvDateLatest as TextView
    }

    interface OnItemClickListener {
        fun onItemClick(item: ArticlesItem)
    }

}