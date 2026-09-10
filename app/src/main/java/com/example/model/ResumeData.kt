package com.example.model

data class SkillCategory(
  val title: String,
  val icon: String,
  val skills: List<String>,
  val description: String
)

data class ProjectItem(
  val title: String,
  val subtitle: String,
  val description: String,
  val technologies: List<String>,
  val keyFeatures: List<String>,
  val isFeatured: Boolean = true
)

data class EducationItem(
  val institution: String,
  val degree: String,
  val location: String,
  val scoreOrStatus: String,
  val isPursuing: Boolean = false,
  val isDistinction: Boolean = false
)

data class ContactMessage(
  val name: String,
  val email: String,
  val subject: String,
  val message: String,
  val timestamp: Long = System.currentTimeMillis()
)

object ResumeData {
  const val NAME = "PRADEEP A"
  const val TAGLINE = "Software Developer & Data Science Student"
  const val LOCATION = "Salem, Tamil Nadu, India"
  const val PHONE = "+91 8072637400"
  const val EMAIL = "pradeep438072@gmail.com"

  const val SUMMARY =
    "Enthusiastic and motivated Software Developer & Data Science student with strong fundamentals in Python, C++, and MySQL database management. Passionate about leveraging AI and modern technologies to solve real-world problems, as demonstrated by hands-on project experience in voice-assisted healthcare solutions."

  val SKILL_CATEGORIES = listOf(
    SkillCategory(
      title = "Programming Languages",
      icon = "💻",
      skills = listOf("Python", "C++"),
      description = "Core languages used for data manipulation, algorithmic problem solving, and backend logic."
    ),
    SkillCategory(
      title = "Database & Storage",
      icon = "🗄️",
      skills = listOf("MySQL"),
      description = "Structured relational database design, data queries, and secure user profile persistence."
    ),
    SkillCategory(
      title = "Tools & Technologies",
      icon = "🛠️",
      skills = listOf("Git", "Microsoft Excel"),
      description = "Version control, collaborative workflows, and structured tabular data management."
    ),
    SkillCategory(
      title = "Core Concepts",
      icon = "🧠",
      skills = listOf("Data Structures", "Object-Oriented Programming (OOP)", "AI Integration"),
      description = "Strong foundation in software architecture, algorithm design, and intelligent AI model integration."
    )
  )

  val FEATURED_PROJECT = ProjectItem(
    title = "MediVoice AI – Medicine Reminder & Voice Assistant",
    subtitle = "AI-Powered Healthcare Voice Solution",
    description =
      "Designed and developed an AI-powered voice assistant application created to assist users with medication schedules and smart audio reminders.",
    technologies = listOf("Python", "AI Integration", "MySQL", "Voice Interaction", "Prescription DB"),
    keyFeatures = listOf(
      "Designed and developed an AI-powered voice assistant application created to assist users with medication schedules and smart audio reminders.",
      "Implemented voice interaction pipelines to capture user inputs, schedule automated dosage alerts, and convey medicine details effortlessly.",
      "Utilized structured database operations for secure and accurate user profile and prescription management."
    ),
    isFeatured = true
  )

  val EDUCATION_LIST = listOf(
    EducationItem(
      institution = "Sona College of Arts and Science",
      degree = "Bachelor's Degree",
      location = "Salem, India",
      scoreOrStatus = "Pursuing",
      isPursuing = true
    ),
    EducationItem(
      institution = "Sri Gayathiri Matriculation Higher Secondary School",
      degree = "Higher Secondary Certificate (12th Standard)",
      location = "Salem, India",
      scoreOrStatus = "Score: 83.5%",
      isDistinction = true
    ),
    EducationItem(
      institution = "Sri Gayathiri Matriculation Higher Secondary School",
      degree = "Secondary School Leaving Certificate (10th Standard)",
      location = "Salem, India",
      scoreOrStatus = "Score: 84.4%",
      isDistinction = true
    )
  )

  val STRENGTHS = listOf(
    "Strong problem-solving mindset and logical thinking ability.",
    "Eager to learn new tools, technologies, and software frameworks quickly.",
    "Effective team communication and project execution skills."
  )

  val FULL_RESUME_TEXT = """
PRADEEP A
Salem, Tamil Nadu • +91 8072637400 • pradeep438072@gmail.com

PROFESSIONAL SUMMARY
Enthusiastic and motivated Software Developer & Data Science student with strong fundamentals in Python, C++, and MySQL database management. Passionate about leveraging AI and modern technologies to solve real-world problems, as demonstrated by hands-on project experience in voice-assisted healthcare solutions.

TECHNICAL SKILLS
Programming Languages: Python, C++
Database & Tools: MySQL, Microsoft Excel, Git
Core Concepts: Data Structures, Object-Oriented Programming (OOP), AI Integration

KEY PROJECTS
MediVoice AI – Medicine Reminder & Voice Assistant
• Designed and developed an AI-powered voice assistant application created to assist users with medication schedules and smart audio reminders.
• Implemented voice interaction pipelines to capture user inputs, schedule automated dosage alerts, and convey medicine details effortlessly.
• Utilized structured database operations for secure and accurate user profile and prescription management.

EDUCATION
Sona College of Arts and Science, Salem, India
Bachelor's Degree (Pursuing)

Sri Gayathiri Matriculation Higher Secondary School, Salem, India
Higher Secondary Certificate (12th Standard) — Score: 83.5%
Secondary School Leaving Certificate (10th Standard) — Score: 84.4%

STRENGTHS & PERSONAL QUALITIES
• Strong problem-solving mindset and logical thinking ability.
• Eager to learn new tools, technologies, and software frameworks quickly.
• Effective team communication and project execution skills.
""".trimIndent()
}
