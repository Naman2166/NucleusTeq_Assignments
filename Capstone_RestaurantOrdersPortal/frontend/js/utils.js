//demo data 
const APP_KEYS = {
    demoData: "rop_demo_data",
    guestCart: "rop_guest_cart",
    selectedAddressId: "rop_selected_address_id",
    activeCartRestaurantId: "rop_active_cart_restaurant_id",
    imageOverrides: "rop_image_overrides",
    user: "user",
    role: "role",
    token: "token"
};

//status for restaurant owner
const OWNER_STATUSES = ["PLACED", "PENDING", "DELIVERED", "COMPLETED", "CANCELLED"];
const CANCELLATION_WINDOW_MS = 30 * 1000;

function getToken() {
    return localStorage.getItem(APP_KEYS.token);
}

function getUser() {
    const raw = localStorage.getItem(APP_KEYS.user);
    return raw ? JSON.parse(raw) : null;
}

function setUser(user) {
    localStorage.setItem(APP_KEYS.user, JSON.stringify(user));
}

function getRole() {
    return localStorage.getItem(APP_KEYS.role) || getUser()?.role || "";
}

function isLoggedIn() {
    return Boolean(getToken());
}

function logoutUser() {
    localStorage.removeItem(APP_KEYS.token);
    localStorage.removeItem(APP_KEYS.user);
    localStorage.removeItem(APP_KEYS.role);
    localStorage.removeItem(APP_KEYS.selectedAddressId);
}

function currency(amount) {
    return new Intl.NumberFormat("en-IN", {
        style: "currency",
        currency: "INR",
        maximumFractionDigits: 0
    }).format(Number(amount || 0));
}

function formatDate(value) {
    return new Intl.DateTimeFormat("en-IN", {
        dateStyle: "medium",
        timeStyle: "short"
    }).format(new Date(value));
}

function statusTone(status) {
    const tones = {
        OPEN: "success",
        CLOSED: "danger",
        PLACED: "brand",
        PENDING: "warning",
        DELIVERED: "info",
        COMPLETED: "completed",
        CANCELLED: "danger"
    };

    return tones[status] || "neutral";
}


function getErrorMessage(payload) {
    if (!payload) {
        return "Something went wrong.";
    }

    if (typeof payload === "string") {
        return payload;
    }

    if (payload.message) {
        return payload.message;
    }

    const values = Object.values(payload);
    return values.length ? values[0] : "Something went wrong.";
}


//--------------- API and data handling utils ---------------

//API helper with automatic token handling and error parsing
async function apiFetch(path, options = {}) {
    const headers = { ...(options.headers || {}) };
    const hasBody = options.body !== undefined;

    const isFormDataBody = hasBody && typeof FormData !== "undefined" && options.body instanceof FormData;

    if (hasBody && !isFormDataBody && !headers["Content-Type"]) {
        headers["Content-Type"] = "application/json";
    }

    //getting token from localstorage
    const token = getToken();

    //adding header for authorization if token is present and header is not already set
    if (token && !headers.Authorization) {
        headers.Authorization = `Bearer ${token}`;
    }

    //API Call
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

//safe API call that returns null on error for graceful UI handling
async function safeApi(path, options = {}) {
    try {
        return await apiFetch(path, options);
    } catch (error) {
        return null;
    }
}

// Stores local image URLs keyed by entity id to handle list APIs
// that may not return imageUrl immediately after create/update.
function getImageOverrides() {
    const raw = localStorage.getItem(APP_KEYS.imageOverrides);
    if (!raw) {
        return { restaurants: {}, menuItems: {} };
    }

    try {
        const parsed = JSON.parse(raw);
        return {
            restaurants: parsed?.restaurants || {},
            menuItems: parsed?.menuItems || {}
        };
    } catch (error) {
        return { restaurants: {}, menuItems: {} };
    }
}

function setImageOverrides(overrides) {
    localStorage.setItem(APP_KEYS.imageOverrides, JSON.stringify(overrides));
}

function rememberRestaurantImage(restaurantId, imageUrl) {
    if (!restaurantId || !imageUrl) return;
    const overrides = getImageOverrides();
    overrides.restaurants[String(restaurantId)] = imageUrl;
    setImageOverrides(overrides);
}

function rememberMenuItemImage(menuItemId, imageUrl) {
    if (!menuItemId || !imageUrl) return;
    const overrides = getImageOverrides();
    overrides.menuItems[String(menuItemId)] = imageUrl;
    setImageOverrides(overrides);
}

function applyRestaurantImageOverrides(restaurants) {
    const restaurantMap = getImageOverrides().restaurants;
    return (restaurants || []).map((restaurant) => ({
        ...restaurant,
        imageUrl: restaurant.imageUrl || restaurantMap[String(restaurant.id)] || ""
    }));
}

function applyMenuImageOverrides(items) {
    const menuMap = getImageOverrides().menuItems;
    return (items || []).map((item) => ({
        ...item,
        imageUrl: item.imageUrl || menuMap[String(item.id)] || ""
    }));
}


// --------------- Restaurant and Menu API calls---------------

//API call to fetch restaurant collections.
async function getRestaurants() {
    const remote = await safeApi("/restaurants");
    return Array.isArray(remote) ? applyRestaurantImageOverrides(remote) : [];
}


//API call to fetch restaurant by id.
async function getRestaurantById(restaurantId) {
    const remote = await safeApi(`/restaurants/${restaurantId}`);
    if (!remote?.id) return null;
    return applyRestaurantImageOverrides([remote])[0];
}


//API call to fetch categories by restaurant id.
async function getCategoriesByRestaurant(restaurantId) {
    const remote = await safeApi(`/categories/restaurants/${restaurantId}`);
    return Array.isArray(remote) ? remote : [];
}


//API call to fetch menu items by categoryId.
async function getMenuItemsByCategory(categoryId) {
    const remote = await safeApi(`/menu-items/category/${categoryId}`);
    return Array.isArray(remote) ? applyMenuImageOverrides(remote) : [];
}

//------------- user data API ------------------
async function fetchCurrentUser() {
    return apiFetch("/users/me");
}


//---------- dummy Cart/cartItem API calls -----------

function getGuestCart() {
    return JSON.parse(localStorage.getItem(APP_KEYS.guestCart)) || {
        restaurantId: null,
        restaurantName: "",
        items: []
    };
}

function setGuestCart(cart) {
    localStorage.setItem(APP_KEYS.guestCart, JSON.stringify(cart));
}

function addToGuestCart(item, restaurant) {
    const cart = getGuestCart();

    if (cart.restaurantId && String(cart.restaurantId) !== String(restaurant.id)) {
        throw new Error("Only one restaurant order at a time. Clear the cart before adding from another restaurant.");
    }

    const existingItem = cart.items.find((cartItem) => String(cartItem.id) === String(item.id));

    if (existingItem) {
        existingItem.quantity += 1;
    } else {
        cart.items.push({
            id: item.id,
            name: item.name,
            price: Number(item.price),
            quantity: 1
        });
    }

    cart.restaurantId = restaurant.id;
    cart.restaurantName = restaurant.name;
    setGuestCart(cart);
    return cart;
}

function updateGuestCartQuantity(itemId, quantity) {
    const cart = getGuestCart();
    const item = cart.items.find((cartItem) => String(cartItem.id) === String(itemId));

    if (!item) {
        return cart;
    }

    if (quantity <= 0) {
        cart.items = cart.items.filter((cartItem) => String(cartItem.id) !== String(itemId));
    } else {
        item.quantity = quantity;
    }

    if (!cart.items.length) {
        cart.restaurantId = null;
        cart.restaurantName = "";
    }

    setGuestCart(cart);
    return cart;
}

function clearGuestCart() {
    setGuestCart({ restaurantId: null, restaurantName: "", items: [] });
}

function getGuestCartTotal(cart = getGuestCart()) {
    return cart.items.reduce((total, item) => total + Number(item.price) * Number(item.quantity), 0);
}

function getGuestCartCount() {
    return getGuestCart().items.reduce((total, item) => total + Number(item.quantity), 0);
}


//---------- backend Cart/cartItem API call -----------

//API calls for cart management for logged in users, with fallback to guest cart if API fails or user is not logged in
async function fetchServerCart() {
    if (!isLoggedIn()) {
        return null;
    }

    const role = getRole();

    // Only USER can access cart
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


//API to add item in cart in backend
async function addServerCartItem(menuItemId, quantity) {
    return apiFetch("/cart/items", {
        method: "POST",
        body: JSON.stringify({ menuItemId, quantity })
    });
}


//API to update cart item quantity in backend
async function updateServerCartItem(cartItemId, quantity) {
    return apiFetch(`/cart-items/${cartItemId}`, {
        method: "PUT",
        body: JSON.stringify({ quantity })
    });
}

//API to remove item from cart in backend
async function removeServerCartItem(cartItemId) {
    return apiFetch(`/cart/items/${cartItemId}`, { method: "DELETE" });
}


//API to clear entire cart in backend
async function clearServerCart() {
    return apiFetch("/cart", { method: "DELETE" });
}

//API call to fetch cart item count for navbar display, returns guest cart count if user is not logged in or API fails
async function getNavbarCartCount() {
    const role = getRole();

    if (!isLoggedIn() || role !== "USER") {
        return 0;   // don't call cart
    }

    try {
        const cart = await fetchServerCart();
        return (cart?.items || []).reduce((total, item) => total + Number(item.quantity), 0);
    } catch (error) {
        return 0;
    }
}


//---------- Address API calls -----------

//API calls for address management
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

function getSelectedAddressId() {
    return localStorage.getItem(APP_KEYS.selectedAddressId);
}

function setSelectedAddressId(addressId) {
    localStorage.setItem(APP_KEYS.selectedAddressId, String(addressId));
}


//----------- Order API calls -------------------

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


//---------- Restaurant API calls -----------

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


//---------- Category API calls -----------

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


//---------- MenuItem API calls -----------

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

// Uploads an image file and returns the relative image path from backend.
async function uploadImage(type, file) {
    const formData = new FormData();
    formData.append("file", file);
    return apiFetch(`/image-upload/${encodeURIComponent(type)}`, {
        method: "POST",
        body: formData
    });
}

// Display requirement: <img src="http://localhost:8080{imageUrl}">
function resolveImageUrl(imageUrl) {
    if (!imageUrl) {
        return "";
    }

    const apiOrigin = (() => {
        try {
            return new URL(BASE_URL).origin; // http://localhost:8080
        } catch (error) {
            return "http://localhost:8080";
        }
    })();

    if (String(imageUrl).startsWith("/")) {
        return `${apiOrigin}${imageUrl}`;
    }

    return imageUrl;
}

async function deleteMenuItem(itemId) {
    return apiFetch(`/menu-items/${itemId}`, {
        method: "DELETE"
    });
}


//---------- Owner Order management API calls -----------

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


//----------------- Other utils -----------------

function adjustWalletBalance(delta) {
    const user = getUser();

    if (!user || typeof user.walletBalance !== "number") {
        return;
    }

    user.walletBalance = Number(user.walletBalance) + Number(delta);
    setUser(user);
}


function isOrderCancellable(orderTime, status) {
    if (status === "CANCELLED" || status === "COMPLETED") {
        return false;
    }

    return Date.now() - new Date(orderTime).getTime() <= CANCELLATION_WINDOW_MS;
}

function getQueryParam(name) {
    return new URLSearchParams(window.location.search).get(name);
}



function escapeHtml(value) {
    return String(value ?? "").replace(/[&<>"']/g, (char) => ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;"
    }[char]));
}


//slugify string for generating image keys
function slugify(value) {
    return String(value || "")
        .toLowerCase()
        .replace(/[^a-z0-9]+/g, "-")
        .replace(/^-|-$/g, "");
}

//restaurant image 
function getRestaurantImage(restaurant) {
    if (restaurant?.imageUrl) {
        return resolveImageUrl(restaurant.imageUrl);
    }
    const key = slugify(restaurant?.name || "restaurant");
    return `https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D`;
}

//menu item image
function getMenuItemImage(item) {
    if (item?.imageUrl) {
        return resolveImageUrl(item.imageUrl);
    }
    const key = slugify(item?.name || "food");
    return `https://plus.unsplash.com/premium_photo-1675252369719-dd52bc69c3df?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D`;
}

