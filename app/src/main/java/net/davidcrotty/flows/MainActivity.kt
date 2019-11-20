package net.davidcrotty.flows

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            snackBarFlow()
        }
    }

    /**
     * Show snackbar in response to an event rather than state
     */
    private fun snackBarFlow() {
        GlobalScope.launch(Dispatchers.IO) {
            val dataFlow = dataSource()
            withContext(Dispatchers.Main) {
                dataFlow.collect {
                    Snackbar.make(fab, "On boarding complete", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun dataSource(): Flow<Boolean> = flow {
        // simulate network call
        delay(3000)
        val passwordDidReset = true
        emit(passwordDidReset)
    }


    private fun createFlow(): Flow<Int> {
        return flow {
            emit(1)
            emit(2)
            emit(3)
        }.flowOn(Dispatchers.IO) // explicit about where we run
    }

    private fun doFlow() {
        GlobalScope.launch {
            createFlow().map {
                if (it == 2) {
                    throw Exception("oops")
                }
                it
            }.retryWhen { _, attempt ->
                emit(5) // pass downstream
                attempt < 3
            }.catch {
                // retry will blow up after exceeding attempts
            }.collect { // flow must conclude with terminal operator as no action (equivilent to subscribe)
                Log.d("MainActivity", "value $it")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
