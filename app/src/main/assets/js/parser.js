/**
 * ResumeFolio - Intelligent Resume Parser
 * Parses PDF, DOCX, and raw text into a structured portfolio schema
 */

const KNOWN_SKILL_DATABASE = {
  languages: [
    'Python', 'C++', 'C', 'C#', 'Java', 'JavaScript', 'TypeScript', 'Kotlin', 'Swift',
    'Go', 'Golang', 'Rust', 'PHP', 'Ruby', 'R', 'Dart', 'SQL', 'Bash', 'Shell', 'Scala', 'MATLAB'
  ],
  frontend: [
    'React', 'React.js', 'Next.js', 'Vue', 'Vue.js', 'Angular', 'Svelte', 'HTML', 'HTML5',
    'CSS', 'CSS3', 'Tailwind CSS', 'Tailwind', 'Bootstrap', 'Sass', 'SCSS', 'Redux', 'Zustand',
    'Material UI', 'MUI', 'Chakra UI', 'Jetpack Compose', 'Flutter'
  ],
  backend: [
    'Node.js', 'Express', 'Express.js', 'Django', 'FastAPI', 'Flask', 'Spring Boot', 'Spring',
    'ASP.NET', 'NestJS', 'GraphQL', 'REST API', 'RESTful APIs', 'gRPC', 'WebSockets'
  ],
  databases: [
    'MySQL', 'PostgreSQL', 'MongoDB', 'SQLite', 'Redis', 'Firebase', 'Supabase',
    'Oracle', 'DynamoDB', 'Cassandra', 'MariaDB', 'Prisma', 'Mongoose'
  ],
  tools: [
    'Git', 'GitHub', 'GitLab', 'Docker', 'Kubernetes', 'AWS', 'Google Cloud', 'GCP',
    'Azure', 'Vercel', 'Netlify', 'Linux', 'Postman', 'Figma', 'Jira', 'Microsoft Excel',
    'Excel', 'CI/CD', 'GitHub Actions', 'VS Code'
  ],
  frameworks_concepts: [
    'Data Structures', 'Algorithms', 'Object-Oriented Programming (OOP)', 'OOP',
    'Machine Learning', 'AI Integration', 'Deep Learning', 'Computer Vision',
    'NLP', 'Natural Language Processing', 'Microservices', 'System Design', 'Agile'
  ]
};

class ResumeParser {
  /**
   * Extract text from a PDF ArrayBuffer using pdfjsLib
   */
  static async extractTextFromPDF(arrayBuffer) {
    if (typeof pdfjsLib === 'undefined') {
      throw new Error('PDF.js library is not loaded');
    }
    const loadingTask = pdfjsLib.getDocument({ data: arrayBuffer });
    const pdf = await loadingTask.promise;
    let fullText = '';

    for (let pageNum = 1; pageNum <= pdf.numPages; pageNum++) {
      const page = await pdf.getPage(pageNum);
      const textContent = await page.getTextContent();
      const pageText = textContent.items.map(item => item.str).join(' ');
      fullText += pageText + '\n\n';
    }

    return fullText;
  }

  /**
   * Extract text from a DOCX ArrayBuffer using mammoth
   */
  static async extractTextFromDOCX(arrayBuffer) {
    if (typeof mammoth === 'undefined') {
      throw new Error('Mammoth.js library is not loaded');
    }
    const result = await mammoth.extractRawText({ arrayBuffer });
    return result.value || '';
  }

  /**
   * Clean and normalize raw text
   */
  static normalizeText(text) {
    return text
      .replace(/\r\n/g, '\n')
      .replace(/\r/g, '\n')
      .replace(/\t/g, ' ')
      .replace(/[ \u00a0\u2000-\u200b]+/g, ' ')
      .trim();
  }

  /**
   * Extract Email
   */
  static extractEmail(text) {
    const emailRegex = /([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,})/i;
    const match = text.match(emailRegex);
    return match ? match[1].trim() : '';
  }

  /**
   * Extract Phone Number
   */
  static extractPhone(text) {
    const phoneRegex = /(?:(?:\+?\d{1,3}[-.\s]?)?(?:\(?\d{3}\)?[-.\s]?)?\d{3}[-.\s]?\d{4}|\+?\d{10,12})/i;
    const match = text.match(phoneRegex);
    return match ? match[0].trim() : '';
  }

  /**
   * Extract Social and Portfolio Links
   */
  static extractLinks(text) {
    const links = {
      github: '',
      linkedin: '',
      portfolio: ''
    };

    const githubMatch = text.match(/(?:https?:\/\/)?(?:www\.)?github\.com\/([a-zA-Z0-9_-]+)/i);
    if (githubMatch) {
      links.github = `https://github.com/${githubMatch[1]}`;
    }

    const linkedinMatch = text.match(/(?:https?:\/\/)?(?:www\.)?linkedin\.com\/in\/([a-zA-Z0-9_-]+)/i);
    if (linkedinMatch) {
      links.linkedin = `https://linkedin.com/in/${linkedinMatch[1]}`;
    }

    const portfolioMatch = text.match(/(?:https?:\/\/)?([a-zA-Z0-9-]+\.(?:github\.io|vercel\.app|netlify\.app|dev|me|tech))(?:\/[^\s]*)?/i);
    if (portfolioMatch) {
      links.portfolio = portfolioMatch[0].startsWith('http') ? portfolioMatch[0] : `https://${portfolioMatch[0]}`;
    }

    return links;
  }

  /**
   * Extract Location
   */
  static extractLocation(text) {
    const locationKeywords = /(Salem|Chennai|Bangalore|Bengaluru|Hyderabad|Mumbai|Delhi|Pune|Coimbatore|Kolkata|New York|San Francisco|London|California|Texas|Tamil Nadu|India|USA|UK|Canada)/i;
    const lines = text.split('\n').slice(0, 15);
    for (const line of lines) {
      if (locationKeywords.test(line)) {
        // Clean line to only location words
        const cleaned = line.replace(/(email|phone|mobile|tel|linkedin|github)[\s\S]*/i, '').trim();
        if (cleaned.length > 3 && cleaned.length < 50) {
          return cleaned.replace(/[•|,-]$/, '').trim();
        }
      }
    }
    return '';
  }

  /**
   * Extract Candidate Name
   */
  static extractName(lines, email) {
    const blacklist = [
      'resume', 'curriculum vitae', 'cv', 'profile', 'contact', 'email', 'phone',
      'portfolio', 'software developer', 'engineer', 'experience', 'education', 'skills'
    ];

    for (let i = 0; i < Math.min(lines.length, 8); i++) {
      const line = lines[i].trim();
      if (!line) continue;

      const lower = line.toLowerCase();
      if (blacklist.some(b => lower === b || lower.startsWith(b + ':'))) continue;
      if (lower.includes('@') || lower.includes('http') || /\d{5,}/.test(line)) continue;

      // Check if it's 2-4 words, alphabetic
      const words = line.split(/\s+/);
      if (words.length >= 1 && words.length <= 4 && /^[a-zA-Z\s.'-]+$/.test(line)) {
        return line.trim();
      }
    }

    // Fallback: derive from email
    if (email) {
      const userPart = email.split('@')[0].replace(/[0-9_.-]/g, ' ').trim();
      if (userPart) {
        return userPart.split(' ').map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ');
      }
    }

    return 'Candidate Name';
  }

  /**
   * Section Header Identification
   */
  static identifySections(text) {
    const lines = text.split('\n').map(l => l.trim()).filter(Boolean);
    const sections = {};
    let currentSection = 'header';
    sections[currentSection] = [];

    const sectionRegexes = {
      summary: /^(professional\s+summary|summary|profile|about\s*me|career\s+objective|objective)$/i,
      skills: /^(technical\s+skills|skills\s*(?:&|and)?\s*technologies?|skills|core\s+competencies|technologies)$/i,
      experience: /^(experience|work\s+experience|employment\s+history|internships?|professional\s+experience)$/i,
      projects: /^(key\s+projects|projects|featured\s+projects|academic\s+projects|personal\s+projects)$/i,
      education: /^(education|academic\s+background|academic\s+record|qualifications)$/i,
      certifications: /^(certifications?|licenses\s*(?:&|and)?\s*certifications?|courses)$/i,
      achievements: /^(achievements?|honors?\s*(?:&|and)?\s*awards?|awards?)$/i,
      strengths: /^(strengths?\s*(?:&|and)?\s*personal\s+qualities|personal\s+qualities|strengths)$/i
    };

    for (const line of lines) {
      let matchedSection = null;
      for (const [key, regex] of Object.entries(sectionRegexes)) {
        if (regex.test(line.replace(/[:\-#*]/g, '').trim())) {
          matchedSection = key;
          break;
        }
      }

      if (matchedSection) {
        currentSection = matchedSection;
        if (!sections[currentSection]) sections[currentSection] = [];
      } else {
        if (!sections[currentSection]) sections[currentSection] = [];
        sections[currentSection].push(line);
      }
    }

    return sections;
  }

  /**
   * Extract Technical Skills
   */
  static extractSkills(skillsLines, fullText) {
    const allText = (skillsLines ? skillsLines.join(' ') : '') + ' ' + fullText;
    const categorized = {
      languages: [],
      frontend: [],
      backend: [],
      databases: [],
      tools: [],
      frameworks_concepts: []
    };

    const foundSet = new Set();

    for (const [cat, skills] of Object.entries(KNOWN_SKILL_DATABASE)) {
      for (const skill of skills) {
        const regex = new RegExp(`(?:\\b|[#+.])${skill.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}(?:\\b|[#+.]|$)`, 'i');
        if (regex.test(allText) && !foundSet.has(skill.toLowerCase())) {
          categorized[cat].push(skill);
          foundSet.add(skill.toLowerCase());
        }
      }
    }

    return categorized;
  }

  /**
   * Extract Education Records
   */
  static extractEducation(lines) {
    if (!lines || lines.length === 0) return [];
    const eduList = [];
    let currentEdu = null;

    for (const line of lines) {
      const isDegree = /(bachelor|master|b\.?tech|b\.?sc|b\.?ca|m\.?ca|m\.?tech|diploma|higher\s+secondary|secondary\s+school|10th|12th|hsc|sslc)/i.test(line);
      const isSchool = /(college|university|institute|school|academy)/i.test(line);
      const isScore = /(score|percentage|cgpa|gpa|distinction|%)/i.test(line);

      if (isDegree || isSchool) {
        if (currentEdu && (currentEdu.institution || currentEdu.degree)) {
          eduList.push(currentEdu);
        }
        currentEdu = {
          institution: isSchool ? line : '',
          degree: isDegree ? line : '',
          score: '',
          year: '',
          details: ''
        };
      } else if (currentEdu) {
        if (isScore && !currentEdu.score) {
          const scoreMatch = line.match(/(?:\b\d{1,2}(?:\.\d+)?%|\b(?:CGPA|GPA)[:\s]*\d(?:\.\d+)?(?:\/\d+)?|\b\d{1,2}(?:\.\d+)?\s*distinction)/i);
          currentEdu.score = scoreMatch ? scoreMatch[0] : line;
        } else if (/\b(19|20)\d{2}\b/.test(line) && !currentEdu.year) {
          currentEdu.year = line;
        } else {
          currentEdu.details += (currentEdu.details ? ' ' : '') + line;
        }
      }
    }

    if (currentEdu && (currentEdu.institution || currentEdu.degree)) {
      eduList.push(currentEdu);
    }

    return eduList;
  }

  /**
   * Extract Projects
   */
  static extractProjects(lines) {
    if (!lines || lines.length === 0) return [];
    const projects = [];
    let currentProject = null;

    for (const line of lines) {
      // Check if line looks like a project title (short, capitalized, or has separator like - / :)
      const isNewProject = (line.length < 80 && /^[A-Z0-9][\w\s\-–—:]+$/.test(line) && !line.startsWith('-') && !line.startsWith('•')) ||
                           /(–|—|:)\s*(voice|assistant|app|system|website|platform|clone|tool|generator|management)/i.test(line);

      if (isNewProject && (!currentProject || currentProject.bullets.length > 0 || currentProject.description)) {
        if (currentProject) projects.push(currentProject);
        currentProject = {
          title: line.replace(/^[\s•*-]+/, '').trim(),
          description: '',
          bullets: [],
          technologies: [],
          github: '',
          demo: ''
        };
      } else if (currentProject) {
        if (line.startsWith('•') || line.startsWith('-') || line.startsWith('*')) {
          currentProject.bullets.push(line.replace(/^[\s•*-]+/, '').trim());
        } else if (/github\.com\/[a-zA-Z0-9_-]+/i.test(line)) {
          const m = line.match(/https?:\/\/github\.com\/[^\s]+/i);
          currentProject.github = m ? m[0] : '';
        } else {
          if (!currentProject.description) {
            currentProject.description = line;
          } else {
            currentProject.bullets.push(line);
          }
        }
      }
    }

    if (currentProject) projects.push(currentProject);

    // Extract technologies for each project
    projects.forEach(p => {
      const combined = p.title + ' ' + p.description + ' ' + p.bullets.join(' ');
      const allKnown = Object.values(KNOWN_SKILL_DATABASE).flat();
      p.technologies = allKnown.filter(t => new RegExp(`\\b${t}\\b`, 'i').test(combined));
    });

    return projects;
  }

  /**
   * Extract Work & Internship Experience
   */
  static extractExperience(lines) {
    if (!lines || lines.length === 0) return [];
    const expList = [];
    let currentExp = null;

    for (const line of lines) {
      const isHeader = (line.length < 80 && !line.startsWith('•') && !line.startsWith('-') &&
        /(intern|engineer|developer|lead|analyst|associate|manager|consultant|specialist|trainee)/i.test(line));

      if (isHeader) {
        if (currentExp) expList.push(currentExp);
        currentExp = {
          role: line.replace(/^[\s•*-]+/, '').trim(),
          company: '',
          period: '',
          location: '',
          bullets: []
        };
      } else if (currentExp) {
        if (line.startsWith('•') || line.startsWith('-') || line.startsWith('*')) {
          currentExp.bullets.push(line.replace(/^[\s•*-]+/, '').trim());
        } else if (/\b(19|20)\d{2}\b|present|current/i.test(line) && !currentExp.period) {
          currentExp.period = line;
        } else if (!currentExp.company) {
          currentExp.company = line;
        } else {
          currentExp.bullets.push(line);
        }
      }
    }

    if (currentExp) expList.push(currentExp);
    return expList;
  }

  /**
   * Master Parse Function
   */
  static parseResume(rawText) {
    const text = this.normalizeText(rawText);
    const lines = text.split('\n').map(l => l.trim()).filter(Boolean);
    const sections = this.identifySections(text);

    const email = this.extractEmail(text);
    const phone = this.extractPhone(text);
    const links = this.extractLinks(text);
    const location = this.extractLocation(text);
    const name = this.extractName(lines, email);

    // Summary & Objective
    let summary = '';
    if (sections.summary && sections.summary.length > 0) {
      summary = sections.summary.join(' ');
    }

    // Skills
    const skills = this.extractSkills(sections.skills, text);

    // Projects
    const projects = this.extractProjects(sections.projects);

    // Experience
    const experience = this.extractExperience(sections.experience);

    // Education
    const education = this.extractEducation(sections.education);

    // Strengths
    const strengths = (sections.strengths || []).map(s => s.replace(/^[\s•*-]+/, '').trim()).filter(Boolean);

    // Detect job title / role tagline
    let tagline = 'Software Developer';
    if (/data science/i.test(text)) tagline = 'Software Developer & Data Science Specialist';
    else if (/full\s*stack/i.test(text)) tagline = 'Full Stack Software Engineer';
    else if (/frontend/i.test(text)) tagline = 'Frontend Developer';
    else if (/backend/i.test(text)) tagline = 'Backend Developer';

    return {
      name,
      tagline,
      email,
      phone,
      location,
      links,
      summary,
      skills,
      projects,
      experience,
      education,
      strengths,
      rawText
    };
  }
}

if (typeof module !== 'undefined' && module.exports) {
  module.exports = ResumeParser;
}
