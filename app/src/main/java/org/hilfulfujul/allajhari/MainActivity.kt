package org.hilfulfujul.allajhari

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.ads.MobileAds
import org.hilfulfujul.allajhari.ads.AdMobHelper
import org.hilfulfujul.allajhari.ads.AdMobHelper.Companion.BANNER_AD_UNIT_ID
import org.hilfulfujul.allajhari.ads.AdMobHelper.Companion.INTERSTITIAL_AD_UNIT_ID
import org.hilfulfujul.allajhari.databinding.ActivityMainBinding
import org.hilfulfujul.allajhari.setting.AppUpdateHelper
import org.hilfulfujul.allajhari.setting.CloseApplication
import org.hilfulfujul.allajhari.setting.MY_REQUEST_CODE
import org.hilfulfujul.allajhari.setting.NavigationActions
import org.hilfulfujul.allajhari.setting.Response
import org.hilfulfujul.allajhari.setting.SharedPreferencesAds
import org.hilfulfujul.allajhari.setting.SharedPreferencesAds.Companion.BANNER_ADS_ID
import org.hilfulfujul.allajhari.setting.SharedPreferencesAds.Companion.BANNER_ADS_KEY
import org.hilfulfujul.allajhari.setting.SharedPreferencesAds.Companion.INTERSTITIAL_ADS_ID
import org.hilfulfujul.allajhari.setting.SharedPreferencesAds.Companion.INTERSTITIAL_ADS_KEY
import org.hilfulfujul.allajhari.setting.exitAlertApplication
import org.hilfulfujul.allajhari.setting.gotoUrlRedirect
import org.hilfulfujul.allajhari.setting.loadUrlInApp
import org.hilfulfujul.allajhari.viewmodel.AdsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var navController: NavController
    private lateinit var adsViewModel: AdsViewModel
    private lateinit var sharedPreferencesAds: SharedPreferencesAds


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        // init variable
        sharedPreferencesAds = SharedPreferencesAds(this)

        adsViewModel = ViewModelProvider(this)[AdsViewModel::class.java]
        adsViewModel.bannerAds.observe(this) { responsive ->
            when (responsive) {
                is Response.Error -> {}
                Response.Loading -> {}
                is Response.Success -> {
                    sharedPreferencesAds.saveBoolean(BANNER_ADS_KEY, responsive.data.ads)
                    sharedPreferencesAds.saveString(BANNER_ADS_ID, responsive.data.id)
                }
            }
        }

        adsViewModel.interstitial.observe(this) { responsive ->
            when (responsive) {
                is Response.Error -> {}
                Response.Loading -> {}
                is Response.Success -> {
                    sharedPreferencesAds.saveBoolean(INTERSTITIAL_ADS_KEY, responsive.data.ads)
                    sharedPreferencesAds.saveString(INTERSTITIAL_ADS_ID, responsive.data.id)
                }
            }
        }

        val isBannerAd = sharedPreferencesAds.getBoolean(BANNER_ADS_KEY)
        val isInterstitialAd = sharedPreferencesAds.getBoolean(INTERSTITIAL_ADS_KEY)
        val bannerAdId = sharedPreferencesAds.getString(BANNER_ADS_ID)
        val interstitialAdId = sharedPreferencesAds.getString(INTERSTITIAL_ADS_ID)

        drawerLayout = binding.main

        setSupportActionBar(binding.mainView.mApplicationToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)


        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.mainView.mApplicationToolbar,
            R.string.openNavigation,
            R.string.closeNavigation
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_justification_icon)

        navController = findNavController(this, R.id.mainFragmentLayout)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> NavigationActions.navigateToHome(navController, drawerLayout)
                R.id.writter -> loadUrlInApp("https://hilfulfujul.org/android-apk/all-ajhari/writer-info/")
                R.id.privacy -> loadUrlInApp("https://hilfulfujul.org/android-apk/all-ajhari/privacy-policies/?ref=All Ajhari Android Application Navigation Bar")
                // R.id.update_app -> AppUpdateHelper.checkForAppUpdate(this)
                R.id.moreApp -> gotoUrlRedirect("https://play.google.com/store/apps/dev?id=5871408368342725724")
                // R.id.ratting -> InAppRatting.promptAppRating(this)
                R.id.contact_us -> return@setNavigationItemSelectedListener false
                R.id.website -> loadUrlInApp("https://hilfulfujul.org/?ref=All Ajhari Android Application Navigation Bar")
                R.id.shair -> NavigationActions.shareApp(this)
                R.id.facebook_page -> gotoUrlRedirect("https://hilfulfujul.org/android-apk/all-ajhari/facebook/")
                R.id.facebook_group -> gotoUrlRedirect("https://hilfulfujul.org/android-apk/all-ajhari/facebook-group/")
                R.id.youtube -> gotoUrlRedirect("https://hilfulfujul.org/android-apk/all-ajhari/youtube/")
                R.id.linkedin -> gotoUrlRedirect("https://hilfulfujul.org/android-apk/all-ajhari/linkedin/")
            }

            drawerLayout.closeDrawers()

            true
        }

        viewModelStore.let {
            if (isBannerAd) {
                AdMobHelper.loadBannerAd(
                    applicationContext,
                    binding.mainView.bannerAdsLayout,
                    bannerAdId ?: BANNER_AD_UNIT_ID
                )
            }
            if (isInterstitialAd) {
                INTERSTITIAL_AD_UNIT_ID = interstitialAdId ?: INTERSTITIAL_AD_UNIT_ID

                if (!AdMobHelper.isLoaded()) {
                    AdMobHelper.loadInterstitialAd(applicationContext)
                }
            }
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    if (navController.currentDestination?.id != R.id.homeFragment) { // Replace with your home fragment ID
                        navController.navigateUp()
                    } else {
                        exitAlertApplication(object : CloseApplication {
                            override fun onCloseApplication() {
                                isEnabled = false
                                onBackPressedDispatcher.onBackPressed()
                            }
                        })
                    }
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            AppUpdateHelper.handleUpdateResult(this, resultCode)
        }
    }

    override fun onPause() {
        super.onPause()
        if (AdMobHelper.mAdView != null) {
            AdMobHelper.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (AdMobHelper.mAdView != null) {
            AdMobHelper.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (AdMobHelper.mAdView != null) {
            AdMobHelper.onDestroy()
        }

    }
}