package com.example.mydialer

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import com.example.mydialer.Contact
import com.example.mydialer.ContactAdapter

class MainActivity : AppCompatActivity() {
    private val contacts = mutableListOf<Contact>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        val searchEditText = findViewById<EditText>(R.id.et_search)
        val searchButton = findViewById<Button>(R.id.btn_search)
        recyclerView = findViewById(R.id.rView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchContacts()

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString().trim()
            val filteredContacts = if (query.isEmpty()) {
                contacts
            } else {
                contacts.filter { it.name.contains(query, ignoreCase = true) }
            }
            contactAdapter = ContactAdapter(filteredContacts)
            recyclerView.adapter = contactAdapter
        }
    }

    private fun fetchContacts() {
        val json = assets.open("contacts.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        contacts.clear()
        contacts.addAll(gson.fromJson(json, Array<Contact>::class.java).toList())
        Timber.d("Contacts loaded: ${contacts.size}")

        runOnUiThread {
            contactAdapter = ContactAdapter(contacts)
            recyclerView.adapter = contactAdapter
        }
    }
}