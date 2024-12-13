package com.frxhaikal_plg.ingrevia.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.frxhaikal_plg.ingrevia.MainActivity
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import com.frxhaikal_plg.ingrevia.data.local.model.UserInfo
import com.frxhaikal_plg.ingrevia.databinding.ActivityUserInformationBinding
import kotlinx.coroutines.launch

class UserInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserInformationBinding
    private lateinit var userPreferences: UserPreferences
    private var selectedActivityLevel: Int = 0
    private var selectedGender: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userPreferences = UserPreferences(this)
        setupButtons()
        setupRadioGroup()
        setupGenderRadioGroup()
    }
    
    private fun setupRadioGroup() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedActivityLevel = when (checkedId) {
                R.id.radio_basic -> 1
                R.id.radio_simple -> 2
                R.id.radio_standard -> 3
                R.id.radio_advanced -> 4
                R.id.radio_comprehensive -> 5
                else -> 0
            }
        }
    }
    
    private fun setupGenderRadioGroup() {
        binding.genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedGender = when (checkedId) {
                R.id.rbMale -> "male"
                R.id.rbFemale -> "female"
                else -> ""
            }
        }
    }
    
    private fun setupButtons() {
        binding.backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.startedButton.setOnClickListener {
            if (isInputValid()) {
                handleSaveInformation()
            } else {
                Toast.makeText(this, getString(R.string.please_complete_data), Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun isInputValid(): Boolean {
        return binding.edHeight.error == null && 
               binding.edWeight.error == null &&
               binding.edAge.error == null &&
               binding.edHeight.text?.isNotEmpty() == true &&
               binding.edWeight.text?.isNotEmpty() == true &&
               binding.edAge.text?.isNotEmpty() == true &&
               selectedActivityLevel != 0 &&
               selectedGender.isNotEmpty()
    }
    
    private fun handleSaveInformation() {
        lifecycleScope.launch {
            try {
                val userInfo = UserInfo(
                    height = binding.edHeight.text.toString().toInt(),
                    weight = binding.edWeight.text.toString().toInt(),
                    age = binding.edAge.text.toString().toInt(),
                    activityLevel = selectedActivityLevel,
                    gender = selectedGender
                )
                
                userPreferences.saveUserInfo(userInfo)
                
                startActivity(Intent(this@UserInformationActivity, MainActivity::class.java))
                finishAffinity()
            } catch (e: Exception) {
                Toast.makeText(
                    this@UserInformationActivity, 
                    getString(R.string.failed_save_data, e.message), 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}