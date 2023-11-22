package edu.uw.ischool.lwong121.quizdroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class PreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val urlEditText = findViewById<EditText>(R.id.preferencesUrl)
        val intervalEditText = findViewById<EditText>(R.id.preferencesInterval)
        val saveBtn = findViewById<Button>(R.id.savePreferencesBtn)

        val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)

        // need to set the current value
        urlEditText.setText(prefs.getString(PREF_URL, DEFAULT_URL))
        // extra credit version
//        urlEditText.setText(prefs.getString(PREF_URL, CUSTOM_URL))

        intervalEditText.setText(prefs.getInt(PREF_INTERVAL, DEFAULT_INTERVAL).toString())

        saveBtn.setOnClickListener {
            val url = urlEditText.text.toString()
            val interval = intervalEditText.text.toString().toInt()

            val prefsEdit = prefs.edit()
            prefsEdit.putString(PREF_URL, url)
            prefsEdit.putInt(PREF_INTERVAL, interval)
            prefsEdit.commit()

            // likely need to add some downloading functionality here for the next HW

            Log.i(TAG, "PreferencesActivity: Preferences saved... url = $url, interval = $interval")

            // once prefs are updated, go back to main activity
            val context = it.context
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            context.startActivity(intent)

            finish()
        }
    }
}