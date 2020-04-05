package com.coronatrace.covidtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


/**
 * A simple [Fragment] subclass.
 * Use the [SymptomsQuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SymptomsQuizFragment : Fragment() {

    private lateinit var model: SymptomsQuizViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_symptoms_quiz, container, false)

        // Get references
        val questionTitle = view.findViewById<TextView>(R.id.questionTitle)
        val questionBody = view.findViewById<TextView>(R.id.questionBody)
        val buttonNo = view.findViewById<Button>(R.id.buttonNo)
        val buttonYes = view.findViewById<Button>(R.id.buttonYes)

        // Create the observer which updates the UI.
        val questionObserver = Observer<QuizState> { question ->
            questionTitle.setText(question.questionTitle)
            questionBody.setText(question.questionBody)
        }

        model = ViewModelProvider(this).get(SymptomsQuizViewModel::class.java)
        model.question.observe(this, questionObserver )

        return view;
    }


}
