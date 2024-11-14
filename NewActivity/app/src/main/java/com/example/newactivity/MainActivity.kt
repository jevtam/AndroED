package com.example.newactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val showPicButton = findViewById<Button>(R.id.btn_show_pic)

        showPicButton.setOnClickListener {
            val intent = Intent(this, PicActivity::class.java)
            intent.putExtra("picLink", "https://example.com/image.jpg") // Укажи свою ссылку на изображение
            startActivity(intent)
        }
    }
}