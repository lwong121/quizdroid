package edu.uw.ischool.lwong121.quizdroid

import org.junit.Test

import org.junit.Assert.*

class TopicRepositoryTests {
    private val topicRepository: TopicRepository = TopicRepositoryImpl()

    @Test
    fun topicRepository_getTopics() {
        val topics = topicRepository.getTopics()
        assertTrue(topics.size == 4)
    }

    @Test
    fun topicRepository_getTopic() {
        val topic = topicRepository.getTopic(0)
        assertEquals(MATH, topic.title)
        assertEquals("Test your math skills with these challenges.", topic.shortDescription)
        assertEquals("This quiz will test you on some basic math knowledge and test your problem-solving abilities.", topic.longDescription)
        assertTrue(topic.questions.size == 3)
        assertTrue(topic.iconId == R.drawable.math_icon)
    }

    @Test
    fun topicRepository_getQuiz() {
        val quiz = topicRepository.getQuiz(0, 0)
        assertEquals("What is x in the equation 3x - 8 = 16?", quiz.question)
        val correctAnswer = quiz.options[quiz.correctAnswer]
        assertEquals("x = 8", correctAnswer)
    }

    @Test
    fun topicRepository_saveTopic() {
        val newTopic = Topic("test topic", "short description", "long description",
            mutableListOf(Quiz("question 1", listOf("option 1", "option 2", "option 3", "option 4"), 0),
                          Quiz("question 2", listOf("option 1", "option 2", "option 3", "option 4"), 1)),
            R.drawable.math_icon)
        topicRepository.saveTopic(newTopic)

        val topic = topicRepository.getTopic(topicRepository.getTopics().size - 1)
        assertEquals("test topic", topic.title)
        assertEquals("short description", topic.shortDescription)
        assertEquals("long description", topic.longDescription)
        assertTrue(topic.questions.size == 2)
        assertTrue(topic.iconId == R.drawable.math_icon)
    }

    @Test
    fun topicRepository_saveQuiz() {
        val newQuiz = Quiz("question", listOf("option 1", "option 2", "option 3", "option 4"), 0)
        topicRepository.saveQuiz(0, newQuiz)

        val topic = topicRepository.getTopic(0)
        assertTrue(topic.questions.size == 4)

        val quiz = topicRepository.getQuiz(0, 3)
        assertEquals("question", quiz.question)
        val correctAnswer = quiz.options[quiz.correctAnswer]
        assertEquals("option 1", correctAnswer)
    }
}