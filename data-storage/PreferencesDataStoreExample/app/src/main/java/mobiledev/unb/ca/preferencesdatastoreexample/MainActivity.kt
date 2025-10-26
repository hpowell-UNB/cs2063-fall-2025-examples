package mobiledev.unb.ca.preferencesdatastoreexample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var highScoreTextView: TextView
    private lateinit var gameScoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initSharedPreferences()

        highScoreTextView = findViewById(R.id.highScoreText)
        lifecycleScope.launch {
            setHighScoreText(readHighScoreFromSharedPreferences())
        }

        gameScoreTextView = findViewById(R.id.gameScoreText)
        setGameScoreText(INITIAL_HIGH_SCORE)

        val playButton: Button = findViewById(R.id.playButton)
        playButton.setOnClickListener {
            handlePlayButton()
        }

        val resetButton: Button = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            handleResetButton()
        }
    }

    private fun handlePlayButton() {
        val randomScore = Random.nextInt(1000)
        setGameScoreText(randomScore)

        lifecycleScope.launch {
            val currHighScore = readHighScoreFromSharedPreferences()
            if (randomScore > currHighScore) {
                writeHighScoreToSharedPreferences(randomScore)
                setHighScoreText(randomScore)
            }
        }
    }

    private fun handleResetButton() {
        // Reset the high score
        resetHighScore()
        setGameScoreText(INITIAL_HIGH_SCORE)
        setHighScoreText(INITIAL_HIGH_SCORE)
    }

    // Shared Preferences Helper Functions
    private fun initSharedPreferences() {
        SharedPreferencesManager.init(applicationContext.appPreferencesDataStore)
    }

    private suspend fun readHighScoreFromSharedPreferences(): Int {
        return withContext(Dispatchers.IO) {
            // val highScore: Int = sharedPreferencesManager.getHighScore()
            val highScore: Int = SharedPreferencesManager.getIntValue(KEY_NAME_HIGH_SCORE)
            highScore
        }
    }

    private fun writeHighScoreToSharedPreferences(score: Int) {
        // Since the saveHighScore() is a suspend function,
        // it has to be launched in a coroutine scope
        lifecycleScope.launch {
            SharedPreferencesManager.saveIntValue(KEY_NAME_HIGH_SCORE, score)
        }
    }

    private fun resetHighScore() {
        // Since the clearHighScore() is a suspend function,
        // it has to be launched in a coroutine scope
        lifecycleScope.launch {
            SharedPreferencesManager.clearIntValue(KEY_NAME_HIGH_SCORE)
        }
    }

    private fun setHighScoreText(highScore: Int) {
        highScoreTextView.text = getString(R.string.high_score, highScore)
    }

    private fun setGameScoreText(gameScore: Int) {
        gameScoreTextView.text = gameScore.toString()
    }

    companion object {
        private const val KEY_NAME_HIGH_SCORE = "HIGH_SCORE"
        private const val INITIAL_HIGH_SCORE = 0
    }
}