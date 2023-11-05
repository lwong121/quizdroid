package edu.uw.ischool.lwong121.quizdroid

import android.app.Application
import android.util.Log

class QuizApp : Application() {
    private val topicRepository: TopicRepository = TopicRepositoryImpl()

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "QuizApp is loaded and being run.")
    }

    companion object {
        private lateinit var instance: QuizApp

        fun getInstance(): QuizApp {
            if (!::instance.isInitialized) {
                synchronized(this) {
                    if (!::instance.isInitialized) {
                        instance = QuizApp()
                    }
                }
            }
            return instance
        }
    }

    fun getTopicRepository(): TopicRepository {
        return topicRepository
    }
}