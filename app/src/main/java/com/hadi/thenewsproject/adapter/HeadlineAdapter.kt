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
import kotlinx.android.synthetic.main.item_headline.view.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HeadlineAdapter(
    val context: Context,
    val list: List<ArticlesItem>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<HeadlineAdapter.HeadlineViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlineViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_headline, parent, false)
        return HeadlineViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: HeadlineViewHolder, position: Int) {
        val item = list[position]

        holder.author.text = item.author
        holder.title.text = item.title
        holder.source.text = item.source.name

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dtf = DateTimeFormatter.ISO_DATE_TIME
            val zdt = ZonedDateTime.parse(list[position].publishedAt,dtf)

            val output = "${zdt.month} - ${zdt.dayOfMonth}"
            holder.date.text =output
        }

        Glide.with(context).load(item.urlToImage).into(holder.image)
    }

    class HeadlineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.imgUrl as ImageView
        val author= itemView.tvAuthor as TextView
        val title = itemView.tvTitle as TextView
        val source = itemView.tvSource as TextView
        val date = itemView.tvDate as TextView
    }

    interface OnItemClickListener {
        fun onItemClick(item: ArticlesItem)
    }

}