// --------------- Shared DOM References ---------------

const ownerName = document.getElementById("ownerName");
const ownerMessage = document.getElementById("ownerMessage");

// --------------- Page-level State ---------------

let ownerRestaurants = [];
let ownerCategories = {};
let ownerMenuItems = {};
let ownerOrdersData = [];


// --------------- Shared Helpers ---------------

// Shows feedback for all owner dashboard actions.
function showOwnerMessage(message, tone = "info") {
    ownerMessage.className = `banner ${tone}`;
    ownerMessage.textContent = message;
}

// Switches between owner workspace sections.
function setOwnerView(viewName) {
    document.querySelectorAll("[data-owner-view]").forEach((button) => {
        button.classList.toggle("active", button.dataset.ownerView === viewName);
    });

    document.querySelectorAll("[data-owner-panel]").forEach((panel) => {
        panel.classList.toggle("active", panel.dataset.ownerPanel === viewName);
    });
}

// Reloads every owner panel after any create, update, or delete.
async function refreshOwnerPage() {
    await loadOwnerData();
    renderManagedRestaurants();
    renderCategoryManagement();
    renderMenuManagement();
    renderIncomingOrders();
    populateRestaurantOptions();
    bindRestaurantActions();
    bindCategoryActions();
    bindMenuActions();
}

// Loads all owner data once and stores it in page-level state.
async function loadOwnerData() {
    ownerRestaurants = await fetchOwnerRestaurants();

    const categoriesEntries = await Promise.all(
        ownerRestaurants.map(async (restaurant) => [restaurant.id, await getCategoriesByRestaurant(restaurant.id)])
    );

    const menuEntries = await Promise.all(
        ownerRestaurants.map(async (restaurant) => [restaurant.id, await fetchMenuItemsByRestaurant(restaurant.id)])
    );

    const orderGroups = await Promise.all(
        ownerRestaurants.map(async (restaurant) => await fetchOrdersByRestaurant(restaurant.id))
    );

    ownerCategories = Object.fromEntries(categoriesEntries);
    ownerMenuItems = Object.fromEntries(menuEntries);
    ownerOrdersData = orderGroups.flat();
    populateRestaurantOptions();
}

// Keeps sidebar navigation in sync with the visible workspace panel.
function bindOwnerNavigation() {
    document.querySelectorAll("[data-owner-view]").forEach((button) => {
        button.addEventListener("click", () => {
            resetRestaurantForm();
            resetCategoryForm();
            resetMenuForm();
            setOwnerView(button.dataset.ownerView);
        });
    });
}

// Collects all per-feature cancel/show-form button bindings in one place.
function bindOwnerFormCancelButtons() {
    bindRestaurantFormButtons();
    bindCategoryFormButtons();
    bindMenuFormButtons();
}

// Boots the owner dashboard.
async function initOwnerPage() {
    if (!isLoggedIn()) {
        window.location.href = "login.html";
        return;
    }

    resetRestaurantForm();
    resetCategoryForm();
    resetMenuForm();
    bindOwnerNavigation();
    bindOwnerFormCancelButtons();
    await refreshOwnerPage();
}
