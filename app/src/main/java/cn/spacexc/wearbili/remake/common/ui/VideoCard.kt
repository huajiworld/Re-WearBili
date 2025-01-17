package cn.spacexc.wearbili.remake.common.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cn.spacexc.wearbili.remake.R
import cn.spacexc.wearbili.remake.app.Application
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID
import cn.spacexc.wearbili.remake.app.video.info.ui.PARAM_VIDEO_ID_TYPE
import cn.spacexc.wearbili.remake.app.video.info.ui.VideoInformationActivity
import cn.spacexc.wearbili.remake.common.ui.theme.AppTheme
import cn.spacexc.wearbili.remake.common.ui.theme.wearbiliFontFamily

/**
 * Created by Xiaochang on 2022/9/17.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoCard(
    modifier: Modifier = Modifier,
    videoName: String,
    uploader: String,
    views: String,
    badge: String? = "",
    coverUrl: String,
    videoId: String? = null,
    videoIdType: String? = null,
    context: Context = Application.getApplication()
) {
    Card(modifier = modifier, onClick = {
        if (!videoId.isNullOrEmpty() && !videoIdType.isNullOrEmpty()) {
            Intent(context, VideoInformationActivity::class.java).apply {
                putExtra(PARAM_VIDEO_ID, videoId)
                putExtra(PARAM_VIDEO_ID_TYPE, videoIdType)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp, horizontal = 2.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(modifier = Modifier.weight(6f)) {
                    BiliImage(
                        url = coverUrl,
                        contentDescription = "$videoName 封面",
                        modifier = Modifier
                            .aspectRatio(16f / 10f)
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                    if (!badge.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(BilibiliPink)
                                .padding(2.dp)
                                .align(Alignment.TopEnd),
                            //.fillMaxSize()
                            //.offset(y = (1).dp)
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = badge,
                                fontSize = 8.spx,
                                fontFamily = wearbiliFontFamily,
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }
                Text(
                    text = videoName,
                    style = AppTheme.typography.h3,
                    maxLines = 3,
                    modifier = Modifier.weight(7f),
                    overflow = TextOverflow.Ellipsis
                )
            }

            FlowRow {
                if (views.isNotEmpty()) {
                    val inlineTextContent = mapOf(
                        "viewCountIcon" to InlineTextContent(
                            placeholder = Placeholder(
                                width = 12.spx,
                                height = 12.spx,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_view_count),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(end = 2.dp)
                            )
                        }
                    )
                    Text(
                        text = buildAnnotatedString {
                            if (views.isNotEmpty()) {
                                appendInlineContent("viewCountIcon")
                                append(views)
                            }
                        },
                        fontSize = 9.spx,
                        modifier = Modifier.alpha(0.7f),
                        color = Color.White,
                        inlineContent = inlineTextContent,
                        //maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                if (uploader.isNotEmpty()) {
                    val inlineTextContent = mapOf(
                        "uploaderIcon" to InlineTextContent(
                            placeholder = Placeholder(
                                width = 12.spx,
                                height = 12.spx,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_uploader),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(end = 2.dp)
                            )
                        }
                    )
                    Text(
                        text = buildAnnotatedString {
                            if (uploader.isNotEmpty()) {
                                appendInlineContent("uploaderIcon")
                                append(uploader)
                            }
                        },
                        fontSize = 9.spx,
                        modifier = Modifier.alpha(0.7f),
                        color = Color.White,
                        inlineContent = inlineTextContent,
                        //maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

}
