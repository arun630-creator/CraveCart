"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@clerk/nextjs";
import Link from "next/link";
import { cartAPI } from "@/lib/api";

interface CartItem {
  id: string;
  productId: string;
  productName: string;
  productImage?: string;
  price: number;
  quantity: number;
}

export default function CartPage() {
  const router = useRouter();
  const { userId } = useAuth();
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    if (!userId) {
      router.push("/sign-in");
      return;
    }

    fetchCart();
  }, [userId, router]);

  const fetchCart = async () => {
    try {
      const items = await cartAPI.getCart(userId!);
      setCartItems(items);
      calculateTotal(items);
    } catch (error) {
      console.error("Error fetching cart:", error);
    } finally {
      setLoading(false);
    }
  };

  const calculateTotal = (items: CartItem[]) => {
    const sum = items.reduce((acc, item) => acc + item.price * item.quantity, 0);
    setTotal(sum);
  };

  const handleUpdateQuantity = async (itemId: string, newQuantity: number) => {
    if (newQuantity <= 0) {
      handleRemoveItem(itemId);
      return;
    }

    try {
      const updated = await cartAPI.updateQuantity(itemId, newQuantity);
      const newItems = cartItems.map((item) =>
        item.id === itemId ? { ...item, quantity: newQuantity } : item
      );
      setCartItems(newItems);
      calculateTotal(newItems);
    } catch (error) {
      console.error("Error updating quantity:", error);
    }
  };

  const handleRemoveItem = async (itemId: string) => {
    try {
      await cartAPI.removeFromCart(itemId);
      const newItems = cartItems.filter((item) => item.id !== itemId);
      setCartItems(newItems);
      calculateTotal(newItems);
    } catch (error) {
      console.error("Error removing item:", error);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-xl text-gray-600">Loading your cart...</div>
      </div>
    );
  }

  if (cartItems.length === 0) {
    return (
      <div className="min-h-screen flex flex-col justify-center items-center">
        <h1 className="text-3xl font-bold mb-4">Your Cart is Empty</h1>
        <p className="text-gray-600 mb-8">Start shopping to add items to your cart</p>
        <Link
          href="/"
          className="px-6 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg"
        >
          Continue Shopping
        </Link>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-4xl mx-auto px-4">
        <h1 className="text-3xl font-bold mb-8">Shopping Cart</h1>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Cart Items */}
          <div className="lg:col-span-2">
            <div className="bg-white rounded-lg shadow-md p-6">
              {cartItems.map((item) => (
                <div
                  key={item.id}
                  className="flex gap-4 pb-6 border-b last:border-b-0 last:pb-0"
                >
                  {item.productImage && (
                    <img
                      src={item.productImage}
                      alt={item.productName}
                      className="w-20 h-20 object-cover rounded"
                    />
                  )}
                  <div className="flex-1">
                    <h3 className="font-semibold text-lg">{item.productName}</h3>
                    <p className="text-gray-600">${item.price.toFixed(2)}</p>

                    <div className="flex items-center gap-2 mt-4">
                      <button
                        onClick={() => handleUpdateQuantity(item.id, item.quantity - 1)}
                        className="px-2 py-1 bg-gray-200 hover:bg-gray-300 rounded"
                      >
                        −
                      </button>
                      <span className="px-4 py-1 border rounded">{item.quantity}</span>
                      <button
                        onClick={() => handleUpdateQuantity(item.id, item.quantity + 1)}
                        className="px-2 py-1 bg-gray-200 hover:bg-gray-300 rounded"
                      >
                        +
                      </button>
                      <button
                        onClick={() => handleRemoveItem(item.id)}
                        className="ml-auto px-4 py-1 bg-red-500 hover:bg-red-600 text-white rounded"
                      >
                        Remove
                      </button>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className="font-semibold text-lg">
                      ${(item.price * item.quantity).toFixed(2)}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Order Summary */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-md p-6 sticky top-4">
              <h2 className="text-xl font-bold mb-4">Order Summary</h2>

              <div className="space-y-3 mb-6">
                <div className="flex justify-between">
                  <span>Subtotal</span>
                  <span>${total.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span>Shipping</span>
                  <span>FREE</span>
                </div>
                <div className="flex justify-between">
                  <span>Tax</span>
                  <span>${(total * 0.1).toFixed(2)}</span>
                </div>
                <div className="border-t pt-3 flex justify-between font-bold text-lg">
                  <span>Total</span>
                  <span>${(total + total * 0.1).toFixed(2)}</span>
                </div>
              </div>

              <Link
                href="/checkout"
                className="w-full inline-block text-center px-6 py-3 bg-orange-500 hover:bg-orange-600 text-white font-semibold rounded-lg"
              >
                Proceed to Checkout
              </Link>

              <Link
                href="/"
                className="w-full inline-block text-center mt-3 px-6 py-3 bg-gray-200 hover:bg-gray-300 text-gray-800 font-semibold rounded-lg"
              >
                Continue Shopping
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
