package cn.spacexc.wearbili.remake.app

//import cn.spacexc.wearbili.videoplayer.VideoPlayerManager

import android.os.Build
import android.util.Log
import cn.spacexc.bilibilisdk.BilibiliSdkManager
import cn.spacexc.bilibilisdk.data.DataManager
import cn.spacexc.wearbili.common.APP_CENTER_SECRET
import cn.spacexc.wearbili.common.domain.data.DataStoreManager
import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.remake.app.crash.ui.CrashActivity
import com.developer.crashx.config.CrashConfig
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


/**
 * Created by XC-Qan on 2023/3/23.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val TAG = "Re:WearBili"
val APP_VERSION_NAME = Application.getVersionName()
val APP_VERSION_CODE = Application.getVersionCode()

@HiltAndroidApp
class Application : android.app.Application() {
    @Inject
    lateinit var dataManager: DataManager   //TODO 别在application下放这种对象啊喂！（虽然是迫不得已的啦，不过还是找个时间研究一下拿走罢（20230711

    override fun onCreate() {
        super.onCreate()
        AppCenter.start(
            this, APP_CENTER_SECRET,
            Analytics::class.java, Crashes::class.java
        )
        val dataStore = DataStoreManager(this)
        BilibiliSdkManager.initSdk(
            dataManager = dataStore
        )
        CrashConfig.Builder.create()
            .errorActivity(CrashActivity::class.java)
            .apply()
        //VideoPlayerManager.dataManager = dataStore
        logd("mainApplicationContext")
        Log.e(
            TAG,
            "20230708: 记住，在这里每一处不合理的地方，都有他的合理之处和存在于此的理由 ————XC于00:17有感而发"
        )
    }

    init {
        mApplication = this
    }

    companion object {
        private lateinit var mApplication: Application
        fun getApplication(): Application = mApplication

        fun getVersionName(): String {
            val packageInfo =
                getApplication().packageManager.getPackageInfo(getApplication().packageName, 0)
            return packageInfo.versionName
        }

        fun getVersionCode(): Long {
            val packageInfo =
                getApplication().packageManager.getPackageInfo(getApplication().packageName, 0)
            return if (Build.VERSION.SDK_INT >= 28) packageInfo.longVersionCode else packageInfo.versionCode.toLong()
        }
    }
}