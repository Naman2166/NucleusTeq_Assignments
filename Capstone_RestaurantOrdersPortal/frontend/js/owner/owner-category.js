const ownerCategoriesList = document.getElementById("ownerCategoriesList");
const categoryForm = document.getElementById("categoryForm");
const categoryRestaurant = document.getElementById("categoryRestaurant");
const categoryFormTitle = document.getElementById("categoryFormTitle");
const categorySubmitBtn = document.getElementById("categorySubmitBtn");
const categoryCancelBtn = document.getElementById("categoryCancelBtn");
const showCategoryFormBtn = document.getElementById("showCategoryFormBtn");

// Fills restaurant dropdowns dynamically
function populateRestaurantOptions() {
    const options = ownerRestaurants.map((restaurant) => `
        <option value="${restaurant.id}">${restaurant.name}</option>
    `).join("");

    categoryRestaurant.innerHTML = `<option value="">Choose restaurant</option>${options}`;

    // Also refresh the menu restaurant select if it exists on the page
    const menuRestaurantEl = document.getElementById("menuRestaurant");
    if (menuRestaurantEl) {
        menuRestaurantEl.innerHTML = `<option value="">Choose restaurant</option>${options}`;
    }
}


// form reset
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


// display editable category 
function renderCategoryManagement() {
    ownerCategoriesList.innerHTML = ownerRestaurants.length
        ? ownerRestaurants.map((restaurant) => {
            const categories = ownerCategories[restaurant.id] || [];
            return `
                <article class="panel owner-restaurant-card">
                    <div class="inline-head">
                        <div>
                            <p class="eyebrow">Restaurant</p>
                            <h3>${restaurant.name}</h3>
                        </div>
                        <span class="chip">${categories.length} categories</span>
                    </div>
                    <div class="owner-subcard-list">
                        ${categories.length ? categories.map((category) => `
                            <div class="compact-row owner-mini-row">
                                <span class="categories">${category.name}</span>
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

//activate edit and delete buttons for categories
function bindCategoryActions() {
    document.querySelectorAll("[data-edit-category]").forEach((button) => {
        button.addEventListener("click", () => {
            const restaurantId = button.dataset.restaurant;
            const category = (ownerCategories[restaurantId] || []).find((item) => String(item.id) === String(button.dataset.editCategory));

            if (!category) return;

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
}


//from submit
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

// actiavtes add category button
function bindCategoryFormButtons() {
    if (categoryCancelBtn) {
        categoryCancelBtn.addEventListener("click", resetCategoryForm);
    }
    if (showCategoryFormBtn) {
        showCategoryFormBtn.addEventListener("click", () => {
            categoryFormTitle.textContent = "Add category";
            categorySubmitBtn.textContent = "Save category";
            categoryForm.classList.remove("hidden-form");
            categoryCancelBtn.style.display = "inline-flex";
        });
    }
}