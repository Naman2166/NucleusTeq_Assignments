// it is a Default Product data which will be visible even when no product is there
let products = [
  { id: 1, name: "Laptop", price: 50000, stock: 8, category: "Electronics" },
  { id: 2, name: "Headphones", price: 1500, stock: 3, category: "Electronics" },
  { id: 3, name: "Shirt", price: 800, stock: 10, category: "Clothing" },
  { id: 4, name: "Shoes", price: 2500, stock: 0, category: "Footwear" },
  { id: 5, name: "Notebook", price: 100, stock: 20, category: "Stationery" },
  { id: 6, name: "jeans", price: 1000, stock: 20, category: "Clothing" },
  { id: 7, name: "PS5", price: 100, stock: 20, category: "Electronics" },
];



//here we are getting all required dom elements

//getting Product grid elements
const productGrid = document.getElementById("productGrid");
const noProductMessage = document.getElementById("noProductMessage");

//add product form elemnst
const addProductForm = document.getElementById("addProductForm");
const productName = document.getElementById("productName");
const productPrice = document.getElementById("productPrice");
const productStock = document.getElementById("productStock");
const productCategory = document.getElementById("productCategory");

//getting analytics elememts
const totalProducts = document.getElementById("totalProducts");
const totalValue = document.getElementById("totalValue");
const outOfStock = document.getElementById("outOfStock");
const categoryCount = document.getElementById("categoryCount");

//getting controls elements
const search = document.getElementById("search");
const categoryFilter = document.getElementById("categoryFilter");
const lowStockFilter = document.getElementById("lowStockFilter");
const sort = document.getElementById("sort");
const clearAllBtn = document.getElementById("clearAllBtn");




// Add product      
//here i have implmented functionality for adding new product using productform 
addProductForm.addEventListener("submit", function (e) {
  e.preventDefault();

  //it is a product object conatining all data about new product
  const product = {
    id: Date.now(),
    name: productName.value,
    price: Number(productPrice.value),
    stock: Number(productStock.value),
    category: productCategory.value
  };

  //this psuh new product data inside all products array
  products.push(product);
   
  //it resets the form value after clicking add button
  addProductForm.reset();

  //this updates the values
  updateCategoryOptions();
  renderProducts();
});





//Render products
//this functions render and shows all data pn screen
function renderProducts() {

  let filtered = [...products];

  //this is search functionality, it filters products based on name entered by user
  const searchValue = search.value.toLowerCase();
  if (searchValue) {
    filtered = filtered.filter(product =>
      product.name.toLowerCase().includes(searchValue)
    );
  }

  //this filters products based on selected category
  if (categoryFilter.value) {
    filtered = filtered.filter(p =>
      p.category === categoryFilter.value
    );
  }

  //this filters products having stock < 5
  if (lowStockFilter.value === "Yes") {
    filtered = filtered.filter(p => p.stock < 5);
  }

  //this sorts products based on selected option
  if (sort.value === "price-low") {
    filtered.sort((a, b) => a.price - b.price);
  } else if (sort.value === "price-high") {
    filtered.sort((a, b) => b.price - a.price);
  } else if (sort.value === "name-az") {
    filtered.sort((a, b) => a.name.localeCompare(b.name));
  } else if (sort.value === "name-za") {
    filtered.sort((a, b) => b.name.localeCompare(a.name));
  }

  //it clears previous products before rendering new ones
  productGrid.innerHTML = "";

  //if no product is found then show message
  if (filtered.length === 0) {
    noProductMessage.style.display = "block";
  } else {
    noProductMessage.style.display = "none";
  }


  //this loop creates product cards for each product
  filtered.forEach(product => {

    const card = document.createElement("div");
    card.classList.add("product-card");

    let stockClass = "";
    if (product.stock === 0) stockClass = "out";
    else if (product.stock < 5) stockClass = "low";

    //here i have added product data inside card which will change dyncmiclly
    card.innerHTML = `
      <h3>${product.name}</h3>
      <p>Price: ₹${product.price}</p>
      <p class="${stockClass}">Stock: ${product.stock}</p>
      <p>Category: ${product.category}</p>

      <div class="product-actions">
        <button class="edit-btn" onclick="editProduct(${product.id})">Edit</button>
        <button class="delete-btn" onclick="deleteProduct(${product.id})">Delete</button>
      </div>
    `;

    //adding card into product grid
    productGrid.appendChild(card);
  });

  //this upadtes value of analytics also after adding product
  updateAnalytics(filtered);
}

