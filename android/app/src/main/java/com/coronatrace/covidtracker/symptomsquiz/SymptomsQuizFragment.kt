package com.coronatrace.covidtracker.symptomsquiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.coronatrace.covidtracker.R
import com.google.android.material.card.MaterialCardView

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
        val cardTitle = view.findViewById<TextView>(R.id.cardTitle)
        val cardBody = view.findViewById<TextView>(R.id.cardBody)
        val card = view.findViewById<MaterialCardView>(R.id.card)
        val buttonNo = view.findViewById<Button>(R.id.buttonNo)
        val buttonYes = view.findViewById<Button>(R.id.buttonYes)

        // Create the observer which updates the UI.
        val questionObserver = Observer<QuizState> { question ->
            if (question.infectedResult == null) {
                // If no result yet, update the question
                cardTitle.setText(question.questionTitle)
                cardBody.setText(question.questionBody)
            } else {
                // Otherwise setup response layout
                val textColor = resources.getColor(R.color.colorOnPrimary)
                cardTitle.setTextColor(textColor)
                cardBody.setTextColor(textColor)

                // Update buttons
                buttonNo.visibility = View.GONE
                buttonYes.text = resources.getText(R.string.ok)
                buttonYes.setOnClickListener { view ->
                    view.findNavController()
                        .navigate(R.id.action_symptomsQuizFragment_to_homeFragment)
                }

                // If a positive result
                if (question.infectedResult == true) {
                    // Show a message
                    cardTitle.setText(R.string.symptoms_quiz_positive_title)
                    cardBody.setText(R.string.symptoms_quiz_positive_body)

                    // Update colors
                    val backgroundColor = resources.getColor(R.color.colorError)
                    card.setCardBackgroundColor(backgroundColor)
                }

                // If a negative result
                if (question.infectedResult == false) {
                    // Show a message
                    cardTitle.setText(R.string.symptoms_quiz_negative_title)
                    cardBody.setText(R.string.symptoms_quiz_negative_body)

                    // Update colors
                    val backgroundColor = resources.getColor(R.color.colorSecondaryVariant)
                    card.setCardBackgroundColor(backgroundColor)
                }
            }


        }

        model = ViewModelProvider(this).get(SymptomsQuizViewModel::class.java)
        model.question.observe(this, questionObserver)

        buttonNo.setOnClickListener { view -> model.answerNo() }
        buttonYes.setOnClickListener { view -> model.answerYes() }

        return view;
    }


}
