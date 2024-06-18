package com.saitejajanjirala.task_1.domain.usecases

import com.saitejajanjirala.task_1.domain.models.GroupedItemsWrapper
import com.saitejajanjirala.task_1.domain.models.Item
import com.saitejajanjirala.task_1.domain.models.Result
import com.saitejajanjirala.task_1.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.SortedMap
import java.util.regex.Matcher
import java.util.regex.Pattern

class GetItemsUseCase (private val itemRepository: ItemRepository) {
    operator fun invoke(): Flow<Result<GroupedItemsWrapper>> = flow {
        itemRepository.getItems().collect { result ->
            when (result) {
                is Result.Loading -> emit(Result.Loading())
                is Result.Error -> emit(Result.Error(result.message ?: "An unexpected error occurred"))
                is Result.Success -> {
                    val filteredItems = result.data ?: emptyList()
                    val groupedItems = filteredItems.groupBy { it.listId!! }
                    val sortedGroupedItems = groupedItems.mapValues { (_, items) ->
                        items.sortedWith(compareBy(NaturalOrderComparator(), { it.name!! }))
                    }.toSortedMap()
                    emit(Result.Success(GroupedItemsWrapper(sortedGroupedItems)))
                }
            }
        }
    }
}
class NaturalOrderComparator : Comparator<String> {
    override fun compare(a: String, b: String): Int {
        val splitPattern = Pattern.compile("([0-9]+|\\D+)")
        val aParts = splitPattern.matcher(a).toList()
        val bParts = splitPattern.matcher(b).toList()

        val length = Math.min(aParts.size, bParts.size)
        for (i in 0 until length) {
            val aPart = aParts[i]
            val bPart = bParts[i]
            val result = if (aPart is Int && bPart is Int) {
                (aPart as Int).compareTo(bPart as Int)
            } else {
                aPart.toString().compareTo(bPart.toString())
            }
            if (result != 0) return result
        }
        return aParts.size.compareTo(bParts.size)
    }

    private fun Matcher.toList(): List<Any> {
        val list = mutableListOf<Any>()
        while (find()) {
            val group = group()
            list.add(group.toIntOrNull() ?: group)
        }
        return list
    }
}