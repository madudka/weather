package com.madudka.weather

import android.content.Context
import androidx.core.view.isVisible
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.madudka.weather.databinding.ActivitySettingsBinding
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.*
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener



private const val AD_UNIT_ID_BANNER = "R-M-1694518-1"
private const val AD_UNIT_ID_INTERSTITIAL = "R-M-1694518-2"
//Тестовые id
//private const val AD_UNIT_ID_BANNER = "R-M-DEMO-320x50"
//private const val AD_UNIT_ID_INTERSTITIAL = "R-M-DEMO-interstitial"

fun initAdYandex(context: Context) {
    MobileAds.initialize(context, InitializationListener { })
}

class AdYandexBanner {

    fun loadBanner(binding: ActivitySettingsBinding) {
        showLoading(binding.bannerAdView)

        val adRequest = AdRequest.Builder().build()

        binding.bannerAdView.apply {
            setAdUnitId(AD_UNIT_ID_BANNER)
            setAdSize(AdSize.BANNER_320x50)
            setBannerAdEventListener(
                BannerAdYandexAdsEventListener(binding)
            )
        }

        binding.bannerAdView.loadAd(adRequest);
    }

    private fun showLoading(bannerAdView: BannerAdView) {
        bannerAdView.isVisible = false
    }


    private inner class BannerAdYandexAdsEventListener(
        val binding: ActivitySettingsBinding
    ) : BannerAdEventListener {


        override fun onAdLoaded() {
            binding.bannerAdView.isVisible = true
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
        }

        override fun onImpression(impressionData: ImpressionData?) {
        }

        override fun onAdClicked() {
        }

        override fun onLeftApplication() {
        }

        override fun onReturnedToApplication() {
        }
    }
}

class AdYandexInterstitial {

    companion object {
        var interstitialAd : InterstitialAd? = null

        fun onInterstitialAdDestroy(){
            interstitialAd?.destroy()
            interstitialAd = null
        }
    }

    fun loadInterstitial(context : Context, binding: ActivitySettingsBinding) {
        showLoading(binding.adLoadingProgress)

        val adRequest = AdRequest.Builder().build()

        interstitialAd = InterstitialAd(context)

        interstitialAd?.apply {
            setAdUnitId(AD_UNIT_ID_INTERSTITIAL)
            setInterstitialAdEventListener(InterstitialAdYandexAdsEventListener(binding.adLoadingProgress))
        }

        interstitialAd?.loadAd(adRequest);
    }



    private fun showLoading(adLoadingProgress: CircularProgressIndicator) {
        adLoadingProgress.isVisible = true
    }

    private fun hideLoading(mCircularProgressIndicator: CircularProgressIndicator) {
        mCircularProgressIndicator.isVisible = false
    }

    private inner class InterstitialAdYandexAdsEventListener(
        val mCircularProgressIndicator: CircularProgressIndicator
    ) : InterstitialAdEventListener {


        override fun onAdLoaded() {
            interstitialAd?.show()
            hideLoading(mCircularProgressIndicator)
        }

        override fun onAdFailedToLoad(adRequestError: AdRequestError) {
            hideLoading(mCircularProgressIndicator)
        }

        override fun onAdShown() {
        }

        override fun onAdDismissed() {
        }

        override fun onImpression(impressionData: ImpressionData?) {
        }

        override fun onAdClicked() {
        }

        override fun onLeftApplication() {
        }

        override fun onReturnedToApplication() {
        }
    }
}




