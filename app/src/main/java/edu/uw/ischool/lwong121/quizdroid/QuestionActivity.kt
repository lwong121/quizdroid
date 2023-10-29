package edu.uw.ischool.lwong121.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val btnSubmit = findViewById<Button>(R.id.qPageBtnSubmit)
        btnSubmit.isEnabled = false

        // add the question number and question
        val questionCount = findViewById<TextView>(R.id.qPageQuestionCount)
        val questionText = findViewById<TextView>(R.id.qPageQuestion)

        val selectedTopic = intent?.extras?.getSerializable(TOPIC_EXTRA) as Topic
        var currentQuestionNum = intent?.extras?.getInt(QUESTION_NUM_EXTRA) as Int
        val numCorrectAnswers = intent?.extras?.getInt(CORRECT_NUM_EXTRA) as Int

        val questions = selectedTopic.questions
        val currentQuestion = questions[currentQuestionNum]
        Log.i(TAG, "QuestionActivity Topic.questions: $currentQuestion")

        questionCount.text = "Question ${(currentQuestionNum + 1)} of ${questions.size}"
        questionText.text = currentQuestion.question

        // add the answer options
        val optionGroup = findViewById<RadioGroup>(R.id.qPageRadioGroup)
        val option1 = findViewById<RadioButton>(R.id.qPageOption1)
        val option2 = findViewById<RadioButton>(R.id.qPageOption2)
        val option3 = findViewById<RadioButton>(R.id.qPageOption3)
        val option4 = findViewById<RadioButton>(R.id.qPageOption4)

        option1.text = currentQuestion.options[0]
        option2.text = currentQuestion.options[1]
        option3.text = currentQuestion.options[2]
        option4.text = currentQuestion.options[3]

        var selectedAnswer: Int = -1
        optionGroup.setOnCheckedChangeListener { _, checkedId ->
            btnSubmit.isEnabled = true
            selectedAnswer = when (checkedId) {
                R.id.qPageOption1 -> 0
                R.id.qPageOption2 -> 1
                R.id.qPageOption3 -> 2
                else -> 3
            }
        }

        // add the submit button listener
        btnSubmit.setOnClickListener {
            val context = it.context
            val intent = Intent(context, AnswerActivity::class.java)
            intent.putExtra(TOPIC_EXTRA, selectedTopic)
            intent.putExtra(QUESTION_NUM_EXTRA, currentQuestionNum)
            intent.putExtra(CORRECT_NUM_EXTRA, numCorrectAnswers)
            intent.putExtra(SELECTION_OPTION_EXTRA, selectedAnswer)
            context.startActivity(intent)
        }
    }
}