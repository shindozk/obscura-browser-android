package com.obscura.browser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.obscura.browser.R

class BrowserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_browser, container, false)

        // Set up URL bar
        val urlInput = view.findViewById<android.widget.EditText>(R.id.url_edit_text)
        urlInput.setOnKeyListener { _, key, _ ->
            if (key.code == android.view.KeyEvent.KEYCODE_ENTER) {
                val url = urlInput.text.toString()
                // TODO: Load URL in web view
                true
            } else {
                false
            }
        }

        // Set up FAB for new tab
        val fabNewTab = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab_new_tab)
        fabNewTab.setOnClickListener {
            // Navigate to new tab
        }

        return view
    }
}