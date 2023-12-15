package com.example.myapplication.fragments

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.fragments.basefragment.BaseFragment
import com.example.myapplication.PreferencesRepository
import com.example.myapplication.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun setupUI() {
        observeAuthenticationStatus()
    }

    override fun setupListeners() {
        logOut()
    }

    private fun logOut() {
        binding.btnLogOut.setOnClickListener {
            lifecycleScope.launch {
                PreferencesRepository.endSession()
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }
    }

    private fun displayEmail() {
        lifecycleScope.launch {
            PreferencesRepository.readEmail().collect { email ->
                binding.tvEmail.text = email
            }
        }
    }

    private suspend fun isUserAuthenticated(): Boolean {
        return PreferencesRepository.readEmail().first().isNotBlank()
    }
    private fun observeAuthenticationStatus() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (isUserAuthenticated()) {
                    displayEmail()
                } else {
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
            }
        }
    }
}
