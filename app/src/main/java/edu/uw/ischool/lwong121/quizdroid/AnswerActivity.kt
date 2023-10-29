package edu.uw.ischool.lwong121.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        // add the question number and question
        val questionCount = findViewById<TextView>(R.id.aPageQuestionCount)
        val questionText = findViewById<TextView>(R.id.aPageQuestion)

        val selectedTopic = intent?.extras?.getSerializable(TOPIC_EXTRA) as Topic
        var selectedOptionNum = intent?.extras?.getInt(SELECTION_OPTION_EXTRA) as Int
        var currentQuestionNum = intent?.extras?.getInt(QUESTION_NUM_EXTRA) as Int

        val questions = selectedTopic.questions
        val currentQuestion = questions[currentQuestionNum]
        Log.i(TAG, "AnswerActivity Topic.questions: $currentQuestion")

        questionCount.text = "Question ${(currentQuestionNum + 1)} of ${questions.size}"
        questionText.text = currentQuestion.question

        // add the answer options
        val option1 = findViewById<RadioButton>(R.id.aPageOption1)
        val option2 = findViewById<RadioButton>(R.id.aPageOption2)
        val option3 = findViewById<RadioButton>(R.id.aPageOption3)
        val option4 = findViewById<RadioButton>(R.id.aPageOption4)

        option1.text = currentQuestion.options[0]
        option2.text = currentQuestion.options[1]
        option3.text = currentQuestion.options[2]
        option4.text = currentQuestion.options[3]

        option1.isEnabled = false;
        option2.isEnabled = false;
        option3.isEnabled = false;
        option4.isEnabled = false;

        // determine whether selected option is correct + mark which are correct/incorrect
        val selectedOption = when (selectedOptionNum) {
            0 -> option1
            1 -> option2
            2 -> option3
            else -> option4
        }

        selectedOption.isChecked = true

        val correctAnswerNum = currentQuestion.correctAnswer
        val correctAnswer = when (correctAnswerNum) {
            0 -> option1
            1 -> option2
            2 -> option3
            else -> option4
        }

        var numCorrectAnswers = intent?.extras?.getInt(CORRECT_NUM_EXTRA) as Int

        if (selectedOptionNum == correctAnswerNum) {
            numCorrectAnswers++
            selectedOption.background = getDrawable(R.drawable.correct_rounded_corner_view)
        } else {
            selectedOption.background = getDrawable(R.drawable.wrong_rounded_corner_view)
            correctAnswer.background = getDrawable(R.drawable.correct_rounded_corner_view)
        }

        // add the current score
        val score = findViewById<TextView>(R.id.aPageScore)

        // handle next question or finish quiz + update score at the top of the page
        val btnNext = findViewById<Button>(R.id.aPageBtnNext)
        if (currentQuestionNum + 1 < questions.size) {
            score.text = "You have $numCorrectAnswers out of ${currentQuestionNum + 1} correct"

            btnNext.text = getString(R.string.next_btn)
            btnNext.setOnClickListener {
                val context = it.context
                val intent = Intent(context, QuestionActivity::class.java)
                intent.putExtra(TOPIC_EXTRA, selectedTopic)
                currentQuestionNum++
                intent.putExtra(QUESTION_NUM_EXTRA, currentQuestionNum)
                intent.putExtra(CORRECT_NUM_EXTRA, numCorrectAnswers)
                context.startActivity(intent)
            }
        } else {
            score.text = "You have reached the end of the quiz.\nYou got $numCorrectAnswers out of ${currentQuestionNum + 1} correct!"

            btnNext.text = getString(R.string.finish_btn)
            btnNext.setOnClickListener {
                val context = it.context
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}