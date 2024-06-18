package com.saitejajanjirala.task_1.di

import com.saitejajanjirala.task_1.util.Keys
import com.saitejajanjirala.task_1.data.network.ApiService
import com.saitejajanjirala.task_1.data.repository.ItemRepositoryImpl
import com.saitejajanjirala.task_1.domain.repository.ItemRepository
import com.saitejajanjirala.task_1.domain.usecases.GetItemsUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    companion object{
        @Provides
        @Singleton
        fun providesGetItemsUseCase(itemRepository: ItemRepository) : GetItemsUseCase{
            return GetItemsUseCase(itemRepository)
        }

        @Provides
        @Singleton
        fun providesMoshi(): Moshi {
            return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }

        @Provides
        @Singleton
        fun providesApiService(moshi: Moshi) : ApiService {
            return Retrofit.Builder()
                .baseUrl(Keys.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(ApiService::class.java)
        }

    }

    @Binds
    @Singleton
    abstract fun bindsItemRepository(itemRepositoryImpl: ItemRepositoryImpl) : ItemRepository



}