package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast

class MainActivity : AppCompatActivity(), CellClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val colors = arrayListOf(
            ColorData("Red", android.graphics.Color.RED),
            ColorData("Green", android.graphics.Color.GREEN),
            ColorData("Blue", android.graphics.Color.BLUE),
            ColorData("Yellow", android.graphics.Color.YELLOW),
            ColorData("Cyan", android.graphics.Color.CYAN)
        )

        recyclerView.adapter = Adapter(this, colors, this)
    }

    override fun onCellClickListener(colorName: String) {
        Toast.makeText(this, "IT'S $colorName", Toast.LENGTH_SHORT).show()
    }
}