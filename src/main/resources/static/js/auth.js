async function login() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    });

    if (response.ok) {
        window.location.href = "index.html";
    } else {
        alert("Invalid email or password");
    }
}

async function register() {
    const username = document.getElementById("registerName").value;
    const password = document.getElementById("registerPassword").value;

    const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    });

    if (response.ok) {
        alert("Registration successful");
    } else {
        alert("Registration failed");
    }
}

function togglePassword() {
    const passwordInput = document.getElementById('password');
    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordInput.setAttribute('type', type);
}