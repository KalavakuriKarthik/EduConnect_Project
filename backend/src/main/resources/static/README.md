# EduConnect – Frontend

Static HTML/CSS/JavaScript frontend for the EduConnect backend REST API.  
No build tools required — open directly in a browser.

---

## File Structure

```
educonnect-frontend/
├── index.html                        ← Login / Register
├── css/
│   └── style.css                     ← All styles (variables, layout, components)
├── js/
│   ├── api.js                        ← All API calls + utility functions
│   └── layout.js                     ← Sidebar builder (role-based nav)
└── pages/
    ├── student-dashboard.html        ← Student home
    ├── student-courses.html          ← Browse & enroll in courses
    ├── student-assessments.html      ← View & submit assessments
    ├── student-progress.html         ← Progress & grades
    ├── teacher-dashboard.html        ← Teacher home + create course
    ├── teacher-courses.html          ← Teacher course list (alias admin-courses)
    ├── teacher-content.html          ← Upload & manage content
    ├── teacher-assessments.html      ← Create assessments
    ├── teacher-submissions.html      ← Review & grade submissions
    ├── admin-dashboard.html          ← Admin/Manager home
    ├── admin-courses.html            ← Full course CRUD
    ├── admin-students.html           ← All students
    ├── admin-users.html              ← All system users
    ├── compliance-dashboard.html     ← Compliance Officer / Auditor home
    ├── compliance-records.html       ← Compliance records CRUD
    ├── audits.html                   ← Audit creation and status
    ├── reports.html                  ← Dashboard metrics + report generation
    ├── notifications.html            ← User notifications
    └── audit-logs.html               ← System audit trail
```

---

## Setup

### 1. Configure Backend URL
Open `js/api.js` and set your backend address:
```js
const API_BASE = 'http://localhost:8080';   // ← change if needed
```

### 2. Ensure Backend CORS allows the frontend origin
In `application.properties`:
```properties
app.cors.allowed-origins=http://localhost:3000,http://localhost:5500
```

### 3. Open in browser
**Option A – VS Code Live Server (recommended)**
- Right-click `index.html` → Open with Live Server
- Runs on `http://localhost:5500`

**Option B – Python**
```bash
cd educonnect-frontend
python -m http.server 5500
# open http://localhost:5500
```

**Option C – Direct file open**
- Double-click `index.html` (note: some browsers block fetch for `file://` — use a server instead)

---

## How it Works

### Authentication Flow
1. User registers or logs in → backend returns a JWT token
2. Token is stored in `localStorage` as `ec_token`
3. Every API call automatically adds `Authorization: Bearer <token>`
4. On 401 response, user is redirected to login

### Role-Based Routing
After login, users are redirected based on their role:

| Role | Dashboard |
|------|-----------|
| STUDENT | student-dashboard.html |
| TEACHER | teacher-dashboard.html |
| SCHOOL_ADMIN | admin-dashboard.html |
| PROGRAM_MANAGER | admin-dashboard.html |
| COMPLIANCE_OFFICER | compliance-dashboard.html |
| GOVERNMENT_AUDITOR | compliance-dashboard.html |

### API Layer (`js/api.js`)
All backend calls go through named modules:

```js
// Examples
AuthAPI.login({ email, password })
CourseAPI.getAll()
CourseAPI.create({ title, description, teacherId, startDate, endDate })
EnrollmentAPI.enroll(studentId, courseId)
AssessmentAPI.submit({ assessmentId, studentId, fileUri })
AssessmentAPI.grade(submissionId, { grade, feedback })
ComplianceAPI.createRecord({ type, entityId, result, notes })
NotifAPI.markAllRead(userId)
ReportAPI.dashboard()
```

---

## Pages & Endpoints Used

| Page | Backend Endpoints |
|------|-------------------|
| index.html | POST /api/auth/login, POST /api/auth/register |
| student-dashboard | GET /api/students/user/:id, /api/enrollments/student/:id, /api/progress/student/:id, /api/assessments/submissions/student/:id |
| student-courses | GET /api/courses, POST /api/enrollments/:sid/course/:cid, DELETE enroll |
| student-assessments | GET /api/assessments, POST /api/assessments/submit |
| student-progress | GET /api/progress/student/:id, submissions |
| teacher-dashboard | GET /api/courses/teacher/:id, submissions, grading |
| teacher-content | GET /api/content/course/:id, POST /api/content, DELETE |
| teacher-assessments | GET+POST /api/assessments |
| teacher-submissions | GET /api/assessments/submissions/assessment/:id, PUT grade |
| admin-dashboard | GET /api/reports/dashboard, /api/auth/users, courses, students |
| admin-courses | Full CRUD /api/courses |
| admin-students | GET /api/students, /api/enrollments/student/:id |
| admin-users | GET /api/auth/users |
| compliance-dashboard | GET /api/compliance/summary, records, audits |
| compliance-records | Full CRUD /api/compliance/records |
| audits | POST /api/compliance/audits/:officerId, PUT status |
| reports | GET /api/reports/dashboard, course/:id, POST generate |
| notifications | GET/PUT /api/notifications/user/:id |
| audit-logs | GET /api/audit-logs |

---

## Quick Test Accounts (register via the app)

| Role | Email example | Suggested password |
|------|---------------|--------------------|
| STUDENT | student@test.com | password123 |
| TEACHER | teacher@test.com | password123 |
| SCHOOL_ADMIN | admin@test.com | password123 |
| COMPLIANCE_OFFICER | compliance@test.com | password123 |

---

## Tech Used
- HTML5, CSS3 (custom — no Bootstrap dependency)
- Vanilla JavaScript (ES2020+, no frameworks)
- Font Awesome 6.5 (CDN) for icons
- Google Fonts — Nunito + Poppins
- Native `fetch` API for all HTTP calls
