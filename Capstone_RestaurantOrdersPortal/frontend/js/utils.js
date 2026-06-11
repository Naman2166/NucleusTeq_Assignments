// constants
const APP_KEYS = {
    demoData: "demo_data",
    guestCart: "guest_cart",
    selectedAddressId: "selected_address_id",
    activeCartRestaurantId: "active_cart_restaurant_id",
    imageOverrides: "image_overrides",
    user: "user",
    role: "role",
    token: "token"
};

// Status for restaurant owner
const OWNER_STATUSES = ["PLACED", "PENDING", "DELIVERED", "COMPLETED", "CANCELLED"];
const CANCELLATION_WINDOW_MS = 30 * 1000;


//helper functions
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

async function logoutUser() {
    localStorage.removeItem(APP_KEYS.token);
    localStorage.removeItem(APP_KEYS.user);
    localStorage.removeItem(APP_KEYS.role);
    localStorage.removeItem(APP_KEYS.selectedAddressId);
    await clearServerCart();
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

//Returning CSS class based on status
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


//Extracting error message from backend response
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


// image handling 
// Getting saved image URLs from localStorage
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

//Saves image data in localstorage
function setImageOverrides(overrides) {
    localStorage.setItem(APP_KEYS.imageOverrides, JSON.stringify(overrides));
}

//Stores restaurant image URL in localStorage 
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

//Add locally saved images to restaurant objects even if backend not return them
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


// cart for not logged-in users
// Getting cart from localStorage
function getGuestCart() {
    return JSON.parse(localStorage.getItem(APP_KEYS.guestCart)) || {
        restaurantId: null,
        restaurantName: "",
        items: []
    };
}

//Saves guest cart
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
    localStorage.removeItem(APP_KEYS.guestCart);
    localStorage.removeItem(APP_KEYS.activeCartRestaurantId);
}

async function clearServerCart() {
    const cart = await fetchServerCart();
    const items = cart?.items || [];

    for (const item of items) {
        await removeServerCartItem(item.id);
    }

    localStorage.removeItem(APP_KEYS.activeCartRestaurantId);
}


//Calculates total price of items in guest cart
function getGuestCartTotal(cart = getGuestCart()) {
    return cart.items.reduce((total, item) => total + Number(item.price) * Number(item.quantity), 0);
}

//Counts total items in guest cart
function getGuestCartCount() {
    return getGuestCart().items.reduce((total, item) => total + Number(item.quantity), 0);
}


//Address Helpers
function getSelectedAddressId() {
    return localStorage.getItem(APP_KEYS.selectedAddressId);
}

function setSelectedAddressId(addressId) {
    localStorage.setItem(APP_KEYS.selectedAddressId, String(addressId));
}


// wallet helper, here updating wallet amount
function adjustWalletBalance(delta) {
    const user = getUser();

    if (!user || typeof user.walletBalance !== "number") {
        return;
    }

    user.walletBalance = Number(user.walletBalance) + Number(delta);
    setUser(user);
}


// Order helper
function isOrderCancellable(orderTime, status) {
    if (status === "CANCELLED" || status === "COMPLETED") {
        return false;
    }

    return Date.now() - new Date(orderTime).getTime() <= CANCELLATION_WINDOW_MS;
}


function getQueryParam(name) {
    return new URLSearchParams(window.location.search).get(name);
}


//creating full image url path 
function resolveImageUrl(imageUrl) {
    if (!imageUrl) {
        return "";
    }

    const apiOrigin = (() => {
        try {
            return new URL(BASE_URL).origin;
        } catch (error) {
            return "http://localhost:8080";
        }
    })();

    if (String(imageUrl).startsWith("/")) {
        return `${apiOrigin}${imageUrl}`;
    }

    return imageUrl;
}


function getRestaurantImage(restaurant) {
    if (restaurant?.imageUrl) {
        return resolveImageUrl(restaurant.imageUrl);
    }
    return `https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D`;
}

function getMenuItemImage(item) {
    if (item?.imageUrl) {
        return resolveImageUrl(item.imageUrl);
    }
    return `https://plus.unsplash.com/premium_photo-1675252369719-dd52bc69c3df?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D`;
}