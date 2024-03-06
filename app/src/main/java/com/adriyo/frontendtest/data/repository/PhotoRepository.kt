package com.adriyo.frontendtest.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.adriyo.frontendtest.data.db.dao.PhotoDao
import com.adriyo.frontendtest.data.db.entity.PhotoEntity
import com.adriyo.frontendtest.data.db.mapper.EntityMapper
import com.adriyo.frontendtest.data.model.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val pager: Pager<Int, PhotoEntity>,
    private val entityMapper: EntityMapper,
    private val photoDao: PhotoDao,
) {

    fun getPager(): Flow<PagingData<Photo>> = pager.flow.map { pagingData ->
        pagingData.map {
            entityMapper.getPhoto(it)
        }
    }

    suspend fun deleteAll() {
        photoDao.clearAll()
    }

}
