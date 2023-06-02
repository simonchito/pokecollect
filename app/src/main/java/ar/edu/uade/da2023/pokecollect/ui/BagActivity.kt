package ar.edu.uade.da2023.pokecollect.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.uade.da2023.pokecollect.R

class BagActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bag)


        var prefs= getSharedPreferences("ar.edu.uade.da2023.pokecollect.sharedpref", Context.MODE_PRIVATE)
        var user = prefs.getString("user", "")
    }
}