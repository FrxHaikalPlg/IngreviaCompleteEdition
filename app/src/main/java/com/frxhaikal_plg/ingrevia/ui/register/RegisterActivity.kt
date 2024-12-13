package com.frxhaikal_plg.ingrevia.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import com.frxhaikal_plg.ingrevia.data.network.NetworkResult
import com.frxhaikal_plg.ingrevia.data.remote.api.ApiService
import com.frxhaikal_plg.ingrevia.data.remote.api.RetrofitClient
import com.frxhaikal_plg.ingrevia.data.remote.source.RemoteDataSource
import com.frxhaikal_plg.ingrevia.data.repository.AuthRepository
import com.frxhaikal_plg.ingrevia.databinding.ActivityRegisterBinding
import com.frxhaikal_plg.ingrevia.ui.UserInformationActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupViewModel()
        setupButtons()
        observeRegisterState()
    }
    
    private fun setupViewModel() {
        val userPreferences = UserPreferences(this)
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(ApiService::class.java)
        val remoteDataSource = RemoteDataSource(apiService)
        val repository = AuthRepository(remoteDataSource, userPreferences)
        viewModel = ViewModelProvider(
            this,
            RegisterViewModelFactory(repository)
        )[RegisterViewModel::class.java]
    }

    private fun setupButtons() {
        binding.backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            if (!isInputValid()) {
                Toast.makeText(this, getString(R.string.please_complete_register), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val password = binding.edRegisterPassword.text.toString()
            val confirmPassword = binding.edRegisterConfirmPassword.text.toString()
            
            if (password != confirmPassword) {
                binding.edRegisterConfirmPassword.error = getString(R.string.password_not_match)
                return@setOnClickListener
            } else {
                binding.edRegisterConfirmPassword.error = null
            }
            
            handleRegister()
        }
        
        binding.loginHere.setOnClickListener {
            finish()
        }
    }
    
    private fun observeRegisterState() {
        lifecycleScope.launch {
            viewModel.registerState.collect { result ->
                when (result) {
                    is NetworkResult.Loading -> setLoading(true)
                    is NetworkResult.Success -> {
                        setLoading(false)
                        if (result.data.success) {
                            Toast.makeText(
                                this@RegisterActivity,
                                getString(R.string.register_success_please_login),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            showError(result.data.message)
                        }
                    }
                    is NetworkResult.Error -> {
                        setLoading(false)
                        showError(result.message)
                    }
                    null -> { /* Initial state, do nothing */ }
                }
            }
        }
    }
    
    private fun isInputValid(): Boolean {
        return binding.edRegisterEmail.error == null && 
               binding.edRegisterPassword.error == null &&
               binding.edRegisterConfirmPassword.error == null &&
               binding.edRegisterName.error == null &&
               binding.edRegisterEmail.text?.isNotEmpty() == true &&
               binding.edRegisterPassword.text?.isNotEmpty() == true &&
               binding.edRegisterConfirmPassword.text?.isNotEmpty() == true &&
               binding.edRegisterName.text?.isNotEmpty() == true
    }
    
    private fun handleRegister() {
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        val name = binding.edRegisterName.text.toString()
        viewModel.register(email, password, name)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.loading.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.loading.loadingText.text = getString(R.string.registering)
        }
        binding.registerButton.isEnabled = !isLoading
        binding.edRegisterName.isEnabled = !isLoading
        binding.edRegisterEmail.isEnabled = !isLoading
        binding.edRegisterPassword.isEnabled = !isLoading
        binding.edRegisterConfirmPassword.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}