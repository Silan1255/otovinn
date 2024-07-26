package com.example.otovinncase.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.otovinncase.R
import com.example.otovinncase.data.model.Menu

class MenuAdapter(
    private var menus: ArrayList<Menu>,
    private val context: Context,
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_menu_list, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = menus[position]
        menu.title?.let { holder.menu.text = it }
    }

    override fun getItemCount(): Int = menus.size

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var menu: TextView = itemView.findViewById(R.id.txt_menu)
    }
}

