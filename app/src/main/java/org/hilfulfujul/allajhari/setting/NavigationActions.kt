package org.hilfulfujul.allajhari.setting

import android.content.Context
import android.content.Intent
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import org.hilfulfujul.allajhari.R

object NavigationActions {
    fun navigateToHome(navController: NavController, drawerLayout: DrawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            if (navController.currentDestination?.id != R.id.nav_home) {
                // Navigate to the destination fragment
                navController.navigateUp()
            }
        }
    }

    fun shareApp(context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "All Ajhari App contact")
        val shareMessage = "https://play.google.com/store/apps/details?id=org.hilfulfujul.allajhari"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        context.startActivity(Intent.createChooser(shareIntent, "ShareVia"))
    }
}