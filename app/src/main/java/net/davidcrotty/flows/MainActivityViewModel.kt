package net.davidcrotty.flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val onBoardingChannel = Channel<Boolean>()

    // TODO call via data binding
    fun onBoardingCommand() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = longRunningTask()
            onBoardingChannel.send(result)
        }
    }

    suspend fun longRunningTask() : Boolean {
        delay(8000)
        return true
    }

    override fun onCleared() {
        onBoardingChannel.close()
    }
}