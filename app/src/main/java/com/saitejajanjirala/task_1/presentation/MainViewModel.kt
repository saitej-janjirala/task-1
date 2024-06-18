package com.saitejajanjirala.task_1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saitejajanjirala.task_1.domain.models.GroupedItemsWrapper
import com.saitejajanjirala.task_1.domain.models.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.saitejajanjirala.task_1.domain.models.Result
import com.saitejajanjirala.task_1.domain.usecases.GetItemsUseCase


@HiltViewModel
class MainViewModel @Inject constructor(val getItemsUseCase: GetItemsUseCase): ViewModel() {

    private val _items = MutableStateFlow<Result<GroupedItemsWrapper>>(Result.Loading())
    val items: StateFlow<Result<GroupedItemsWrapper>> = _items

    init {
        fetchItems()
    }

    fun fetchItems() {
        viewModelScope.launch {
            getItemsUseCase().collect { result ->
                _items.value = result
            }
        }
    }



}