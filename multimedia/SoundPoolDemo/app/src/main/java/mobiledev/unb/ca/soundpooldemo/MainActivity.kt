package mobiledev.unb.ca.soundpooldemo

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var soundPool: SoundPool? = null
    private var fireplaceSoundId = 0
    private var thunderSoundId = 0
    private var formula1SoundId = 0
    private var airplaneSoundId = 0
    private var trainWhistleSoundId = 0
    private var tollingBellSoundId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initSound(applicationContext)

        val buttonSound1: Button = findViewById(R.id.button_sound1)
        buttonSound1.setOnClickListener {
            soundPool?.autoPause()  // Pause all other sounds
            playSound(fireplaceSoundId)
        }

        val buttonSound2: Button = findViewById(R.id.button_sound2)
        buttonSound2.setOnClickListener {
            playSound(thunderSoundId)
        }

        val buttonSound3: Button = findViewById(R.id.button_sound3)
        buttonSound3.setOnClickListener {
            playSound(formula1SoundId)
        }

        val buttonSound4: Button = findViewById(R.id.button_sound4)
        buttonSound4.setOnClickListener {
            playSound(airplaneSoundId)
        }

        val buttonSound5: Button = findViewById(R.id.button_sound5)
        buttonSound5.setOnClickListener {
            playSound(trainWhistleSoundId)
        }

        val buttonSound6: Button = findViewById(R.id.button_sound6)
        buttonSound6.setOnClickListener {
            playSound(tollingBellSoundId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (null != soundPool) {
            soundPool?.unload(fireplaceSoundId)
            soundPool?.unload(thunderSoundId)
            soundPool?.unload(formula1SoundId)
            soundPool?.unload(airplaneSoundId)
            soundPool?.unload(trainWhistleSoundId)
            soundPool?.unload(tollingBellSoundId)

            soundPool?.release()
            soundPool = null
        }
    }

    private fun initSound(context: Context) {
        soundPool = createNewSoundPool()
        fireplaceSoundId = loadSound(context, R.raw.crackling_fireplace)
        thunderSoundId = loadSound(context, R.raw.thunder)
        formula1SoundId = loadSound(context, R.raw.formula1)
        airplaneSoundId = loadSound(context, R.raw.airplane_landing)
        trainWhistleSoundId = loadSound(context, R.raw.steam_train_whistle)
        tollingBellSoundId = loadSound(context, R.raw.tolling_bell)
    }

    private fun createNewSoundPool(): SoundPool {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        return SoundPool.Builder()
            .setMaxStreams(SOUND_POOL_MAX_STREAMS)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private fun loadSound(context: Context, resourceId: Int, priority: Int = 1): Int {
        return soundPool!!.load(context, resourceId, priority)
    }

    private fun playSound(soundId: Int) {
        soundPool?.play(soundId, 1f, 1f, 1, 0, 1.0f)
    }

    companion object {
        private const val SOUND_POOL_MAX_STREAMS = 6
    }
}