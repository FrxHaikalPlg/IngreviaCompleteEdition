package com.frxhaikal_plg.ingrevia.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import com.frxhaikal_plg.ingrevia.MainActivity
import com.frxhaikal_plg.ingrevia.R
import com.frxhaikal_plg.ingrevia.data.local.UserPreferences
import com.frxhaikal_plg.ingrevia.data.network.NetworkResult
import com.frxhaikal_plg.ingrevia.data.repository.AuthRepository
import com.frxhaikal_plg.ingrevia.databinding.ActivityLoginBinding
import com.frxhaikal_plg.ingrevia.ui.register.RegisterActivity
import com.frxhaikal_plg.ingrevia.ui.introduction.IntroductionActivity
import com.frxhaikal_plg.ingrevia.data.remote.api.RetrofitClient
import com.frxhaikal_plg.ingrevia.data.remote.api.ApiService
import com.frxhaikal_plg.ingrevia.data.remote.source.RemoteDataSource
import com.frxhaikal_plg.ingrevia.ui.dialog.UserInfoDialog
import com.frxhaikal_plg.ingrevia.ui.forgotpassword.ForgotPasswordActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userPreferences = UserPreferences(this)
        
        setupViewModel()
        setupButtons()
        observeLoginState()
    }

    private fun setupViewModel() {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(ApiService::class.java)
        val remoteDataSource = RemoteDataSource(apiService)
        val repository = AuthRepository(remoteDataSource, userPreferences)
        viewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(repository)
        )[LoginViewModel::class.java]
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            viewModel.loginState.collect { result ->
                when (result) {
                    is NetworkResult.Loading -> setLoading(true)
                    is NetworkResult.Success -> {
                        setLoading(false)
                        checkUserInfo()
                    }
                    is NetworkResult.Error -> {
                        setLoading(false)
                        showError(result.message)
                    }
                    null -> { /* Initial state */ }
                }
            }
        }
    }

    private fun checkUserInfo() {
        lifecycleScope.launch {
            val hasUserInfo = userPreferences.hasUserInfo.first()
            if (hasUserInfo) {
                navigateToMain()
            } else {
                UserInfoDialog().show(supportFragmentManager, UserInfoDialog.TAG)
            }
        }
    }

    private fun setupButtons() {
        binding.backArrow.setOnClickListener {
            startActivity(Intent(this, IntroductionActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }
        
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.forgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
    
    private fun isInputValid(): Boolean {
        return binding.edLoginEmail.error == null && 
               binding.edLoginPassword.error == null &&
               binding.edLoginEmail.text?.isNotEmpty() == true &&
               binding.edLoginPassword.text?.isNotEmpty() == true
    }
    
    private fun handleLogin() {
        if (!isInputValid()) {
            Toast.makeText(this, getString(R.string.please_complete_login), Toast.LENGTH_SHORT).show()
            return
        }

        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()
        viewModel.login(email, password)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setLoading(isLoading: Boolean) {
        binding.loading.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.loading.loadingText.text = getString(R.string.logging_in)
        }
        binding.btnLogin.isEnabled = !isLoading
        binding.edLoginEmail.isEnabled = !isLoading
        binding.edLoginPassword.isEnabled = !isLoading
    }
}