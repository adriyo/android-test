package com.adriyo.frontendtest.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.adriyo.frontendtest.data.db.entity.PhotoEntity
import com.adriyo.frontendtest.data.db.mapper.EntityMapper
import com.adriyo.frontendtest.di.AppDatabase
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by adriyo on 05/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
@OptIn(ExperimentalPagingApi::class)
class PhotoRemoteMediator(
    private val appDatabase: AppDatabase,
    private val mapper: EntityMapper,
    private val photoApi: PhotoApi,
) : RemoteMediator<Int, PhotoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>,
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    delay(2000)
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }
            val photos = photoApi.getPhotos(
                page = page,
                limit = state.config.pageSize
            )

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.photoDao().clearAll()
                }
                appDatabase.photoDao().save(
                    mapper.getPhotosEntity(photos)
                )
            }
            MediatorResult.Success(endOfPaginationReached = photos.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}