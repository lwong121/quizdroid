package edu.uw.ischool.lwong121.quizdroid

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlertDialog
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var topicsListView : ListView
    private var receiver : BroadcastReceiver? = null
    private var downloadRequestCode = 0
    private val filePath = "${Environment.getExternalStorageDirectory()}$DEFAULT_DATA_REL_PATH$DEFAULT_FILE_NAME"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.appToolbar)
        setSupportActionBar(actionBar)

        val activityContext = this
        if (isAirplaneModeOn()) {
            Toast.makeText(
                activityContext,
                "You are currently in airplane mode and you have no access to the internet. Do you want to turn airplane mode off to continue?",
                Toast.LENGTH_LONG).show()
            askEnableAirplaneMode()
        } else if (!isOnline()) {
            Toast.makeText(
                activityContext,
                "You are currently offline and you have no access to the internet. Please check your connection.",
                Toast.LENGTH_LONG).show()
        } else {
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "File access permission is required for quizdroid. Allow file access permissions and re-open quizdroid", Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 0)
            } else {
                val file = File(filePath)
                if (!file.exists()) {
                    val fileWriter = FileWriter(filePath)
                    fileWriter.write("[]")
                    fileWriter.close()
                }

                setDownloadManager()
            }
        }

        topicsListView = findViewById(R.id.listViewTopics)
        updateTopicsList(activityContext)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (receiver != null) {
            Log.i(TAG, "MainActivity: unregistering receiver $receiver")
            unregisterReceiver(receiver)
            receiver = null
        }
    }

    private fun updateTopicsList(activityContext: MainActivity) {
        // get all the topics from QuizApp + make sure to get the newly updated data
        val topics = (application as QuizApp).getTopicRepository().getTopics()

        // create list of topics to take the quiz on
        topicsListView.adapter = TopicAdapter(activityContext, topics)

        topicsListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, view, position, _ ->
                val selectedTopic: Topic = topics[position]

                Log.i(TAG, "MainActivity: ${selectedTopic.title}, ${selectedTopic.desc}")

                // send data to overview page
                val context = view.context
                val intent = Intent(context, OverviewActivity::class.java)
                intent.putExtra(TOPIC_INDEX_EXTRA, position)
                context.startActivity(intent)
            }

        Log.i(TAG, "MainActivity: Updated the topics in the list view")
    }

    private fun setDownloadManager() {
        val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val url = prefs.getString(PREF_URL, DEFAULT_URL) as String
        val interval = prefs.getInt(PREF_INTERVAL, DEFAULT_INTERVAL)

        Log.i(TAG, "MainActivity: starting download manager with url: $url, receiver: $receiver")

        val intent = Intent(ALARM_ACTION)
        intent.putExtra(DATA_URL_EXTRA, url)
        val pendingIntent = PendingIntent.getBroadcast(this, downloadRequestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        downloadRequestCode++

        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            (interval * MIN_IN_MILLIS).toLong(),
            pendingIntent
        )

        Toast.makeText(this, "The URL: $url will eventually be hit.", Toast.LENGTH_LONG).show()

        val mainActivity = this
        if (receiver == null) {
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val dataUrl = intent?.extras?.getString(DATA_URL_EXTRA)
                    Log.i(TAG, "MainActivity: reached receiver $receiver, will attempt to download question data from the url: $dataUrl")

                    if (!dataUrl.isNullOrEmpty()) {
                        // download the file + save as questions.json
                        val executor: Executor = Executors.newSingleThreadExecutor()
                        executor.execute {
                            val isDownloadSuccessful = downloadFileContents(dataUrl)
                            if (isDownloadSuccessful) {
                                runOnUiThread {
                                    updateTopicsList(mainActivity)
                                }
                            }
                        }
                    } else {
                        Log.e(TAG, "MainActivity: Attempted to download data with a null or blank url.")
                    }
                }
            }
        }

        val filter = IntentFilter(ALARM_ACTION)
        registerReceiver(receiver, filter)

        Log.i(TAG, "MainActivity: registering receiver $receiver")
    }

    private fun downloadFileContents(dataUrl: String): Boolean {
        try {
            if (!isOnline()) {
                runOnUiThread {
                    Toast.makeText(
                        this,
                        "You are currently offline, so we cannot download the file at the URL. Please check your connection.",
                        Toast.LENGTH_LONG).show()
                }
            } else {
                if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    val url = URL(dataUrl)
                    val urlConnection = when (url.protocol) {
                        "http" -> url.openConnection() as HttpURLConnection
                        else -> url.openConnection() as HttpsURLConnection
                    }

                    val statusCode = urlConnection.responseCode
                    if (statusCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = urlConnection.inputStream
                        val reader = InputStreamReader(inputStream)
                        reader.use {
                            val text = it.readText()
                            val fileWriter = FileWriter(filePath)
                            fileWriter.write(text)
                            fileWriter.close()
                        }
                        reader.close()

                        (application as QuizApp).getTopicRepository().updateData()
                        Log.i(TAG, "MainActivity: questions.json now updated with new data")

                        runOnUiThread {
                            Toast.makeText(this, "Successfully downloaded data from $url", Toast.LENGTH_LONG).show()
                        }

                        return true // successful download
                    } else {
                        Log.e(TAG, "MainActivity: Getting the file contents failed with $statusCode. Keeping the previous file state.")
                    }
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 0)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "MainActivity: Error occurred during the download. Message: ${e.message}")
        }

        askRetryOrQuitOnDownloadFail()
        return false // unsuccessful download
    }

    private fun isOnline(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetwork = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
        val isOnline = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        Log.i(TAG, "MainActivity: Is this device is online? $isOnline")
        return isOnline
    }

    private fun isAirplaneModeOn(): Boolean {
        val isAirplaneModeOn = Settings.Global.getInt(this.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
        Log.i(TAG, "MainActivity: Does this device have airplane mode enabled? $isAirplaneModeOn")
        return isAirplaneModeOn
    }

    private fun askEnableAirplaneMode() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Airplane Mode Enabled")
        alertDialogBuilder.setMessage("Do you want to turn Airplane Mode off?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            startActivity(intent)
            Log.i(TAG, "MainActivity: User has gone to try and turn airplane mode off.")
        }
        alertDialogBuilder.setNegativeButton("No") { _, _ ->
            Toast.makeText(this, "You did not turn off airplane mode. Application has no access to the internet.", Toast.LENGTH_LONG).show()
        }
        alertDialogBuilder.show()
    }

    private fun askRetryOrQuitOnDownloadFail() {
        val contextIntent = intent
        runOnUiThread {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Question Data Download Failed")
            alertDialogBuilder.setMessage("Do you want to retry or quit the application and try again later?")
            alertDialogBuilder.setPositiveButton("Retry") { _, _ ->
                Log.i(TAG, "MainActivity: User chose to retry the download.")
                finish()
                startActivity(contextIntent)
            }
            alertDialogBuilder.setNegativeButton("Quit") { _, _ ->
                Log.i(TAG, "MainActivity: User chose to quit the application and try again later.")
                finish()
            }
            alertDialogBuilder.show()
        }
    }

    // Only have the toolbar on the topics page
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_preferences -> {
                startActivity(Intent(this, PreferencesActivity::class.java))
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}