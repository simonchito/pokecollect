package ar.edu.uade.da2023.pokecollect.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ar.edu.uade.da2023.pokecollect.R
import ar.edu.uade.da2023.pokecollect.R.drawable.pikachu
import ar.edu.uade.da2023.pokecollect.R.id.imageView
import com.bumptech.glide.Glide


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        showGIF()
        Handler(Looper.getMainLooper()).postDelayed({

            var userName = "fgastiarena"

            var prefs= getSharedPreferences("ar.edu.uade.da2023.pokecollect.sharedpref", Context.MODE_PRIVATE)
            prefs.edit().putString("user", userName).apply()

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },3000)


    }

    fun showGIF() {
        val imageView:ImageView = findViewById(imageView)
        Glide.with(this).load(pikachu).into(imageView)

    }

}