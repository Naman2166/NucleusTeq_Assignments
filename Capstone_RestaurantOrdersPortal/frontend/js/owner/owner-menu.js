const ownerMenuList = document.getElementById("ownerMenuList");
const menuItemForm = document.getElementById("menuItemForm");
const menuRestaurant = document.getElementById("menuRestaurant");
const menuCategory = document.getElementById("menuCategory");
const menuFormTitle = document.getElementById("menuFormTitle");
const menuSubmitBtn = document.getElementById("menuSubmitBtn");
const menuCancelBtn = document.getElementById("menuCancelBtn");
const showMenuFormBtn = document.getElementById("showMenuFormBtn");
const menuItemImageFile = document.getElementById("menuItemImageFile");
const menuItemImagePreview = document.getElementById("menuItemImagePreview");

let imgPreviewUrl = "";


//prvovides category options based on the chosen restaurant
function populateCategoryOptions(restaurantId, selectedCategoryId = "") {
    const categories = ownerCategories[restaurantId] || [];
    menuCategory.innerHTML = `<option value="">Choose category</option>${categories.map((category) => `
        <option value="${category.id}" ${String(selectedCategoryId) === String(category.id) ? "selected" : ""}>${category.name}</option>
    `).join("")}`;
}


//image preview
function showLocalMenuPreview() {
    if (!menuItemImageFile || !menuItemImagePreview) return;

    if (imgPreviewUrl) {
        URL.revokeObjectURL(imgPreviewUrl);
        imgPreviewUrl = "";
    }

    const file = menuItemImageFile.files?.[0];
    if (!file) {
        menuItemImagePreview.src = "";
        menuItemImagePreview.style.display = "none";
        return;
    }

    imgPreviewUrl = URL.createObjectURL(file);
    menuItemImagePreview.src = imgPreviewUrl;
    menuItemImagePreview.style.display = "block";
}

if (menuItemForm?.elements?.imageUrl) {
    if (menuItemImageFile) {
        menuItemImageFile.addEventListener("keydown", (event) => {
            if (event.key === "Enter") {
                event.preventDefault();
                event.stopPropagation();
            }
        });

        menuItemImageFile.addEventListener("change", (event) => {
            event.preventDefault();
            event.stopImmediatePropagation();
            showLocalMenuPreview();
            if (!menuItemImageFile.files?.[0]) {
                menuItemForm.elements.imageUrl.value = "";
            }
        });
    }
}

// Populate categories when restaurant selection changes
if (menuRestaurant) {
    menuRestaurant.addEventListener("change", (event) => {
        populateCategoryOptions(event.target.value);
    });
}


//form reset
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
    if (imgPreviewUrl) {
        URL.revokeObjectURL(imgPreviewUrl);
        imgPreviewUrl = "";
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


// fetching menu rows by restaurant
function renderMenuManagement() {
    ownerMenuList.innerHTML = ownerRestaurants.length
        ? ownerRestaurants.map(renderRestaurant).join("")
        : `<div class="empty-state compact">
                <h3>No restaurants yet</h3>
                <p>Create a restaurant before adding menu items.</p>
           </div>`;
}


//fetching restauarnt with categories
function renderRestaurant(restaurant) {
    const items = ownerMenuItems[restaurant.id] || [];
    const categories = ownerCategories[restaurant.id] || [];

    return `
        <article class="panel owner-restaurant-card">
            <div class="inline-head">
                <div>
                  <p class="eyebrow">Restaurant</p>
                  <h3>${restaurant.name}</h3>
                </div>
                <span class="chip">${items.length} items</span>
            </div>

            <div class="owner-subcard-list">
              ${categories.map(category => {
                 const filtered = items.filter(i => i.categoryId == category.id);
                   return `
                    <p class="category-name">${category.name}</p>
                      ${filtered.length
                        ? filtered.map(item => renderMenuItem(item, restaurant.id)).join("")
                        : `<p class="subtle">No items</p>`
                       }`;
                    }).join("")
                }
            </div>
        </article>
    `;
}


//fetching menu items
function renderMenuItem(item, restaurantId) {
    return `
        <div class="owner-menu-row compact-row">
            <img class="owner-menu-thumb" src="${getMenuItemImage(item)}" alt="${item.name}">

            <div class="owner-menu-copy">
                <p class="eyebrow">Menu item</p>
                <h3>${item.name}</h3>
                <p class="subtle">${currency(item.price)}</p>
            </div>

            <div class="inline-actions">
                <button class="secondary-btn compact"
                    data-edit-menu="${item.id}"
                    data-restaurant="${restaurantId}">
                    Update
                </button>

                <button class="ghost-btn compact"
                    data-delete-menu="${item.id}">
                    Delete
                </button>
            </div>
        </div>
    `;
}

// Loading item into form for updating
function bindMenuActions() {
    document.querySelectorAll("[data-edit-menu]").forEach((button) => {
        button.addEventListener("click", () => {
            const restaurantId = button.dataset.restaurant;
            const menuItem = (ownerMenuItems[restaurantId] || []).find((item) => String(item.id) === String(button.dataset.editMenu));

            if (!menuItem) return;

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



//form submit
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

// Cancel and show-form buttons for menu items.
function bindMenuFormButtons() {
    if (menuCancelBtn) {
        menuCancelBtn.addEventListener("click", resetMenuForm);
    }
    if (showMenuFormBtn) {
        showMenuFormBtn.addEventListener("click", () => {
            menuFormTitle.textContent = "Add menu item";
            menuSubmitBtn.textContent = "Save item";
            menuItemForm.classList.remove("hidden-form");
            menuCancelBtn.style.display = "inline-flex";
        });
    }
}