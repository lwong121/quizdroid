package edu.uw.ischool.lwong121.quizdroid

import android.os.Environment
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

interface TopicRepository {
    fun updateData()
    fun getTopics(): List<Topic>
    fun getTopic(topicIndex: Int): Topic
    fun getQuiz(topicIndex: Int, quizIndex: Int): Quiz

    // These are not used for HW 8, but they do have unit tests as part of the extra credit
    fun saveTopic(newTopic: Topic)
    fun saveQuiz(topicIndex: Int, newQuiz: Quiz)
}

data class Topic(var title: String, var desc: String, var questions: MutableList<Quiz>)

data class Quiz(var text: String, var answers: List<String>, var answer: Int)

class TopicRepositoryImpl() : TopicRepository {
    private var topics: MutableList<Topic> = mutableListOf()
    private val filePath = "${Environment.getExternalStorageDirectory()}$DEFAULT_DATA_REL_PATH$DEFAULT_FILE_NAME"

    init {
        val file = File(filePath)
        Log.i(TAG, "TopicRepository: file stored at $filePath")
        if (!file.exists()) {
            // cannot do here, no permissions on initial startup
//            val fileWriter = FileWriter(filePath)
//            fileWriter.write("[]")
//            fileWriter.close()
        } else {
            readDataFile()
        }
    }

    private fun readDataFile() {
        FileReader(filePath).use {
            val text = it.readText()

            // deserialize JSON into Mutable list of topic objects, made sure to match names to make it easier
            val topicsList: MutableList<Topic> = Gson().fromJson(text, object : TypeToken<MutableList<Topic>>() {}.type)

            // need to convert the correct answer to 0-based
            for (i in 0..<topicsList.size) {
                val questions = topicsList.get(i).questions
                for (j in 0..<questions.size) {
                    val question = questions.get(j)
                    question.answer = question.answer - 1
                }
            }

            topics = topicsList

            Log.i(TAG, "TopicRepository: read the data the file, topics: $topics")
        }
    }

    override fun updateData() {
        readDataFile()
    }

    override fun getTopics(): List<Topic> {
        return topics
    }

    override fun getTopic(topicIndex: Int): Topic {
        return topics[topicIndex]
    }

    override fun getQuiz(topicIndex: Int, quizIndex: Int): Quiz {
        return topics[topicIndex].questions[quizIndex]
    }

    override fun saveTopic(newTopic: Topic) {
        topics.add(newTopic)
    }

    override fun saveQuiz(topicIndex: Int, newQuiz: Quiz) {
        topics[topicIndex].questions.add(newQuiz)
    }
}