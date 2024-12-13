package com.frxhaikal_plg.ingrevia.ui.forgotpassword

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
import com.frxhaikal_plg.ingrevia.databinding.ActivityForgotPasswordBinding
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupButtons()
        observeForgotPasswordState()
    }

    private fun setupViewModel() {
        val userPreferences = UserPreferences(this)
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(ApiService::class.java)
        val remoteDataSource = RemoteDataSource(apiService)
        val repository = AuthRepository(remoteDataSource, userPreferences)
        viewModel = ViewModelProvider(
            this,
            ForgotPasswordViewModelFactory(repository)
        )[ForgotPasswordViewModel::class.java]
    }

    private fun setupButtons() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }

        binding.btnforgot.setOnClickListener {
            handleForgotPassword()
        }
    }

    private fun handleForgotPassword() {
        if (!isInputValid()) {
            Toast.makeText(this, getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show()
            return
        }

        val email = binding.edLoginEmail.text.toString()
        viewModel.forgotPassword(email)
    }

    private fun observeForgotPasswordState() {
        lifecycleScope.launch {
            viewModel.forgotPasswordState.collect { result ->
                when (result) {
                    is NetworkResult.Loading -> setLoading(true)
                    is NetworkResult.Success -> {
                        setLoading(false)
                        if (result.data.success) {
                            result.data.data?.let { data ->
                                showResetPasswordBottomSheet(data.resetLink)
                            }
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
        return binding.edLoginEmail.error == null && 
               binding.edLoginEmail.text?.isNotEmpty() == true
    }

    private fun setLoading(isLoading: Boolean) {
        binding.loading.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.loading.loadingText.text = getString(R.string.sending_reset_link)
        }
        binding.btnforgot.isEnabled = !isLoading
        binding.edLoginEmail.isEnabled = !isLoading
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showResetPasswordBottomSheet(resetLink: String) {
        ResetPasswordBottomSheet(resetLink).show(
            supportFragmentManager,
            ResetPasswordBottomSheet.TAG
        )
    }
}