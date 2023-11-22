package edu.uw.ischool.lwong121.quizdroid

// CONSTANTS
const val TAG = "quizdroid"

const val TOPIC_INDEX_EXTRA = "TOPIC_INDEX_EXTRA"
const val QUESTION_NUM_EXTRA = "QUESTION_NUM_EXTRA"
const val SELECTION_OPTION_EXTRA = "SELECTED_OPTION_EXTRA"
const val CORRECT_NUM_EXTRA = "CORRECT_NUM_EXTRA"

// TOPIC NAME CONSTANTS
const val MATH = "Math"
const val PHYSICS = "Physics"
const val MARVEL = "Marvel Super Heroes"
const val HP = "Harry Potter"

// PREFERENCE CONSTANTS
const val PREFS = "prefs"
const val PREF_URL = "pref_url"
const val PREF_INTERVAL = "pref_interval"
const val DEFAULT_URL = "https://tednewardsandbox.site44.com/questions.json"
//const val CUSTOM_URL = "https://raw.githubusercontent.com/lwong121/quizdroid-data/main/quizdroid-questions.json"
const val DEFAULT_INTERVAL = 60
const val DEFAULT_FILE_NAME = "questions.json"
const val DEFAULT_DATA_REL_PATH = "/Android/data/"
//const val CUSTOM_DATA_REL_PATH = "/Android/data/edu.uw.ischool.lwong121.quizdroid/quizdroid-questions.json"

const val DATA_URL_EXTRA = "DATA_URL_EXTRA"
const val MIN_IN_MILLIS = 60 * 1000
const val ALARM_ACTION = "edu.uw.ischool.lwong121.ALARM"