package com.adriyo.frontendtest.di

import com.adriyo.frontendtest.shared.DefaultDispatcherProvider
import com.adriyo.frontendtest.shared.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by adriyo on 06/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
@Module
@InstallIn(SingletonComponent::class)
interface BindModule {

    @Binds
    fun bindsDefaultDispatcherProvider(defaultDispatcherProvider: DefaultDispatcherProvider): DispatcherProvider

}