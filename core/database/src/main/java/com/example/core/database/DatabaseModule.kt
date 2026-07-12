package com.example.core.database

import androidx.room.Room
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "courses_database"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppDatabase>().favoriteCourseDao() }
}
