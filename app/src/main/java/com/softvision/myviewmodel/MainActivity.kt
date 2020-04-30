package com.softvision.myviewmodel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel : SharedViewModel = ViewModelProviders(this)
        Log.d("MainActivity", "sharedViewModel: $viewModel")

        viewModel.navigate.observe(this, Observer {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SecondFragment())
                .commit()
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, FirstFragment())
                .commit()
        }
    }
}

fun LifecycleOwner.getFragmentManager(): FragmentManager = when (this) {
    is AppCompatActivity -> this.supportFragmentManager
    is Fragment -> this.childFragmentManager
    else -> throw IllegalArgumentException("Unrecognized lifecycle owner: $this")
}

class ViewModelProviders {
    companion object {
        inline operator fun <reified T : ViewModel> invoke(lifecycleOwner: LifecycleOwner): T {
            val fragmentManager = lifecycleOwner.getFragmentManager()

            val tag = T::class.java.toString()
            var viewModel: T? = fragmentManager.findFragmentByTag(tag) as? T

            return if (viewModel != null) {
                viewModel
            } else {
                viewModel = T::class.constructors.first().call()
                fragmentManager.beginTransaction()
                    .add(viewModel, tag)
                    .commitNowAllowingStateLoss()

                viewModel
            }
        }
    }
}

class SharedViewModel : ViewModel() {
    companion object {
        private const val TAG = "ViewModelImpl"
    }

    init {
        Log.d(TAG, "create()")
    }

    private val _navigate = MutableLiveData<Boolean>()
    val navigate : LiveData<Boolean>
        get() = _navigate

    fun youShouldNavigate() {
        _navigate.value = true
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