package edu.uw.ischool.lwong121.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.appToolbar)
        setSupportActionBar(actionBar)

        // get all the topics from QuizApp
        val topics = (application as QuizApp).getTopicRepository().getTopics()
        Log.i(TAG, topics.toString())

        // create list of topics to take the quiz on
        val topicsListView = findViewById<ListView>(R.id.listViewTopics)

        topicsListView.adapter = TopicAdapter(this, topics)

        topicsListView.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
            val selectedTopic: Topic = topics[position]

            Log.i(TAG, "MainActivity: ${selectedTopic.title}, ${selectedTopic.desc}")

            // send data to overview page
            val context = view.context
            val intent = Intent(context, OverviewActivity::class.java)
            intent.putExtra(TOPIC_INDEX_EXTRA, position)
            context.startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_preferences -> {
                startActivity(Intent(this, PreferencesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
