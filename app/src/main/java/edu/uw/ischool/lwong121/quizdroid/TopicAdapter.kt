package edu.uw.ischool.lwong121.quizdroid

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TopicAdapter(context: Context, private val topics: List<Topic>)
    : ArrayAdapter<Topic>(context, android.R.layout.simple_list_item_2, android.R.id.text1, topics) {
    init {
        Log.v(TAG, "Constructing...")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.i(TAG, "getView() called")
        val view = super.getView(position, convertView, parent)

        val titleText : TextView = view.findViewById(android.R.id.text1)
        val descriptionText : TextView = view.findViewById(android.R.id.text2)

        Log.i(TAG, "topic.name = ${topics[position]}")
        titleText.text = topics[position].title
        descriptionText.text = topics[position].shortDescription

        return view
    }
    override fun getCount(): Int {
        Log.i(TAG, "count = ${super.getCount()}")
        return super.getCount()
    }
}