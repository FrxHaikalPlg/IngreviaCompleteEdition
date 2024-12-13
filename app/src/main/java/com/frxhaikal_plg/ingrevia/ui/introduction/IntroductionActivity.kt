package com.frxhaikal_plg.ingrevia.ui.introduction

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import com.frxhaikal_plg.ingrevia.databinding.ActivityIntroductionBinding
import com.frxhaikal_plg.ingrevia.ui.login.LoginActivity
import kotlinx.coroutines.launch

class IntroductionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroductionBinding
    private lateinit var userPreferences: UserPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userPreferences = UserPreferences(this)
        
        setupViewPager()
        setupButtons()
    }
    
    private fun setupViewPager() {
        val fragments = listOf(
            Introduction1Fragment(),
            Introduction2Fragment(),
            Introduction3Fragment()
        )
        
        val adapter = IntroductionPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = true
        
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNextButton(position)
            }
            
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                
                // Reset semua dot
                listOf(binding.dot1, binding.dot2, binding.dot3).forEach { dot ->
                    dot.setImageResource(R.drawable.dot_inactive)
                    dot.scaleX = 0.8f
                    dot.scaleY = 0.8f
                    dot.alpha = 0.5f
                }
                
                when (position) {
                    0 -> {
                        // Animasi dot pertama ke dot kedua
                        binding.dot1.apply {
                            setImageResource(R.drawable.dot_active)
                            scaleX = 1.2f - (positionOffset * 0.4f)
                            scaleY = 1.2f - (positionOffset * 0.4f)
                            alpha = 1f - (positionOffset * 0.5f)
                        }
                        
                        binding.dot2.apply {
                            setImageResource(R.drawable.dot_active)
                            scaleX = 0.8f + (positionOffset * 0.4f)
                            scaleY = 0.8f + (positionOffset * 0.4f)
                            alpha = 0.5f + (positionOffset * 0.5f)
                        }
                    }
                    1 -> {
                        // Animasi dot kedua ke dot ketiga
                        binding.dot2.apply {
                            setImageResource(R.drawable.dot_active)
                            scaleX = 1.2f - (positionOffset * 0.4f)
                            scaleY = 1.2f - (positionOffset * 0.4f)
                            alpha = 1f - (positionOffset * 0.5f)
                        }
                        
                        binding.dot3.apply {
                            setImageResource(R.drawable.dot_active)
                            scaleX = 0.8f + (positionOffset * 0.4f)
                            scaleY = 0.8f + (positionOffset * 0.4f)
                            alpha = 0.5f + (positionOffset * 0.5f)
                        }
                    }
                    2 -> {
                        // Dot terakhir
                        binding.dot3.apply {
                            setImageResource(R.drawable.dot_active)
                            scaleX = 1.2f
                            scaleY = 1.2f
                            alpha = 1f
                        }
                    }
                }
            }
        })
    }
    
    private fun setupButtons() {
        binding.nextButton.setOnClickListener {
            when (binding.viewPager.currentItem) {
                2 -> {
                    lifecycleScope.launch {
                        userPreferences.clearUserSession()
                        navigateToLogin()
                    }
                }
                else -> binding.viewPager.currentItem += 1
            }
        }
        
        binding.skipButton.setOnClickListener {
            navigateToLogin()
        }
    }
    
    private fun updateNextButton(position: Int) {
        binding.nextButton.text = if (position == 2) {
            getString(R.string.get_started)
        } else {
            getString(R.string.next)
        }
    }
    
    private fun navigateToLogin() {
        lifecycleScope.launch {
            userPreferences.setLoggedIn(false)
            startActivity(Intent(this@IntroductionActivity, LoginActivity::class.java))
            finish()
        }
    }
}
