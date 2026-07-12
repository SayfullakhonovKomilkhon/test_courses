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
import com.example.feature.auth.R
import com.example.feature.auth.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(email, password)
        }

        binding.tvRegisterLink.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(requireContext(), "Ссылка для восстановления отправлена на почту", Toast.LENGTH_SHORT).show()
        }

        binding.btnVK.setOnClickListener {
            Toast.makeText(requireContext(), "Вход через VK...", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }

        binding.btnOK.setOnClickListener {
            Toast.makeText(requireContext(), "Вход через OK...", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collectLatest { state ->
                    when (state) {
                        is Resource.Loading -> {
                            binding.btnLogin.isEnabled = false
                            binding.btnLogin.text = "Загрузка..."
                        }
                        is Resource.Success -> {
                            binding.btnLogin.isEnabled = true
                            binding.btnLogin.text = "Вход"
                            Toast.makeText(requireContext(), "Добро пожаловать!", Toast.LENGTH_SHORT).show()
                            navigateToMain()
                        }
                        is Resource.Error -> {
                            binding.btnLogin.isEnabled = true
                            binding.btnLogin.text = "Вход"
                        }
                        else -> {
                            binding.btnLogin.isEnabled = true
                            binding.btnLogin.text = "Вход"
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
