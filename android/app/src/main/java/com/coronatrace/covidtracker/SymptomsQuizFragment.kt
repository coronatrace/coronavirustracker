package com.coronatrace.covidtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * A simple [Fragment] subclass.
 * Use the [SymptomsQuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SymptomsQuizFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_symptoms_quiz, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SymptomsQuizFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
