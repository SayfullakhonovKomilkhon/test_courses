package com.example.presentation

import com.example.core.common.model.Course
import com.example.databinding.ItemCategoryBinding
import com.example.databinding.ItemCourseBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

fun courseAdapterDelegate(
    onFavoriteClick: (Course) -> Unit,
    onCourseClick: (Course) -> Unit
) = adapterDelegateViewBinding<Course, Any, ItemCourseBinding>(
    { layoutInflater, parent -> ItemCourseBinding.inflate(layoutInflater, parent, false) }
) {
    binding.root.setOnClickListener {
        onCourseClick(item)
    }

    binding.ivFavorite.setOnClickListener {
        onFavoriteClick(item)
    }

    bind {
        binding.tvCourseTitle.text = item.title
        binding.tvCourseDesc.text = item.description
        binding.tvCourseCategory.text = item.category
        binding.tvRating.text = item.rating.toString()
        binding.tvDate.text = item.date
        binding.tvCoursePrice.text = item.price

        try {
            val context = binding.root.context
            if (item.title.contains("Java", ignoreCase = true)) {
                binding.ivCourseImage.setImageResource(com.example.core.common.R.drawable.img_java_course)
            } else if (item.title.contains("3D", ignoreCase = true) || item.title.contains("дженералист", ignoreCase = true)) {
                binding.ivCourseImage.setImageResource(com.example.core.common.R.drawable.img_design_course)
            } else if (item.title.contains("Python", ignoreCase = true) || item.title.contains("продвинутых", ignoreCase = true)) {
                binding.ivCourseImage.setImageResource(com.example.core.common.R.drawable.img_python_course)
            } else {
                val backgroundDrawable = androidx.core.content.ContextCompat.getDrawable(
                    context,
                    com.example.core.common.R.drawable.bg_course_image
                )?.mutate()
                backgroundDrawable?.setTint(android.graphics.Color.parseColor(item.imageBgColor))
                binding.ivCourseImage.setImageDrawable(backgroundDrawable)
            }
        } catch (e: Exception) {
            binding.ivCourseImage.setImageDrawable(null)
            binding.ivCourseImage.setBackgroundColor(android.graphics.Color.GRAY)
        }

        if (item.isFavorite) {
            binding.ivFavorite.setImageResource(com.example.core.common.R.drawable.ic_bookmark_filled)
        } else {
            binding.ivFavorite.setImageResource(com.example.core.common.R.drawable.ic_bookmark_outline)
        }
    }
}

fun categoryAdapterDelegate(
    selectedCategoryProvider: () -> String,
    onCategoryClick: (String) -> Unit
) = adapterDelegateViewBinding<String, Any, ItemCategoryBinding>(
    { layoutInflater, parent -> ItemCategoryBinding.inflate(layoutInflater, parent, false) }
) {
    binding.root.setOnClickListener {
        onCategoryClick(item)
    }

    bind {
        binding.tvCategoryName.text = item
        val isSelected = item == selectedCategoryProvider()
        if (isSelected) {
            binding.tvCategoryName.setBackgroundResource(com.example.core.common.R.drawable.bg_badge_green)
        } else {
            binding.tvCategoryName.setBackgroundResource(com.example.core.common.R.drawable.bg_badge_grey)
        }
    }
}

class CoursesAdapter(
    onFavoriteClick: (Course) -> Unit,
    onCourseClick: (Course) -> Unit
) : ListDelegationAdapter<List<Any>>() {
    init {
        delegatesManager.addDelegate(courseAdapterDelegate(onFavoriteClick, onCourseClick))
    }
}

class CategoriesAdapter(
    selectedCategoryProvider: () -> String,
    onCategoryClick: (String) -> Unit
) : ListDelegationAdapter<List<Any>>() {
    init {
        delegatesManager.addDelegate(categoryAdapterDelegate(selectedCategoryProvider, onCategoryClick))
    }
}
