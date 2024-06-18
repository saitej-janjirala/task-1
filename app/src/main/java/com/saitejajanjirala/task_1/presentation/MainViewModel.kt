package com.saitejajanjirala.task_1.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saitejajanjirala.task_1.domain.models.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.saitejajanjirala.task_1.domain.models.Result
import com.saitejajanjirala.task_1.domain.usecases.GetItemsUseCase
import kotlinx.coroutines.Dispatchers
import java.util.SortedMap


@HiltViewModel
class MainViewModel @Inject constructor(val getItemsUseCase: GetItemsUseCase): ViewModel() {

    private val _items = MutableStateFlow<Result<SortedMap<String, List<Item>>>>(Result.Loading())
    val items: StateFlow<Result<SortedMap<String, List<Item>>>> = _items

    init {
        fetchItems()
    }

    fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getItemsUseCase.invoke().collect {
                _items.value = it
            }
        }
    }



}