package com.bomb.app.whatsappstatussaver.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bomb.app.whatsappstatussaver.MainActivity
import com.bomb.app.whatsappstatussaver.R
import com.bomb.app.whatsappstatussaver.ads.Adsimpl
import com.bomb.app.whatsappstatussaver.databinding.FragmentSettingBinding
import com.google.android.gms.ads.AdRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {
    @Inject
    lateinit var adsimpl: Adsimpl
    lateinit var binding: FragmentSettingBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        binding.adView.loadAd(adsimpl.preperad())
        binding.adView2.loadAd(adsimpl.preperad())

        (activity as MainActivity).binding.navView.visibility = View.VISIBLE
        binding.otherApps.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://play.google.com/store/apps/dev?id=7430114440944842738")
            startActivity(i)
        }
        binding.shareApp.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            val shareMessage =
                "https://play.google.com/store/apps/details?id=com.bomb.app.whatsappstatussaver"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        }
        binding.rateApp.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data =
                Uri.parse("https://play.google.com/store/apps/details?id=com.bomb.app.whatsappstatussaver")
            startActivity(i)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.navView.visibility=View.VISIBLE

    }
}