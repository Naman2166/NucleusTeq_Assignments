// helper function to go back to home page
function goHome() {
    window.location.href = "index.html";
}

// helper function to display messages
function showMessage(errorElement, successElement, errorText = "", successText = "") {
    if (errorElement) {
        errorElement.textContent = errorText;
    }

    if (successElement) {
        successElement.textContent = successText;
    }
}

// helper function to redirect based on role
function redirectToDashboard(role) {
    window.location.href = role === "RESTAURANT_OWNER" ? "owner.html" : "customer.html";
}



//login
const loginForm = document.getElementById("loginForm");

if (loginForm) {
    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const errorMsg = document.getElementById("errorMsg");
        const successMsg = document.getElementById("successMsg");
        const email = document.getElementById("email").value.trim();
        const password = btoa(document.getElementById("password").value.trim());     //here i have converted password in base64 format

        showMessage(errorMsg, successMsg);

        if (!email || !password) {
            showMessage(errorMsg, successMsg, "Email and password are required.");
            return;
        }

        try {
            const data = await apiFetch("/users/login", {
                method: "POST",
                body: JSON.stringify({ email, password })
            });

            localStorage.setItem(APP_KEYS.token, data.token);
            localStorage.setItem(APP_KEYS.role, data.role);
            setUser(data.user);

            //clear server cart and transfer guest cart item to server cart so that cart items still present after login also
            const guestCart = getGuestCart();

            await clearServerCart();
            for (const item of guestCart.items) {
                await addServerCartItem(item.id, item.quantity);
            }

            showMessage(errorMsg, successMsg, "", "Login successful. Redirecting...");

            setTimeout(() => {
                redirectToDashboard(data.role);
            }, 1000);

        } catch (error) {
            showMessage(errorMsg, successMsg, error.message);
        }
    });
}



//registraton
const registerForm = document.getElementById("registerForm");

if (registerForm) {
    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const regError = document.getElementById("regError");
        const regSuccess = document.getElementById("regSuccess");
        const formData = new FormData(registerForm);
        const userData = {
            firstName: formData.get("firstName")?.trim(),
            lastName: formData.get("lastName")?.trim(),
            email: formData.get("email")?.trim(),
            password: btoa(formData.get("password")?.trim()),       //here i have converted password in base64 format
            phoneNumber: formData.get("phoneNumber")?.trim(),
            role: formData.get("role")
        };

        showMessage(regError, regSuccess);

        const { firstName, lastName, email, password, phoneNumber, role } = userData;

        if (!firstName && !lastName && !email && !password && !phoneNumber && !role) {
            showMessage(regError, regSuccess, "Form cannot be empty");
            return;
        }

        if (!role) {
            showMessage(regError, regSuccess, "Please select a role.");
            return;
        }

        try {
            await apiFetch("/users/register", {
                method: "POST",
                body: JSON.stringify(userData)
            });

            showMessage(regError, regSuccess, "", "Registration successful. Redirecting to login...");

            setTimeout(() => {
                window.location.href = "login.html";
            }, 1000);
        } catch (error) {
            showMessage(regError, regSuccess, error.message);
        }
    });
}
