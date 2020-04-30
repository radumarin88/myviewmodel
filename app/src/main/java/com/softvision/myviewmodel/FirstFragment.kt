package com.softvision.myviewmodel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_first.*

class FirstViewModel : ViewModel() {
    companion object {
        private const val TAG = "FirstViewModel"
    }

    init {
        Log.d(TAG, "create()")
    }

    override fun onCleared() {
        Log.d(TAG, "$this onCleared()")
    }
}

/**
 * A simple [Fragment] subclass.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val myViewModel : FirstViewModel = ViewModelProviders(this)
        val sharedViewModel : SharedViewModel = ViewModelProviders(activity as AppCompatActivity)

        Log.d("FirstFragment", "myViewModel: $myViewModel sharedViewModel: $sharedViewModel")

        navigate.setOnClickListener {
            sharedViewModel.youShouldNavigate()
        }
    }

}
