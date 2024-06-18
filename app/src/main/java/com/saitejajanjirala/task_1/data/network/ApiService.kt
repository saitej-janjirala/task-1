package com.saitejajanjirala.task_1.data.network

import com.saitejajanjirala.task_1.util.Keys
import com.saitejajanjirala.task_1.domain.models.Item
import retrofit2.http.GET

interface ApiService {

    @GET(Keys.GET_ITEMS)
    suspend fun getItems(): List<Item>
}