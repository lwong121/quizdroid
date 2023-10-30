package edu.uw.ischool.lwong121.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create list of topics to take the quiz on
        val topicsListView = findViewById<ListView>(R.id.listViewTopics)

        topicsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, topicLabels)

        topicsListView.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
            val selectedTopic = when (position) {
                0 -> mathTopic
                1 -> physicsTopic
                2 -> marvelTopic
                else -> hpTopic
            }

            Log.i(TAG, "MainActivity: ${selectedTopic.name}, ${selectedTopic.description}")

            // send data to overview page
            val context = view.context
            val intent = Intent(context, OverviewActivity::class.java)
            intent.putExtra(TOPIC_EXTRA, selectedTopic as Serializable?)
            context.startActivity(intent)
        }
    }
}
