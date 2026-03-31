"use client";

import { useEffect, useState } from "react";
import { useAuth } from "@clerk/nextjs";
import Link from "next/link";
import ProductCard from "@/components/ProductCard";
import { productAPI, categoryAPI, seederAPI, cartAPI } from "@/lib/api";

interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  categoryId: string;
  brand: string;
  imageUrl?: string;
  rating?: number;
  reviews?: number;
}

interface Category {
  id: string;
  name: string;
  description: string;
}

export default function ProductsPage() {
  const { userId } = useAuth();
  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<string>("all");
  const [loading, setLoading] = useState(true);
  const [initializing, setInitializing] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    initializeAndLoad();
  }, []);

  const initializeAndLoad = async () => {
    try {
      setInitializing(true);
      // Try to initialize database with sample data
      await seederAPI.initializeDatabase();
    } catch (error) {
      console.log("Database already initialized or error:", error);
    } finally {
      setInitializing(false);
      loadData();
    }
  };

  const loadData = async () => {
    try {
      setLoading(true);
      const [productsData, categoriesData] = await Promise.all([
        productAPI.getAllProducts(),
        categoryAPI.getAllCategories(),
      ]);

      setProducts(Array.isArray(productsData) ? productsData : []);
      setCategories(Array.isArray(categoriesData) ? categoriesData : []);
      setError("");
    } catch (err) {
      setError("Failed to load products. Please try again.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = async (product: Product, quantity: number) => {
    if (!userId) {
      alert("Please sign in to add items to cart");
      return;
    }

    try {
      await cartAPI.addToCart(userId, product.id, quantity);
    } catch (error) {
      throw new Error("Failed to add to cart");
    }
  };

  const filteredProducts =
    selectedCategory === "all"
      ? products
      : products.filter((p) => p.categoryId === selectedCategory);

  if (loading && !initializing) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-xl text-gray-600">Loading products...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white shadow-sm">
        <div className="max-w-7xl mx-auto px-4 py-6">
          <div className="flex justify-between items-center">
            <h1 className="text-3xl font-bold">CraveCart Menu</h1>
            <div className="flex gap-4">
              <Link
                href="/cart"
                className="px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg font-semibold"
              >
                🛒 View Cart
              </Link>
            </div>
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="max-w-7xl mx-auto px-4 py-8">
        {initializing && (
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
            <p className="text-blue-800">Initializing database with sample products...</p>
          </div>
        )}

        {error && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
            <p className="text-red-800">{error}</p>
            <button
              onClick={loadData}
              className="mt-2 px-4 py-1 bg-red-500 text-white rounded hover:bg-red-600"
            >
              Retry
            </button>
          </div>
        )}

        {products.length === 0 ? (
          <div className="text-center py-12">
            <p className="text-xl text-gray-600 mb-4">No products available</p>
            <button
              onClick={loadData}
              className="px-6 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg"
            >
              Reload Products
            </button>
          </div>
        ) : (
          <>
            {/* Category Filter */}
            <div className="mb-8">
              <h2 className="text-xl font-bold mb-4">Categories</h2>
              <div className="flex flex-wrap gap-2">
                <button
                  onClick={() => setSelectedCategory("all")}
                  className={`px-4 py-2 rounded-lg font-semibold transition ${
                    selectedCategory === "all"
                      ? "bg-orange-500 text-white"
                      : "bg-white border-2 border-orange-500 text-orange-500 hover:bg-orange-50"
                  }`}
                >
                  All Products
                </button>
                {categories.map((category) => (
                  <button
                    key={category.id}
                    onClick={() => setSelectedCategory(category.id)}
                    className={`px-4 py-2 rounded-lg font-semibold transition ${
                      selectedCategory === category.id
                        ? "bg-orange-500 text-white"
                        : "bg-white border-2 border-orange-500 text-orange-500 hover:bg-orange-50"
                    }`}
                  >
                    {category.name}
                  </button>
                ))}
              </div>
            </div>

            {/* Products Grid */}
            <div>
              <h2 className="text-2xl font-bold mb-6">
                {selectedCategory === "all"
                  ? `All Products (${filteredProducts.length})`
                  : `${categories.find((c) => c.id === selectedCategory)?.name} (${filteredProducts.length})`}
              </h2>

              {filteredProducts.length === 0 ? (
                <p className="text-gray-600 text-center py-8">No products in this category</p>
              ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                  {filteredProducts.map((product) => (
                    <ProductCard
                      key={product.id}
                      product={product}
                      onAddToCart={handleAddToCart}
                    />
                  ))}
                </div>
              )}
            </div>
          </>
        )}
      </div>
    </div>
  );
}
