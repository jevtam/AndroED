package com.example.gson

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter

    private val picViewerResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val picUrl = data?.getStringExtra("picUrl")
            val isFavorite = data?.getBooleanExtra("isFavorite", false) ?: false

            if (isFavorite && picUrl != null) {
                Snackbar.make(
                    recyclerView, "Картинка добавлена в избранное", Snackbar.LENGTH_LONG
                ).setAction("Открыть") {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(picUrl))
                    startActivity(browserIntent)
                }.show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val photoUrls = listOf(
            "https://farm66.staticflickr.com/65535/51182261779_0c3c64a8db_z.jpg",
            "https://farm66.staticflickr.com/65535/51181413271_8b1d9a6243_z.jpg",
            "https://farm66.staticflickr.com/65535/51181228281_46671aaf4b_z.jpg",
            "https://farm66.staticflickr.com/65535/51180669862_2b46bda8ff_z.jpg",
            "https://farm66.staticflickr.com/65535/51180537716_7b25ef5f1e_z.jpg"
        )

        photoAdapter = PhotoAdapter(photoUrls) { photoUrl ->
            val intent = Intent(this, PicViewer::class.java)
            intent.putExtra("picUrl", photoUrl)
            picViewerResultLauncher.launch(intent)
        }
        recyclerView.adapter = photoAdapter
    }
}