const ownerName = document.getElementById("ownerName");
const managedRestaurants = document.getElementById("managedRestaurants");
const ownerOrders = document.getElementById("ownerOrders");
const ownerMessage = document.getElementById("ownerMessage");
const ownerCategoriesList = document.getElementById("ownerCategoriesList");
const ownerMenuList = document.getElementById("ownerMenuList");

const restaurantForm = document.getElementById("restaurantForm");
const categoryForm = document.getElementById("categoryForm");
const menuItemForm = document.getElementById("menuItemForm");

const categoryRestaurant = document.getElementById("categoryRestaurant");
const menuRestaurant = document.getElementById("menuRestaurant");
const menuCategory = document.getElementById("menuCategory");

const restaurantFormTitle = document.getElementById("restaurantFormTitle");
const categoryFormTitle = document.getElementById("categoryFormTitle");
const menuFormTitle = document.getElementById("menuFormTitle");
const restaurantSubmitBtn = document.getElementById("restaurantSubmitBtn");
const categorySubmitBtn = document.getElementById("categorySubmitBtn");
const menuSubmitBtn = document.getElementById("menuSubmitBtn");
const restaurantCancelBtn = document.getElementById("restaurantCancelBtn");
const categoryCancelBtn = document.getElementById("categoryCancelBtn");
const menuCancelBtn = document.getElementById("menuCancelBtn");
const showRestaurantFormBtn = document.getElementById("showRestaurantFormBtn");
const showCategoryFormBtn = document.getElementById("showCategoryFormBtn");
const showMenuFormBtn = document.getElementById("showMenuFormBtn");
const restaurantImageFile = document.getElementById("restaurantImageFile");
const restaurantImagePreview = document.getElementById("restaurantImagePreview");
const menuItemImageFile = document.getElementById("menuItemImageFile");
const menuItemImagePreview = document.getElementById("menuItemImagePreview");

let restaurantPreviewObjectUrl = "";
let menuPreviewObjectUrl = "";

let ownerRestaurants = [];
let ownerCategories = {};
let ownerMenuItems = {};
let ownerOrdersData = [];


// Shows feedback for all owner dashboard actions.
function showOwnerMessage(message, tone = "info") {
    ownerMessage.className = `banner ${tone}`;
    ownerMessage.textContent = message;
}

// Restores the restaurant form to create mode after saving or editing.
function resetRestaurantForm() {
    restaurantForm.reset();
    restaurantForm.elements.id.value = "";
    if (restaurantForm.elements.imageUrl) {
        restaurantForm.elements.imageUrl.value = "";
    }
    if (restaurantImageFile) {
        restaurantImageFile.value = "";
    }
    if (restaurantPreviewObjectUrl) {
        URL.revokeObjectURL(restaurantPreviewObjectUrl);
        restaurantPreviewObjectUrl = "";
    }
    if (restaurantImagePreview) {
        restaurantImagePreview.src = "";
        restaurantImagePreview.style.display = "none";
    }
    restaurantFormTitle.textContent = "Restaurants";
    restaurantSubmitBtn.textContent = "Save restaurant";
    restaurantForm.classList.add("hidden-form");
    if (restaurantCancelBtn) {
        restaurantCancelBtn.style.display = "none";
    }
}

// Restores the category form to create mode after saving or editing.
function resetCategoryForm() {
    categoryForm.reset();
    categoryForm.elements.id.value = "";
    categoryFormTitle.textContent = "Categories";
    categorySubmitBtn.textContent = "Save category";
    categoryForm.classList.add("hidden-form");
    if (categoryCancelBtn) {
        categoryCancelBtn.style.display = "none";
    }
}

// Restores the menu form to create mode after saving or editing.
function resetMenuForm() {
    menuItemForm.reset();
    menuItemForm.elements.id.value = "";
    if (menuItemForm.elements.imageUrl) {
        menuItemForm.elements.imageUrl.value = "";
    }
    menuCategory.innerHTML = `<option value="">Choose category</option>`;
    if (menuItemImageFile) {
        menuItemImageFile.value = "";
    }
    if (menuPreviewObjectUrl) {
        URL.revokeObjectURL(menuPreviewObjectUrl);
        menuPreviewObjectUrl = "";
    }
    if (menuItemImagePreview) {
        menuItemImagePreview.src = "";
        menuItemImagePreview.style.display = "none";
    }
    menuFormTitle.textContent = "Menu Items";
    menuSubmitBtn.textContent = "Save item";
    menuItemForm.classList.add("hidden-form");
    if (menuCancelBtn) {
        menuCancelBtn.style.display = "none";
    }
}

function showLocalPreview(fileInput, previewImg, getUrl, setUrl) {
    if (!fileInput || !previewImg) return;

    const existing = getUrl();
    if (existing) {
        URL.revokeObjectURL(existing);
        setUrl("");
    }

    const file = fileInput.files?.[0];
    if (!file) {
        previewImg.src = "";
        previewImg.style.display = "none";
        return;
    }

    const objectUrl = URL.createObjectURL(file);
    setUrl(objectUrl);
    previewImg.src = objectUrl;
    previewImg.style.display = "block";
}

// Preview on file select; upload happens during submit.
function bindImagePreview({ fileInput, previewImg, getUrl, setUrl, hiddenInput }) {
    if (!fileInput || !hiddenInput) return;

    // Prevent Enter on file input from submitting parent form in some browsers.
    fileInput.addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            event.preventDefault();
            event.stopPropagation();
        }
    });

    fileInput.addEventListener("change", (event) => {
        // Never let file selection trigger any form navigation.
        event.preventDefault();
         event.stopImmediatePropagation(); 

        showLocalPreview(fileInput, previewImg, getUrl, setUrl);

        const file = fileInput.files?.[0];
        if (!file) {
            hiddenInput.value = "";
        }
    });
}

// Opens a form in create mode for the selected owner section.
function showCreateForm(sectionName) {
    if (sectionName === "restaurants") {
        restaurantFormTitle.textContent = "Add restaurant";
        restaurantSubmitBtn.textContent = "Save restaurant";
        restaurantForm.classList.remove("hidden-form");
        restaurantCancelBtn.style.display = "inline-flex";
        return;
    }

    if (sectionName === "categories") {
        categoryFormTitle.textContent = "Add category";
        categorySubmitBtn.textContent = "Save category";
        categoryForm.classList.remove("hidden-form");
        categoryCancelBtn.style.display = "inline-flex";
        return;
    }

    if (sectionName === "menu") {
        menuFormTitle.textContent = "Add menu item";
        menuSubmitBtn.textContent = "Save item";
        menuItemForm.classList.remove("hidden-form");
        menuCancelBtn.style.display = "inline-flex";
    }
}

// Fills restaurant select boxes used by category and menu forms.
function populateRestaurantOptions() {
    const options = ownerRestaurants.map((restaurant) => `
        <option value="${restaurant.id}">${escapeHtml(restaurant.name)}</option>
    `).join("");

    categoryRestaurant.innerHTML = `<option value="">Choose restaurant</option>${options}`;
    menuRestaurant.innerHTML = `<option value="">Choose restaurant</option>${options}`;
}

// Fills the category select box based on the chosen restaurant.
function populateCategoryOptions(restaurantId, selectedCategoryId = "") {
    const categories = ownerCategories[restaurantId] || [];
    menuCategory.innerHTML = `<option value="">Choose category</option>${categories.map((category) => `
        <option value="${category.id}" ${String(selectedCategoryId) === String(category.id) ? "selected" : ""}>${escapeHtml(category.name)}</option>
    `).join("")}`;
}

// Loads all owner data once, then stores it in simple page-level state.
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

// Switches between owner workspace sections so the dashboard stays manageable.
function setOwnerView(viewName) {
    document.querySelectorAll("[data-owner-view]").forEach((button) => {
        button.classList.toggle("active", button.dataset.ownerView === viewName);
    });

    document.querySelectorAll("[data-owner-panel]").forEach((panel) => {
        panel.classList.toggle("active", panel.dataset.ownerPanel === viewName);
    });
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

// Renders restaurant cards only; category and menu details have their own sections.
function renderManagedRestaurants() {
    ownerName.textContent = getUser()?.firstName || "Owner";

    managedRestaurants.innerHTML = ownerRestaurants.length
        ? ownerRestaurants.map((restaurant) => {
            const categories = ownerCategories[restaurant.id] || [];
            const menuItems = ownerMenuItems[restaurant.id] || [];

            return `
                <article class="panel owner-restaurant-card">
                    <img class="owner-card-thumb" src="${getRestaurantImage(restaurant)}" alt="${escapeHtml(restaurant.name)}">
                    <div class="inline-head">
                        <div>
                            <p class="eyebrow">${escapeHtml(restaurant.address)}</p>
                            <h3>${escapeHtml(restaurant.name)}</h3>
                        </div>
                        <span class="status-pill ${statusTone(restaurant.status)}">${escapeHtml(restaurant.status)}</span>
                    </div>

                    <div class="owner-card-stats">
                        <span>${categories.length} categories</span>
                        <span>${menuItems.length} menu items</span>
                    </div>
                    <div class="inline-actions owner-actions">
                        <button class="primary-btn compact" data-edit-restaurant="${restaurant.id}">Edit</button>
                        <button class="ghost-btn compact" data-delete-restaurant="${restaurant.id}">Delete</button>
                    </div>
                </article>
            `;
        }).join("")
        : `<div class="empty-state compact"><h3>No restaurants yet</h3><p>Create your first restaurant to start receiving orders.</p></div>`;

}

// Renders editable category rows grouped by restaurant.
function renderCategoryManagement() {
    ownerCategoriesList.innerHTML = ownerRestaurants.length
        ? ownerRestaurants.map((restaurant) => {
            const categories = ownerCategories[restaurant.id] || [];
            return `
                <article class="panel owner-restaurant-card">
                    <div class="inline-head">
                        <div>
                            <p class="eyebrow">Restaurant</p>
                            <h3>${escapeHtml(restaurant.name)}</h3>
                        </div>
                        <span class="chip">${categories.length} categories</span>
                    </div>
                    <div class="owner-subcard-list">
                        ${categories.length ? categories.map((category) => `
                            <div class="compact-row owner-mini-row">
                                <span>${escapeHtml(category.name)}</span>
                                <div class="inline-actions">
                                    <button class="secondary-btn compact" data-edit-category="${category.id}" data-restaurant="${restaurant.id}">Update</button>
                                    <button class="ghost-btn compact" data-delete-category="${category.id}">Delete</button>
                                </div>
                            </div>
                        `).join("") : `<p class="subtle">No categories added yet.</p>`}
                    </div>
                </article>
            `;
        }).join("")
        : `<div class="empty-state compact"><h3>No restaurants yet</h3><p>Create a restaurant before adding categories.</p></div>`;
}

// Renders editable menu rows grouped by restaurant.
function renderMenuManagement() {
    ownerMenuList.innerHTML = ownerRestaurants.length
        ? ownerRestaurants.map((restaurant) => {
            const menuItems = ownerMenuItems[restaurant.id] || [];
            return `
                <article class="panel owner-restaurant-card">
                    <div class="inline-head">
                        <div>
                            <p class="eyebrow">Restaurant</p>
                            <h3>${escapeHtml(restaurant.name)}</h3>
                        </div>
                        <span class="chip">${menuItems.length} items</span>
                    </div>
                    <div class="owner-subcard-list">
                        ${menuItems.length ? menuItems.map((item) => `
                            <div class="owner-menu-row compact-row">
                                <img class="owner-menu-thumb" src="${getMenuItemImage(item)}" alt="${escapeHtml(item.name)}">
                                <div class="owner-menu-copy">
                                    <p class="eyebrow">Menu item</p>
                                    <h3>${escapeHtml(item.name)}</h3>
                                    <p class="subtle">${currency(item.price)}</p>
                                </div>
                                <div class="inline-actions">
                                    <button class="secondary-btn compact" data-edit-menu="${item.id}" data-restaurant="${restaurant.id}">Update</button>
                                    <button class="ghost-btn compact" data-delete-menu="${item.id}">Delete</button>
                                </div>
                            </div>
                        `).join("") : `<p class="subtle">No menu items added yet.</p>`}
                    </div>
                </article>
            `;
        }).join("")
        : `<div class="empty-state compact"><h3>No restaurants yet</h3><p>Create a restaurant before adding menu items.</p></div>`;
}

// Returns status options available from the order's current state.
function getAllowedStatuses(currentStatus) {
    switch (currentStatus) {
        case "PLACED":
            return ["PLACED","PENDING", "CANCELLED", "DELIVERED", "COMPLETED"];
        case "PENDING":
            return ["PENDING", "DELIVERED","COMPLETED", "CANCELLED"];
        case "DELIVERED":
            return ["DELIVERED", "COMPLETED"];
        case "COMPLETED":
        case "CANCELLED":
            return [currentStatus];
        default:
            return [currentStatus];
    }
}


// Renders incoming orders and wires status/cancel controls.
function renderIncomingOrders() {
    ownerOrders.innerHTML = ownerOrdersData.length
        ? ownerOrdersData.map((order) => `
            <article class="panel order-card">
                <div class="inline-head">
                    <div>
                        <p class="eyebrow">${formatDate(order.orderTime)}</p>
                        <h3>${escapeHtml(order.restaurantName)}</h3>
                    </div>
                    <span class="status-pill ${statusTone(order.status)}">${escapeHtml(order.status)}</span>
                </div>
                <div class="stack soft-gap">
                    ${order.items.map((item) => `
                        <div class="list-row">
                            <span>${escapeHtml(item.menuItemName)} x ${item.quantity}</span>
                            <strong>${currency(item.totalPrice)}</strong>
                        </div>
                    `).join("")}
                </div>
                <div class="inline-head">
                    <strong>${currency(order.totalPrice)}</strong>
                    <div class="inline-actions">
                        <select class="status-select" data-order-status="${order.orderId}">
                         ${getAllowedStatuses(order.status).map((status) => `
                            <option value="${status}" ${status === order.status ? "selected" : ""}>
                            ${status}
                               </option>
                            `).join("")}
                        </select>
                    </div>
                </div>
            </article>
        `).join("")
        : `<div class="empty-state compact"><h3>No incoming orders</h3><p>New customer orders will show up here for status updates.</p></div>`;

    document.querySelectorAll("[data-order-status]").forEach((select) => {
        select.addEventListener("change", async () => {
            try {
                await updateOwnerOrderStatus(select.dataset.orderStatus, select.value);
                await refreshOwnerPage();
                showOwnerMessage("Order status updated.", "success");
            } catch (error) {
                showOwnerMessage(error.message, "danger");
            }
        });
    });

}

// Binds edit/delete/status actions after each render.
function bindRestaurantActions() {
    document.querySelectorAll("[data-edit-restaurant]").forEach((button) => {
        button.addEventListener("click", () => {
            const restaurant = ownerRestaurants.find((item) => String(item.id) === String(button.dataset.editRestaurant));

            if (!restaurant) {
                return;
            }

            restaurantForm.elements.id.value = restaurant.id;
            restaurantForm.elements.name.value = restaurant.name;
            restaurantForm.elements.address.value = restaurant.address;
            restaurantForm.elements.status.value = restaurant.status;
            if (restaurantForm.elements.imageUrl) {
                restaurantForm.elements.imageUrl.value = restaurant.imageUrl || "";
            }
            if (restaurantImagePreview) {
                restaurantImagePreview.src = restaurant.imageUrl ? resolveImageUrl(restaurant.imageUrl) : getRestaurantImage(restaurant);
                restaurantImagePreview.style.display = "block";
            }
            restaurantFormTitle.textContent = "Update restaurant";
            restaurantSubmitBtn.textContent = "Update restaurant";
            restaurantForm.classList.remove("hidden-form");
            if (restaurantCancelBtn) {
                restaurantCancelBtn.style.display = "inline-flex";
            }
        });
    });

    document.querySelectorAll("[data-delete-restaurant]").forEach((button) => {
        button.addEventListener("click", async () => {
            try {
                await deleteRestaurant(button.dataset.deleteRestaurant);
                await refreshOwnerPage();
                showOwnerMessage("Restaurant deleted.", "success");
            } catch (error) {
                showOwnerMessage(error.message, "danger");
            }
        });
    });

    document.querySelectorAll("[data-edit-category]").forEach((button) => {
        button.addEventListener("click", () => {
            const restaurantId = button.dataset.restaurant;
            const category = (ownerCategories[restaurantId] || []).find((item) => String(item.id) === String(button.dataset.editCategory));

            if (!category) {
                return;
            }

            categoryForm.elements.id.value = category.id;
            categoryRestaurant.value = restaurantId;
            categoryForm.elements.name.value = category.name;
            categoryFormTitle.textContent = "Update category";
            categorySubmitBtn.textContent = "Update category";
            categoryForm.classList.remove("hidden-form");
            if (categoryCancelBtn) {
                categoryCancelBtn.style.display = "inline-flex";
            }
            setOwnerView("categories");
        });
    });

    document.querySelectorAll("[data-delete-category]").forEach((button) => {
        button.addEventListener("click", async () => {
            try {
                await deleteCategory(button.dataset.deleteCategory);
                await refreshOwnerPage();
                showOwnerMessage("Category deleted.", "success");
            } catch (error) {
                showOwnerMessage(error.message, "danger");
            }
        });
    });

    document.querySelectorAll("[data-edit-menu]").forEach((button) => {
        button.addEventListener("click", () => {
            const restaurantId = button.dataset.restaurant;
            const menuItem = (ownerMenuItems[restaurantId] || []).find((item) => String(item.id) === String(button.dataset.editMenu));

            if (!menuItem) {
                return;
            }

            menuItemForm.elements.id.value = menuItem.id;
            menuRestaurant.value = restaurantId;
            populateCategoryOptions(restaurantId, menuItem.categoryId);
            menuItemForm.elements.name.value = menuItem.name;
            menuItemForm.elements.price.value = menuItem.price;
            if (menuItemForm.elements.imageUrl) {
                menuItemForm.elements.imageUrl.value = menuItem.imageUrl || "";
            }
            if (menuItemImagePreview) {
                menuItemImagePreview.src = menuItem.imageUrl ? resolveImageUrl(menuItem.imageUrl) : getMenuItemImage(menuItem);
                menuItemImagePreview.style.display = "block";
            }
            menuFormTitle.textContent = "Update menu item";
            menuSubmitBtn.textContent = "Update item";
            menuItemForm.classList.remove("hidden-form");
            if (menuCancelBtn) {
                menuCancelBtn.style.display = "inline-flex";
            }
            setOwnerView("menu");
        });
    });

    document.querySelectorAll("[data-delete-menu]").forEach((button) => {
        button.addEventListener("click", async () => {
            try {
                await deleteMenuItem(button.dataset.deleteMenu);
                await refreshOwnerPage();
                showOwnerMessage("Menu item deleted.", "success");
            } catch (error) {
                showOwnerMessage(error.message, "danger");
            }
        });
    });
}

// Reloads every owner panel after create, update, delete, or order changes.
async function refreshOwnerPage() {
    await loadOwnerData();
    renderManagedRestaurants();
    renderCategoryManagement();
    renderMenuManagement();
    renderIncomingOrders();
    populateRestaurantOptions();
    bindRestaurantActions();
}

if (restaurantForm) {
    restaurantForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(restaurantForm);
        const id = formData.get("id");
        const selectedFile = restaurantImageFile?.files?.[0] || null;
        let imageUrl = restaurantForm.elements.imageUrl?.value || "";
        const payload = {
            name: formData.get("name")?.trim(),
            address: formData.get("address")?.trim(),
            status: formData.get("status"),
            imageUrl: ""
        };

        if (!payload.name || !payload.address) {
            showOwnerMessage("Please fill the restaurant form before submitting.", "danger");
            return;
        }

        try {
            if (selectedFile) {
                imageUrl = await uploadImage("restaurant", selectedFile);
                if (restaurantForm.elements.imageUrl) {
                    restaurantForm.elements.imageUrl.value = imageUrl || "";
                }
            }

            payload.imageUrl = imageUrl || "";
            if (id) {
                const updated = await updateRestaurant({ id, ...payload });
                rememberRestaurantImage(updated?.id || id, payload.imageUrl);
                showOwnerMessage("Restaurant updated successfully.", "success");
            } else {
                const created = await createRestaurant(payload);
                rememberRestaurantImage(created?.id, payload.imageUrl);
                showOwnerMessage("Restaurant created successfully.", "success");
            }

            resetRestaurantForm();
            await refreshOwnerPage();
            setOwnerView("restaurants");
        } catch (error) {
            showOwnerMessage(error.message, "danger");
        }
    });
}

if (categoryForm) {
    categoryForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(categoryForm);
        const id = formData.get("id");
        const payload = {
            name: formData.get("name")?.trim(),
            restaurantId: formData.get("restaurantId") ? Number(formData.get("restaurantId")) : null
        };

        if (!payload.name || !payload.restaurantId) {
            showOwnerMessage("Please fill the category form before submitting.", "danger");
            return;
        }

        try {
            if (id) {
                await updateCategory({ id, ...payload });
                showOwnerMessage("Category updated successfully.", "success");
            } else {
                await createCategory(payload);
                showOwnerMessage("Category added successfully.", "success");
            }

            resetCategoryForm();
            await refreshOwnerPage();
            setOwnerView("categories");
        } catch (error) {
            showOwnerMessage(error.message, "danger");
        }
    });
}

if (menuRestaurant) {
    menuRestaurant.addEventListener("change", (event) => {
        populateCategoryOptions(event.target.value);
    });
}

// Returns form controls from edit mode to create mode on user action.
function bindOwnerFormCancelButtons() {
    if (restaurantCancelBtn) {
        restaurantCancelBtn.addEventListener("click", resetRestaurantForm);
    }
    if (categoryCancelBtn) {
        categoryCancelBtn.addEventListener("click", resetCategoryForm);
    }
    if (menuCancelBtn) {
        menuCancelBtn.addEventListener("click", resetMenuForm);
    }

    if (showRestaurantFormBtn) {
        showRestaurantFormBtn.addEventListener("click", () => showCreateForm("restaurants"));
    }

    if (showCategoryFormBtn) {
        showCategoryFormBtn.addEventListener("click", () => showCreateForm("categories"));
    }

    if (showMenuFormBtn) {
        showMenuFormBtn.addEventListener("click", () => showCreateForm("menu"));
    }
}

// Wire image upload + preview for owner forms.
if (restaurantForm?.elements?.imageUrl) {
    bindImagePreview({
        fileInput: restaurantImageFile,
        previewImg: restaurantImagePreview,
        getUrl: () => restaurantPreviewObjectUrl,
        setUrl: (next) => { restaurantPreviewObjectUrl = next; },
        hiddenInput: restaurantForm.elements.imageUrl
    });
}

if (menuItemForm?.elements?.imageUrl) {
    bindImagePreview({
        fileInput: menuItemImageFile,
        previewImg: menuItemImagePreview,
        getUrl: () => menuPreviewObjectUrl,
        setUrl: (next) => { menuPreviewObjectUrl = next; },
        hiddenInput: menuItemForm.elements.imageUrl
    });
}

if (menuItemForm) {
    menuItemForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(menuItemForm);
        const id = formData.get("id");
        const selectedFile = menuItemImageFile?.files?.[0] || null;
        let imageUrl = menuItemForm.elements.imageUrl?.value || "";
        const payload = {
            name: formData.get("name")?.trim(),
            price: formData.get("price") ? Number(formData.get("price")) : null,
            categoryId: formData.get("categoryId") ? Number(formData.get("categoryId")) : null,
            restaurantId: formData.get("restaurantId") ? Number(formData.get("restaurantId")) : null,
            imageUrl: ""
        };

        if (!payload.name || !payload.price || !payload.categoryId || !payload.restaurantId) {
            showOwnerMessage("Please fill the menu form before submitting.", "danger");
            return;
        }

        try {
            if (selectedFile) {
                imageUrl = await uploadImage("menu", selectedFile);
                if (menuItemForm.elements.imageUrl) {
                    menuItemForm.elements.imageUrl.value = imageUrl || "";
                }
            }

            payload.imageUrl = imageUrl || "";
            if (id) {
                const updated = await updateMenuItem({ id, ...payload });
                rememberMenuItemImage(updated?.id || id, payload.imageUrl);
                showOwnerMessage("Menu item updated successfully.", "success");
            } else {
                const created = await createMenuItem(payload);
                rememberMenuItemImage(created?.id, payload.imageUrl);
                showOwnerMessage("Menu item added successfully.", "success");
            }

            resetMenuForm();
            await refreshOwnerPage();
            setOwnerView("menu");
        } catch (error) {
            showOwnerMessage(error.message, "danger");
        }
    });
}

// Boots the owner dashboard and opens the first workspace view.
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

initOwnerPage();
