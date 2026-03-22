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
const searchInput = document.getElementById("search");
const categoryFilter = document.getElementById("categoryFilter");
const lowStockFilter = document.getElementById("lowStockFilter");
const sortSelect = document.getElementById("sort");
const clearBtn = document.getElementById("clearAllBtn");


