package com.coronatrace.covidtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        val navigateCheckSymptomsButton = view.findViewById<Button>(R.id.navigateCheckSymptoms)
        navigateCheckSymptomsButton.setOnClickListener { view -> view.findNavController().navigate(R.id.action_homeFragment_to_symptomsQuizFragment)}

        return view
    }
}
