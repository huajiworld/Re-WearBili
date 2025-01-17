package cn.spacexc.wearbili.remake.app.update.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cn.spacexc.wearbili.remake.app.splash.ui.SplashScreenActivity

/**
 * Created by XC-Qan on 2023/4/30.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

class UpdateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val versionInfo = if (Build.VERSION.SDK_INT >= 33) intent.getParcelableExtra(
            "updateInfo",
            SplashScreenActivity.AppUpdatesResult::class.java
        ) else intent.getParcelableExtra("updateInfo")
        setContent {
            UpdateActivityScreen(versionInfo = versionInfo)
        }
    }
}