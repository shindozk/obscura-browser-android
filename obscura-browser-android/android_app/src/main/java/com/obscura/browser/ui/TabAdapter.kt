package com.obscura.browser.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.obscura.browser.R

class TabAdapter(private val context: android.content.Context) : RecyclerView.Adapter<TabViewHolder>() {

    private val tabIcons = intArrayOf(
        R.drawable.ic_tab_1,
        R.drawable.ic_tab_2,
        R.drawable.ic_tab_3,
        R.drawable.ic_tab_4
    )

    private val tabTitles = arrayOf(
        "1",
        "2",
        "3",
        "4"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false)
        return TabViewHolder(view)
    }

    override fun getItemCount(): Int = tabIcons.size

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        holder.bind(position)
    }
}

class TabViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val iconView: android.widget.ImageView = view.findViewById(R.id.tab_icon)
    private val titleView: android.widget.TextView = view.findViewById(R.id.tab_title)

    fun bind(position: Int) {
        iconView.setImageResource(TabAdapter.tabIcons[position])
        titleView.text = TabAdapter.tabTitles[position]
    }
}