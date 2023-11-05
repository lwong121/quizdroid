package edu.uw.ischool.lwong121.quizdroid

// these domain objects are replaced with Topic and Quiz in the TopicRepository
// DATA CLASSES
//data class Topic(var name: String, var description: String, var questions: List<Question>) : Serializable
//data class Question(var question: String, var options: List<String>, var correctAnswer: Int) : Serializable

//// QUESTION LISTS
//private val mathQuestions = listOf(
//    Quiz(
//        "What is x in the equation 3x - 8 = 16?",
//        listOf("x = 8", "x = 6", "x = 12", "x = 7"),
//        0),
//    Quiz(
//        "What is the sum of the angles in a triangle?",
//        listOf("90 degrees", "120 degrees", "180 degrees", "270 degrees"),
//        2),
//    Quiz(
//        "If a rectangle has a length of 8 units and a width of 6 units, what is its area?",
//        listOf("48 square units", "14 square units", "30 square units", "56 square units"),
//        0)
//)
//
//private val physicsQuestions = listOf(
//    Quiz(
//        "What is the SI unit of force?",
//        listOf("Watts", "Volts", "Newtons", "Joules"),
//        2),
//    Quiz(
//        "When an object is in free fall, what is the acceleration due to gravity?",
//        listOf("10 m/s²", "5.4 m/s²", "2 m/s²", "9.8 m/s²"),
//        3),
//    Quiz(
//        "What does the law of conservation of energy state?",
//        listOf(
//            "Energy can only be destroyed, but never created ",
//            "Energy cannot be created or destroyed, only converted from one form to another",
//            "Energy can be created or destroyed",
//            "Energy can only be created, but never destroyed"),
//        1)
//)
//
//private val marvelQuestions = listOf(
//    Quiz(
//        "Who is known as the \"God of Thunder\"?",
//        listOf("Iron Man", "Spider-Man", "Captain America", "Thor"),
//        3),
//    Quiz(
//        "What is Iron Man's real name?",
//        listOf("Peter Parker", "Tony Stark", "T'Challa", "Steve Rogers"),
//        1),
//    Quiz(
//        "Who was a neurosurgeon before becoming a Marvel super hero?",
//        listOf("Doctor Strange", "Shang-Chi", "Black Widow", "Scarlet Witch"),
//        0)
//)
//
//private val hpQuestions = listOf(
//    Quiz(
//        "What is the name of the school that Harry Potter attends?",
//        listOf("Beauxbatons Academy of Magic", "Ilvermorny School of Witchcraft and Wizardry", " Durmstrang Institute", "Hogwarts School of Witchcraft and Wizardry"),
//        3),
//    Quiz(
//        "What is the name of Harry Potter's owl?",
//        listOf("Hedwig", "Crookshanks", "Scabbers", "Fawkes"),
//        0),
//    Quiz(
//        "Which of these villains is also known as \"The Dark Lord\"?",
//        listOf("Severus Snape", "Gellert Grindelwald", "Voldemort", "Bellatrix Lestrange"),
//        2)
//)
//
//// TOPICS
//val mathTopic = Topic(
//    MATH,
//    "Basic math quiz",
//    "This quiz will test you on some basic math knowledge and test your problem-solving abilities.",
//    mathQuestions)
//
//val physicsTopic = Topic(
//    PHYSICS,
//    "Basic physics quiz",
//    "This quiz will test you on some physics fundamentals and test your problem-solving abilities.",
//    physicsQuestions)
//
//val marvelTopic = Topic(
//    MARVEL,
//    "Marvel super heroes quiz",
//    "This quiz will test your knowledge of the Marvel Universe and the super heroes that work to protect it.",
//    marvelQuestions)
//
//val hpTopic = Topic(
//    HP,
//    "Harry Potter quiz",
//    "This quiz will test you on your knowledge of the Harry Potter series and the wizarding realm.",
//    hpQuestions)