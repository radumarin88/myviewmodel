package com.softvision.myviewmodel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

class SecondFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val myViewModel : FirstViewModel = ViewModelProviders(this)
        val sharedViewModel : SharedViewModel = ViewModelProviders(activity as AppCompatActivity)

        Log.d("SecondFragment", "myViewModel: $myViewModel sharedViewModel: $sharedViewModel")
    }
}
