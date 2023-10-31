package edu.uw.ischool.lwong121.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
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
        val currentQuestionNum = intent?.extras?.getInt(QUESTION_NUM_EXTRA) as Int
        val numCorrectAnswers = intent?.extras?.getInt(CORRECT_NUM_EXTRA) as Int

        val questions = selectedTopic.questions
        val currentQuestion = questions[currentQuestionNum]
        Log.i(TAG, "QuestionActivity Topic.questions: $currentQuestion")

        questionCount.text = getString(R.string.question_count, currentQuestionNum + 1, questions.size)
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

        // extra credit
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                val targetActivity = when (currentQuestionNum) {
                    // 1. back button on first question takes you to topic list page, not topic overview page
                    0 -> MainActivity::class.java
                    // 2. back button on any other question takes you to previous question page, not previous answer page
                    else -> QuestionActivity::class.java
                }
                val intent = Intent(this@QuestionActivity, targetActivity)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}