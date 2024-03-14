package com.bomb.app.whatsappstatussaver

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bomb.app.whatsappstatussaver.ads.Adsimpl
import com.bomb.app.whatsappstatussaver.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var adsimpl: Adsimpl
    lateinit var link: String
    lateinit var appOpenManager: MyApplication
    lateinit var binding: ActivityMainBinding
    lateinit var dialog: BottomSheetDialog
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*
        if (intent.extras != null) {
            link = intent.extras!!.getString("link").toString()
            if (link != "m") {
                val i = Intent()
                i.data = Uri.parse(link)
                i.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                application.startActivity(i)
                finish()
            } else {
                Start()
            }
        } else {
            Start()
        }
*/
        Start()

    }



    private fun Start() {
        MobileAds.initialize(this) {}
        adsimpl.preperad()
        appOpenManager = MyApplication()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav)
        navView.setupWithNavController(navController)
        binding.navView.visibility = View.GONE

    }

    override fun onBackPressed() {
        if (navController.currentDestination!!.id != R.id.showVideoFragment) {
            bottomSheet()
        } else {
            super.onBackPressed()

        }
    }

    private fun bottomSheet() {
        val vie: View = layoutInflater.inflate(R.layout.back_bottom_sheet, null)
        dialog = BottomSheetDialog(this)
        dialog.setContentView(vie)
        val adview:AdView = vie.findViewById(R.id.adView22)
        adview.loadAd(adsimpl.preperad())
        val b = vie.findViewById<Button>(R.id.i)
        b.setOnClickListener {
            finish()
        }
        dialog.show()
    }
}