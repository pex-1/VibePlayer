package com.example.vibeplayer.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibeplayer.core.domain.SongRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update


@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: SongRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    init {
        observeQuery()
    }

    private fun observeQuery() {
        _state
            .map { it.query }
            .debounce(400)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.isBlank()) {
                    _state.update {
                        it.copy(query = "", isLoading = false)
                    }
                } else {
                    val results = repository.searchSongs(query)
                    _state.update {
                        it.copy(
                            query = query, isLoading = false,
                            songs = results
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SearchActions) {
        when (action) {
            is SearchActions.OnQueryChanged -> {
                _state.update {
                    it.copy(query = action.query, isLoading = true)
                }
            }

            is SearchActions.OnClearClicked -> _state.value = SearchState()

            else -> {}
        }
    }
}