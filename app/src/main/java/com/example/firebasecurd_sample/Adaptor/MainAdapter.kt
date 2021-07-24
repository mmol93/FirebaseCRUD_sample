package com.example.firebasecurd_sample.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasecurd_sample.R
import com.example.firebasecurd_sample.User
import com.example.firebasecurd_sample.databinding.RecyclerMainItemBinding

class MainAdapter(
    private val userList : ArrayList<User>
    ) : RecyclerView.Adapter<MainAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).
        inflate(R.layout.recycler_main_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.nameTextView.text = currentItem.name
        holder.courseTextView.text = currentItem.course
        holder.emailTextView.text = currentItem.email
        Glide.with(holder.itemView).load(currentItem.ImageURL)
            .placeholder(R.mipmap.ic_launcher).into(holder.roundImageView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val binder : RecyclerMainItemBinding = RecyclerMainItemBinding.bind(view)

        val nameTextView = binder.nameTextView
        val emailTextView = binder.emailTextView
        val courseTextView = binder.courseTextView
        val roundImageView = binder.roundImageView
    }
}