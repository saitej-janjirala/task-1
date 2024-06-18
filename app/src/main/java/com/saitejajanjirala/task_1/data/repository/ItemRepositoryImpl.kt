package com.saitejajanjirala.task_1.data.repository

import com.saitejajanjirala.task_1.data.network.ApiService
import com.saitejajanjirala.task_1.domain.models.Item
import com.saitejajanjirala.task_1.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.saitejajanjirala.task_1.domain.models.Result
import java.net.UnknownHostException


class ItemRepositoryImpl @Inject constructor(private val apiService: ApiService) : ItemRepository {
    override fun getItems(): Flow<Result<List<Item>>> = flow {
        emit(Result.Loading())
        try {
            val fetchedItems = apiService.getItems()
            val filteredItems = fetchedItems.filter {
                !it.name.isNullOrBlank() && it.listId != null
            }
            emit(Result.Success(filteredItems))
        }  catch (e: UnknownHostException) {
            emit(Result.Error("Couldn't reach server. Check your internet connection."))
        }catch (e: Exception) {
            emit(Result.Error("An unexpected error occurred"))
        }
    }
}
