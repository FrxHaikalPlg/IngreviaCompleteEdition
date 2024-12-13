package com.frxhaikal_plg.ingrevia.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.frxhaikal_plg.ingrevia.MainActivity
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import com.frxhaikal_plg.ingrevia.ui.introduction.IntroductionActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var userPreferences: UserPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        userPreferences = UserPreferences(this)
        
        lifecycleScope.launch {
            val isLoggedIn = userPreferences.isLoggedIn.first()
            navigateToNextScreen(isLoggedIn)
        }
    }
    
    private fun navigateToNextScreen(isLoggedIn: Boolean) {
        lifecycleScope.launch {
            val intent = if (isLoggedIn) {
                val hasUserInfo = userPreferences.hasUserInfo.first()
                if (hasUserInfo) {
                    Intent(this@SplashActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashActivity, UserInformationActivity::class.java)
                }
            } else {
                Intent(this@SplashActivity, IntroductionActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }
}
