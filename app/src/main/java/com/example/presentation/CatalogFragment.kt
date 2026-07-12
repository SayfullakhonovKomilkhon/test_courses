package com.example.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.common.Resource
import com.example.databinding.FragmentCatalogBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatalogViewModel by viewModel()

    private lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add top padding dynamically based on system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.root.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        setupRecyclerViews()
        setupSearch()
        setupSort()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        coursesAdapter = CoursesAdapter(
            onFavoriteClick = { course ->
                viewModel.toggleFavorite(course)
            },
            onCourseClick = { course ->
                val bottomSheet = CourseDetailBottomSheet.newInstance(
                    title = course.title,
                    category = course.category,
                    desc = course.description
                )
                bottomSheet.show(childFragmentManager, "CourseDetail")
            }
        )

        binding.rvCourses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = coursesAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupSort() {
        binding.tvSort.setOnClickListener {
            viewModel.toggleSort()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSortedByDate.collectLatest { isSorted ->
                    if (isSorted) {
                        binding.tvSort.setTextColor(android.graphics.Color.parseColor("#13C268")) // Active Green
                        binding.tvSort.text = "По дате публикации (убыв.)"
                    } else {
                        binding.tvSort.setTextColor(android.graphics.Color.parseColor("#9A9C9E")) // Inactive Gray
                        binding.tvSort.text = "По умолчанию"
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coursesState.collectLatest { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Can show a loading spinner if desired
                        }
                        is Resource.Success -> {
                            coursesAdapter.items = resource.data
                            coursesAdapter.notifyDataSetChanged()
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
