package com.adriyo.frontendtest.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by adriyo on 04/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */

interface PhotoApi {

    @GET("photos")
    suspend fun getPhotos(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = 10,
    ): List<PhotoDto>

}