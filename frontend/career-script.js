const API_URL = "https://vision-path-production.up.railway.app/api";

/* Signup */

function registerUser() {
    const email = document.getElementById("registerEmail").value;
    const username = document.getElementById("registerUsername").value;
    const password = document.getElementById("registerPassword").value;

    fetch(`${API_URL}/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            username: username,
            password: password,
            interest: "",
            skill: "",
            role: "USER"
        })
    })
    .then(response => response.text())
    .then(data => {
        if (data.includes("Username already registered")) {
            document.getElementById("registerMessage").innerText =
                "Username already registered. Please choose another username.";
        } else {
            document.getElementById("registerMessage").innerText =
                "Signup Successful. Please login.";
        }
    })
    .catch(error => {
        document.getElementById("registerMessage").innerText =
            "Signup failed. Backend not connected.";
        console.log(error);
    });
}

/* Login */

function loginUser() {
    const username = document.getElementById("loginUsername").value;
    const password = document.getElementById("loginPassword").value;

    fetch(`${API_URL}/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(response => response.text())
    .then(data => {
        if (data.startsWith("eyJ")) {
            localStorage.setItem("token", data);

            const payload = JSON.parse(atob(data.split(".")[1]));
            const role = payload.role;

            if (role === "ADMIN") {
                window.location.href = "career-admin.html";
            } else {
                window.location.href = "career-dashboard.html";
            }
        } else {
            document.getElementById("loginMessage").innerText = data;
        }
    })
    .catch(error => {
        document.getElementById("loginMessage").innerText =
            "Backend not connected";
        console.log(error);
    });
}

/* Forgot Password */

function sendResetLink() {
    const email = document.getElementById("resetEmail").value;

    fetch(`${API_URL}/forgot-password`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email
        })
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById("resetMessage").innerText = data;
    })
    .catch(error => {
        document.getElementById("resetMessage").innerText =
            "Reset email failed. Backend not connected.";
        console.log(error);
    });
}

function resetPassword() {
    const email = document.getElementById("resetEmail").value;
    const newPassword = document.getElementById("newPassword").value;

    fetch(`${API_URL}/reset-password`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: newPassword
        })
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById("resetMessage").innerText = data;
    })
    .catch(error => {
        document.getElementById("resetMessage").innerText =
            "Password reset failed. Backend not connected.";
        console.log(error);
    });
}

/* Dashboard Greeting */

function loadDashboardGreeting() {
    const token = localStorage.getItem("token");

    let username = "User";

    if (token) {
        try {
            const payload = JSON.parse(atob(token.split(".")[1]));
            username = payload.sub;
        } catch (error) {
            username = "User";
        }
    }

    const hour = new Date().getHours();
    let greeting = "Good Night";

    if (hour >= 5 && hour < 12) {
        greeting = "Good Morning";
    } else if (hour >= 12 && hour < 17) {
        greeting = "Good Afternoon";
    } else if (hour >= 17 && hour < 21) {
        greeting = "Good Evening";
    }

    const greetingText = document.getElementById("greetingText");
    const navUsername = document.getElementById("navUsername");

    if (greetingText) {
        greetingText.innerText = `${greeting}, ${username} 👋`;
    }

    if (navUsername) {
        navUsername.innerText = username;
    }
}

loadDashboardGreeting();

/* Domain Assessment */

function calculateDomain() {

    const answers = [];

    for (let i = 1; i <= 8; i++) {
        answers.push(document.getElementById("q" + i).value);
    }

    let score = {
        cyber: 0,
        fullstack: 0,
        quantum: 0,
        ai: 0
    };

    answers.forEach(answer => {
        score[answer]++;
    });

    let bestDomain = Object.keys(score).reduce((a, b) =>
        score[a] > score[b] ? a : b
    );

    let result = "";

    if (bestDomain === "cyber") {

        result =
`Best Suitable Domain: Cybersecurity

You are interested in security, networking and protecting systems from cyber attacks.

Suggested Path:
Networking → Linux → Web Security → SOC Analyst → Ethical Hacking`;

    }

    else if (bestDomain === "fullstack") {

        result =
`Best Suitable Domain: Full Stack Development

You enjoy building applications and solving real-world software problems.

Suggested Path:
HTML → CSS → JavaScript → Java → Spring Boot → MySQL`;

    }

    else if (bestDomain === "quantum") {

        result =
`Best Suitable Domain: Quantum Computing

You enjoy mathematics, research and future technologies.

Suggested Path:
Mathematics → Python → Quantum Basics → Qiskit → Quantum Algorithms`;

    }

    else {

        result =
`Best Suitable Domain: Intelligence System

You are interested in AI, automation and smart systems.

Suggested Path:
Python → Data Science → Machine Learning → NLP → AI Systems`;

    }

    localStorage.setItem("domainResult", result);

    window.location.href = "career-result.html";
}

/* Admin Dashboard */

function viewUsers() {
    const token = localStorage.getItem("token");

    fetch(`${API_URL}/users`, {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("adminResult").innerText =
            JSON.stringify(data, null, 2);
    });
}

function viewLogs() {
    const token = localStorage.getItem("token");

    fetch(`${API_URL}/logs`, {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => response.json())
    .then(data => {
        document.getElementById("adminResult").innerText =
            JSON.stringify(data, null, 2);
    });
}

function unlockUser() {
    const username = document.getElementById("unlockUsername").value;
    const token = localStorage.getItem("token");

    fetch(`${API_URL}/unlock/${username}`, {
        method: "POST",
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById("adminResult").innerText = data;
    });
}

/* Logout */

function logoutUser() {
    localStorage.removeItem("token");
    window.location.href = "career-login.html";
}

function getRoadmapData(domain) {

    if (domain === "cyber") {
        return {
            title: "🔐 Cybersecurity Roadmap",
            duration: "Estimated Duration: 6 - 8 Months",
            steps: [
                "Networking",
                "Linux",
                "Web Security",
                "SOC Operations",
                "Ethical Hacking",
                "CEH",
                "Security Projects"
            ],
            skills: [
                "Networking",
                "Linux",
                "Security Basics",
                "Problem Solving"
            ],
            roles: [
                "SOC Analyst",
                "Cybersecurity Analyst",
                "Ethical Hacker",
                "Penetration Tester"
            ]
        };
    }

    if (domain === "fullstack") {
        return {
            title: "💻 Full Stack Development Roadmap",
            duration: "Estimated Duration: 5 - 7 Months",
            steps: [
                "HTML",
                "CSS",
                "JavaScript",
                "Java",
                "Spring Boot",
                "MySQL",
                "Full Stack Project"
            ],
            skills: [
                "Frontend Development",
                "Backend Development",
                "Database",
                "REST APIs"
            ],
            roles: [
                "Frontend Developer",
                "Backend Developer",
                "Full Stack Developer",
                "Software Engineer"
            ]
        };
    }

    if (domain === "quantum") {
        return {
            title: "⚛ Quantum Computing Roadmap",
            duration: "Estimated Duration: 8 - 12 Months",
            steps: [
                "Mathematics",
                "Linear Algebra",
                "Python",
                "Qubits",
                "Superposition",
                "Qiskit",
                "Quantum Projects"
            ],
            skills: [
                "Mathematics",
                "Python",
                "Research Thinking",
                "Logical Reasoning"
            ],
            roles: [
                "Quantum Researcher",
                "Quantum Developer",
                "Research Scientist",
                "Quantum Security Specialist"
            ]
        };
    }

    return {
        title: "🤖 Intelligence System Roadmap",
        duration: "Estimated Duration: 6 - 9 Months",
        steps: [
            "Python",
            "Statistics",
            "Data Analysis",
            "Machine Learning",
            "Deep Learning",
            "NLP",
            "AI Projects"
        ],
        skills: [
            "Python",
            "Data Analysis",
            "Machine Learning",
            "Problem Solving"
        ],
        roles: [
            "AI Engineer",
            "ML Engineer",
            "Data Scientist",
            "AI Developer"
        ]
    };
}

function displayRoadmap(data, titleId, durationId, pathId, skillsId, rolesId) {

    document.getElementById(titleId).innerText = data.title;
    document.getElementById(durationId).innerText = data.duration;

    const pathBox = document.getElementById(pathId);
    pathBox.innerHTML = "";

    data.steps.forEach((step, index) => {
        pathBox.innerHTML += `
            <div class="roadmap-step">
                <div class="step-circle">${index + 1}</div>
                <div class="step-text">${step}</div>
            </div>
        `;
    });

    document.getElementById(skillsId).innerHTML =
        data.skills.map(skill => `<li>${skill}</li>`).join("");

    document.getElementById(rolesId).innerHTML =
        data.roles.map(role => `<li>${role}</li>`).join("");
}

function loadRecommendedRoadmap() {

    const result = localStorage.getItem("domainResult") || "";

    let domain = "cyber";

    if (result.includes("Full Stack")) {
        domain = "fullstack";
    } else if (result.includes("Quantum")) {
        domain = "quantum";
    } else if (result.includes("Intelligence")) {
        domain = "ai";
    } else if (result.includes("Cybersecurity")) {
        domain = "cyber";
    }

    const data = getRoadmapData(domain);

    displayRoadmap(
        data,
        "roadmapTitle",
        "roadmapDuration",
        "roadmapPath",
        "skillsList",
        "rolesList"
    );

    const dropdown = document.getElementById("roadmapDomain");
    if (dropdown) {
        dropdown.value = domain;
    }
}

function generateOtherRoadmap() {

    const domain = document.getElementById("roadmapDomain").value;

    const data = getRoadmapData(domain);

    document.getElementById("otherRoadmapSection").style.display = "block";

    displayRoadmap(
        data,
        "otherRoadmapTitle",
        "otherRoadmapDuration",
        "otherRoadmapPath",
        "otherSkillsList",
        "otherRolesList"
    );
}