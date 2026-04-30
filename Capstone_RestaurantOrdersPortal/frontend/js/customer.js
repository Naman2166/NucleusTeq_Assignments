const customerName = document.getElementById("customerName");
const customerWallet = document.getElementById("customerWallet");
const activeOrdersContainer = document.getElementById("activeOrders");
const orderHistoryContainer = document.getElementById("orderHistory");
const addressList = document.getElementById("addressList");
const addressForm = document.getElementById("addressForm");
const dashboardMessage = document.getElementById("dashboardMessage");

// Shows customer dashboard feedback in a shared banner.
function showDashboardMessage(message, tone = "info") {
    dashboardMessage.className = `banner ${tone}`;
    dashboardMessage.textContent = message;
}

// Renders the current user's name and wallet from local storage.
function renderCustomerSummary() {
    const user = getUser();

    if (!user) {
        window.location.href = "login.html";
        return;
    }

    customerName.textContent = user.firstName || "Customer";
    customerWallet.textContent = currency(user.walletBalance || 0);
}

// Fetches the latest user record so wallet changes made by owners appear here.
async function refreshCustomerSummary() {
    const user = await fetchCurrentUser();
    setUser(user);
    renderCustomerSummary();
}

// Renders saved addresses and keeps one valid address selected.
function renderAddresses(addresses) {
    if (!addresses.length) {
        localStorage.removeItem(APP_KEYS.selectedAddressId);
        addressList.innerHTML = `
            <div class="empty-state compact">
                <h3>No address added</h3>
                <p>Add one address before placing an order.</p>
            </div>
        `;
        return;
    }

    let selectedAddressId = getSelectedAddressId();

    if (!selectedAddressId || !addresses.some((address) => String(address.id) === String(selectedAddressId))) {
        selectedAddressId = addresses[0].id;
        setSelectedAddressId(selectedAddressId);
    }

    addressList.innerHTML = addresses.map((address) => `
        <label class="address-card ${String(selectedAddressId) === String(address.id) ? "selected" : ""}">
            <div class="address-main">
                <input type="radio" name="selectedAddress" value="${address.id}" ${String(selectedAddressId) === String(address.id) ? "checked" : ""}>
                <div>
                    <strong>${escapeHtml(address.street)}</strong>
                    <p>${escapeHtml(address.city)}, ${escapeHtml(address.state)} - ${escapeHtml(address.pincode)}</p>
                </div>
            </div>
            <button type="button" class="text-btn" data-delete-address="${address.id}">Delete</button>
        </label>
    `).join("");

    document.querySelectorAll('input[name="selectedAddress"]').forEach((input) => {
        input.addEventListener("change", () => {
            setSelectedAddressId(input.value);
            renderAddresses(addresses);
        });
    });

    document.querySelectorAll("[data-delete-address]").forEach((button) => {
        button.addEventListener("click", async () => {
            try {
                await deleteAddress(button.dataset.deleteAddress);
                await loadAddresses();
                showDashboardMessage("Address deleted.", "success");
            } catch (error) {
                showDashboardMessage(error.message, "danger");
            }
        });
    });
}

// Loads saved customer delivery addresses.
async function loadAddresses() {
    try {
        const addresses = await fetchAddresses();
        renderAddresses(addresses);
    } catch (error) {
        addressList.innerHTML = `<div class="empty-state compact"><h3>Unable to load addresses</h3><p>${escapeHtml(error.message)}</p></div>`;
    }
}

// Builds one order card for active and historical order sections.
function orderCard(order, includeCancel) {
    return `
        <article class="order-card panel">
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
            </div>
        </article>
    `;
}

// Loads active and historical orders, then refreshes wallet data from the server.
async function loadOrders() {
    try {
        const orders = await fetchUserOrders();
        await refreshCustomerSummary();
        const activeOrders = orders.filter((order) => !["COMPLETED", "CANCELLED"].includes(order.status));
        const historyOrders = orders.filter((order) => ["COMPLETED", "CANCELLED"].includes(order.status));

        activeOrdersContainer.innerHTML = activeOrders.length
            ? activeOrders.map((order) => orderCard(order, true)).join("")
            : `<div class="empty-state compact"><h3>No live orders</h3><p>Your active orders will appear here.</p></div>`;

        orderHistoryContainer.innerHTML = historyOrders.length
            ? historyOrders.map((order) => orderCard(order, false)).join("")
            : `<div class="empty-state compact"><h3>No order history yet</h3><p>Completed and cancelled orders will be listed here.</p></div>`;

    } catch (error) {
        activeOrdersContainer.innerHTML = `<div class="empty-state compact"><h3>Unable to load orders</h3><p>${escapeHtml(error.message)}</p></div>`;
        orderHistoryContainer.innerHTML = "";
    }
}

if (addressForm) {
    addressForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(addressForm);
        const payload = {
            street: formData.get("street")?.trim(),
            city: formData.get("city")?.trim(),
            state: formData.get("state")?.trim(),
            pincode: formData.get("pincode")?.trim()
        };

        if (!payload.street && !payload.city && !payload.state && !payload.pincode) {
            showDashboardMessage("Please fill the address form before submitting.", "danger");
            return;
        }

        try {
            const address = await createAddress(payload);
            setSelectedAddressId(address.id);
            addressForm.reset();
            await loadAddresses();
            showDashboardMessage("Address added successfully.", "success");
        } catch (error) {
            showDashboardMessage(error.message, "danger");
        }
    });
}

// Boots the customer dashboard after verifying authentication.
async function initCustomerPage() {
    if (!isLoggedIn()) {
        window.location.href = "login.html";
        return;
    }

    await refreshCustomerSummary();
    await Promise.all([loadOrders(), loadAddresses()]);
}

initCustomerPage();
