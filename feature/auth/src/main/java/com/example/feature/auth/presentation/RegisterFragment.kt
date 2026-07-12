package com.example.feature.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core.common.Resource
import com.example.feature.auth.databinding.FragmentRegisterBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val repeatPassword = binding.etRepeatPassword.text.toString().trim()
            viewModel.register(email, password, repeatPassword)
        }

        binding.tvLoginLink.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnVK.setOnClickListener {
            Toast.makeText(requireContext(), "Регистрация через VK...", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }

        binding.btnOK.setOnClickListener {
            Toast.makeText(requireContext(), "Регистрация через OK...", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collectLatest { state ->
                    when (state) {
                        is Resource.Loading -> {
                            binding.btnRegister.isEnabled = false
                            binding.btnRegister.text = "Создание..."
                        }
                        is Resource.Success -> {
                            binding.btnRegister.isEnabled = true
                            binding.btnRegister.text = "Регистрация"
                            Toast.makeText(requireContext(), "Аккаунт создан!", Toast.LENGTH_SHORT).show()
                            navigateToMain()
                        }
                        is Resource.Error -> {
                            binding.btnRegister.isEnabled = true
                            binding.btnRegister.text = "Регистрация"
                        }
                        else -> {
                            binding.btnRegister.isEnabled = true
                            binding.btnRegister.text = "Регистрация"
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collectLatest { msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToMain() {
        try {
            val intent = Intent(requireContext(), Class.forName("com.example.MainActivity")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            activity?.finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
