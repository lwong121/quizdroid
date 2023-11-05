package edu.uw.ischool.lwong121.quizdroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        // add the topic title and description
        val title = findViewById<TextView>(R.id.topic)
        val description = findViewById<TextView>(R.id.description)

        val selectedTopicIndex = intent?.extras?.getInt(TOPIC_INDEX_EXTRA) as Int
        val selectedTopic = QuizApp.getInstance().getTopicRepository().getTopic(selectedTopicIndex)
        Log.i(TAG, "OverviewActivity Topic: $selectedTopic")

        title.text = selectedTopic.title
        description.text = "${selectedTopic.longDescription} ${getString(R.string.common_description_msg)} ${selectedTopic.questions.size}"

        // add event listener to button
        val btnBegin = findViewById<Button>(R.id.btnBegin)
        btnBegin.setOnClickListener {
            val context = it.context
            val intent = Intent(context, QuestionActivity::class.java)
            intent.putExtra(TOPIC_INDEX_EXTRA, selectedTopicIndex)
            intent.putExtra(QUESTION_NUM_EXTRA, 0)
            intent.putExtra(CORRECT_NUM_EXTRA, 0)
            context.startActivity(intent)
        }
    }
}