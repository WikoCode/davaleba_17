package com.example.myapplication.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myapplication.LoginResponse
import com.example.myapplication.R
import com.example.myapplication.common.Resource
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.fragments.basefragment.BaseFragment
import com.example.myapplication.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences


    override fun setupUI() {
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        observeLoginResult()
        activeSession()
    }

    override fun setupListeners() {
        goToRegister()
        setupLogin()
    }


    private fun goToRegister() {
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun goToHome() {
        val bundle = Bundle().apply {
            putString("email", binding.etEmail.text.toString())
        }
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment, bundle)
    }


    private fun handleLoginButtonClick() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (validateCredentials(email, password)) {
            viewModel.login(email, password)
            goToHome()
        }
    }

    private fun setupLogin() {
        binding.btnLogin.setOnClickListener {
            handleLoginButtonClick()
        }
    }


    private fun validateCredentials(email: String, password: String): Boolean {
        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            Toast.makeText(context, "Email is incorrect", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(context, "Password required", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun observeLoginResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { saveAuthentication(it) }
                            goToHome()
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                context,
                                result.errorMessage ?: "Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Resource.Loading -> {
                            binding.progressBar.visibility =
                                if (result.loading) View.VISIBLE else View.GONE
                        }

                        else -> {}
                    }
                }
            }
        }
    }


    private fun activeSession() {
        if (ifActiveToken()) {
            goToHome()
        }
    }

    private fun saveAuthentication(response: LoginResponse) {
        val boxChecked = binding.cbRemember.isChecked
        with(sharedPreferences.edit()) {
            putBoolean("Remember", boxChecked)
            putString("Token", response.token)
            apply()
        }
    }


    private fun ifActiveToken(): Boolean {
        val token = sharedPreferences.getString("Token", "")
        return !(token.isNullOrBlank())
    }

}