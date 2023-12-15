package com.example.myapplication.fragments

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
import com.example.myapplication.PreferencesRepository
import com.example.myapplication.R
import com.example.myapplication.common.Resource
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.fragments.basefragment.BaseFragment
import com.example.myapplication.viewmodels.LoginViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel: LoginViewModel by viewModels()


    override fun setupUI() {

        observeLoginResult()
        activeSession()
        fillFromRegister()
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

        else if (password.isEmpty()) {
            Toast.makeText(context, "Password required", Toast.LENGTH_SHORT).show()
            return false
        }

        else if (password.length < 8) {
            Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        else return true
    }

    private fun observeLoginResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { response ->
                                saveAuthentication(response)
                                goToHome()
                            }
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
        lifecycleScope.launch {
            if (PreferencesRepository.readToken().first().isNotBlank() && binding.cbRemember.isChecked) {
                goToHome()
            }
        }
    }


    private fun saveAuthentication(response: LoginResponse) {
        val email = binding.etEmail.text.toString()

        lifecycleScope.launch {
            PreferencesRepository.saveEmailAndToken(email, response.token)
        }
    }


    private fun fillFromRegister() {
        lifecycleScope.launch {
            val email = PreferencesRepository.readEmail().first()
            val password = ""

            binding.etEmail.setText(email)
            binding.etPassword.setText(password)
        }
    }


}