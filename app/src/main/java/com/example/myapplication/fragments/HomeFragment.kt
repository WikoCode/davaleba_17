package com.example.myapplication.fragments

import android.content.Context
import android.content.SharedPreferences
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.fragments.basefragment.BaseFragment


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var sharedPreferences: SharedPreferences


    override fun setupUI() {
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)

        displayEmail()
        handleAuthenticationStatus()

    }

    override fun setupListeners() {
        logOut()
    }

    private fun logOut() {
        binding.btnLogOut.setOnClickListener {

            sharedPreferences.edit().clear().apply()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    private fun displayEmail() {
        binding.tvEmail.text = arguments?.getString("email")
    }

    private fun isUserAuthenticated(): Boolean {
        val authToken = sharedPreferences.getString("Token", null)

        return !authToken.isNullOrEmpty()
    }

    private fun handleAuthenticationStatus() {
        if (isUserAuthenticated()) {
            displayEmail()
        } else {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }


}