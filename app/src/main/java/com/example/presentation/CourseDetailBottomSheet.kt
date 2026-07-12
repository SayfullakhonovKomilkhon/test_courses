package com.example.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.databinding.FragmentCourseDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CourseDetailBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString(ARG_TITLE) ?: ""
        val category = arguments?.getString(ARG_CATEGORY) ?: ""
        val desc = arguments?.getString(ARG_DESC) ?: ""

        binding.tvDetailTitle.text = title
        binding.tvDetailCategory.text = category
        binding.tvDetailDesc.text = desc

        binding.btnStartLearning.setOnClickListener {
            Toast.makeText(requireContext(), "Обучение начато!", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_CATEGORY = "arg_category"
        private const val ARG_DESC = "arg_desc"

        fun newInstance(title: String, category: String, desc: String): CourseDetailBottomSheet {
            return CourseDetailBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_CATEGORY, category)
                    putString(ARG_DESC, desc)
                }
            }
        }
    }
}
