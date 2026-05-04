// --------------- Shared DOM References ---------------

const ownerName = document.getElementById("ownerName");
const ownerMessage = document.getElementById("ownerMessage");


let ownerRestaurants = [];
let ownerCategories = {};
let ownerMenuItems = {};
let ownerOrdersData = [];


// Shows message owner dashboard
function showOwnerMessage(message, tone = "info") {
    ownerMessage.className = `banner ${tone}`;
    ownerMessage.textContent = message;
}

// Switch between owner workspace
function setOwnerView(viewName) {
    document.querySelectorAll("[data-owner-view]").forEach((button) => {
        button.classList.toggle("active", button.dataset.ownerView === viewName);
    });

    document.querySelectorAll("[data-owner-panel]").forEach((panel) => {
        panel.classList.toggle("active", panel.dataset.ownerPanel === viewName);
    });
}

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

// Loads all owner data once and stores it in page-level state
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


function bindOwnerFormCancelButtons() {
    bindRestaurantFormButtons();
    bindCategoryFormButtons();
    bindMenuFormButtons();
}

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
