// EduConnect Layout JS
// Builds sidebar and layout based on user role

function buildLayout(pageTitle, activeNav) {
    Auth.redirectIfNotLoggedIn();

    const user = Auth.getUser();
    const role = user?.role || 'STUDENT';

    // Navigation items based on role
    const navMap = {
        STUDENT: [
            { id: 'dashboard', label: 'Dashboard', href: 'student-dashboard.html' },
            { id: 'courses', label: 'My Courses', href: 'student-courses.html' },
            { id: 'assessments', label: 'Assessments', href: 'student-assessments.html' },
            { id: 'progress', label: 'My Progress', href: 'student-progress.html' },
            { id: 'notifications', label: 'Notifications', href: 'notifications.html' }
        ],

        TEACHER: [
            { id: 'dashboard', label: 'Dashboard', href: 'teacher-dashboard.html' },
            { id: 'courses', label: 'My Courses', href: 'teacher-courses.html' },
            { id: 'content', label: 'Content', href: 'teacher-content.html' },
            { id: 'assessments', label: 'Assessments', href: 'teacher-assessments.html' },
            { id: 'submissions', label: 'Submissions', href: 'teacher-submissions.html' },
            { id: 'notifications', label: 'Notifications', href: 'notifications.html' }
        ],

        SCHOOL_ADMIN: [
            { id: 'dashboard', label: 'Dashboard', href: 'admin-dashboard.html' },
            { id: 'users', label: 'Users', href: 'admin-users.html' },
            { id: 'courses', label: 'Courses', href: 'admin-courses.html' },
            { id: 'students', label: 'Students', href: 'admin-students.html' },
            { id: 'reports', label: 'Reports', href: 'reports.html' },
            { id: 'audit-logs', label: 'Audit Logs', href: 'audit-logs.html' },
            { id: 'notifications', label: 'Notifications', href: 'notifications.html' }
        ],

        PROGRAM_MANAGER: [
            { id: 'dashboard', label: 'Dashboard', href: 'admin-dashboard.html' },
            { id: 'courses', label: 'Courses', href: 'admin-courses.html' },
            { id: 'students', label: 'Students', href: 'admin-students.html' },
            { id: 'reports', label: 'Reports', href: 'reports.html' },
            { id: 'notifications', label: 'Notifications', href: 'notifications.html' }
        ],

        COMPLIANCE_OFFICER: [
            { id: 'dashboard', label: 'Dashboard', href: 'compliance-dashboard.html' },
            { id: 'compliance', label: 'Compliance', href: 'compliance-records.html' },
            { id: 'audits', label: 'Audits', href: 'audits.html' },
            { id: 'reports', label: 'Reports', href: 'reports.html' },
            { id: 'notifications', label: 'Notifications', href: 'notifications.html' }
        ],

        GOVERNMENT_AUDITOR: [
            { id: 'dashboard', label: 'Dashboard', href: 'compliance-dashboard.html' },
            { id: 'compliance', label: 'Compliance', href: 'compliance-records.html' },
            { id: 'audits', label: 'Audits', href: 'audits.html' },
            { id: 'reports', label: 'Reports', href: 'reports.html' },
            { id: 'audit-logs', label: 'Audit Logs', href: 'audit-logs.html' },
            { id: 'notifications', label: 'Notifications', href: 'notifications.html' }
        ]
    };

    const navItems = navMap[role] || navMap.STUDENT;

    const navHTML = navItems.map(item => `
        <div class="nav-item ${activeNav === item.id ? 'active' : ''}"
             onclick="window.location.href='${item.href}'">
            ${item.label}
        </div>
    `).join('');

    document.getElementById('sidebar').innerHTML = `
        <div class="sidebar-brand">
            <h2>EduConnect</h2>
        </div>
        <div class="nav-section">Menu</div>
        ${navHTML}
        <div class="sidebar-footer">
            <div>${user.name}</div>
            <div>${user.role}</div>
            <button onclick="logout()">Logout</button>
        </div>
    `;

    const titleElement = document.querySelector('.topbar-title');
    if (titleElement) {
        titleElement.textContent = pageTitle;
    }
}

// Logout
function logout() {
    Auth.clear();
    window.location.href = "/index.html";
}
