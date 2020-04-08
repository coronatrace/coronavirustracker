package com.coronatrace.covidtracker.symptomsquiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.coronatrace.covidtracker.R
import com.coronatrace.covidtracker.databinding.SymptomsQuizFragBinding

class SymptomsQuizFragment : Fragment() {

    private lateinit var viewDataBinding: SymptomsQuizFragBinding

    private val viewModel by viewModels<SymptomsQuizViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Data binding
        val root = inflater.inflate(R.layout.symptoms_quiz_frag, container, false)
        viewDataBinding = SymptomsQuizFragBinding.bind(root).apply {
            this.viewmodel = viewModel
        }

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Navigate to home screen when finished
        viewModel.quizEndedState.observe(viewLifecycleOwner, Observer {endedState ->
            if (endedState) findNavController().navigate(R.id.action_symptomsQuizFragment_to_homeFragment)
        })
    }



}
