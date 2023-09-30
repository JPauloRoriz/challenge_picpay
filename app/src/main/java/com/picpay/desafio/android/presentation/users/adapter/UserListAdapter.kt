package com.picpay.desafio.android.presentation.users.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.databinding.ListItemUserBinding
import com.picpay.desafio.android.domain.model.UserModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class UserListAdapter : ListAdapter<UserModel, UserListAdapter.UserListItemViewHolder>(COMPARATOR) {

    var userModels = mutableListOf<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemViewHolder {
        val binding = ListItemUserBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserListItemViewHolder, position: Int) {
        holder.bind(userModels[position])
    }

    override fun getItemCount(): Int = userModels.size

    fun updateList(newList : List<UserModel>){
        userModels.clear()
        userModels.addAll(newList)
        notifyDataSetChanged()
    }

    inner class UserListItemViewHolder(
        val binding: ListItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(userModel: UserModel) {
            binding.name.text = userModel.name
            binding.username.text = userModel.username
            binding.progressBar.visibility = View.VISIBLE
            Picasso.get()
                .load(userModel.img)
                .error(R.drawable.ic_round_account_circle)
                .into(binding.picture, object : Callback {
                    override fun onSuccess() {
                        binding.progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        binding.progressBar.visibility = View.GONE
                    }
                })
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}