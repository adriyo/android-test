package com.adriyo.frontendtest.data.db.mapper

import com.adriyo.frontendtest.data.db.entity.PhotoEntity
import com.adriyo.frontendtest.data.db.entity.UserEntity
import com.adriyo.frontendtest.data.model.Photo
import com.adriyo.frontendtest.data.model.User
import com.adriyo.frontendtest.data.remote.PhotoDto

class EntityMapper {
    fun getUser(entity: UserEntity): User {
        return User(
            id = entity.id,
            username = entity.username,
            email = entity.email,
            role = entity.role,
            password = entity.password
        )
    }

    fun getPhotosEntity(photos: List<PhotoDto>): List<PhotoEntity> {
        return photos.map { getPhotoEntity(it) }
    }

    private fun getPhotoEntity(dto: PhotoDto): PhotoEntity {
        return PhotoEntity(
            id = dto.id,
            albumId = dto.albumId,
            title = dto.title,
            url = dto.url,
            thumbnailUrl = dto.thumbnailUrl
        )
    }

    fun getPhoto(entity: PhotoEntity): Photo {
        return Photo(
            id = entity.id,
            albumId = entity.albumId,
            title = entity.title,
            url = entity.url,
            thumbnailUrl = entity.thumbnailUrl
        )
    }

    fun getUserEntity(user: User): UserEntity {
        return UserEntity(
            id = user.id,
            username = user.username,
            email = user.email,
            role = user.role,
            password = user.password,
        )
    }
}
