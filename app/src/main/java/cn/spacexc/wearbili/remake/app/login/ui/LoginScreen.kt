package cn.spacexc.wearbili.remake.app.login.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R

/**
 * Created by XC-Qan on 2023/4/2.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

@Composable
fun LoginScreen(
    state: LoginScreenState,
    onQrcodeClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                //.aspectRatio(1f)
                .fillMaxSize(0.7f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .padding(4.dp)
        ) {
            Crossfade(targetState = state.currentLoginStatus) {
                when (it) {
                    LoginStatus.Loading, LoginStatus.GettingKey -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_loading_2233),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    LoginStatus.Failed, LoginStatus.Timeout, LoginStatus.FailedGettingKey -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_loading_2233_error),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null
                                ) {
                                    onQrcodeClicked()
                                }
                        )
                    }
                    LoginStatus.Pending, LoginStatus.Waiting -> {
                        state.qrCodeBitmap?.let {
                            Image(
                                bitmap = state.qrCodeBitmap,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(6.dp)
                                    .clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null
                                    ) {
                                        onQrcodeClicked()
                                    }
                            )
                        }
                    }
                    LoginStatus.Success -> {
                        Text(text = "登录成功")
                    }
                }
            }

        }
        Crossfade(targetState = state.currentLoginStatus) {
            when (it) {
                LoginStatus.Loading -> {
                    Text(
                        text = "二维码加载中",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                LoginStatus.Failed -> {
                    Text(
                        text = "加载失败啦",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                LoginStatus.Timeout -> {
                    Text(
                        text = "二维码失效啦",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                LoginStatus.Pending -> {
                    Text(
                        text = "使用手机客户端扫描二维码",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                LoginStatus.Waiting -> {
                    Text(
                        text = "请在手机上轻触确认",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                LoginStatus.Success -> {
                    Text(
                        text = "登录成功",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                LoginStatus.FailedGettingKey -> {
                    Text(
                        text = "跳转失败, 点击重新登录",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
                LoginStatus.GettingKey -> {
                    Text(
                        text = "登录成功, 正在跳转",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}