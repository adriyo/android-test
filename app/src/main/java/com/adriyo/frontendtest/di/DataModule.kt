package com.adriyo.frontendtest.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.adriyo.frontendtest.data.db.dao.PhotoDao
import com.adriyo.frontendtest.data.db.dao.UserDao
import com.adriyo.frontendtest.data.db.entity.PhotoEntity
import com.adriyo.frontendtest.data.db.mapper.EntityMapper
import com.adriyo.frontendtest.data.remote.PhotoApi
import com.adriyo.frontendtest.data.remote.PhotoRemoteMediator
import com.adriyo.frontendtest.data.repository.AuthRepository
import com.adriyo.frontendtest.data.repository.PhotoRepository
import com.adriyo.frontendtest.shared.Resource
import com.adriyo.frontendtest.shared.Validator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext applicationContext: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "data_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun providePhotoDao(appDatabase: AppDatabase): PhotoDao {
        return appDatabase.photoDao()
    }

    @Provides
    @Singleton
    fun providePhotoApi(retrofit: Retrofit): PhotoApi {
        return retrofit.create(PhotoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideValidator(
        resource: Resource,
    ): Validator {
        return Validator(resource = resource)
    }

    @Provides
    @Singleton
    fun provideResource(
        @ApplicationContext applicationContext: Context,
    ): Resource {
        return Resource(context = applicationContext)
    }

    @Provides
    @Singleton
    fun provideEntityMapper(): EntityMapper {
        return EntityMapper()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        entityMapper: EntityMapper,
        resource: Resource,
    ): AuthRepository {
        return AuthRepository(
            userDao = userDao,
            entityMapper = entityMapper,
            resource = resource
        )
    }

    @Provides
    @Singleton
    fun providePhotoPager(
        appDatabase: AppDatabase,
        entityMapper: EntityMapper,
        photoApi: PhotoApi,
    ): Pager<Int, PhotoEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PhotoRemoteMediator(
                appDatabase = appDatabase,
                mapper = entityMapper,
                photoApi = photoApi,
            ),
            pagingSourceFactory = {
                appDatabase.photoDao().getPhotosPaged()
            }
        )
    }

    @Provides
    @Singleton
    fun providePhotoRepository(pager: Pager<Int, PhotoEntity>, entityMapper: EntityMapper, photoDao: PhotoDao ): PhotoRepository {
        return PhotoRepository(
            pager = pager,
            entityMapper = entityMapper,
            photoDao = photoDao
        )
    }

}