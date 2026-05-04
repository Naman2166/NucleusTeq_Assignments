const ownerOrders = document.getElementById("ownerOrders");


//order status helper
function getAllowedStatuses(currentStatus) {
    switch (currentStatus) {
        case "PLACED":
            return ["PLACED", "PENDING", "CANCELLED", "DELIVERED", "COMPLETED"];
        case "PENDING":
            return ["PENDING", "DELIVERED", "COMPLETED", "CANCELLED"];
        case "DELIVERED":
            return ["DELIVERED", "COMPLETED"];
        case "COMPLETED":
        case "CANCELLED":
            return [currentStatus];
        default:
            return [currentStatus];
    }
}


// Renders incoming orders
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

