package com.coronatrace.covidtracker.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.coronatrace.covidtracker.R
import com.coronatrace.covidtracker.data.Infection

class HomeFragment : Fragment() {

    private lateinit var model: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        // Status
        val infectionObserver = Observer<Infection> { infection -> {
            Log.d("infect", infection.toString())
        }}
        model = ViewModelProvider(this).get(HomeViewModel::class.java)
        model.latestInfection.observe(this, infectionObserver)

        // Bottom navigation
        val navigateCheckSymptomsButton = view.findViewById<Button>(R.id.navigateCheckSymptoms)
        navigateCheckSymptomsButton.setOnClickListener { view -> view.findNavController().navigate(
            R.id.action_homeFragment_to_symptomsQuizFragment
        )}

        return view
    }
}
