package com.example.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModel()

    private lateinit var coursesAdapter: CoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Add top padding dynamically based on system bar insets to avoid sticking to notch or top status bar
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.root.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        coursesAdapter = CoursesAdapter(
            onFavoriteClick = { course ->
                viewModel.toggleFavorite(course)
            },
            onCourseClick = { course ->
                val bottomSheet = CourseDetailBottomSheet.newInstance(
                    title = course.title,
                    category = course.category,
                    desc = "Вы добавили этот курс в избранное."
                )
                bottomSheet.show(childFragmentManager, "CourseDetail")
            }
        )

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = coursesAdapter
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favoritesState.collectLatest { list ->
                    if (list.isEmpty()) {
                        binding.tvNoFavorites.visibility = View.VISIBLE
                        binding.rvFavorites.visibility = View.GONE
                    } else {
                        binding.tvNoFavorites.visibility = View.GONE
                        binding.rvFavorites.visibility = View.VISIBLE
                    }
                    coursesAdapter.items = list
                    coursesAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
