package com.coronatrace.covidtracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SymptomsQuizViewModel(application: Application) : AndroidViewModel(application) {

    private var questionNumber = 1;

    // Create a LiveData with a String
    val question: MutableLiveData<QuizState> by lazy {
        MutableLiveData<QuizState>()
    }

    init {
        question.value = QuizState(R.string.symptoms_quiz_temperature_title,R.string.symptoms_quiz_temperature_body)
    }

//    fun question(): QuizQuestion {
//        var question: QuizQuestion;
//
//        if (questionNumber == 1) {
//            question = QuizQuestion(R.string.symptoms_quiz_temperature_title,R.string.symptoms_quiz_temperature_body)
//        } else {
//            question = QuizQuestion(R.string.symptoms_quiz_cough_title,R.string.symptoms_quiz_cough_body)
//        }
//
//        return question;
//    }

    fun answerNo() {
        questionNumber++;
    }

}

data class QuizState(var questionTitle: Int, var questionBody: Int)
