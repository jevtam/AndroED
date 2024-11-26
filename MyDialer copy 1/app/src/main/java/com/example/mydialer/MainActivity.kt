package com.example.mydialer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydialer.databinding.ActivityMainBinding
import Contact

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: Adapter
    private val contacts = mutableListOf<Contact>()

    private val sharedPreferences by lazy {
        getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = Adapter { contact ->
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${contact.phone}")
            }
            startActivity(intent)
        }
        binding.rView.layoutManager = LinearLayoutManager(this)
        binding.rView.adapter = adapter

        val savedFilter = sharedPreferences.getString("SEARCH_FILTER", "") ?: ""
        binding.etSearch.setText(savedFilter)
        filterContacts(savedFilter)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterContacts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {
                sharedPreferences.edit().putString("SEARCH_FILTER", s.toString()).apply()
            }
        })
    }

    private fun filterContacts(query: String) {
        val filteredContacts = contacts.filter { it.name.contains(query, ignoreCase = true) }
        adapter.submitList(filteredContacts)
    }
}