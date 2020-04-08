package com.coronatrace.covidtracker.symptomsquiz

import android.app.Application
import android.os.health.SystemHealthManager
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.coronatrace.covidtracker.R
import com.coronatrace.covidtracker.data.Infection
import com.coronatrace.covidtracker.data.source.InfectionRepository
import com.coronatrace.covidtracker.data.source.local.AppRoomDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SymptomsQuizViewModel(application: Application) : AndroidViewModel(application) {

    var cardTitle = ObservableField<Int>()
    var cardBody = ObservableField<Int>()
    val cardBackgroundColor = ObservableField<Int>(R.color.lightGray)
    val cardTextColor = ObservableField<Int>(R.color.onBackground)
    val noButtonVisibility = ObservableField<Int>(View.VISIBLE)
    val yesButtonText = ObservableField<Int>(R.string.yes)
    val quizEndedState = MutableLiveData<Boolean>(false)

    private val questions: Array<Question> = arrayOf(
        Question(R.string.symptoms_quiz_temperature_title, R.string.symptoms_quiz_temperature_body),
        Question(R.string.symptoms_quiz_cough_title, R.string.symptoms_quiz_cough_body)
    )
    private var questionsAnswered = 0;
    private var viewingResult = false;

    private val repository: InfectionRepository

    init {
        setQuestion()
        val infectionDao = AppRoomDatabase.getDatabase(application).infectionDao()
        repository =
            InfectionRepository(
                infectionDao
            )
    }

    fun answerNo() {
        if (questionsAnswered < questions.size - 1) {
            questionsAnswered++
            setQuestion()
        } else {
            setNotInfected()
        }
    }

    fun answerYes() {
        if (!viewingResult) {
            setInfected()
        } else {
            // Update the quizEndedState for an observer in the fragment (to navigate with)
            quizEndedState.value = true;
        }
    }

    private fun setQuestion() {
        cardTitle.set(questions[questionsAnswered].title)
        cardBody.set(questions[questionsAnswered].body)
    }

    private fun setNotInfected() {
        cardTitle.set(R.string.symptoms_quiz_negative_title)
        cardBody.set(R.string.symptoms_quiz_negative_body)
        cardBackgroundColor.set(R.color.secondaryVariant)
        cardTextColor.set(R.color.onSecondary)
        setButtonsOnResult()
    }

    private fun setInfected() {
        // Store details (DB and remotely)
        val infectionTime = System.currentTimeMillis()
        val infection = Infection(null,null, infectionTime, "symptoms")
        GlobalScope.launch {
            Log.d("Inserting infection", "$infection")
            repository.insertInfection(infection)
        }

        // Update view
        cardTitle.set(R.string.symptoms_quiz_positive_title)
        cardBody.set(R.string.symptoms_quiz_positive_body)
        cardBackgroundColor.set(R.color.error)
        cardTextColor.set(R.color.onError)
        setButtonsOnResult()
    }

    private fun setButtonsOnResult() {
        viewingResult = true;
        noButtonVisibility.set(View.GONE)
        yesButtonText.set(R.string.ok)
    }

}

data class Question(val title: Int, val body: Int)

