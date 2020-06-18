package acerezo.android.viewmodeltest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class MyViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    constructor() : this(Dispatchers.IO, Dispatchers.Main)

    private val _uiState = MutableLiveData<UiState>()

    val uiState: LiveData<UiState>
        get() = _uiState

    fun fetchData() {
        _uiState.value = Loading
        viewModelScope.launch(ioDispatcher) {
            delay(300) // Do the network request here
            withContext(mainDispatcher) {
                _uiState.value = Success
            }
        }
    }

}

sealed class UiState
object Loading : UiState()
object Success : UiState()
object Error : UiState()
