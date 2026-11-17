package com.obscura.browser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.obscura.browser.R

class TabFragment : Fragment() {

    private lateinit var adapter: TabAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_tabs, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.tab_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        recyclerView.setHasFixedSize(true)

        adapter = TabAdapter(requireContext())
        recyclerView.adapter = adapter

        return view
    }
}