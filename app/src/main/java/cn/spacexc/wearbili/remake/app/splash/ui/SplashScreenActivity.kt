package cn.spacexc.wearbili.remake.app.splash.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import cn.spacexc.bilibilisdk.sdk.user.webi.WebiSignature
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.login.ui.LoginActivity
import cn.spacexc.wearbili.remake.app.main.ui.MainActivity
import cn.spacexc.wearbili.remake.app.update.ui.UpdateActivity
import cn.spacexc.wearbili.remake.common.ToastUtils
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.request.header
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject


/**
 * Created by XC-Qan on 2023/3/21.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
@UnstableApi
class SplashScreenActivity : ComponentActivity() {
    @Inject
    lateinit var networkUtils: cn.spacexc.wearbili.common.domain.network.KtorNetworkUtils
    @Inject
    lateinit var userManager: cn.spacexc.wearbili.common.domain.user.UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)*/
        /*Intent(this, DlnaDeviceDiscoverActivity::class.java).apply {
            putExtra(PARAM_VIDEO_ID_TYPE, VIDEO_TYPE_BVID)
            putExtra(PARAM_VIDEO_ID, "BV1tQ4y1R7MR")
            putExtra(PARAM_VIDEO_CID, 341732612L)
            startActivity(this)
        }
        finish()
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)*/
        val currentTime = System.currentTimeMillis()    //后面leancloud签名用到，别删
        setContent {
            SplashScreen()
        }
        lifecycleScope.launch {
            //VideoInfo.getVideoInfoApp(VIDEO_TYPE_AID, "954781099").logd()

            networkUtils.get<String>("https://bilibili.com")    // 每次启动获取最新的cookie
            WebiSignature.getWebiSignature()    //保存新的webi签名
            //print(VideoInfo.getVideoPlaybackUrls(videoId = "BV1ng4y1877V", videoCid = 175354448))

            val appUpdatesResponse =
                networkUtils.get<LeanCloudAppUpdatesSearch>("https://mae7lops.lc-cn-n1-shared.com/1.1/classes/AppUpdates") {
                    header("X-LC-Id", cn.spacexc.wearbili.common.LEANCLOUD_APP_ID)
                    header(
                        "X-LC-Sign",
                        "${cn.spacexc.wearbili.common.EncryptUtils.md5("$currentTime${cn.spacexc.wearbili.common.LEANCLOUD_APP_KEY}")},$currentTime"
                    )
                }
            appUpdatesResponse.data?.results?.let { updateLists ->
                updateLists.find { it.versionCode.toLong() > Application.getVersionCode() }
                    ?.let { version ->
                        startActivity(
                            Intent(
                                this@SplashScreenActivity,
                                UpdateActivity::class.java
                            ).apply {
                                putExtra("updateInfo", version)
                            })
                        finish()
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
                        return@launch
                    }
            }
            if (userManager.isUserLoggedIn()) {
                val response = userManager.mid()?.let { uid ->
                    networkUtils.get<LeanCloudUserSearch>("https://mae7lops.lc-cn-n1-shared.com/1.1/classes/ActivatedUIDs?where={\"uid\":\"$uid\"}") {
                        header("X-LC-Id", cn.spacexc.wearbili.common.LEANCLOUD_APP_ID)
                        header(
                            "X-LC-Sign",
                            "${cn.spacexc.wearbili.common.EncryptUtils.md5("$currentTime${cn.spacexc.wearbili.common.LEANCLOUD_APP_KEY}")},$currentTime"
                        )
                    }
                }
                response?.data?.results?.let {
                    if (it.isNotEmpty()) {
                        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                        finish()
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
                    } else {
                        ToastUtils.showText("现暂未开放内测申请, 请耐心等待正式版")
                        userManager.logout()
                        startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                        finish()
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
                    }
                    return@launch
                }
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
            } else {
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
                overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out)
            }
        }
    }

    data class LeanCloudUserSearch(
        val results: List<UserResult>?
    )

    @Parcelize
    data class LeanCloudAppUpdatesSearch(
        val results: List<AppUpdatesResult>?
    ) : Parcelable

    data class UserResult(
        val createdAt: String,
        val objectId: String,
        val uid: String,
        val addSource: String,
        val updatedAt: String
    )

    @Parcelize
    data class AppUpdatesResult(
        val createdAt: String,
        val objectId: String,
        val channel: String,
        val versionName: String,
        val versionCode: Int,
        val downloadAddress: String,
        val updateLog: String,
        val updatedAt: String
    ) : Parcelable
}