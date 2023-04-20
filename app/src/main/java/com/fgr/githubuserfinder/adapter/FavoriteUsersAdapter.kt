package com.fgr.githubuserfinder.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fgr.githubuserfinder.R
import com.fgr.githubuserfinder.local.entity.UserEntity
import com.fgr.githubuserfinder.ui.DetailActivity

class FavoriteUsersAdapter(private val context: Context, private val listUsers: List<UserEntity>):  RecyclerView.Adapter<FavoriteUsersAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userImage: ImageView = view.findViewById(R.id.item_user_image)
        val userName: TextView = view.findViewById(R.id.item_user_name)
        val userId: TextView = view.findViewById(R.id.item_user_id_value)
        val userItemLayout: ConstraintLayout = view.findViewById(R.id.item_user_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(listUsers[position].imageUrl)
            .into(holder.userImage)
        holder.userName.text = listUsers[position].username
        holder.userId.text = listUsers[position].id.toString()
        holder.userItemLayout.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, listUsers[position].id)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, listUsers[position].username)
            intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, listUsers[position].imageUrl)
            intent.putExtra(DetailActivity.EXTRA_PROFILE_URL, listUsers[position].profile_url)
            ContextCompat.startActivity(context, intent, null)
        }
    }

    companion object {
    }
}