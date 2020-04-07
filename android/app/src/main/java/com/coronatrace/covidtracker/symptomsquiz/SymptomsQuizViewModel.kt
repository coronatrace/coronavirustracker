package com.coronatrace.covidtracker.symptomsquiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coronatrace.covidtracker.R
import com.coronatrace.covidtracker.data.Infection
import com.coronatrace.covidtracker.data.source.InfectionRepository
import com.coronatrace.covidtracker.data.source.local.AppRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SymptomsQuizViewModel(application: Application) : AndroidViewModel(application) {

    private var questionNumber = 1;
    private var questionsTotal = 2;
    private val repository: InfectionRepository

    // Create a LiveData with a String
    val question: MutableLiveData<QuizState> by lazy {
        MutableLiveData<QuizState>()
    }

    init {
        setQuestion()
        val infectionDao = AppRoomDatabase.getDatabase(application).infectionDao()
        repository =
            InfectionRepository(
                infectionDao
            )
    }

    fun answerNo() {
        if (questionNumber < questionsTotal) {
            questionNumber++
            setQuestion()
        } else {
            setNotInfected()
        }
    }

    fun answerYes() {
        setInfected()
    }

    private fun setQuestion() {
        if (questionNumber == 1) {
            question.value =
                QuizState(
                    R.string.symptoms_quiz_temperature_title,
                    R.string.symptoms_quiz_temperature_body,
                    null
                )
        } else {
            question.value =
                QuizState(
                    R.string.symptoms_quiz_cough_title,
                    R.string.symptoms_quiz_cough_body,
                    null
                )
        }
    }

    private fun setInfected() {
        viewModelScope.launch(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis()
            repository.insertInfection(
                Infection(
                    null,
                    null,
                    timestamp,
                    "symptoms",
                    true
                )
            )
        }

        question.value =
            QuizState(0, 0, true)
    }

    private fun setNotInfected() {
        question.value  =
            QuizState(0, 0, false)
    }

}

data class QuizState(var questionTitle: Int, var questionBody: Int, var infectedResult: Boolean?)
