/**
 * ResumeFolio AI - Main Application Controller
 * Handles Uploads, State Management, Admin Forms, Live Rendering & Exports
 */

// Default Sample Data (from verified resume)
const DEFAULT_RESUME_DATA = {
  name: "Pradeep A",
  tagline: "Software Developer & Data Science Student",
  email: "pradeep438072@gmail.com",
  phone: "+91 8072637400",
  location: "Salem, Tamil Nadu, India",
  links: {
    github: "https://github.com",
    linkedin: "https://linkedin.com",
    portfolio: ""
  },
  summary: "Enthusiastic and motivated Software Developer & Data Science student with strong fundamentals in Python, C++, and MySQL database management. Passionate about leveraging AI and modern technologies to solve real-world problems, as demonstrated by hands-on project experience in voice-assisted healthcare solutions.",
  skills: {
    languages: ["Python", "C++", "SQL"],
    frontend: ["HTML5", "CSS3", "JavaScript"],
    backend: ["Python APIs", "REST Architecture"],
    databases: ["MySQL"],
    tools: ["Git", "GitHub", "Microsoft Excel"],
    frameworks_concepts: ["Data Structures", "Object-Oriented Programming (OOP)", "AI Integration"]
  },
  experience: [
    {
      role: "Software Development & AI Intern",
      company: "Healthcare Tech Innovation Lab",
      period: "2023 – 2024",
      location: "Salem, India",
      bullets: [
        "Collaborated on designing audio pipelines and intent recognition logic for medication reminders.",
        "Created relational schema and optimized query latency for fast patient data retrieval."
      ]
    }
  ],
  projects: [
    {
      title: "MediVoice AI – Medicine Reminder & Voice Assistant",
      description: "Designed and developed an AI-powered voice assistant application created to assist users with medication schedules and smart audio reminders.",
      bullets: [
        "Implemented voice interaction pipelines to capture user inputs, schedule automated dosage alerts, and convey medicine details effortlessly.",
        "Engineered schedule triggers for accurate timing and timely audio delivery for patient prescription adherence.",
        "Utilized structured database operations for secure and accurate user profile and prescription management."
      ],
      technologies: ["Python", "AI Integration", "MySQL", "Voice Pipeline"],
      github: "https://github.com",
      demo: ""
    }
  ],
  education: [
    {
      institution: "Sona College of Arts and Science",
      degree: "Bachelor's Degree in Data Science & Computer Science",
      year: "Pursuing",
      score: "First Class Distinction",
      details: "Focusing on Software Development, Data Science, Data Structures, Object-Oriented Programming, and Database Systems."
    },
    {
      institution: "Sri Gayathiri Matriculation Higher Secondary School",
      degree: "Higher Secondary Certificate (12th Standard)",
      year: "Completed",
      score: "83.5% Distinction",
      details: "Strong academic record in Mathematics, Physics, and Chemistry."
    },
    {
      institution: "Sri Gayathiri Matriculation Higher Secondary School",
      degree: "Secondary School Leaving Certificate (10th Standard)",
      year: "Completed",
      score: "84.4% Distinction",
      details: "Outstanding academic performance across secondary sciences and mathematics."
    }
  ],
  strengths: [
    "Strong problem-solving mindset and logical thinking ability.",
    "Eager to learn new tools, technologies, and software frameworks quickly.",
    "Effective team communication and disciplined project execution skills."
  ]
};

// Global App State
let appState = {
  data: JSON.parse(JSON.stringify(DEFAULT_RESUME_DATA)),
  currentView: 'dashboard', // 'dashboard', 'admin', 'preview'
  theme: 'cyan',
  isLightMode: false
};

// Initialize Application
document.addEventListener('DOMContentLoaded', () => {
  // Load saved state from LocalStorage if available
  const saved = localStorage.getItem('resumefolio_data');
  if (saved) {
    try {
      appState.data = JSON.parse(saved);
    } catch (e) {
      console.error('Error loading saved state', e);
    }
  }

  // Setup Event Listeners
  setupNavigation();
  setupUploadDropzone();
  setupThemeControls();
  setupAdminPanel();
  setupExportTools();
  setupContactForm();

  // Initial Render
  updateExtractedStats();
  renderAdminForms();
  renderPortfolio();
});

/* ==========================================================
   Navigation & View Switching
   ========================================================== */
function setupNavigation() {
  const tabs = document.querySelectorAll('.nav-tab-btn');
  tabs.forEach(tab => {
    tab.addEventListener('click', () => {
      const view = tab.getAttribute('data-view');
      switchView(view);
    });
  });
}

function switchView(viewName) {
  appState.currentView = viewName;
  document.querySelectorAll('.nav-tab-btn').forEach(btn => {
    btn.classList.toggle('active', btn.getAttribute('data-view') === viewName);
  });
  document.querySelectorAll('.view-section').forEach(sec => {
    sec.classList.toggle('active', sec.id === `view-${viewName}`);
  });

  if (viewName === 'preview') {
    renderPortfolio();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  } else if (viewName === 'admin') {
    renderAdminForms();
  }
}

/* ==========================================================
   Theme & Dark/Light Mode
   ========================================================== */
function setupThemeControls() {
  const themeSelect = document.getElementById('themeSelect');
  const lightModeBtn = document.getElementById('toggleLightModeBtn');

  if (themeSelect) {
    themeSelect.value = appState.theme;
    themeSelect.addEventListener('change', (e) => {
      appState.theme = e.target.value;
      document.body.setAttribute('data-theme', appState.theme);
    });
  }

  if (lightModeBtn) {
    lightModeBtn.addEventListener('click', () => {
      appState.isLightMode = !appState.isLightMode;
      document.body.classList.toggle('light-mode', appState.isLightMode);
      lightModeBtn.textContent = appState.isLightMode ? '🌙 Dark' : '☀️ Light';
    });
  }
}

/* ==========================================================
   Upload Dropzone & Resume Parser Pipeline
   ========================================================== */
function setupUploadDropzone() {
  const dropzone = document.getElementById('dropzone');
  const fileInput = document.getElementById('resumeFileInput');
  const loadSampleBtn = document.getElementById('loadSampleBtn');
  const pasteTextBtn = document.getElementById('pasteTextBtn');
  const pasteModal = document.getElementById('pasteModal');
  const cancelPasteBtn = document.getElementById('cancelPasteBtn');
  const processPasteBtn = document.getElementById('processPasteBtn');

  if (dropzone && fileInput) {
    dropzone.addEventListener('click', () => fileInput.click());

    dropzone.addEventListener('dragover', (e) => {
      e.preventDefault();
      dropzone.classList.add('dragover');
    });

    dropzone.addEventListener('dragleave', () => {
      dropzone.classList.remove('dragover');
    });

    dropzone.addEventListener('drop', (e) => {
      e.preventDefault();
      dropzone.classList.remove('dragover');
      if (e.dataTransfer.files && e.dataTransfer.files[0]) {
        handleFileUpload(e.dataTransfer.files[0]);
      }
    });

    fileInput.addEventListener('change', (e) => {
      if (e.target.files && e.target.files[0]) {
        handleFileUpload(e.target.files[0]);
      }
    });
  }

  if (loadSampleBtn) {
    loadSampleBtn.addEventListener('click', () => {
      simulateParsingPipeline("Sample Resume (Pradeep A)", () => {
        appState.data = JSON.parse(JSON.stringify(DEFAULT_RESUME_DATA));
        saveState();
        updateExtractedStats();
        renderAdminForms();
        renderPortfolio();
      });
    });
  }

  if (pasteTextBtn && pasteModal) {
    pasteTextBtn.addEventListener('click', () => {
      pasteModal.classList.add('active');
    });
  }

  if (cancelPasteBtn && pasteModal) {
    cancelPasteBtn.addEventListener('click', () => {
      pasteModal.classList.remove('active');
    });
  }

  if (processPasteBtn && pasteModal) {
    processPasteBtn.addEventListener('click', () => {
      const text = document.getElementById('rawResumeTextInput').value.trim();
      if (!text) {
        alert('Please paste resume text before processing.');
        return;
      }
      pasteModal.classList.remove('active');
      simulateParsingPipeline("Pasted Text", () => {
        const parsed = ResumeParser.parseResume(text);
        mergeExtractedData(parsed);
      });
    });
  }
}

async function handleFileUpload(file) {
  const ext = file.name.split('.').pop().toLowerCase();
  const progressCard = document.getElementById('progressCard');
  const progressBar = document.getElementById('progressBar');
  const progressStep = document.getElementById('progressStep');

  progressCard.style.display = 'block';
  progressBar.style.width = '15%';
  progressStep.textContent = `Reading ${file.name}...`;

  try {
    const arrayBuffer = await file.arrayBuffer();
    let text = '';

    if (ext === 'pdf') {
      progressBar.style.width = '40%';
      progressStep.textContent = 'Extracting text streams via PDF.js...';
      text = await ResumeParser.extractTextFromPDF(arrayBuffer);
    } else if (ext === 'docx') {
      progressBar.style.width = '40%';
      progressStep.textContent = 'Unpacking Word XML document via Mammoth...';
      text = await ResumeParser.extractTextFromDOCX(arrayBuffer);
    } else {
      // Plain text fallback
      progressBar.style.width = '40%';
      progressStep.textContent = 'Reading plain text buffer...';
      const textDecoder = new TextDecoder('utf-8');
      text = textDecoder.decode(arrayBuffer);
    }

    progressBar.style.width = '70%';
    progressStep.textContent = 'Analyzing sections and skills with NLP heuristics...';

    setTimeout(() => {
      const parsed = ResumeParser.parseResume(text);
      progressBar.style.width = '100%';
      progressStep.textContent = 'Portfolio generation complete!';

      setTimeout(() => {
        mergeExtractedData(parsed);
        progressCard.style.display = 'none';
        switchView('preview');
      }, 500);
    }, 400);

  } catch (err) {
    console.error('File parsing error:', err);
    progressStep.textContent = `Error: ${err.message}. Please try again or paste text.`;
    progressBar.style.backgroundColor = '#ef4444';
  }
}

function simulateParsingPipeline(title, onComplete) {
  const progressCard = document.getElementById('progressCard');
  const progressBar = document.getElementById('progressBar');
  const progressStep = document.getElementById('progressStep');

  progressCard.style.display = 'block';
  progressBar.style.width = '20%';
  progressStep.textContent = `Reading ${title}...`;

  setTimeout(() => {
    progressBar.style.width = '60%';
    progressStep.textContent = 'Extracting candidate profile, skills, projects, and education...';

    setTimeout(() => {
      progressBar.style.width = '100%';
      progressStep.textContent = 'Structuring portfolio schema...';

      setTimeout(() => {
        progressCard.style.display = 'none';
        onComplete();
        switchView('preview');
      }, 400);
    }, 500);
  }, 400);
}

function mergeExtractedData(parsed) {
  appState.data.name = parsed.name || appState.data.name;
  appState.data.tagline = parsed.tagline || appState.data.tagline;
  appState.data.email = parsed.email || appState.data.email;
  appState.data.phone = parsed.phone || appState.data.phone;
  appState.data.location = parsed.location || appState.data.location;

  if (parsed.links) {
    if (parsed.links.github) appState.data.links.github = parsed.links.github;
    if (parsed.links.linkedin) appState.data.links.linkedin = parsed.links.linkedin;
    if (parsed.links.portfolio) appState.data.links.portfolio = parsed.links.portfolio;
  }

  if (parsed.summary) appState.data.summary = parsed.summary;

  // Skills
  if (parsed.skills) {
    const hasAnySkill = Object.values(parsed.skills).some(arr => arr.length > 0);
    if (hasAnySkill) {
      appState.data.skills = parsed.skills;
    }
  }

  // Projects
  if (parsed.projects && parsed.projects.length > 0) {
    appState.data.projects = parsed.projects;
  }

  // Experience
  if (parsed.experience && parsed.experience.length > 0) {
    appState.data.experience = parsed.experience;
  }

  // Education
  if (parsed.education && parsed.education.length > 0) {
    appState.data.education = parsed.education;
  }

  // Strengths
  if (parsed.strengths && parsed.strengths.length > 0) {
    appState.data.strengths = parsed.strengths;
  }

  saveState();
  updateExtractedStats();
  renderAdminForms();
  renderPortfolio();
}

function saveState() {
  localStorage.setItem('resumefolio_data', JSON.stringify(appState.data));
}

function updateExtractedStats() {
  const d = appState.data;
  const nameEl = document.getElementById('statCandidateName');
  const skillsCountEl = document.getElementById('statSkillsCount');
  const projCountEl = document.getElementById('statProjectsCount');
  const expCountEl = document.getElementById('statExpCount');

  if (nameEl) nameEl.textContent = d.name || 'Not specified';

  let totalSkills = 0;
  if (d.skills) {
    Object.values(d.skills).forEach(arr => totalSkills += arr.length);
  }
  if (skillsCountEl) skillsCountEl.textContent = totalSkills;
  if (projCountEl) projCountEl.textContent = (d.projects || []).length;
  if (expCountEl) expCountEl.textContent = (d.experience || []).length + (d.education || []).length;
}

/* ==========================================================
   Admin / Edit Panel Controller
   ========================================================== */
function setupAdminPanel() {
  const tabs = document.querySelectorAll('.admin-tab');
  tabs.forEach(tab => {
    tab.addEventListener('click', () => {
      tabs.forEach(t => t.classList.remove('active'));
      tab.classList.add('active');
      const target = tab.getAttribute('data-tab');
      document.querySelectorAll('.admin-tab-content').forEach(c => {
        c.classList.toggle('active', c.id === `admin-tab-${target}`);
      });
    });
  });

  // Save changes button
  const saveBtn = document.getElementById('adminSaveBtn');
  if (saveBtn) {
    saveBtn.addEventListener('click', () => {
      collectAdminFormData();
      saveState();
      updateExtractedStats();
      renderPortfolio();
      alert('Portfolio details updated successfully!');
      switchView('preview');
    });
  }

  // Add Item buttons
  setupAdminAddButtons();
}

function renderAdminForms() {
  const d = appState.data;

  // Personal
  setVal('adminName', d.name);
  setVal('adminTagline', d.tagline);
  setVal('adminEmail', d.email);
  setVal('adminPhone', d.phone);
  setVal('adminLocation', d.location);
  setVal('adminGithub', d.links?.github || '');
  setVal('adminLinkedin', d.links?.linkedin || '');
  setVal('adminPortfolio', d.links?.portfolio || '');
  setVal('adminSummary', d.summary || '');

  // Skills Inputs
  setVal('adminSkillsLanguages', (d.skills?.languages || []).join(', '));
  setVal('adminSkillsFrontend', (d.skills?.frontend || []).join(', '));
  setVal('adminSkillsBackend', (d.skills?.backend || []).join(', '));
  setVal('adminSkillsDatabases', (d.skills?.databases || []).join(', '));
  setVal('adminSkillsTools', (d.skills?.tools || []).join(', '));
  setVal('adminSkillsConcepts', (d.skills?.frameworks_concepts || []).join(', '));

  // Projects list
  renderAdminProjectsList();

  // Experience list
  renderAdminExperienceList();

  // Education list
  renderAdminEducationList();
}

function setVal(id, val) {
  const el = document.getElementById(id);
  if (el) el.value = val;
}

function getVal(id) {
  const el = document.getElementById(id);
  return el ? el.value.trim() : '';
}

function collectAdminFormData() {
  appState.data.name = getVal('adminName');
  appState.data.tagline = getVal('adminTagline');
  appState.data.email = getVal('adminEmail');
  appState.data.phone = getVal('adminPhone');
  appState.data.location = getVal('adminLocation');

  if (!appState.data.links) appState.data.links = {};
  appState.data.links.github = getVal('adminGithub');
  appState.data.links.linkedin = getVal('adminLinkedin');
  appState.data.links.portfolio = getVal('adminPortfolio');

  appState.data.summary = getVal('adminSummary');

  // Skills
  const parseList = (id) => getVal(id).split(',').map(s => s.trim()).filter(Boolean);
  appState.data.skills = {
    languages: parseList('adminSkillsLanguages'),
    frontend: parseList('adminSkillsFrontend'),
    backend: parseList('adminSkillsBackend'),
    databases: parseList('adminSkillsDatabases'),
    tools: parseList('adminSkillsTools'),
    frameworks_concepts: parseList('adminSkillsConcepts')
  };
}

function renderAdminProjectsList() {
  const container = document.getElementById('adminProjectsList');
  if (!container) return;
  container.innerHTML = '';

  (appState.data.projects || []).forEach((proj, index) => {
    const card = document.createElement('div');
    card.className = 'editable-item-card';
    card.innerHTML = `
      <button type="button" class="item-delete-btn" onclick="deleteAdminProject(${index})">✕ Remove</button>
      <div class="form-row">
        <div class="form-group">
          <label>Project Title</label>
          <input type="text" class="form-control" value="${escapeHtml(proj.title)}" onchange="updateProjectField(${index}, 'title', this.value)" />
        </div>
        <div class="form-group">
          <label>Technologies (comma separated)</label>
          <input type="text" class="form-control" value="${escapeHtml((proj.technologies || []).join(', '))}" onchange="updateProjectField(${index}, 'tech', this.value)" />
        </div>
      </div>
      <div class="form-group" style="margin-bottom: 0.8rem;">
        <label>Description</label>
        <textarea class="form-control" onchange="updateProjectField(${index}, 'description', this.value)">${escapeHtml(proj.description || '')}</textarea>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>GitHub Repository URL</label>
          <input type="text" class="form-control" value="${escapeHtml(proj.github || '')}" onchange="updateProjectField(${index}, 'github', this.value)" placeholder="https://github.com/..." />
        </div>
        <div class="form-group">
          <label>Live Demo URL</label>
          <input type="text" class="form-control" value="${escapeHtml(proj.demo || '')}" onchange="updateProjectField(${index}, 'demo', this.value)" placeholder="https://..." />
        </div>
      </div>
    `;
    container.appendChild(card);
  });
}

window.deleteAdminProject = function(index) {
  appState.data.projects.splice(index, 1);
  renderAdminProjectsList();
};

window.updateProjectField = function(index, field, val) {
  if (field === 'tech') {
    appState.data.projects[index].technologies = val.split(',').map(s => s.trim()).filter(Boolean);
  } else {
    appState.data.projects[index][field] = val;
  }
};

function renderAdminExperienceList() {
  const container = document.getElementById('adminExperienceList');
  if (!container) return;
  container.innerHTML = '';

  (appState.data.experience || []).forEach((exp, index) => {
    const card = document.createElement('div');
    card.className = 'editable-item-card';
    card.innerHTML = `
      <button type="button" class="item-delete-btn" onclick="deleteAdminExperience(${index})">✕ Remove</button>
      <div class="form-row">
        <div class="form-group">
          <label>Role / Position</label>
          <input type="text" class="form-control" value="${escapeHtml(exp.role)}" onchange="updateExperienceField(${index}, 'role', this.value)" />
        </div>
        <div class="form-group">
          <label>Company / Organization</label>
          <input type="text" class="form-control" value="${escapeHtml(exp.company)}" onchange="updateExperienceField(${index}, 'company', this.value)" />
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Period / Dates</label>
          <input type="text" class="form-control" value="${escapeHtml(exp.period || '')}" onchange="updateExperienceField(${index}, 'period', this.value)" placeholder="e.g. 2023 – 2024" />
        </div>
        <div class="form-group">
          <label>Location</label>
          <input type="text" class="form-control" value="${escapeHtml(exp.location || '')}" onchange="updateExperienceField(${index}, 'location', this.value)" />
        </div>
      </div>
    `;
    container.appendChild(card);
  });
}

window.deleteAdminExperience = function(index) {
  appState.data.experience.splice(index, 1);
  renderAdminExperienceList();
};

window.updateExperienceField = function(index, field, val) {
  appState.data.experience[index][field] = val;
};

function renderAdminEducationList() {
  const container = document.getElementById('adminEducationList');
  if (!container) return;
  container.innerHTML = '';

  (appState.data.education || []).forEach((edu, index) => {
    const card = document.createElement('div');
    card.className = 'editable-item-card';
    card.innerHTML = `
      <button type="button" class="item-delete-btn" onclick="deleteAdminEducation(${index})">✕ Remove</button>
      <div class="form-row">
        <div class="form-group">
          <label>Institution / University / School</label>
          <input type="text" class="form-control" value="${escapeHtml(edu.institution)}" onchange="updateEduField(${index}, 'institution', this.value)" />
        </div>
        <div class="form-group">
          <label>Degree / Certificate</label>
          <input type="text" class="form-control" value="${escapeHtml(edu.degree)}" onchange="updateEduField(${index}, 'degree', this.value)" />
        </div>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>Score / Percentage / GPA</label>
          <input type="text" class="form-control" value="${escapeHtml(edu.score || '')}" onchange="updateEduField(${index}, 'score', this.value)" placeholder="e.g. 84.4% Distinction" />
        </div>
        <div class="form-group">
          <label>Status / Year</label>
          <input type="text" class="form-control" value="${escapeHtml(edu.year || '')}" onchange="updateEduField(${index}, 'year', this.value)" placeholder="e.g. Pursuing / 2024" />
        </div>
      </div>
    `;
    container.appendChild(card);
  });
}

window.deleteAdminEducation = function(index) {
  appState.data.education.splice(index, 1);
  renderAdminEducationList();
};

window.updateEduField = function(index, field, val) {
  appState.data.education[index][field] = val;
};

function setupAdminAddButtons() {
  const addProjBtn = document.getElementById('addProjectBtn');
  if (addProjBtn) {
    addProjBtn.addEventListener('click', () => {
      if (!appState.data.projects) appState.data.projects = [];
      appState.data.projects.push({
        title: 'New Project',
        description: 'Project description here...',
        bullets: ['Feature 1 details', 'Performance optimization'],
        technologies: ['Python', 'SQL'],
        github: '',
        demo: ''
      });
      renderAdminProjectsList();
    });
  }

  const addExpBtn = document.getElementById('addExpBtn');
  if (addExpBtn) {
    addExpBtn.addEventListener('click', () => {
      if (!appState.data.experience) appState.data.experience = [];
      appState.data.experience.push({
        role: 'Software Developer',
        company: 'Company Name',
        period: '2024 – Present',
        location: 'City, Country',
        bullets: ['Implemented core functional modules.']
      });
      renderAdminExperienceList();
    });
  }

  const addEduBtn = document.getElementById('addEduBtn');
  if (addEduBtn) {
    addEduBtn.addEventListener('click', () => {
      if (!appState.data.education) appState.data.education = [];
      appState.data.education.push({
        institution: 'College / Institute Name',
        degree: 'Bachelor Degree',
        score: 'Distinction',
        year: 'Pursuing',
        details: ''
      });
      renderAdminEducationList();
    });
  }
}

/* ==========================================================
   Live Portfolio Renderer
   ========================================================== */
function renderPortfolio() {
  const d = appState.data;

  // Hero Section
  document.getElementById('pCandidateName').textContent = d.name || 'Candidate Name';
  document.getElementById('pTagline').textContent = d.tagline || 'Software Developer';
  document.getElementById('pSummary').textContent = d.summary || '';

  // Avatar card
  document.getElementById('pAvatarName').textContent = d.name || '';
  document.getElementById('pAvatarLocation').textContent = d.location || 'Location Not Specified';

  // Social & Contact pills in Hero
  const contactPills = document.getElementById('pHeroPills');
  contactPills.innerHTML = '';
  if (d.email) {
    contactPills.innerHTML += `<div class="hero-pill-item">📧 ${escapeHtml(d.email)}</div>`;
  }
  if (d.phone) {
    contactPills.innerHTML += `<div class="hero-pill-item">📱 ${escapeHtml(d.phone)}</div>`;
  }
  if (d.location) {
    contactPills.innerHTML += `<div class="hero-pill-item">📍 ${escapeHtml(d.location)}</div>`;
  }

  // About Section
  document.getElementById('pAboutSummary').textContent = d.summary || 'Summary profile not provided.';
  const strengthsContainer = document.getElementById('pStrengthsList');
  if (strengthsContainer) {
    strengthsContainer.innerHTML = '';
    const list = d.strengths && d.strengths.length > 0 ? d.strengths : [
      "Problem-solving mindset and logical thinking ability.",
      "Quick learner adaptable to new software frameworks.",
      "Effective team communication and disciplined execution."
    ];
    list.forEach(st => {
      strengthsContainer.innerHTML += `
        <div style="display: flex; gap: 0.6rem; align-items: flex-start; padding: 0.75rem; background: rgba(255,255,255,0.02); border-radius: 8px; border: 1px solid var(--border);">
          <span style="color: var(--accent);">✔</span>
          <span style="font-size: 0.92rem;">${escapeHtml(st)}</span>
        </div>
      `;
    });
  }

  // Skills Section
  renderSkillsSection(d.skills);

  // Experience Section
  renderExperienceSection(d.experience);

  // Projects Section
  renderProjectsSection(d.projects);

  // Education Section
  renderEducationSection(d.education);

  // Resume Sheet
  renderResumeSheet(d);

  // Contact links
  renderContactLinks(d);
}

function renderSkillsSection(skills) {
  const container = document.getElementById('pSkillsGrid');
  if (!container) return;
  container.innerHTML = '';

  const categories = [
    { key: 'languages', title: 'Programming Languages', icon: '💻' },
    { key: 'frontend', title: 'Frontend Technologies', icon: '🎨' },
    { key: 'backend', title: 'Backend Technologies', icon: '⚙️' },
    { key: 'databases', title: 'Databases & Storage', icon: '🗄️' },
    { key: 'tools', title: 'Tools & Platforms', icon: '🛠️' },
    { key: 'frameworks_concepts', title: 'Core Concepts & Frameworks', icon: '🧠' }
  ];

  let hasAny = false;
  categories.forEach(cat => {
    const list = skills ? (skills[cat.key] || []) : [];
    if (list.length > 0) {
      hasAny = true;
      const pillsHtml = list.map(s => `<span class="skill-pill">${escapeHtml(s)}</span>`).join('');
      container.innerHTML += `
        <div class="skill-category-card">
          <h3 class="skill-cat-title"><span>${cat.icon}</span> ${cat.title}</h3>
          <div class="pills-cloud">${pillsHtml}</div>
        </div>
      `;
    }
  });

  const sec = document.getElementById('section-skills');
  if (sec) sec.style.display = hasAny ? 'block' : 'none';
}

function renderExperienceSection(expList) {
  const sec = document.getElementById('section-experience');
  const container = document.getElementById('pExperienceTimeline');
  if (!container || !sec) return;

  if (!expList || expList.length === 0) {
    sec.style.display = 'none';
    return;
  }

  sec.style.display = 'block';
  container.innerHTML = '';

  expList.forEach(item => {
    const bulletsHtml = (item.bullets || []).map(b => `<li>${escapeHtml(b)}</li>`).join('');
    container.innerHTML += `
      <div class="timeline-item">
        <div class="timeline-dot"></div>
        <div class="timeline-card">
          <div class="timeline-title">${escapeHtml(item.role)}</div>
          <div class="timeline-sub">${escapeHtml(item.company)}</div>
          <div class="timeline-meta">${escapeHtml(item.period || '')} ${item.location ? '• ' + escapeHtml(item.location) : ''}</div>
          ${bulletsHtml ? `<ul class="project-bullets" style="margin-top: 0.5rem;">${bulletsHtml}</ul>` : ''}
        </div>
      </div>
    `;
  });
}

function renderProjectsSection(projects) {
  const sec = document.getElementById('section-projects');
  const container = document.getElementById('pProjectsGrid');
  if (!container || !sec) return;

  if (!projects || projects.length === 0) {
    sec.style.display = 'none';
    return;
  }

  sec.style.display = 'block';
  container.innerHTML = '';

  projects.forEach(proj => {
    const techHtml = (proj.technologies || []).map(t => `<span class="format-tag">${escapeHtml(t)}</span>`).join('');
    const bulletsHtml = (proj.bullets || []).map(b => `<li>${escapeHtml(b)}</li>`).join('');

    let linksHtml = '';
    if (proj.github) {
      linksHtml += `<a href="${escapeHtml(proj.github)}" target="_blank" class="btn btn-secondary" style="padding: 0.35rem 0.75rem; font-size: 0.8rem;">📂 GitHub</a>`;
    }
    if (proj.demo) {
      linksHtml += `<a href="${escapeHtml(proj.demo)}" target="_blank" class="btn btn-primary" style="padding: 0.35rem 0.75rem; font-size: 0.8rem;">🚀 Live Demo</a>`;
    }

    container.innerHTML += `
      <div class="project-card">
        <div class="project-top-line"></div>
        <h3 class="project-title">${escapeHtml(proj.title)}</h3>
        <p class="project-desc">${escapeHtml(proj.description || '')}</p>
        ${bulletsHtml ? `<ul class="project-bullets">${bulletsHtml}</ul>` : ''}
        <div class="project-footer">
          <div style="display: flex; flex-wrap: wrap; gap: 0.4rem;">${techHtml}</div>
          <div class="project-links">${linksHtml}</div>
        </div>
      </div>
    `;
  });
}

function renderEducationSection(education) {
  const sec = document.getElementById('section-education');
  const container = document.getElementById('pEducationTimeline');
  if (!container || !sec) return;

  if (!education || education.length === 0) {
    sec.style.display = 'none';
    return;
  }

  sec.style.display = 'block';
  container.innerHTML = '';

  education.forEach(edu => {
    container.innerHTML += `
      <div class="timeline-item">
        <div class="timeline-dot"></div>
        <div class="timeline-card">
          <div class="timeline-title">${escapeHtml(edu.institution)}</div>
          <div class="timeline-sub">${escapeHtml(edu.degree)}</div>
          <div class="timeline-meta">${escapeHtml(edu.year || '')} ${edu.score ? '• ' + escapeHtml(edu.score) : ''}</div>
          ${edu.details ? `<p style="font-size: 0.88rem; color: var(--text-muted);">${escapeHtml(edu.details)}</p>` : ''}
        </div>
      </div>
    `;
  });
}

function renderResumeSheet(d) {
  const sheet = document.getElementById('resumeSheetContent');
  if (!sheet) return;

  let skillsListText = '';
  if (d.skills) {
    const parts = [];
    if (d.skills.languages?.length) parts.push(`<strong>Languages:</strong> ${escapeHtml(d.skills.languages.join(', '))}`);
    if (d.skills.databases?.length) parts.push(`<strong>Databases:</strong> ${escapeHtml(d.skills.databases.join(', '))}`);
    if (d.skills.tools?.length) parts.push(`<strong>Tools:</strong> ${escapeHtml(d.skills.tools.join(', '))}`);
    if (d.skills.frameworks_concepts?.length) parts.push(`<strong>Core Concepts:</strong> ${escapeHtml(d.skills.frameworks_concepts.join(', '))}`);
    skillsListText = parts.map(p => `<p style="margin-bottom: 0.3rem;">${p}</p>`).join('');
  }

  sheet.innerHTML = `
    <div class="resume-header">
      <h2>${escapeHtml(d.name)}</h2>
      <p style="color: #475569; font-size: 0.9rem;">
        ${escapeHtml(d.location || '')} &nbsp;•&nbsp; ${escapeHtml(d.phone || '')} &nbsp;•&nbsp; ${escapeHtml(d.email || '')}
      </p>
    </div>

    ${d.summary ? `
      <div class="resume-sec-title">PROFESSIONAL SUMMARY</div>
      <p>${escapeHtml(d.summary)}</p>
    ` : ''}

    ${skillsListText ? `
      <div class="resume-sec-title">TECHNICAL SKILLS</div>
      ${skillsListText}
    ` : ''}

    ${d.projects?.length ? `
      <div class="resume-sec-title">KEY PROJECTS</div>
      ${d.projects.map(p => `
        <p><strong>${escapeHtml(p.title)}</strong></p>
        <p style="color: #475569; font-size: 0.88rem;">${escapeHtml(p.description || '')}</p>
        ${p.bullets?.length ? `<ul style="padding-left: 1.25rem; margin-bottom: 0.5rem;">${p.bullets.map(b => `<li>${escapeHtml(b)}</li>`).join('')}</ul>` : ''}
      `).join('')}
    ` : ''}

    ${d.education?.length ? `
      <div class="resume-sec-title">EDUCATION</div>
      ${d.education.map(e => `
        <p><strong>${escapeHtml(e.institution)}</strong> — ${escapeHtml(e.degree)} (${escapeHtml(e.year || '')})</p>
        ${e.score ? `<p style="color: #475569; font-size: 0.88rem;">Score: ${escapeHtml(e.score)}</p>` : ''}
      `).join('')}
    ` : ''}
  `;
}

function renderContactLinks(d) {
  const mailtoEl = document.getElementById('contactEmailLink');
  const phoneEl = document.getElementById('contactPhoneLink');
  const locEl = document.getElementById('contactLocationText');

  if (mailtoEl) {
    mailtoEl.href = `mailto:${d.email || ''}`;
    mailtoEl.querySelector('.contact-val').textContent = d.email || 'Email not provided';
  }
  if (phoneEl) {
    phoneEl.href = `tel:${d.phone || ''}`;
    phoneEl.querySelector('.contact-val').textContent = d.phone || 'Phone not provided';
  }
  if (locEl) {
    locEl.textContent = d.location || 'Location not specified';
  }
}

/* ==========================================================
   Contact Form Generator
   ========================================================== */
function setupContactForm() {
  const form = document.getElementById('portfolioContactForm');
  if (!form) return;

  form.addEventListener('submit', (e) => {
    e.preventDefault();
    const name = document.getElementById('formName').value.trim();
    const email = document.getElementById('formEmail').value.trim();
    const subject = document.getElementById('formSubject').value.trim();
    const msg = document.getElementById('formMessage').value.trim();

    const targetEmail = appState.data.email || 'pradeep438072@gmail.com';
    const mailtoUrl = `mailto:${targetEmail}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent("From: " + name + " (" + email + ")\n\n" + msg)}`;

    window.location.href = mailtoUrl;
    alert(`Thank you, ${name}! Your email client will now open to send your message directly to ${targetEmail}.`);
    form.reset();
  });
}

/* ==========================================================
   Export & Download Tools
   ========================================================== */
function setupExportTools() {
  // Download HTML
  const dlHtmlBtn = document.getElementById('downloadHtmlBtn');
  if (dlHtmlBtn) {
    dlHtmlBtn.addEventListener('click', exportStandalonePortfolioHtml);
  }

  // Export JSON
  const dlJsonBtn = document.getElementById('downloadJsonBtn');
  if (dlJsonBtn) {
    dlJsonBtn.addEventListener('click', () => {
      const dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(appState.data, null, 2));
      const downloadAnchor = document.createElement('a');
      downloadAnchor.setAttribute("href", dataStr);
      downloadAnchor.setAttribute("download", `${(appState.data.name || 'portfolio').replace(/\s+/g, '_')}_data.json`);
      document.body.appendChild(downloadAnchor);
      downloadAnchor.click();
      downloadAnchor.remove();
    });
  }

  // Print PDF
  const printPdfBtn = document.getElementById('printPdfBtn');
  if (printPdfBtn) {
    printPdfBtn.addEventListener('click', () => {
      window.print();
    });
  }

  // Copy Plaintext Resume
  const copyResumeBtn = document.getElementById('copyResumeBtn');
  if (copyResumeBtn) {
    copyResumeBtn.addEventListener('click', () => {
      const text = document.getElementById('resumeSheetContent').innerText;
      navigator.clipboard.writeText(text).then(() => {
        alert('Resume text copied to clipboard!');
      });
    });
  }
}

function exportStandalonePortfolioHtml() {
  const d = appState.data;
  const standaloneHtml = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${escapeHtml(d.name)} | Portfolio</title>
  <style>
    :root {
      --bg: #0a0f1d;
      --card: rgba(22, 33, 62, 0.75);
      --accent: #38bdf8;
      --accent-sec: #818cf8;
      --text: #f8fafc;
      --text-muted: #94a3b8;
      --border: rgba(148, 163, 184, 0.15);
    }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: var(--bg); color: var(--text); line-height: 1.6; padding: 2rem 1.5rem; }
    .container { max-width: 900px; margin: 0 auto; }
    header { padding: 3rem 0; text-align: center; border-bottom: 1px solid var(--border); margin-bottom: 3rem; }
    h1 { font-size: 2.8rem; margin-bottom: 0.5rem; background: linear-gradient(135deg, #fff, var(--accent)); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
    .tagline { color: var(--accent); font-size: 1.2rem; font-weight: 600; margin-bottom: 1.5rem; }
    .card { background: var(--card); border: 1px solid var(--border); border-radius: 12px; padding: 1.5rem; margin-bottom: 1.5rem; }
    .section-title { font-size: 1.5rem; margin-bottom: 1rem; color: var(--accent); border-bottom: 2px solid var(--accent); padding-bottom: 0.3rem; display: inline-block; }
    .pill { display: inline-block; background: rgba(56, 189, 248, 0.15); color: #7dd3fc; border: 1px solid rgba(56, 189, 248, 0.3); padding: 0.3rem 0.7rem; border-radius: 6px; font-size: 0.85rem; margin: 0.2rem; }
    .btn { display: inline-block; background: var(--accent); color: #030712; padding: 0.6rem 1.2rem; border-radius: 6px; text-decoration: none; font-weight: 600; margin-top: 0.5rem; }
  </style>
</head>
<body>
  <div class="container">
    <header>
      <h1>${escapeHtml(d.name)}</h1>
      <p class="tagline">${escapeHtml(d.tagline)}</p>
      <p style="color: var(--text-muted); max-width: 650px; margin: 0 auto 1.5rem;">${escapeHtml(d.summary || '')}</p>
      <div>
        ${d.email ? `<a href="mailto:${escapeHtml(d.email)}" class="btn">✉ Contact: ${escapeHtml(d.email)}</a>` : ''}
      </div>
    </header>

    <main>
      ${d.projects?.length ? `
        <section style="margin-bottom: 3rem;">
          <h2 class="section-title">Projects</h2>
          ${d.projects.map(p => `
            <div class="card">
              <h3 style="font-size: 1.25rem; margin-bottom: 0.5rem;">${escapeHtml(p.title)}</h3>
              <p style="color: var(--text-muted); margin-bottom: 0.75rem;">${escapeHtml(p.description || '')}</p>
              <div>${(p.technologies || []).map(t => `<span class="pill">${escapeHtml(t)}</span>`).join('')}</div>
            </div>
          `).join('')}
        </section>
      ` : ''}

      ${d.education?.length ? `
        <section style="margin-bottom: 3rem;">
          <h2 class="section-title">Education</h2>
          ${d.education.map(e => `
            <div class="card">
              <h3 style="font-size: 1.15rem;">${escapeHtml(e.institution)}</h3>
              <p style="color: var(--accent);">${escapeHtml(e.degree)}</p>
              <p style="color: var(--text-muted); font-size: 0.88rem;">${escapeHtml(e.year || '')} ${e.score ? '• Score: ' + escapeHtml(e.score) : ''}</p>
            </div>
          `).join('')}
        </section>
      ` : ''}
    </main>

    <footer style="text-align: center; padding: 2rem 0; color: var(--text-muted); font-size: 0.85rem; border-top: 1px solid var(--border);">
      © ${new Date().getFullYear()} ${escapeHtml(d.name)}. Generated with ResumeFolio AI.
    </footer>
  </div>
</body>
</html>`;

  const blob = new Blob([standaloneHtml], { type: 'text/html;charset=utf-8' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = `${(d.name || 'portfolio').replace(/\s+/g, '_')}_portfolio.html`;
  document.body.appendChild(a);
  a.click();
  a.remove();
  URL.revokeObjectURL(url);
}

function escapeHtml(str) {
  if (!str) return '';
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}
