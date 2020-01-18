package com.example.waracletechtest.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.waracletechtest.data.Cake
import com.example.waracletechtest.databinding.ListItemCakeBinding

class CakesAdapter : ListAdapter<Cake, CakeViewHolder>(CakeItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        return CakeViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CakeViewHolder(private val cakeBinding: ListItemCakeBinding) : RecyclerView.ViewHolder(cakeBinding.root) {

    fun bind(cake: Cake) {
        cakeBinding.titleText.text = cake.title
        //TODO handle description and image url
    }

    companion object {
        fun create(parent: ViewGroup): CakeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCakeBinding.inflate(layoutInflater, parent, false)
            return CakeViewHolder(binding)
        }
    }
}

class CakeItemDiffCallback : DiffUtil.ItemCallback<Cake>() {
    override fun areItemsTheSame(oldItem: Cake, newItem: Cake) = oldItem === newItem
    override fun areContentsTheSame(oldItem: Cake, newItem: Cake) = oldItem == newItem
}