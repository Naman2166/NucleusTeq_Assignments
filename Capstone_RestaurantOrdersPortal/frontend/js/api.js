// API helper with error handling
async function apiFetch(path, options = {}) {
    const headers = { ...(options.headers || {}) };
    const hasBody = options.body !== undefined;

    const isFormDataBody = hasBody && typeof FormData !== "undefined" && options.body instanceof FormData;

    if (hasBody && !isFormDataBody && !headers["Content-Type"]) {
        headers["Content-Type"] = "application/json";
    }

    const token = getToken();

    if (token && !headers.Authorization) {
        headers.Authorization = `Bearer ${token}`;
    }

    const response = await fetch(BASE_URL + path, {
        ...options,
        headers
    });

    if (response.status === 204) {
        return null;
    }

    const contentType = response.headers.get("content-type") || "";
    const payload = contentType.includes("application/json")
        ? await response.json()
        : await response.text();

    if (!response.ok) {
        throw new Error(getErrorMessage(payload));
    }

    return payload;
}


// Safe API call that returns null on error for graceful UI handling.
async function safeApi(path, options = {}) {
    try {
        return await apiFetch(path, options);
    } catch (error) {
        return null;
    }
}


// --------------- Restaurant & Menu ---------------

// Fetch all restaurants.
async function getRestaurants() {
    const remote = await safeApi("/restaurants");
    return Array.isArray(remote) ? applyRestaurantImageOverrides(remote) : [];
}

// Fetch a single restaurant by id.
async function getRestaurantById(restaurantId) {
    const remote = await safeApi(`/restaurants/${restaurantId}`);
    if (!remote?.id) return null;
    return applyRestaurantImageOverrides([remote])[0];
}

// Fetch categories for a restaurant.
async function getCategoriesByRestaurant(restaurantId) {
    const remote = await safeApi(`/categories/restaurants/${restaurantId}`);
    return Array.isArray(remote) ? remote : [];
}

// Fetch menu items for a category.
async function getMenuItemsByCategory(categoryId) {
    const remote = await safeApi(`/menu-items/category/${categoryId}`);
    return Array.isArray(remote) ? applyMenuImageOverrides(remote) : [];
}


// --------------- User ---------------

async function fetchCurrentUser() {
    return apiFetch("/users/me");
}


// --------------- Server Cart ---------------

// Fetch the cart for the logged-in USER; returns null for non-USER roles.
async function fetchServerCart() {
    if (!isLoggedIn()) {
        return null;
    }

    const role = getRole();

    if (role !== "USER") {
        return null;
    }

    try {
        return await apiFetch("/cart");
    } catch (error) {
        if (error.message.toLowerCase().includes("cart")) {
            return { cartId: null, totalPrice: 0, items: [] };
        }
        throw error;
    }
}

// Add an item to the server cart.
async function addServerCartItem(menuItemId, quantity) {
    return apiFetch("/cart/items", {
        method: "POST",
        body: JSON.stringify({ menuItemId, quantity })
    });
}

// Update quantity of a cart item on the server.
async function updateServerCartItem(cartItemId, quantity) {
    return apiFetch(`/cart-items/${cartItemId}`, {
        method: "PUT",
        body: JSON.stringify({ quantity })
    });
}

// Remove a cart item from the server.
async function removeServerCartItem(cartItemId) {
    return apiFetch(`/cart/items/${cartItemId}`, { method: "DELETE" });
}

// Clear the entire server cart.
async function clearServerCart() {
    return apiFetch("/cart", { method: "DELETE" });
}

// Returns cart item count for navbar; 0 for non-USER roles or on failure.
async function getNavbarCartCount() {
    const role = getRole();

    if (!isLoggedIn() || role !== "USER") {
        return 0;
    }

    try {
        const cart = await fetchServerCart();
        return (cart?.items || []).reduce((total, item) => total + Number(item.quantity), 0);
    } catch (error) {
        return 0;
    }
}


// --------------- Addresses ---------------

async function fetchAddresses() {
    return apiFetch("/addresses");
}

async function createAddress(payload) {
    return apiFetch("/addresses", {
        method: "POST",
        body: JSON.stringify(payload)
    });
}

async function deleteAddress(addressId) {
    return apiFetch(`/addresses/${addressId}`, {
        method: "DELETE"
    });
}


// --------------- Orders (User) ---------------

async function placeOrder(addressId) {
    return apiFetch("/orders", {
        method: "POST",
        body: JSON.stringify({ addressId })
    });
}

async function fetchUserOrders() {
    return apiFetch("/orders");
}

async function cancelUserOrder(orderId) {
    return apiFetch(`/orders/${orderId}/cancel`, {
        method: "PUT"
    });
}


// --------------- Restaurants (Owner) ---------------

async function fetchOwnerRestaurants() {
    const restaurants = await apiFetch("/restaurants/my-restaurants");
    return applyRestaurantImageOverrides(restaurants);
}

async function createRestaurant(payload) {
    return apiFetch("/restaurants", {
        method: "POST",
        body: JSON.stringify({
            name: payload.name,
            address: payload.address,
            status: payload.status,
            imageUrl: payload.imageUrl || null
        })
    });
}

async function updateRestaurant(payload) {
    return apiFetch(`/restaurants/${payload.id}`, {
        method: "PUT",
        body: JSON.stringify({
            name: payload.name,
            address: payload.address,
            status: payload.status,
            imageUrl: payload.imageUrl || null
        })
    });
}

async function deleteRestaurant(restaurantId) {
    return apiFetch(`/restaurants/${restaurantId}`, {
        method: "DELETE"
    });
}


// --------------- Categories ---------------

async function createCategory(payload) {
    return apiFetch("/categories", {
        method: "POST",
        body: JSON.stringify(payload)
    });
}

async function updateCategory(payload) {
    return apiFetch(`/categories/${payload.id}`, {
        method: "PUT",
        body: JSON.stringify({
            name: payload.name,
            restaurantId: Number(payload.restaurantId)
        })
    });
}

async function deleteCategory(categoryId) {
    return apiFetch(`/categories/${categoryId}`, {
        method: "DELETE"
    });
}


// --------------- Menu Items ---------------

async function fetchMenuItemsByRestaurant(restaurantId) {
    const items = await apiFetch(`/menu-items/restaurants/${restaurantId}`);
    return applyMenuImageOverrides(items);
}

async function createMenuItem(payload) {
    return apiFetch("/menu-items", {
        method: "POST",
        body: JSON.stringify({
            name: payload.name,
            price: Number(payload.price),
            categoryId: Number(payload.categoryId),
            restaurantId: Number(payload.restaurantId),
            imageUrl: payload.imageUrl || null
        })
    });
}

async function updateMenuItem(payload) {
    return apiFetch(`/menu-items/${payload.id}`, {
        method: "PUT",
        body: JSON.stringify({
            name: payload.name,
            price: Number(payload.price),
            categoryId: Number(payload.categoryId),
            restaurantId: Number(payload.restaurantId),
            imageUrl: payload.imageUrl || null
        })
    });
}

async function deleteMenuItem(itemId) {
    return apiFetch(`/menu-items/${itemId}`, {
        method: "DELETE"
    });
}

// Upload an image file
async function uploadImage(type, file) {
    const formData = new FormData();
    formData.append("file", file);
    return apiFetch(`/image-upload/${encodeURIComponent(type)}`, {
        method: "POST",
        body: formData
    });
}


// --------------- Orders  ---------------

async function fetchOrdersByRestaurant(restaurantId) {
    return apiFetch(`/orders/restaurants/${restaurantId}`);
}

async function updateOwnerOrderStatus(orderId, status) {
    return apiFetch(`/orders/${orderId}/status?status=${encodeURIComponent(status)}`, {
        method: "PUT"
    });
}

async function cancelOrderByOwner(orderId) {
    return apiFetch(`/orders/${orderId}/cancel-by-owner`, {
        method: "PUT"
    });
}