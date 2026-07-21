package com.bulletin.news.presentation.components

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

/**
 * انیمیشن Lottie از یه فایل JSON بومی (res/raw)، نه از یه URL خارجی.
 * لینک‌های مستقیم JSON روی LottieFiles زیاد expire/منتقل می‌شن، پس بهتره
 * فایل رو خودت از https://lottiefiles.com دانلود کنی و بذاری توی res/raw،
 * بعد @RawRes آی‌دیشو اینجا پاس بدی. مثال:
 *   LottieStateAnimation(rawRes = R.raw.empty_bookmarks)
 */
@Composable
fun LottieStateAnimation(
    @RawRes rawRes: Int,
    modifier: Modifier = Modifier.size(160.dp)
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}