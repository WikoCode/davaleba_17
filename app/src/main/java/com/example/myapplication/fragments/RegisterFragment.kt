package com.example.myapplication.fragments

import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.common.Resource
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.example.myapplication.fragments.basefragment.BaseFragment
import com.example.myapplication.viewmodels.RegisterViewModel
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: RegisterViewModel by viewModels()


    override fun setupUI() {
        observeRegisterResult()
    }

    override fun setupListeners() {
        performRegistration()
        backButton()
    }

    private fun backButton() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeRegisterResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerResult.collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            val bundle = bundleOf(
                                "email" to binding.etEmail.text.toString(),
                                "password" to binding.etPassword.text.toString()
                            )

                            setFragmentResult("Success", bundle)

                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment, bundle)

                            Toast.makeText(
                                requireContext(),
                                "Register successful",
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun performRegistration() {
        binding.btnRegister.setOnClickListener {
            if (!samePasswords(binding.etPassword.text.toString(), binding.etPasswordRepeat.text.toString())) {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        }
    }

    private fun samePasswords(password: String, repeatPassword: String): Boolean {
        return password == repeatPassword
    }


}