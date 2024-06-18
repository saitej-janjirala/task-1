package com.saitejajanjirala.task_1.domain.models

import java.util.SortedMap

data class GroupedItemsWrapper(
    val groupedItems: SortedMap<String, List<Item>>
)