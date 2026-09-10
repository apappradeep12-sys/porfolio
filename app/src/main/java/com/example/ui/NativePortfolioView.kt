package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ContactMessage
import com.example.model.EducationItem
import com.example.model.ProjectItem
import com.example.model.ResumeData
import com.example.model.SkillCategory

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NativePortfolioView(
  currentSection: String,
  onNavigateToSection: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(bottom = 32.dp)
      .testTag("native_portfolio_scroll")
  ) {
    when (currentSection) {
      "home" -> HomeSection(onNavigateToSection = onNavigateToSection)
      "about" -> AboutSection(onNavigateToSection = onNavigateToSection)
      "skills" -> SkillsSection()
      "projects" -> ProjectsSection(onNavigateToSection = onNavigateToSection)
      "education" -> EducationSection()
      "resume" -> ResumeSection()
      "contact" -> ContactSection()
      else -> HomeSection(onNavigateToSection = onNavigateToSection)
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeSection(
  onNavigateToSection: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("section_home")
  ) {
    // Availability Badge
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(50))
        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(50))
        .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(Color(0xFF22C55E))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Open to Internships & Entry-Level Roles",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Profile Card with Avatar
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      ),
      shape = RoundedCornerShape(20.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_avatar),
            contentDescription = "Pradeep A Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = ResumeData.NAME,
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
          ),
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = ResumeData.TAGLINE,
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = ResumeData.LOCATION,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Brief Introduction
    Text(
      text = "Introduction",
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.primary,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = ResumeData.SUMMARY,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      lineHeight = 22.sp
    )

    Spacer(modifier = Modifier.height(24.dp))

    // Action Buttons
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Button(
        onClick = { onNavigateToSection("projects") },
        modifier = Modifier
          .weight(1f)
          .testTag("btn_home_view_projects"),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("⚡ Projects", fontWeight = FontWeight.Bold)
      }

      OutlinedButton(
        onClick = { onNavigateToSection("contact") },
        modifier = Modifier
          .weight(1f)
          .testTag("btn_home_contact"),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("✉ Contact Me", fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
      onClick = { onNavigateToSection("resume") },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("btn_home_resume"),
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Share,
        contentDescription = "Share",
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text("View & Share Resume Document")
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Key Highlights Grid
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
      )
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Quick Highlights",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))
        HighlightRow("🎯 Specialization", "Software Dev & Data Science")
        HighlightRow("💻 Core Languages", "Python, C++")
        HighlightRow("🗄️ Database", "MySQL")
        HighlightRow("🎓 Education", "Sona College of Arts & Science")
        HighlightRow("🌟 Key Project", "MediVoice AI (Voice Medicine Assistant)")
      }
    }
  }
}

@Composable
fun HighlightRow(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
  }
}

@Composable
fun AboutSection(
  onNavigateToSection: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("section_about")
  ) {
    Text(
      text = "About Me",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Verified profile from original resume",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      )
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "Professional Summary",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = ResumeData.SUMMARY,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurface,
          lineHeight = 22.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Text(
      text = "Strengths & Personal Qualities",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(10.dp))

    ResumeData.STRENGTHS.forEach { strength ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        )
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = "Check",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = strength,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
      )
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "🎯 Objective",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Motivated college student actively seeking software developer / data science internships to apply technical fundamentals in Python, C++, MySQL, and AI integration to solve high-impact challenges.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillsSection(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("section_skills")
  ) {
    Text(
      text = "Technical Skills",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Strictly categorized skills from resume",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    ResumeData.SKILL_CATEGORIES.forEach { category ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        )
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = category.icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = category.title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            category.skills.forEach { skill ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                  .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                  .padding(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Text(
                  text = skill,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.SemiBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = category.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectsSection(
  onNavigateToSection: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val project = ResumeData.FEATURED_PROJECT

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("section_projects")
  ) {
    Text(
      text = "Key Projects",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Practical project execution & voice-assisted healthcare solution",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Featured Project Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("card_featured_project"),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
      ),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column {
        // Project Image Banner
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.img_medivoice),
            contentDescription = "MediVoice AI Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(Color.Transparent, Color(0xCC0A0F1D))
                )
              )
          )
          Box(
            modifier = Modifier
              .align(Alignment.TopStart)
              .padding(12.dp)
              .clip(RoundedCornerShape(6.dp))
              .background(MaterialTheme.colorScheme.primary)
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Featured",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "FEATURED PROJECT",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
              )
            }
          }
        }

        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = project.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = project.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = project.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Key Architecture & Features:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )

          Spacer(modifier = Modifier.height(8.dp))

          project.keyFeatures.forEach { feature ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.Top
            ) {
              Text(
                text = "✦",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
              )
              Text(
                text = feature,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Technologies Used:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(6.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            project.technologies.forEach { tech ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(MaterialTheme.colorScheme.surface)
                  .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = tech,
                  style = MaterialTheme.typography.labelSmall,
                  fontFamily = FontFamily.Monospace,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun EducationSection(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("section_education")
  ) {
    Text(
      text = "Education & Academic Record",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Academic timeline and merit scores from resume",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    ResumeData.EDUCATION_LIST.forEach { edu ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = edu.institution,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = edu.degree,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
              )
            }

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(
                  if (edu.isDistinction) Color(0x2222C55E) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
                .border(
                  1.dp,
                  if (edu.isDistinction) Color(0x4422C55E) else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                  RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = edu.scoreOrStatus,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (edu.isDistinction) Color(0xFF22C55E) else MaterialTheme.colorScheme.primary
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "📍 ${edu.location}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Academic Distinctions Summary
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
      )
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "🏆 Scholastic Achievements",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "• Higher Secondary Certificate (12th): Distinction score of 83.5%\n• Secondary School Leaving Certificate (10th): Merit distinction score of 84.4%",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }
  }
}

@Composable
fun ResumeSection(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("section_resume")
  ) {
    Text(
      text = "Resume Document",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Exact reproduction of original verified resume",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Action buttons: Download/Share and Copy
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Button(
        onClick = {
          val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Pradeep A - Software Developer Resume")
            putExtra(Intent.EXTRA_TEXT, ResumeData.FULL_RESUME_TEXT)
          }
          context.startActivity(Intent.createChooser(shareIntent, "Share / Download Pradeep's Resume"))
        },
        modifier = Modifier
          .weight(1f)
          .testTag("btn_share_resume"),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(10.dp)
      ) {
        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Share / Export", fontSize = 13.sp)
      }

      OutlinedButton(
        onClick = {
          val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
          val clip = ClipData.newPlainText("Pradeep A Resume", ResumeData.FULL_RESUME_TEXT)
          clipboard.setPrimaryClip(clip)
          Toast.makeText(context, "Full resume copied to clipboard!", Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier
          .weight(1f)
          .testTag("btn_copy_resume"),
        shape = RoundedCornerShape(10.dp)
      ) {
        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Copy Text", fontSize = 13.sp)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Formatted Clean Resume Sheet
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      shape = RoundedCornerShape(12.dp),
      border = CardDefaults.outlinedCardBorder()
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = ResumeData.NAME,
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          ),
          color = MaterialTheme.colorScheme.onSurface
        )

        Text(
          text = "${ResumeData.LOCATION} • ${ResumeData.PHONE} • ${ResumeData.EMAIL}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.primary)

        Text(
          text = "PROFESSIONAL SUMMARY",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = ResumeData.SUMMARY, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "TECHNICAL SKILLS",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "• Programming Languages: Python, C++", style = MaterialTheme.typography.bodySmall)
        Text(text = "• Database & Tools: MySQL, Microsoft Excel, Git", style = MaterialTheme.typography.bodySmall)
        Text(text = "• Core Concepts: Data Structures, OOP, AI Integration", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "KEY PROJECTS",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = ResumeData.FEATURED_PROJECT.title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold
        )
        ResumeData.FEATURED_PROJECT.keyFeatures.forEach {
          Text(text = "• $it", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "EDUCATION",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        ResumeData.EDUCATION_LIST.forEach {
          Text(text = "• ${it.institution} (${it.location}) — ${it.degree} [${it.scoreOrStatus}]", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "STRENGTHS & PERSONAL QUALITIES",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        ResumeData.STRENGTHS.forEach {
          Text(text = "• $it", style = MaterialTheme.typography.bodySmall)
        }
      }
    }
  }
}

@Composable
fun ContactSection(modifier: Modifier = Modifier) {
  val context = LocalContext.current

  var senderName by remember { mutableStateOf("") }
  var senderEmail by remember { mutableStateOf("") }
  var senderSubject by remember { mutableStateOf("") }
  var senderMessage by remember { mutableStateOf("") }
  var submittedMessage by remember { mutableStateOf<ContactMessage?>(null) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp)
      .testTag("section_contact")
  ) {
    Text(
      text = "Get in Touch",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Direct contact options and interactive inquiry form",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Direct Contact Actions
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Button(
        onClick = {
          val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:8072637400"))
          context.startActivity(dialIntent)
        },
        modifier = Modifier
          .weight(1f)
          .testTag("btn_contact_call"),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
          contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(imageVector = Icons.Default.Phone, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Call Phone")
      }

      Button(
        onClick = {
          val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:pradeep438072@gmail.com"))
          context.startActivity(emailIntent)
        },
        modifier = Modifier
          .weight(1f)
          .testTag("btn_contact_email"),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
          contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Send Email")
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
      )
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(text = "📍 Location: ${ResumeData.LOCATION}", style = MaterialTheme.typography.bodySmall)
        Text(text = "📱 Phone: ${ResumeData.PHONE}", style = MaterialTheme.typography.bodySmall)
        Text(text = "✉ Email: ${ResumeData.EMAIL}", style = MaterialTheme.typography.bodySmall)
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Interactive Contact Form
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
      ),
      shape = RoundedCornerShape(16.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = "Send a Direct Message",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = senderName,
          onValueChange = {
            senderName = it
            errorMessage = null
          },
          label = { Text("Your Name") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_contact_name"),
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = senderEmail,
          onValueChange = {
            senderEmail = it
            errorMessage = null
          },
          label = { Text("Your Email") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_contact_email"),
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = senderSubject,
          onValueChange = {
            senderSubject = it
            errorMessage = null
          },
          label = { Text("Subject") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_contact_subject"),
          singleLine = true,
          colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = senderMessage,
          onValueChange = {
            senderMessage = it
            errorMessage = null
          },
          label = { Text("Message") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_contact_message"),
          minLines = 4,
          colors = OutlinedTextFieldDefaults.colors()
        )

        if (errorMessage != null) {
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = errorMessage ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = {
            if (senderName.isBlank() || senderEmail.isBlank() || senderSubject.isBlank() || senderMessage.isBlank()) {
              errorMessage = "Please fill in all fields before sending."
              return@Button
            }

            val msg = ContactMessage(
              name = senderName.trim(),
              email = senderEmail.trim(),
              subject = senderSubject.trim(),
              message = senderMessage.trim()
            )
            submittedMessage = msg

            // Launch Email Intent to Pradeep
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
              data = Uri.parse("mailto:pradeep438072@gmail.com")
              putExtra(Intent.EXTRA_SUBJECT, msg.subject)
              putExtra(
                Intent.EXTRA_TEXT,
                "From: ${msg.name} (${msg.email})\n\n${msg.message}"
              )
            }
            try {
              context.startActivity(emailIntent)
            } catch (_: Exception) {
              Toast.makeText(context, "Draft prepared for ${ResumeData.EMAIL}", Toast.LENGTH_SHORT).show()
            }

            // Reset inputs
            senderName = ""
            senderEmail = ""
            senderSubject = ""
            senderMessage = ""
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("btn_contact_send"),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          ),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(imageVector = Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Send Message to Pradeep")
        }

        // Confirmation Banner
        AnimatedVisibility(
          visible = submittedMessage != null,
          enter = fadeIn(),
          exit = fadeOut()
        ) {
          submittedMessage?.let { msg ->
            Spacer(modifier = Modifier.height(14.dp))
            Card(
              colors = CardDefaults.cardColors(
                containerColor = Color(0x2222C55E)
              ),
              border = CardDefaults.outlinedCardBorder(),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF22C55E))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "Message Prepared Successfully!",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF22C55E)
                  )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Addressed to: ${ResumeData.EMAIL}\nSubject: ${msg.subject}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }
  }
}
