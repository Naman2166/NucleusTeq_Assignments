const restaurantContainer = document.getElementById("restaurantGrid");
const walletValue = document.getElementById("walletValue");
let restaurantState = [];


//restaurant card grid on home page
function createRestaurantCard(restaurant) {
    const categories = (restaurant.categories || []).slice(0, 3).map((item) => `<span class="chip">${escapeHtml(item.name)}</span>`).join("");

    return `
        <article class="restaurant-card panel">
            <div class="restaurant-image-wrap">
                <img src="${getRestaurantImage(restaurant)}" alt="${escapeHtml(restaurant.name)}">
            </div>
            <div class="restaurant-body">
                <div class="card-top">
                    <div>
                        <h3>${escapeHtml(restaurant.name)}</h3>
                        <p class="subtle">${escapeHtml(restaurant.address || "Address available on selection")}</p>
                    </div>
                    <span class="status-pill ${statusTone(restaurant.status)}">${escapeHtml(restaurant.status || "OPEN")}</span>
                </div>
                <p class="restaurant-text">${escapeHtml(restaurant.description || "Fresh meals and simple online ordering.")}</p>
                <div class="card-meta">
                    <span class="eta-pill">${escapeHtml(restaurant.eta || "")}</span>
                </div>
                <div class="chip-row">${categories || '<span class="chip">Menu Available</span>'}</div>
                <button class="primary-btn" data-restaurant="${restaurant.id}">View Menu</button>
            </div>
        </article>
    `;
}


function filterRestaurants(query) {
    const cards = document.querySelectorAll(".restaurant-card");

    cards.forEach((card) => {
        const text = card.textContent.toLowerCase();

        if (text.includes(query)) {
            card.style.display = "block";
        } else {
            card.style.display = "none";
        }
    });
}



function renderRestaurantCollections(restaurants) {
    if (!restaurantContainer) {
        return;
    }

    if (!restaurants.length) {
        restaurantContainer.innerHTML = `<div class="empty-state panel"><h3>No restaurants found</h3><p>Try a different search term.</p></div>`;
        return;
    }

    restaurantContainer.innerHTML = restaurants.map(createRestaurantCard).join("");

    document.querySelectorAll("[data-restaurant]").forEach((button) => {
        button.addEventListener("click", () => {
            window.location.href = `restaurant.html?id=${button.dataset.restaurant}`;
        });
    });
}

function renderWalletSummary() {
    const user = getUser();

    if (walletValue) {
        walletValue.textContent = user ? currency(user.walletBalance || 1000) : currency(1000);
    }
}

function renderQuickAction() {
    const quickAction = document.getElementById("quickAction");

    if (!quickAction) {
        return;
    }

    if (!isLoggedIn()) {
        quickAction.innerHTML = `<a class="secondary-btn" href="register.html">Create account</a>`;
        return;
    }

    const dashboard = getRole() === "RESTAURANT_OWNER" ? "owner.html" : "customer.html";
    quickAction.innerHTML = `<a class="secondary-btn" href="${dashboard}">Open dashboard</a>`;
}


window.addEventListener("load", () => {
    window.scrollTo(0, 0);
});


async function initHomePage() {
    restaurantState = await getRestaurants();
    renderRestaurantCollections(restaurantState);
    renderWalletSummary();
    renderQuickAction();

    setupSearch("searchInput", (query) => {
        const filtered = restaurantState.filter((restaurant) => {
            const haystack = `${restaurant.name} ${restaurant.address || ""} ${(restaurant.categories || []).map((item) => item.name).join(" ")}`.toLowerCase();
            return haystack.includes(query);
        });

        renderRestaurantCollections(filtered);
    });
}

initHomePage();
