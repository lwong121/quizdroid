package edu.uw.ischool.lwong121.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get all the topics from QuizApp
        val topics = (application as QuizApp).getTopicRepository().getTopics()
        Log.i(TAG, topics.toString())

        // create list of topics to take the quiz on
        val topicsListView = findViewById<ListView>(R.id.listViewTopics)

        topicsListView.adapter = TopicAdapter(this, topics)

        topicsListView.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
            val selectedTopic: Topic = topics[position]

            Log.i(TAG, "MainActivity: ${selectedTopic.title}, ${selectedTopic.longDescription}")

            // send data to overview page
            val context = view.context
            val intent = Intent(context, OverviewActivity::class.java)
            intent.putExtra(TOPIC_INDEX_EXTRA, position)
            context.startActivity(intent)
        }
    }
}
