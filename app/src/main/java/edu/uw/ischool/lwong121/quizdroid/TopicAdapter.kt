package edu.uw.ischool.lwong121.quizdroid

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class TopicAdapter(context: Context, private val topics: List<Topic>)
    : ArrayAdapter<Topic>(context, R.layout.topic_list_item, R.id.topicTitle, topics) {
    init {
        Log.i(TAG, "Topic Adapter: Constructing...")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

//        val icon : ImageView = view.findViewById(R.id.iconImageView)
        val titleText : TextView = view.findViewById(R.id.topicTitle)
        val descriptionText : TextView = view.findViewById(R.id.topicDescription)

//        Log.i(TAG, "Topic Adapter: topic = ${topics[position]}")
//        icon.setImageResource(topics[position].iconId)
        titleText.text = topics[position].title
        descriptionText.text = topics[position].desc

        return view
    }
    override fun getCount(): Int {
        return super.getCount()
    }
}