package com.bulletin.news.presentation.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun BottomNavBar(
    currentDestination: NavDestination?,
    onItemClick: (Screen) -> Unit
) {
    // ظاهر شیشه‌ای (Glassmorphism): پس‌زمینه‌ی نیمه‌شفاف + حاشیه‌ی نورانی + سایه‌ی نرم،
    // به‌جای یه NavigationBar تخت و تمام‌عرض که به کف صفحه چسبیده.
    //
    // نکته: Compose به‌صورت داخلی "بلور واقعیِ محتوای پشت این بار" رو ساپورت نمی‌کنه؛
    // Modifier.blur() فقط خودِ همین کامپوننت رو بلور می‌کنه، نه لیستی که زیرش اسکرول می‌شه.
    // برای بلور واقعیِ پشت‌زمینه باید از کتابخونه‌ای مثل Haze
    // (https://github.com/chrisbanes/haze) استفاده کرد یا با RenderEffect دستی پیاده‌ش کرد.
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.78f),
        tonalElevation = 0.dp,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(28.dp), clip = false)
            .clip(RoundedCornerShape(28.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                shape = RoundedCornerShape(28.dp)
            )
    ) {
        BottomNavItem.items.forEach { item ->
            val selected = currentDestination?.hasRoute(item.screen::class) == true
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
            )
        }
    }
}