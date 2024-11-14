package com.example.gson

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide

class PicViewer : AppCompatActivity() {

    private var picUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pic_viewer)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Картинка"

        picUrl = intent.getStringExtra("picUrl")
        val imageView = findViewById<ImageView>(R.id.picView)
        picUrl?.let {
            Glide.with(this)
                .load(it)
                .into(imageView)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorite -> {
                Toast.makeText(this, "Добавлено в Избранное", Toast.LENGTH_SHORT).show()

                val resultIntent = Intent()
                resultIntent.putExtra("picUrl", picUrl)
                resultIntent.putExtra("isFavorite", true)
                setResult(RESULT_OK, resultIntent)

                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}