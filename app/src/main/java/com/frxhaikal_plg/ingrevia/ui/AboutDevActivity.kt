package com.frxhaikal_plg.ingrevia.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.databinding.ActivityAboutDevBinding

class AboutDevActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutDevBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAboutDevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupBackButton()
    }

    private fun setupBackButton() {
        binding.backArrow.setOnClickListener {
            finish()
        }
    }
}
