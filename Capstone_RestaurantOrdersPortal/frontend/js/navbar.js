//render navbar
async function renderNavbar() {
    const authSection = document.getElementById("authSection");
    const role = getRole();
    let user = getUser();

    if (isLoggedIn()) {
        try {
            user = await fetchCurrentUser();
        } catch (error) {
        }
    }

    //updating user data in local storage
    if (user) {
        setUser(user);
    }
    if (!authSection) {
        return;
    }


    const cartCount = await getNavbarCartCount();
    const showCart = isLoggedIn() && role !== "RESTAURANT_OWNER";
    const activePage = window.location.pathname.split("/").pop() || "index.html";
    const cartLink = showCart
        ? `<a href="${isLoggedIn() ? "checkout.html" : "login.html"}" class="nav-link cart-link ${activePage === "checkout.html" ? "active" : ""}">&#128722; Cart <span class="cart-count">${cartCount}</span></a>`
        : "";

    const walletPill = user && role === "USER"
        ? `<div class="nav-wallet">Wallet: <span class="wallet-amount">${currency(user.walletBalance)}</span></div>`
        : "";

    const userPill = user
        ? `<div class="user-pill"></div>`
        : "";

    const links = isLoggedIn()
        ? `
        ${walletPill}
        ${role === "RESTAURANT_OWNER" ? "" : `<a href="index.html" class="nav-link ${activePage === "index.html" ? "active" : ""}">Home</a>`}
        <a href="${role === "RESTAURANT_OWNER" ? "owner.html" : "customer.html"}" class="nav-link ${["customer.html", "owner.html"].includes(activePage) ? "active" : ""}">Dashboard</a>
        ${cartLink}
        <button type="button" class="nav-link" id="logoutBtn">Logout</button>
    `
        : `
      
        <a href="index.html" class="nav-link ${activePage === "index.html" ? "active" : ""}">Home</a>
        <a href="login.html" class="nav-link ${activePage === "login.html" ? "active" : ""}">Login</a>
        <a href="register.html" class="nav-link nav-primary ${activePage === "register.html" ? "active" : ""}">Register</a>
    `;


    authSection.innerHTML = `
        ${userPill}
        ${links}
    `;

    const logoutButton = document.getElementById("logoutBtn");

    if (logoutButton) {
        logoutButton.addEventListener("click", async () => {

            //clear guest cart and transfer server cart item to guest cart so that cart items still present after logout also
            const serverCart = await fetchServerCart();

            clearGuestCart();
            for (const item of serverCart.items || []) {
                for (let i = 0; i < item.quantity; i++) {
                    addToGuestCart(
                        {
                        id: item.menuItemId,
                        name: item.menuItemName,
                        price: item.totalPrice / item.quantity
                        },
                        {
                            id: item.restaurantId
                        }
                    );
                }
            }

            logoutUser();
            window.location.href = "index.html";
        });
    }
}


//search 
function setupSearch(inputId, buttonId, onSearch) {
    const searchInput = document.getElementById(inputId);
    const searchBtn = document.getElementById(buttonId);

    if (!searchInput) return;

    const triggerSearch = () => {
        const query = searchInput.value.trim().toLowerCase();
        if (!query) return;
        onSearch(query);
    };


    searchInput.addEventListener("input", () => {
        triggerSearch();
    });

    searchInput.addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
            triggerSearch();
        }
    });

    // Button click
    if (searchBtn) {
        searchBtn.addEventListener("click", triggerSearch);
    }
}


renderNavbar();

setupSearch("searchInput", "searchBtn", (query) => {
    filterRestaurants(query);
});




