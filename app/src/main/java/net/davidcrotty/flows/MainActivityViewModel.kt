package net.davidcrotty.flows

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    val onBoardingChannel: ReceiveChannel<Boolean>
        get() = _onBoardingChannel
    private val _onBoardingChannel = Channel<Boolean>()

    // TODO call via data binding
    fun onBoardingCommand() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = longRunningTask()
            _onBoardingChannel.send(result)
        }
    }

    private suspend fun longRunningTask(): Boolean {
        delay(5000)
        return true
    }

    override fun onCleared() {
        _onBoardingChannel.close()
    }
}