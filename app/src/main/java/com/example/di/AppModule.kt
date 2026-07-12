package com.example.di

import com.example.data.repository.CourseRepositoryImpl
import com.example.domain.repository.CourseRepository
import com.example.domain.usecase.GetCoursesUseCase
import com.example.domain.usecase.GetFavoritesUseCase
import com.example.domain.usecase.ToggleFavoriteUseCase
import com.example.presentation.CatalogViewModel
import com.example.presentation.FavoritesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<CourseRepository> { CourseRepositoryImpl(api = get(), dao = get()) }
    single { GetCoursesUseCase(repository = get()) }
    single { GetFavoritesUseCase(repository = get()) }
    single { ToggleFavoriteUseCase(repository = get()) }
    viewModel { CatalogViewModel(getCoursesUseCase = get(), toggleFavoriteUseCase = get()) }
    viewModel { FavoritesViewModel(getFavoritesUseCase = get(), toggleFavoriteUseCase = get()) }
}
