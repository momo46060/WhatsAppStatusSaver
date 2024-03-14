package com.bomb.app.whatsappstatussaver.ads

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.bomb.app.whatsappstatussaver.MainActivity
import com.bomb.app.whatsappstatussaver.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton


@ActivityScoped
class Adsimpl @Inject constructor(@ActivityContext private val contextt: Context)  {

    var mInterstitialAd: InterstitialAd? = null

     fun preperad(): AdRequest {/*

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            contextt,
            contextt.resources.getString(R.string.intirstl_ad_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
*/
    return AdRequest.Builder()
    .build()
}

 fun showad() {/*
    if (mInterstitialAd != null) {
        mInterstitialAd?.show(contextt as Activity)
    } else {
        Log.d("TAG", "The interstitial ad wasn't ready yet.")
    }*/
}
}