// Restaurant management section.
// Depends on: owner.state.js, utils.js, api.js


// --------------- DOM References ---------------

const managedRestaurants = document.getElementById("managedRestaurants");

const restaurantForm = document.getElementById("restaurantForm");
const restaurantFormTitle = document.getElementById("restaurantFormTitle");
const restaurantSubmitBtn = document.getElementById("restaurantSubmitBtn");
const restaurantCancelBtn = document.getElementById("restaurantCancelBtn");
const showRestaurantFormBtn = document.getElementById("showRestaurantFormBtn");
const restaurantImageFile = document.getElementById("restaurantImageFile");
const restaurantImagePreview = document.getElementById("restaurantImagePreview");

let restaurantPreviewObjectUrl = "";


// --------------- Image Preview ---------------

function showLocalRestaurantPreview() {
    if (!restaurantImageFile || !restaurantImagePreview) return;

    if (restaurantPreviewObjectUrl) {
        URL.revokeObjectURL(restaurantPreviewObjectUrl);
        restaurantPreviewObjectUrl = "";
    }

    const file = restaurantImageFile.files?.[0];
    if (!file) {
        restaurantImagePreview.src = "";
        restaurantImagePreview.style.display = "none";
        return;
    }

    restaurantPreviewObjectUrl = URL.createObjectURL(file);
    restaurantImagePreview.src = restaurantPreviewObjectUrl;
    restaurantImagePreview.style.display = "block";
}

if (restaurantForm?.elements?.imageUrl) {
    if (restaurantImageFile) {
        restaurantImageFile.addEventListener("keydown", (event) => {
            if (event.key === "Enter") {
                event.preventDefault();
                event.stopPropagation();
            }
        });

        restaurantImageFile.addEventListener("change", (event) => {
            event.preventDefault();
            event.stopImmediatePropagation();
            showLocalRestaurantPreview();
            if (!restaurantImageFile.files?.[0]) {
                restaurantForm.elements.imageUrl.value = "";
            }
        });
    }
}


// --------------- Form Reset ---------------

// Restores the restaurant form to create mode.
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


// --------------- Render ---------------

// Renders restaurant cards.
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


// --------------- Action Bindings ---------------

function bindRestaurantActions() {
    document.querySelectorAll("[data-edit-restaurant]").forEach((button) => {
        button.addEventListener("click", () => {
            const restaurant = ownerRestaurants.find((item) => String(item.id) === String(button.dataset.editRestaurant));

            if (!restaurant) return;

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
}


// --------------- Form Submit ---------------

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

// Cancel and show-form buttons for restaurants.
function bindRestaurantFormButtons() {
    if (restaurantCancelBtn) {
        restaurantCancelBtn.addEventListener("click", resetRestaurantForm);
    }
    if (showRestaurantFormBtn) {
        showRestaurantFormBtn.addEventListener("click", () => {
            restaurantFormTitle.textContent = "Add restaurant";
            restaurantSubmitBtn.textContent = "Save restaurant";
            restaurantForm.classList.remove("hidden-form");
            restaurantCancelBtn.style.display = "inline-flex";
        });
    }
}