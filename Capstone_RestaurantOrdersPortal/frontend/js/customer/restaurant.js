const restaurantId = getQueryParam("id");
const restaurantNameElement = document.getElementById("restaurantName");
const restaurantAddressElement = document.getElementById("restaurantAddress");
const restaurantStatusElement = document.getElementById("restaurantStatus");
const restaurantDescriptionElement = document.getElementById("restaurantDescription");
const menuContainer = document.getElementById("menuContainer");
const cartContainer = document.getElementById("cartContainer");
const cartTotalElement = document.getElementById("cartTotal");
const placeOrderButton = document.getElementById("placeOrderBtn");
const pageMessage = document.getElementById("restaurantMessage");
let currentRestaurant = null;

// getting restaurant id whose items are in cart
function getCartRestaurantId(cart) {
    if (!cart) {
        return null;
    }

    if (cart.restaurantId) {
        return Number(cart.restaurantId);
    }

    if (cart.restaurant?.id) {
        return Number(cart.restaurant.id);
    }

    const firstItem = (cart.items || [])[0];
    return Number(firstItem?.restaurantId || firstItem?.menuItemRestaurantId || 0) || null;
}


// Stores the current cart restaurant id in local storage
function setActiveCartRestaurantId(restaurantId) {
    if (!restaurantId) {
        localStorage.removeItem(APP_KEYS.activeCartRestaurantId);
        return;
    }
    localStorage.setItem(APP_KEYS.activeCartRestaurantId, String(restaurantId));
}


function getActiveCartRestaurantId() {
    const value = localStorage.getItem(APP_KEYS.activeCartRestaurantId);
    return value ? Number(value) : null;
}


//Sync cart data with localStorage so data is not lost on page reload also 
function syncActiveCartRestaurantFromCart(cart) {
    const items = cart?.items || [];
    if (!items.length) {
        setActiveCartRestaurantId(null);
        return;
    }

    const restaurantId = getCartRestaurantId(cart) || getActiveCartRestaurantId();
    if (restaurantId) {
        setActiveCartRestaurantId(restaurantId);
    }
}

function showRestaurantMessage(message, tone = "info") {
    pageMessage.className = `banner ${tone}`;
    pageMessage.textContent = message;
}

//Get correct cart based on login status
async function getCurrentCartView() {
    if (isLoggedIn()) {
        return fetchServerCart();
    }

    const guestCart = getGuestCart();
    return {
        totalPrice: getGuestCartTotal(guestCart),
        items: guestCart.items
    };
}

// Renders the sidebar cart
async function renderCart() {
    const cart = await getCurrentCartView();
    let items = cart?.items || [];
    items.sort((a, b) => a.id - b.id);

    syncActiveCartRestaurantFromCart(cart);

    if (!items.length) {
        cartContainer.innerHTML = `
            <div class="empty-state compact">
                <h3>Your cart is empty</h3>
                <p>Add menu items to prepare your order.</p>
            </div>
        `;
        cartTotalElement.textContent = currency(0);
        return;
    }

    // logged in user cart
    if (isLoggedIn()) {
        cartContainer.innerHTML = items.map((item) => `
            <div class="cart-item">
                <div class="cart-item-main">
                    <h4>${item.menuItemName}</h4>
                    <p>${currency(Number(item.totalPrice) * Number(item.quantity || 1))}</p>

                </div>
                <div class="cart-actions">
                    <button class="qty-btn" data-action="decrease" data-id="${item.id}" data-qty="${item.quantity}">-</button>
                    <span>${item.quantity}</span>
                    <button class="qty-btn" data-action="increase" data-id="${item.id}" data-qty="${item.quantity}">+</button>
                    <button class="remove-btn" data-remove="${item.id}">Remove</button>
                </div>
            </div>
        `).join("");

        cartTotalElement.textContent = currency(cart.totalPrice);
    } 

    // guest user cart
    else {
        cartContainer.innerHTML = items.map((item) => `
            <div class="cart-item">
                <div class="cart-item-main">
                    <h4>${item.name}</h4>
                    <p>${currency(item.price)} each</p>
                </div>
                <div class="cart-actions">
                    <button class="qty-btn" data-action="decrease" data-id="${item.id}" data-qty="${item.quantity}">-</button>
                    <span>${item.quantity}</span>
                    <button class="qty-btn" data-action="increase" data-id="${item.id}" data-qty="${item.quantity}">+</button>
                    <button class="remove-btn" data-remove="${item.id}">Remove</button>
                </div>
            </div>
        `).join("");

        cartTotalElement.textContent = currency(getGuestCartTotal());
    }

    cartContainer.querySelectorAll(".qty-btn").forEach((button) => {
        button.addEventListener("click", async () => {
            const currentQty = Number(button.dataset.qty);
            const nextQty = button.dataset.action === "increase" ? currentQty + 1 : currentQty - 1;

            try {
                if (isLoggedIn()) {
                    if (nextQty <= 0) {
                        await removeServerCartItem(button.dataset.id);
                    } else {
                        await updateServerCartItem(button.dataset.id, nextQty);
                    }
                } else {
                    updateGuestCartQuantity(button.dataset.id, nextQty);
                }

                await renderCart();
                if (typeof renderNavbar === "function") {
                    renderNavbar();
                }
            } catch (error) {
                showRestaurantMessage(error.message, "danger");
            }
        });
    });

    cartContainer.querySelectorAll("[data-remove]").forEach((button) => {
        button.addEventListener("click", async () => {
            try {
                if (isLoggedIn()) {
                    await removeServerCartItem(button.dataset.remove);
                } else {
                    updateGuestCartQuantity(button.dataset.remove, 0);
                }

                await renderCart();
                if (typeof renderNavbar === "function") {
                    renderNavbar();
                }
            } catch (error) {
                showRestaurantMessage(error.message, "danger");
            }
        });
    });
}

// Display all categories and menu items
function renderMenu(categories, menuItemsByCategory) {
    if (!categories.length) {
        menuContainer.innerHTML = `
            <div class="empty-state panel">
                <h3>No categories available</h3>
                <p>This restaurant has not published menu categories yet.</p>
            </div>
        `;
        return;
    }

    menuContainer.innerHTML = categories.map((category) => {
        const items = menuItemsByCategory[category.id] || [];

        return `
            <section class="menu-section panel">
                <div class="section-heading">
                    <div>
                        <p class="eyebrow">Category</p>
                        <h3>${category.name}</h3>
                    </div>
                    <span class="chip">${items.length} items</span>
                </div>

                <div class="menu-list">
                    ${items.length ? items.map((item) => `
                        <article class="menu-card">
                            <div class="menu-card-main">
                                <img class="menu-thumb" src="${getMenuItemImage(item)}" alt="${item.name}">
                                <div>
                                    <h4>${item.name}</h4>
                                </div>
                            </div>
                            <div class="menu-card-side">
                                <strong>${currency(item.price)}</strong>
                                
                                <button class="primary-btn compact" data-add="${item.id}"
                                  ${currentRestaurant.status !== "OPEN" ? "disabled" : ""}
                                >
                                  ${currentRestaurant.status !== "OPEN" ? "CLOSED" : "Add"}
                                </button>
                            </div>
                        </article>
                    `).join("") : `<div class="empty-state compact"><h3>No items in this category</h3><p>Add menu items to make this section active.</p></div>`}
                </div>

            </section>
        `;
    }).join("");


    //getting all elements having data-add attribute and attach click event listener to add item to cart
    document.querySelectorAll("[data-add]").forEach((button) => {
        button.addEventListener("click", async () => {
            const itemId = Number(button.dataset.add);
            const item = Object.values(menuItemsByCategory).flat().find((menuItem) => Number(menuItem.id) === itemId);

            try {
                if (isLoggedIn()) {
                    const activeServerCart = await fetchServerCart();
                    const existingRestaurantId = getCartRestaurantId(activeServerCart);
                    const trackedRestaurantId = getActiveCartRestaurantId();
                    const lockedRestaurantId = existingRestaurantId || trackedRestaurantId;

                    if (lockedRestaurantId && Number(lockedRestaurantId) !== Number(currentRestaurant.id)) {
                        showRestaurantMessage("You already have items from another restaurant in cart. Clear cart first.", "danger");
                        return;
                    }
                    await addServerCartItem(item.id, 1);
                    setActiveCartRestaurantId(currentRestaurant.id);
                } else {
                    addToGuestCart(item, currentRestaurant);
                }

                await renderCart();
                if (typeof renderNavbar === "function") {
                    renderNavbar();
                }
                showRestaurantMessage("Item added to cart.", "success");
            } catch (error) {
                showRestaurantMessage(error.message, "danger");
            }
        });
    });
}

// Loads all data for the current restaurant detail page
async function initRestaurantPage() {
    currentRestaurant = await getRestaurantById(restaurantId);

    if (!currentRestaurant) {
        showRestaurantMessage("Restaurant not found.", "danger");
        return;
    }

    restaurantNameElement.textContent = currentRestaurant.name;
    restaurantAddressElement.textContent = currentRestaurant.address || "Address unavailable";
    restaurantStatusElement.innerHTML = `<span class="status-pill ${statusTone(currentRestaurant.status)}">${currentRestaurant.status || "OPEN"}</span>`;
    restaurantDescriptionElement.textContent = currentRestaurant.description || "Explore menu categories and build your order.";

    const categories = await getCategoriesByRestaurant(currentRestaurant.id);
    const menuItemsByCategory = {};

    for (const category of categories) {
        menuItemsByCategory[category.id] = await getMenuItemsByCategory(category.id);
    }

    renderMenu(categories, menuItemsByCategory);
    await renderCart();
}

if (placeOrderButton) {
    placeOrderButton.addEventListener("click", async () => {
        const cart = await getCurrentCartView();
        let items = cart?.items || [];
        items.sort((a, b) => a.id - b.id);

        if (!items.length) {
            showRestaurantMessage("Add at least one item before checkout.", "danger");
            return;
        }

        if (!isLoggedIn()) {
            showRestaurantMessage("Please login first to continue to checkout.", "danger");
            window.location.href = "login.html";
            return;
        }

        window.location.href = "checkout.html";
    });
}

initRestaurantPage();
