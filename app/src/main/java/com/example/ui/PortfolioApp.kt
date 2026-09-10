package com.example.ui

import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ResumeData

data class NavTab(
  val id: String,
  val title: String,
  val icon: ImageVector
)

val NAV_TABS = listOf(
  NavTab("home", "Home", Icons.Default.Home),
  NavTab("about", "About", Icons.Default.Person),
  NavTab("skills", "Skills", Icons.Default.Code),
  NavTab("projects", "Projects", Icons.Default.Work),
  NavTab("education", "Education", Icons.Default.School),
  NavTab("resume", "Resume", Icons.Default.Description),
  NavTab("contact", "Contact", Icons.Default.Email)
)

enum class ViewMode {
  NATIVE_APP,
  WEB_PORTFOLIO
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioApp() {
  val context = LocalContext.current
  var currentMode by remember { mutableStateOf(ViewMode.NATIVE_APP) }
  var currentSection by remember { mutableStateOf("home") }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "PA",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = ResumeData.NAME,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Software Developer & Data Science",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        },
        actions = {
          // Share quick action
          IconButton(
            onClick = {
              val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Pradeep A Portfolio & Resume")
                putExtra(Intent.EXTRA_TEXT, ResumeData.FULL_RESUME_TEXT)
              }
              context.startActivity(Intent.createChooser(shareIntent, "Share Pradeep's Portfolio"))
            },
            modifier = Modifier.testTag("action_share_portfolio")
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share",
              tint = MaterialTheme.colorScheme.primary
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        )
      )
    },
    bottomBar = {
      if (currentMode == ViewMode.NATIVE_APP) {
        val bottomTabs = listOf(
          NAV_TABS[0], // Home
          NAV_TABS[2], // Skills
          NAV_TABS[3], // Projects
          NAV_TABS[5], // Resume
          NAV_TABS[6]  // Contact
        )

        NavigationBar(
          containerColor = MaterialTheme.colorScheme.surface,
          modifier = Modifier.testTag("portfolio_navigation_bar")
        ) {
          bottomTabs.forEach { tab ->
            val isSelected = currentSection == tab.id
            NavigationBarItem(
              icon = {
                Icon(
                  imageVector = tab.icon,
                  contentDescription = tab.title
                )
              },
              label = { Text(tab.title, fontSize = 11.sp) },
              selected = isSelected,
              onClick = { currentSection = tab.id },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
              ),
              modifier = Modifier.testTag("nav_item_${tab.id}")
            )
          }
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      // Mode Switcher Banner: Native View vs Interactive Web Website
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
          .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        FilterChip(
          selected = currentMode == ViewMode.NATIVE_APP,
          onClick = { currentMode = ViewMode.NATIVE_APP },
          label = { Text("📱 Native App") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.PhoneAndroid,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
          ),
          modifier = Modifier.testTag("chip_mode_native")
        )

        Spacer(modifier = Modifier.width(8.dp))

        FilterChip(
          selected = currentMode == ViewMode.WEB_PORTFOLIO,
          onClick = { currentMode = ViewMode.WEB_PORTFOLIO },
          label = { Text("🌐 Web Website") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
          ),
          modifier = Modifier.testTag("chip_mode_web")
        )
      }

      // Secondary horizontal tabs for native mode (allows reaching all 7 sections easily)
      if (currentMode == ViewMode.NATIVE_APP) {
        ScrollableTabRow(
          selectedTabIndex = NAV_TABS.indexOfFirst { it.id == currentSection }.coerceAtLeast(0),
          containerColor = MaterialTheme.colorScheme.surface,
          contentColor = MaterialTheme.colorScheme.primary,
          edgePadding = 12.dp,
          modifier = Modifier.fillMaxWidth().testTag("tabs_sections")
        ) {
          NAV_TABS.forEach { tab ->
            Tab(
              selected = currentSection == tab.id,
              onClick = { currentSection = tab.id },
              text = {
                Text(
                  text = tab.title,
                  fontWeight = if (currentSection == tab.id) FontWeight.Bold else FontWeight.Normal,
                  fontSize = 13.sp
                )
              },
              modifier = Modifier.testTag("tab_section_${tab.id}")
            )
          }
        }
      }

      Crossfade(
        targetState = currentMode,
        label = "mode_crossfade"
      ) { mode ->
        when (mode) {
          ViewMode.NATIVE_APP -> {
            NativePortfolioView(
              currentSection = currentSection,
              onNavigateToSection = { section -> currentSection = section },
              modifier = Modifier.fillMaxSize()
            )
          }
          ViewMode.WEB_PORTFOLIO -> {
            WebPortfolioView(modifier = Modifier.fillMaxSize())
          }
        }
      }
    }
  }
}
