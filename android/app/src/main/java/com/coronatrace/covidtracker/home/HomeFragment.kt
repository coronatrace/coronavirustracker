package com.coronatrace.covidtracker.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.coronatrace.covidtracker.R
import com.coronatrace.covidtracker.data.Infection
import com.coronatrace.covidtracker.databinding.HomeFragBinding
import com.coronatrace.covidtracker.logcontact.LogContactService

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragBinding
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Data binding
        val root = inflater.inflate(R.layout.home_frag, container, false)
        binding = HomeFragBinding.bind(root).apply {
            this.viewmodel = viewModel
        }

        // Status
        val infectionObserver = Observer<Infection> { infection ->
            val fourteenDaysInMillis = 14 * 24 * 60 * 60 * 1000
            val fourteenDaysAgo = System.currentTimeMillis() - fourteenDaysInMillis
            Log.d("Infection status", "$infection")

            if (infection != null && infection.timestamp > fourteenDaysAgo) {
                 viewModel.setStatusSymptoms()
            } else {
                viewModel.setStatusNoContact()
            }
        }
        viewModel.latestInfection.observe(this, infectionObserver)

        // Navigation
        binding.navigateCheckSymptoms.setOnClickListener { view -> view.findNavController().navigate(
            R.id.action_homeFragment_to_symptomsQuizFragment
        )}

        // Log service start/stop
        val trackingIntent = Intent(context, LogContactService::class.java)
        viewModel.trackingEnabled.observe(viewLifecycleOwner, Observer<Boolean> {trackingEnabled ->
            if(trackingEnabled) {
                context?.startService(trackingIntent)
            } else {
                context?.stopService(trackingIntent)
            }
        })

        return binding.root
    }

    over
}
