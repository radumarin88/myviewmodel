package com.softvision.myviewmodel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(LifecycleLogger("xxx Activity: $this"))

        if (savedInstanceState == null) {
            val viewModel = ViewModelImpl()
            viewModel.lifecycle.addObserver(LifecycleLogger("xxx Fragment: $viewModel"))

            supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, viewModel)
                    .commit()
        } else {
            val fragment = supportFragmentManager.findFragmentById(android.R.id.content)
            fragment?.lifecycle?.addObserver(LifecycleLogger("xxx Fragment: $fragment"))
        }
    }
}

class ViewModelImpl : ViewModel() {
    companion object {
        private const val TAG = "ViewModelImpl"
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared()")
    }
}

// Requirements:
// 1. lifecycle-aware:
// 1.1 live as long as the activity or fragment that is binding it - *
// 1.2 must maintain state across configuration changes
// 2. API: onCleared() -> called when ViewModel is finally destroyed
// 3. creation:
// 3.1 ViewModelsProviders(LifecycleOwner, Clazz<out ViewModel>)
// 3.1.1. lifecycleOwner = activity -> 1 per lifecycleOwner
// 3.1.2. lifecycleOwner = fragment -> 1 per lifecycleOwner
abstract class ViewModel: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        onCleared()
    }

    abstract fun onCleared()
}

class LifecycleLogger(private val tag: String) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event) {
        Log.d(tag, "event: $event")
    }
}