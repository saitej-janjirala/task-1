package com.saitejajanjirala.task_1.domain.repository

import com.saitejajanjirala.task_1.domain.models.Item
import kotlinx.coroutines.flow.Flow
import com.saitejajanjirala.task_1.domain.models.Result

interface ItemRepository {

    fun getItems(): Flow<Result<List<Item>>>

}