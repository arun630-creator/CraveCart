"use client";

import Image from "next/image";
import { useState } from "react";

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

interface ProductCardProps {
  product: Product;
  onAddToCart: (product: Product, quantity: number) => void;
}

export default function ProductCard({ product, onAddToCart }: ProductCardProps) {
  const [quantity, setQuantity] = useState(1);
  const [isAdding, setIsAdding] = useState(false);
  const [imageSrc, setImageSrc] = useState(product.imageUrl || "/images/product-fallback.svg");

  const handleAddToCart = async () => {
    setIsAdding(true);
    try {
      await onAddToCart(product, quantity);
      setQuantity(1);
    } catch (error) {
      alert("Failed to add to cart");
    } finally {
      setIsAdding(false);
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition">
      {/* Product Image */}
      <div className="relative h-48 bg-gray-200">
        {imageSrc ? (
          <img
            src={imageSrc}
            alt={product.name}
            className="w-full h-full object-cover"
            onError={(e) => {
              // Use a local fallback so image rendering does not depend on external hosts.
              if (!e.currentTarget.src.includes("/images/product-fallback.svg")) {
                setImageSrc("/images/product-fallback.svg");
              }
            }}
          />
        ) : (
          <div className="w-full h-full flex items-center justify-center">
            <span className="text-gray-400">No Image</span>
          </div>
        )}
      </div>

      {/* Product Info */}
      <div className="p-4">
        <h3 className="font-bold text-lg mb-1">{product.name}</h3>
        <p className="text-sm text-gray-600 mb-2">{product.description}</p>

        {/* Rating */}
        {product.rating && (
          <div className="flex items-center gap-1 mb-2">
            <span className="text-yellow-400">★</span>
            <span className="text-sm font-semibold">{product.rating}</span>
            <span className="text-xs text-gray-500">({product.reviews} reviews)</span>
          </div>
        )}

        {/* Brand */}
        <p className="text-xs text-gray-500 mb-3">{product.brand}</p>

        {/* Price */}
        <p className="text-2xl font-bold text-orange-600 mb-4">${product.price.toFixed(2)}</p>

        {/* Quantity and Add to Cart */}
        <div className="flex gap-2">
          <div className="flex items-center border rounded-lg">
            <button
              onClick={() => setQuantity(Math.max(1, quantity - 1))}
              className="px-3 py-1 text-gray-600 hover:bg-gray-100"
            >
              −
            </button>
            <span className="px-4 py-1 border-l border-r">{quantity}</span>
            <button
              onClick={() => setQuantity(quantity + 1)}
              className="px-3 py-1 text-gray-600 hover:bg-gray-100"
            >
              +
            </button>
          </div>

          <button
            onClick={handleAddToCart}
            disabled={isAdding}
            className="flex-1 bg-orange-500 hover:bg-orange-600 disabled:bg-gray-400 text-white font-semibold py-2 rounded-lg transition"
          >
            {isAdding ? "Adding..." : "Add to Cart"}
          </button>
        </div>
      </div>
    </div>
  );
}
