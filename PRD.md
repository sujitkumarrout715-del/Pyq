# 📚 PRODUCT REQUIREMENTS DOCUMENT (PRD)

## App Name: Class 10 PYQ Master (v1.0)
**Board Target:** CBSE (Central Board of Secondary Education)  
**Target Medium:** English Medium  
**Platform:** Android (Kotlin with Jetpack Compose)  
**Status:** Approved for Development  
**Date:** May 2026

---

## 1. Document Control & Overview

### 1.1 Revision History
| Version | Date | Author | Description of Changes |
| :--- | :--- | :--- | :--- |
| v1.0.0 | 2026-05-27 | Product Lead | Initial comprehensive PRD based on product blueprint. |

### 1.2 Purpose of this Document
This Product Requirements Document (PRD) defines the complete functional, non-functional, and technical requirements for the launch of **Class 10 PYQ Master (v1.0)**. It serves as the single source of truth for design, development, content curation, and quality assurance.

### 1.3 Target Audience & Problem Statement
*   **The Problem:** CBSE Class 10 board exams are high-stakes, yet quality preparation resources are fragmented. Existing previous-year question (PYQ) resources online are cluttered with pop-up ads, redirecting links, unverified step-by-step solutions, and lack full offline or high-performance viewing options.
*   **The Solution:** An elegant, advertisement-free (on launch), and performance-focused Android application designed specifically to empower CBSE Class 10 students with 10 years (2016–2025) of fully solved CBSE papers, grouped by subject and chemistry/chapters, accessible entirely offline with unified bookmarks, custom solutions and a clear visual progress tracker.

---

## 2. Strategic Goals & Success Metrics (KPIs)

### 2.1 Strategic Objectives
1.  **Student Focus:** No barriers to access entry-level study tools. Distraction-free, dark-mode-ready, and high-visibility interfaces.
2.  **Offline Independence:** Fully functional downloads so that students in remote areas can study without constant high-speed internet.
3.  **Accuracy Leadership:** 100% correct answers with highly annotated diagrams and rigorous LaTeX equations.

### 2.2 Success Metrics (First 90 Days Post-Launch)
*   **Adoption:** 20,000+ app store installations.
*   **Retention:** D30 (Day 30 retention) above 45% (showing deep integration into daily study routines).
*   **Usage Density:** Average session duration of 18+ minutes per student session.
*   **Quality Index:** Minimum Play Store rating of 4.6⭐ with zero critical file-system crashes.

---

## 3. Scope & Feature Hierarchy

We prioritize a **"Single-View, Highly Fluid Study Console"** system for the initial version, ensuring lightning-fast load times.

```
┌─────────────────────────────────────────────────────────────┐
│                       CORE CAPABILITIES                     │
├──────────────────────┬──────────────────────┬───────────────┤
│ 10 Years Solved      │ PDF/LaTeX Viewer     │ Offline Core  │
├──────────────────────┼──────────────────────┼───────────────┤
│ Progress Engine      │ Keyword Search       │ Bookmark Sync │
└──────────────────────┴──────────────────────┴───────────────┘
```

### 3.1 Feature Breakdown & Lifecycle Priorities

| Feature ID | Scope Area | Detailed Definition | Launch Priority |
| :--- | :--- | :--- | :--- |
| **FR-01** | **Multi-Subject Suite** | Supports Mathematics, Science, English Language & Literature, Social Science, and Odia. Each subject acts as a primary entry point on the main home portal. | **P0 (Must-Have)** |
| **FR-02** | **10-Year Depth** | Complete access to previous years (2016–2025). | **P0 (Must-Have)** |
| **FR-03** | **Dual Study Modes** | 1. **Full-Paper Stream:** Visual PDF mode representing the official physical exam paper.<br>2. **Chapter-Wise Split:** Organized list of questions sorted by syllabus chapters (e.g., Mathematics categorized into Quadratic Equations, Trigonometry). | **P0 (Must-Have)** |
| **FR-04** | **Step-by-Step Viewer** | Interactive question-solution split screen. Step numbers, formula highlights, inline high-resolution diagrams, and "Hint" tabs to promote self-testing before viewing answers. | **P0 (Must-Have)** |
| **FR-05** | **Offline Sync Engine** | Download paper files (source PDF + state JSON data) to private app memory. Automatic download state indicator shown with progress values. | **P0 (Must-Have)** |
| **FR-06** | **Global Keyword Search**| High-speed localized search filtering by subject, year, chapters, or typed terms (e.g., searching "Mendel" lists all Science genetic questions). | **P0 (Must-Have)** |
| **FR-07** | **Interactive Tracking** | Live visualization of studied papers. Horizontal visual rings showing % progress per subject, with last-viewed papers highlighted. | **P1 (Should-Have)**|
| **FR-08** | **Unified Bookmarks** | Star specific questions or formulas with an option to write custom private text notes (e.g., *"Revise trigonometry formula on page 3"*). | **P1 (Should-Have)**|
| **FR-09** | **Comfort Eye Care** | High-contrast Dark Mode design system adhering to eye comfort during night studies. Custom reader brightness setting. | **P1 (Should-Have)**|
| **FR-10** | **AI Companion** | *Future Integration (v1.2+):* Chat-box leveraging Gemini API to clarify doubt points dynamically from solutions. | **P2 (Nice-To-Have)**|

---

## 4. Visual Conception & Theme Direction

Our design theme is **"Cosmic Slate & Emerald Accent."** This presents a modern, distraction-free atmosphere pairing bold dark interfaces with high-readability body fonts to ease optical strain.

### 4.1 Color Architecture (Material 3 Dynamic Companion)
*   **Primary Deep Canvas:** `#0F172A` (Rich Slate Blue-Grey) to replace standard pure black `#000000`, reducing eye flash in high-brightness rooms.
*   **Surface / Card Elevated Elements:** `#1E293B` (Elevated Slate).
*   **Primary Action Accent:** `#10B981` / `#34D399` (Vapor Emerald Green) — symbolizing focus, progress, and success.
*   **Highlight Gold Accent:** `#F59E0B` (Amber Gold) reserved strictly for Starred bookmarks and high-priority chapter metrics.
*   **Light Mode Canvas:** `#F8FAFC` paired with deep slate text `#0F172A`.

### 4.2 Layout & Screen Hierarchy (Jetpack Compose Specifics)

```
┌────────────────────────────────────────────────────────┐
│ [STREAK: 🔥 5 Days]          Class 10 PYQ Master   ⚙👤 │
├────────────────────────────────────────────────────────┤
│  🎯 BOARDS COUNTDOWN: 42 Days Left                     │
├────────────────────────────────────────────────────────┤
│  📚 MAIN PORTALS                                       │
│  ┌──────────────┐ ┌──────────────┐ ┌────────────────┐  │
│  │ 🔵 Maths     │ │ 🟢 Science   │ │ 🟠 English     │  │
│  │  (10 Years)  │ │  (10 Years)  │ │  (10 Papers)   │  │
│  └──────────────┘ └──────────────┘ └────────────────┘  │
├────────────────────────────────────────────────────────┤
│  📈 STUDY PROGRESS                                     │
│  Maths [▓▓▓▓▓▓░░░░] 60%    Science [▓▓▓▓▓▓▓▓░░] 80%   │
├────────────────────────────────────────────────────────┤
│  🕒 CONTINUE PRACTICE (Recent)                         │
│  └─ Mathematics 2024 (Set A) - Page 5 (Q12)            │
└────────────────────────────────────────────────────────┘
```

1.  **Unified Hub (Home screen):** Standardizes navigation using an implicit edge-to-edge layout via `Scaffold`. Features dynamic subject portals with stylized color identifiers (Maths = Slate Blue, Science = Forest Green, Social Studies = Amethyst Purple, English = Sunset Indigo).
2.  **Paper Portal Screen:** Divided layout. The upper half renders the original paper layout. The lower drawer features question navigation indicators enabling instant jumps to any question index.
3.  **Active Study Sandbox (Step View):** Built using an asymmetric dual pane on tablets (Canonical List-Detail) or a swipeable sliding sheet overlay on compact screens. The top panel presents the problem definition in high-density crisp typography. The bottom area contains the step-by-step solutions layout.

---

## 5. System Architecture & Database Schema

The app features full **Offline Capabilities First**. At runtime, data is checked in local cache before falling back to network queries.

```
                      ┌───────────────────────┐
                      │    Firebase Storage   │
                      │ (PDFs, Diagrams, etc) │
                      └───────────┬───────────┘
                                  │ (Sync App Private DB)
                                  ▼
┌──────────────────┐    ┌───────────────────┐    ┌──────────────────┐
│  Jetpack Compose │◄───┤   Repository      │◄───┤  Room Local DB   │
│  View (UI State) │    │  (Offline-First)  │    │ (Solutions/Cache)│
└──────────────────┘    └───────────────────┘    └──────────────────┘
```

### 5.1 Local Persistence Schema (Room DB SQLite Representation)

We define a relational design mapped directly to Kotlin `@Entity` definitions to maintain referential integrity.

#### Entity 1: `SubjectEntity` (`subjects_table`)
| Column Identifier | Data Type | Key Constraints | Description / Business Rules |
| :--- | :--- | :--- | :--- |
| `subject_id` | `VARCHAR(32)` | **PRIMARY KEY** | Unique ID (e.g., `subj_maths_041`). |
| `subject_name` | `VARCHAR(128)` | **NOT NULL** | Human-readable name. |
| `subject_color_hex`| `VARCHAR(9)` | **NOT NULL** | Direct theme pointer color. |
| `total_year_papers`| `INTEGER` | Default `10` | Helper variable to render UI stats cards. |

#### Entity 2: `YearPaperEntity` (`papers_table`)
Map each paper code securely. Includes difficulty annotations to help students categorize their mock studies.
| Column Identifier | Data Type | Key Constraints | Description / Business Rules |
| :--- | :--- | :--- | :--- |
| `paper_id` | `VARCHAR(64)` | **PRIMARY KEY** | Composite unique code (e.g., `paper_maths_2024_set1`). |
| `subject_id` | `VARCHAR(32)` | **FOREIGN KEY (subjects_table)** | Reference back to parent subject. |
| `exam_year` | `INTEGER` | **NOT NULL** | Numerical representation (2016 - 2025). |
| `paper_set_code` | `VARCHAR(16)` | Default `"Set 1"` | Specifies CBSE Board regions. |
| `pdf_remote_url` | `TEXT` | **NOT NULL** | Storage pointer url. |
| `pdf_local_path` | `TEXT` | **NULLABLE** | Local system path once downloaded. Empty indicates not offline. |

#### Entity 3: `QuestionEntity` (`questions_table`)
| Column Identifier | Data Type | Key Constraints | Description / Business Rules |
| :--- | :--- | :--- | :--- |
| `question_id` | `VARCHAR(128)`| **PRIMARY KEY** | Composite code (e.g., `q_math_24_s1_q05`). |
| `paper_id` | `VARCHAR(64)` | **FOREIGN KEY (papers_table)** | Parent exam mapping reference. |
| `chapter_name` | `VARCHAR(128)`| **NOT NULL** | Syllabus category (Trigonometry, Optics, etc). |
| `question_number` | `INTEGER` | **NOT NULL** | Sequential integer position. |
| `total_marks` | `INTEGER` | **NOT NULL** | Weightage value (1, 2, 3, 5). |
| `question_text` | `TEXT` | **NOT NULL** | Primary question body (Markdown / Math tags). |
| `image_url` | `TEXT` | **NULLABLE** | Figure blueprint pointers if present. |

#### Entity 4: `SolutionStepEntity` (`solutions_table`)
Instead of keeping a long monolith block of HTML, solutions are mapped with sequence step ranks to create sequential scroll overlays.
| Column Identifier | Data Type | Key Constraints | Description / Business Rules |
| :--- | :--- | :--- | :--- |
| `step_id` | `VARCHAR(128)`| **PRIMARY KEY** | Unique reference tag. |
| `question_id` | `VARCHAR(128)`| **FOREIGN KEY (questions_table)** | Matches to exact parent query. |
| `step_sequence_no`| `INTEGER` | **NOT NULL** | Increment index indicating drawing order. |
| `step_explanation`│ `TEXT` | **NOT NULL** | Detailed text with formula/LaTeX pointers. |
| `step_image_url` | `TEXT` | **NULLABLE** | Specific step visual graphs. |

#### Entity 5: `UserBookmarkEntity` (`bookmarks_table`)
Stores starred items locally with customizable user annotations.
| Column Identifier | Data Type | Key Constraints | Description / Business Rules |
| :--- | :--- | :--- | :--- |
| `bookmark_id` | `VARCHAR(128)`| **PRIMARY KEY** | Primary key. |
| `question_id` | `VARCHAR(128)`| **FOREIGN KEY (questions_table)** | Matches marked item. |
| `user_note_text` | `TEXT` | **NULLABLE** | Personal comment string. |
| `starred_at_time` | `TIMESTAMP` | Default **CURRENT_TIMESTAMP** | Created date. |

---

## 6. Offline Storage & Download Flow Architecture

To implement offline capability robustly, we build an atomic file manager class. Download actions will perform the following safe sequence:

```
[INITIATE DOWNLOAD]
       │
       ▼
1. Fetch questions/solutions details JSON from FireStore REST endpoint.
       │
       ▼
2. Download asset PDF binary from Firebase Storage bucket.
       │
       ▼
3. Write PDF and nested state JSON objects into Android's app-private directory:
   "/data/user/0/com.aistudio.class10pyqmaster/files/.../"
       │
       ▼
4. Update Room's "papers_table.pdf_local_path" reference.
       │
       ▼
[DOWNLOAD STATE: COMPLETE ✅] App UI switches local cache toggle on.
```

If a write or network disconnect fails midway:
- The system must abort the transaction.
- It will clear the half-downloaded cached file buffer relative to that paper.
- It will revert the `pdf_local_path` value back to `null`, reporting the error gracefully to the user via a Snackbar element.

---

## 7. Development Plan & Six-Week Timeline

```
Week 1: Paper Ingestion  ═══► Week 2: Solutions Curation ═══► Week 3: UI Design Layout
                                                                   │
                                                                   ▼
Week 6: Deployment & QA ◄═══ Week 5: Offline Integration ◄═══ Week 4: Core Jetpack Compose
```

### 7.1 Week-by-Week Milestones

#### Week 1: Ingestion & Asset Consolidation
*   **Target:** Collect, sanitize & verify 10 years (2016–2025) of official CBSE papers for Mathematics, Science, Social Science, and English.
*   **Milestones:** All source PDFs named using standard format patterns (`pyq_math_2024_delhi.pdf`). Initial deployment of Firebase Storage buckets folder categorization structures complete.

#### Week 2: Content Solutions Generation & Structural JSON
*   **Target:** Map out structured step-by-step solutions. Implement Markdown styling for text, with math formulas expressed in precise LaTeX markers (`$$ a^2 + b^2 = c^2 $$`).
*   **Milestones:** Populate the primary database templates. Test LaTeX expression rendering compatibility across various high-density font sets.

#### Week 3: UI Theme Wireframes & Jetpack Design Handoff
*   **Target:** Assemble design components in Figma according to Room entity representations. Define light and dark mode color variants.
*   **Milestones:** Complete dynamic mock screens with high-contrast text contrast values conforming to WCAG AA guidelines. Hand off layout specifications to development.

#### Week 4: Jetpack Compose App Portals Construction
*   **Target:** Write core Jetpack Compose screen scaffolding. Set up application routes using type-safe Navigation Compose structures.
*   **Milestones:** Splash rendering, main high-impact Home portals, and PDF rendering displays operating with fluid transitions.

#### Week 5: Advanced Navigation, Search Engine & Offline Core Integration
*   **Target:** Program Room database logic, background search filters, download managers, and local bookmark persistence engines.
*   **Milestones:** Complete paper search with search query highlighting. PDF caching is functional across devices.

#### Week 6: Verification, Optimization Testing & Public Launch
*   **Target:** Execute Robolectric visual integration verification checks. Refactor memory and clean rendering threads on slow Android devices.
*   **Milestones:** Zero compiler blocks. Compile application release build, generate distribution package, and deploy online.

---

## 8. Detailed Execution Checklist

Use this checklist to monitor core feature implementation progress:

* [ ] **First-Turn Setup Initialization**
  * [x] Set descriptive application identity inside `metadata.json`.
  * [x] Set descriptive value under `app_name` in `strings.xml`.
  * [x] Update standard unique value under `applicationId` target package within main `build.gradle.kts`.
* [ ] **Data Model & Service layer Creation**
  * [ ] Design local database `@Entity` tables in Kotlin Room modules.
  * [ ] Create background offline file download utilities with standard progress emitters.
* [ ] **User Interface Layout System**
  * [ ] Set up system theme mapping utilizing Dynamic colors & slate colors contrast properties.
  * [ ] Construct high touch target (48dp+) navigation controllers, avoiding notch areas.
  * [ ] Integrate in-app PDF renderers and LaTeX typography systems for solutions viewing.

---

## 9. Future Roadmap: Artificial Intelligence Integrations (v2.0)

1.  **AI doubt-solver (Gemini-Powered):** Integrates Gemini API inside step-views. Users type specific problem questions or highlight blocks, generating immediate step clarifications.
2.  **Adaptive practice generator:** Recommends targeted revision sets dynamically. Custom algorithms analyze weak syllabus areas based on bookmark records.

---
*End of Product Requirements Document (PRD) v1.0.0. Prepared for Class 10 PYQ Master.*
