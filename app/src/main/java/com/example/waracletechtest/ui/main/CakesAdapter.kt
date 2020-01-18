package com.example.waracletechtest.ui.main

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waracletechtest.R
import com.example.waracletechtest.data.Cake
import com.example.waracletechtest.databinding.ListItemCakeBinding
import com.example.waracletechtest.databinding.PopupCakeBinding

class CakesAdapter(private val fragContext: Context) : ListAdapter<Cake, CakeViewHolder>(CakeItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        return CakeViewHolder.create(fragContext, parent)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: CakeViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }
}

class CakeViewHolder(private val fragContext: Context,
                     private val cakeBinding: ListItemCakeBinding) :
    RecyclerView.ViewHolder(cakeBinding.root) {

    private var cake: Cake? = null
    private var popupImageView: ImageView? = null

    fun bind(cake: Cake) {
        this.cake = cake
        cakeBinding.titleText.text = cake.title
        cakeBinding.cakeItem.setOnClickListener { createPopupWindow() }
        initImageView(cakeBinding.cakeImage)
    }

    fun recycle() {
        Glide.with(cakeBinding.root)
            .clear(cakeBinding.cakeImage)
        popupImageView?.let {
            Glide.with(cakeBinding.root)
                .clear(popupImageView!!)
        }
    }

    private fun initImageView(v: ImageView) {
        Glide.with(cakeBinding.root)
            .load(cake?.imageUrl)
            .into(v)
    }

    //TODO This is a little hacky. It would probably work better as a dialog fragment
    // to allow buttons and interactivity as well as avoid destroying on device rotate.
    private fun createPopupWindow() {
        getSystemService(fragContext, LayoutInflater::class.java)?.let { inflater ->
            val popupView = DataBindingUtil.inflate(inflater, R.layout.popup_cake,
                null, false) as PopupCakeBinding
            popupView.popupCakeTitle.text = cake?.title
            popupView.popupCakeDesc.text = cake?.desc
            this.popupImageView = popupView.popupCakeImage

            initImageView(this.popupImageView!!)
            val popupWindow = PopupWindow(popupView.root,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true)
            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow.elevation = 10f
            popupView.root.setOnClickListener { popupWindow.dismiss() }
            popupWindow.showAtLocation(cakeBinding.root, Gravity.CENTER, 0, 0)
        }
    }

    companion object {
        fun create(fragContext: Context, parent: ViewGroup): CakeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCakeBinding.inflate(layoutInflater, parent, false)
            return CakeViewHolder(fragContext, binding)
        }
    }
}

class CakeItemDiffCallback : DiffUtil.ItemCallback<Cake>() {
    override fun areItemsTheSame(oldItem: Cake, newItem: Cake) = oldItem === newItem
    override fun areContentsTheSame(oldItem: Cake, newItem: Cake) = oldItem == newItem
}