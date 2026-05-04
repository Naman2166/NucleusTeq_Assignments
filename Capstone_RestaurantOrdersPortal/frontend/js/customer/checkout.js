const checkoutWallet = document.getElementById("checkoutWallet");
const checkoutCart = document.getElementById("checkoutCart");
const checkoutCartHeaderActions = document.getElementById("checkoutCartHeaderActions");
const checkoutAddresses = document.getElementById("checkoutAddresses");
const checkoutMessage = document.getElementById("checkoutMessage");
const confirmOrderBtn = document.getElementById("confirmOrderBtn");
const checkoutActiveOrders = document.getElementById("checkoutActiveOrders");

let checkoutCartData = { items: [], totalPrice: 0 };
let cancellationTimerId = null;

// Shows checkout message
function showCheckoutMessage(message, tone = "info") {
    checkoutMessage.className = `banner ${tone}`;
    checkoutMessage.textContent = message;
}

//refresh user to get latest data
async function refreshCheckoutUser() {
    const user = await fetchCurrentUser();
    setUser(user);
    checkoutWallet.textContent = currency(user.walletBalance || 0);
}

//render cart
function renderCheckoutCart(cart) {
    let items = cart?.items || [];
    items.sort((a, b) => a.id - b.id);
    checkoutCartData = cart || { items: [], totalPrice: 0 };
    confirmOrderBtn.disabled = !items.length;

    if (checkoutCartHeaderActions) {
        checkoutCartHeaderActions.innerHTML = items.length
            ? `<button type="button" class="danger-btn compact" id="clearCheckoutCartBtn">Clear cart</button>`
            : "";
    }

    if (!items.length) {
        checkoutCart.innerHTML = `
            <div class="empty-state compact">
                <h3>Your cart is empty</h3>
                <p>Add menu items before placing an order.</p>
            </div>
        `;
        return;
    }

    checkoutCart.innerHTML = `
        ${items.map((item) => `
            <div class="list-row cart-row checkout-cart-row compact-cart-row">
                <div class="checkout-item-copy">
                    <strong>${item.menuItemName}</strong>
                    <p>${currency(Number(item.totalPrice) * Number(item.quantity || 1))}</p>
                </div>
                <div class="checkout-item-actions">
                    <div class="price-qty-row">
                        <strong class="item-total">${currency(item.totalPrice)}</strong>
                        <div class="cart-actions cart-actions-compact">
                            <button class="qty-btn" data-action="decrease" data-id="${item.id}" data-qty="${item.quantity}" aria-label="Decrease quantity">-</button>
                            <span class="qty-pill">${item.quantity}</span>
                            <button class="qty-btn" data-action="increase" data-id="${item.id}" data-qty="${item.quantity}" aria-label="Increase quantity">+</button>
                        </div>
                    </div>
                </div>
            </div>
        `).join("")}
        <div class="list-row emphasis">
            <span>Total</span>
            <strong>${currency(cart.totalPrice)}</strong>
        </div>
    `;

    document.querySelectorAll(".qty-btn").forEach((button) => {
        button.addEventListener("click", async () => {
            const currentQty = Number(button.dataset.qty);
            const nextQty = button.dataset.action === "increase" ? currentQty + 1 : currentQty - 1;

            try {
                if (nextQty <= 0) {
                    await removeServerCartItem(button.dataset.id);
                } else {
                    await updateServerCartItem(button.dataset.id, nextQty);
                }

                await loadCheckoutCart();
                if (typeof renderNavbar === "function") {
                    renderNavbar();
                }
            } catch (error) {
                showCheckoutMessage(error.message, "danger");
            }
        });
    });

    const clearCheckoutCartBtn = document.getElementById("clearCheckoutCartBtn");
    if (clearCheckoutCartBtn) {
        clearCheckoutCartBtn.addEventListener("click", async () => {
            try {
                await clearServerCart();
                localStorage.removeItem(APP_KEYS.activeCartRestaurantId);
                await loadCheckoutCart();
                if (typeof renderNavbar === "function") {
                    renderNavbar();
                }
                showCheckoutMessage("Cart cleared.", "success");
            } catch (error) {
                showCheckoutMessage(error.message, "danger");
            }
        });
    }
}

// Load cart for final checkout
async function loadCheckoutCart() {
    const cart = await fetchServerCart();
    renderCheckoutCart(cart || { items: [], totalPrice: 0 });
}

// storing selected delivery addresses in localstorage
function renderCheckoutAddresses(addresses) {
    if (!addresses.length) {
        localStorage.removeItem(APP_KEYS.selectedAddressId);
        checkoutAddresses.innerHTML = `
            <div class="empty-state compact">
                <h3>No address added</h3>
                <p>Add an address from dashboard before placing this order.</p>
            </div>
        `;
        return;
    }

    let selectedAddressId = getSelectedAddressId();

    if (!selectedAddressId || !addresses.some((address) => String(address.id) === String(selectedAddressId))) {
        selectedAddressId = addresses[0].id;
        setSelectedAddressId(selectedAddressId);
    }

    checkoutAddresses.innerHTML = addresses.map((address) => `
        <label class="address-card ${String(selectedAddressId) === String(address.id) ? "selected" : ""}">
            <div class="address-main">
                <input type="radio" name="checkoutAddress" value="${address.id}" ${String(selectedAddressId) === String(address.id) ? "checked" : ""}>
                <div>
                    <strong>${address.street}</strong>
                    <p>${address.city}, ${address.state} - ${address.pincode}</p>
                </div>
            </div>
        </label>
    `).join("");

    document.querySelectorAll('input[name="checkoutAddress"]').forEach((input) => {
        input.addEventListener("change", () => {
            setSelectedAddressId(input.value);
            renderCheckoutAddresses(addresses);
        });
    });
}

// Loads all saved addresses for the checkout
async function loadCheckoutAddresses() {
    const addresses = await fetchAddresses();
    renderCheckoutAddresses(addresses);
}

// Draws each active order with a cancellable countdown button
function checkoutOrderCard(order) {
    const secondsLeft = Math.max(0, Math.ceil((CANCELLATION_WINDOW_MS - (Date.now() - new Date(order.orderTime).getTime())) / 1000));
    const canCancel = isOrderCancellable(order.orderTime, order.status);

    return `
        <article class="panel order-card">
            <div class="inline-head">
                <div>
                    <p class="eyebrow">${formatDate(order.orderTime)}</p>
                    <h3>${order.restaurantName}</h3>
                </div>
                <span class="status-pill ${statusTone(order.status)}">${order.status}</span>
            </div>
            <div class="stack soft-gap">
                ${order.items.map((item) => `
                    <div class="list-row">
                        <span>${item.menuItemName} x ${item.quantity}</span>
                        <strong>${currency(item.totalPrice)}</strong>
                    </div>
                `).join("")}
            </div>
            <div class="inline-head">
                <strong>${currency(order.totalPrice)}</strong>
                ${canCancel ? `<button class="ghost-btn" data-cancel-order="${order.orderId}" data-order-time="${order.orderTime}" data-order-status="${order.status}">Cancel in ${secondsLeft}s</button>` : ""}
            </div>
        </article>
    `;
}

// Keeps cancellation timers dynamic and binds button actions.
function bindCheckoutOrderActions() {
    document.querySelectorAll("[data-cancel-order]").forEach((button) => {
        button.addEventListener("click", async () => {
            try {
                await cancelUserOrder(button.dataset.cancelOrder);
                await refreshCheckoutUser();
                await loadCheckoutOrders();
                if (typeof renderNavbar === "function") {
                    renderNavbar();
                }
                showCheckoutMessage("Order cancelled successfully.", "success");
            } catch (error) {
                showCheckoutMessage(error.message, "danger");
            }
        });
    });
}

// Loads active orders for the cart page and starts live countdown updates.
async function loadCheckoutOrders() {
    const allOrders = await fetchUserOrders();
    const activeOrders = allOrders.filter((order) => !["COMPLETED", "CANCELLED"].includes(order.status));

    checkoutActiveOrders.innerHTML = activeOrders.length
        ? activeOrders.map(checkoutOrderCard).join("")
        : `<div class="empty-state compact"><h3>No active orders</h3><p>Your cancellable orders will appear here after placing one.</p></div>`;

    bindCheckoutOrderActions();
    refreshCancellationLabels();
}

// Updates cancel button every second without making extra API calls.
function refreshCancellationLabels() {
    document.querySelectorAll("[data-cancel-order]").forEach((button) => {
        const orderTime = button.dataset.orderTime;
        const orderStatus = button.dataset.orderStatus;
        const msLeft = CANCELLATION_WINDOW_MS - (Date.now() - new Date(orderTime).getTime());
        const secondsLeft = Math.max(0, Math.ceil(msLeft / 1000));

        if (isOrderCancellable(orderTime, orderStatus) && secondsLeft > 0) {
            button.textContent = `Cancel in ${secondsLeft}s`;
            button.disabled = false;
            return;
        }

        button.textContent = "Cancellation closed";
        button.disabled = true;
    });
}

// Starts the countdown updater for currently rendered orders.
function startCancellationTicker() {
    if (cancellationTimerId) {
        clearInterval(cancellationTimerId);
    }

    cancellationTimerId = setInterval(() => {
        refreshCancellationLabels();
    }, 1000);
}


// Places order
async function handleConfirmOrder() {
    const selectedAddressId = getSelectedAddressId();

    if (!checkoutCartData.items.length) {
        showCheckoutMessage("Your cart is empty.", "danger");
        return;
    }

    if (!selectedAddressId) {
        showCheckoutMessage("Please add and select a delivery address.", "danger");
        return;
    }

    try {
        await placeOrder(Number(selectedAddressId));
        await Promise.all([refreshCheckoutUser(), loadCheckoutCart(), loadCheckoutOrders()]);
        if (typeof renderNavbar === "function") {
            renderNavbar();
        }
        showCheckoutMessage("Order placed successfully.", "success");
    } catch (error) {
        showCheckoutMessage(error.message, "danger");
    }
}

// intialize checkout page
async function initCheckoutPage() {
    if (!isLoggedIn()) {
        window.location.href = "login.html";
        return;
    }

    try {
        await Promise.all([refreshCheckoutUser(), loadCheckoutCart(), loadCheckoutAddresses(), loadCheckoutOrders()]);
        startCancellationTicker();
    } catch (error) {
        showCheckoutMessage(error.message, "danger");
    }
}

if (confirmOrderBtn) {
    confirmOrderBtn.addEventListener("click", handleConfirmOrder);
}

initCheckoutPage();
