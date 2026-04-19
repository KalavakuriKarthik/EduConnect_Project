// ================================
// EduConnect API Layer
// ================================

const API_BASE = "http://localhost:8081";

// ================================
// Auth Session Helpers
// ================================
const Auth = {
    getToken: () => localStorage.getItem('ec_token'),

    getUser: () => JSON.parse(localStorage.getItem('ec_user') || 'null'),

    setSession: (data) => {
        localStorage.setItem('ec_token', data.token);
        localStorage.setItem('ec_user', JSON.stringify({
            userId: data.userId,
            name: data.name,
            email: data.email,
            role: data.role
        }));
    },

    clear: () => {
        localStorage.removeItem('ec_token');
        localStorage.removeItem('ec_user');
    },

    isLoggedIn: () => !!localStorage.getItem('ec_token'),

    redirectIfNotLoggedIn: () => {
        if (!Auth.isLoggedIn()) {
            window.location.href = '/index.html';
        }
    },

    redirectIfLoggedIn: () => {
        if (Auth.isLoggedIn()) {
            const role = Auth.getUser()?.role;
            window.location.href = roleDashboard(role);
        }
    }
};

// ================================
// Role Dashboard Mapping
// ================================
function roleDashboard(role) {
    const map = {
        STUDENT: '/pages/student-dashboard.html',
        TEACHER: '/pages/teacher-dashboard.html',
        SCHOOL_ADMIN: '/pages/admin-dashboard.html',
        PROGRAM_MANAGER: '/pages/admin-dashboard.html',
        COMPLIANCE_OFFICER: '/pages/compliance-dashboard.html',
        GOVERNMENT_AUDITOR: '/pages/compliance-dashboard.html'
    };
    return map[role] || '/index.html';
}

// ================================
// Core Fetch Wrapper
// ================================
async function apiFetch(endpoint, options = {}) {
    const token = Auth.getToken();

    const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {})
    };

    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }

    const res = await fetch(`${API_BASE}${endpoint}`, {
        ...options,
        headers
    });

    if (res.status === 401) {
        Auth.clear();
        window.location.href = '/index.html';
        return;
    }

    const text = await res.text();
    let data;

    try {
        data = text ? JSON.parse(text) : {};
    } catch {
        data = { message: text };
    }

    if (!res.ok) {
        const msg = data?.message || data?.error || `HTTP ${res.status}`;
        throw new Error(msg);
    }

    return data;
}

// ================================
// AUTH APIs
// ================================
const AuthAPI = {
    register: (body) => apiFetch('/api/auth/register', {
        method: 'POST',
        body: JSON.stringify(body)
    }),

    login: (body) => apiFetch('/api/auth/login', {
        method: 'POST',
        body: JSON.stringify(body)
    }),

    getUsers: () => apiFetch('/api/auth/users')
};

// ================================
// COURSE APIs
// ================================
const CourseAPI = {
    getAll: () => apiFetch('/api/courses'),
    getById: (id) => apiFetch(`/api/courses/${id}`),
    getByTeacher: (tid) => apiFetch(`/api/courses/teacher/${tid}`),
    create: (body) => apiFetch('/api/courses', {
        method: 'POST',
        body: JSON.stringify(body)
    }),
    update: (id, body) => apiFetch(`/api/courses/${id}`, {
        method: 'PUT',
        body: JSON.stringify(body)
    }),
    delete: (id) => apiFetch(`/api/courses/${id}`, {
        method: 'DELETE'
    })
};

// ================================
// STUDENT APIs
// ================================
const StudentAPI = {
    getAll: () => apiFetch('/api/students'),
    getById: (id) => apiFetch(`/api/students/${id}`),
    getByUserId: (uid) => apiFetch(`/api/students/user/${uid}`),
    createProfile: (uid, body) => apiFetch(`/api/students/profile/${uid}`, {
        method: 'POST',
        body: JSON.stringify(body)
    }),
    update: (id, body) => apiFetch(`/api/students/${id}`, {
        method: 'PUT',
        body: JSON.stringify(body)
    })
};

// ================================
// ENROLLMENT APIs
// ================================
const EnrollmentAPI = {
    enroll: (sid, cid) => apiFetch(`/api/enrollments/${sid}/course/${cid}`, {
        method: 'POST'
    }),
    unenroll: (sid, cid) => apiFetch(`/api/enrollments/${sid}/course/${cid}`, {
        method: 'DELETE'
    }),
    byStudent: (sid) => apiFetch(`/api/enrollments/student/${sid}`),
    byCourse: (cid) => apiFetch(`/api/enrollments/course/${cid}`)
};

// ================================
// CONTENT APIs
// ================================
const ContentAPI = {
    byCourse: (cid) => apiFetch(`/api/content/course/${cid}`),
    getById: (id) => apiFetch(`/api/content/${id}`),
    upload: (body) => apiFetch('/api/content', {
        method: 'POST',
        body: JSON.stringify(body)
    }),
    delete: (id) => apiFetch(`/api/content/${id}`, {
        method: 'DELETE'
    })
};

// ================================
// REPORT APIs
// ================================
const ReportAPI = {
    dashboard: () => apiFetch('/api/reports/dashboard'),
    course: (cid) => apiFetch(`/api/reports/course/${cid}`),
    getAll: () => apiFetch('/api/reports')
};

// ================================
// LOGOUT
// ================================
function logout() {
    Auth.clear();
    window.location.href = '/index.html';
}
