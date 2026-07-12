package com.example.feature.auth.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
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

    private fun validateInput() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()

        val isValid = email.isNotEmpty() && password.isNotEmpty() && emailPattern.matches(email)
        val isNotLoading = viewModel.authState.value !is Resource.Loading
        binding.btnLogin.isEnabled = isValid && isNotLoading
    }

    private fun setupListeners() {
        // Cyrillic Input Filter for Email
        val cyrillicFilter = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                val c = source[i]
                // Cyrillic unicode block ranges
                if (c.code in 0x0400..0x04FF || c.code in 0x0500..0x052F) {
                    return@InputFilter ""
                }
            }
            null
        }
        binding.etEmail.filters = arrayOf(cyrillicFilter)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateInput()
            }
        }
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)

        // Run validation on start
        validateInput()

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
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/"))
            startActivity(intent)
        }

        binding.btnOK.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ok.ru/"))
            startActivity(intent)
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
                            binding.btnLogin.text = "Вход"
                            Toast.makeText(requireContext(), "Добро пожаловать!", Toast.LENGTH_SHORT).show()
                            navigateToMain()
                        }
                        is Resource.Error -> {
                            binding.btnLogin.text = "Вход"
                            validateInput()
                        }
                        else -> {
                            binding.btnLogin.text = "Вход"
                            validateInput()
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
