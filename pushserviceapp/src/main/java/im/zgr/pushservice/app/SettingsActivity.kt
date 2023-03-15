package im.zgr.pushservice.app

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

open class SettingsActivity : AppCompatActivity() {

    private var settingsFragment = SettingsTogglesFragment()

    val saveButton: Button by lazy { findViewById(R.id.save_button) }
    val connectionButton: Button by lazy { findViewById(R.id.connection_button) }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val app = application as AppBasic
        val sdkVersion = findViewById<TextView>(R.id.sdkVersion)
        sdkVersion.text = String.format("SDK version %s", app.sdkVersion)
        val appVersion = findViewById<TextView>(R.id.appVersion)
        appVersion.text = String.format("APP version %s", app.appVersion)

        connectionButton.setOnClickListener { openConnectionActivity() }

        supportFragmentManager.beginTransaction()
            .replace(R.id.profile, UserFragment()).commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.settings, settingsFragment).commit()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        saveButton.setOnClickListener {
            settingsFragment.saveSettings()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish(); // to close the activity
        return super.onOptionsItemSelected(item)
    }

    private fun openConnectionActivity() {
        startActivity(Intent(this, ConnectionActivity::class.java))
    }

}
