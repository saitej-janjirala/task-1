package com.saitejajanjirala.task_1.domain.models

import com.squareup.moshi.Json


data class Item(
    @Json(name = "id")
    var id: String?,
    @Json(name = "listId")
    var listId: String?,
    @Json(name = "name")
    var name: String?
)