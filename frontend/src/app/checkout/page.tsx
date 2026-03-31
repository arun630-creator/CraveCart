"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@clerk/nextjs";
import { cartAPI, orderAPI, paymentAPI } from "@/lib/api";

interface CartItem {
  id: string;
  productName: string;
  price: number;
  quantity: number;
}

interface CheckoutData {
  shippingAddress: string;
  customerEmail: string;
  paymentMethod: "STRIPE" | "PAYPAL" | "CASH_ON_DELIVERY";
}

export default function CheckoutPage() {
  const router = useRouter();
  const { userId, user } = useAuth();
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(true);
  const [processing, setProcessing] = useState(false);
  const [checkoutData, setCheckoutData] = useState<CheckoutData>({
    shippingAddress: "",
    customerEmail: user?.primaryEmailAddress?.emailAddress || "",
    paymentMethod: "STRIPE",
  });

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
      const sum = items.reduce((acc, item) => acc + item.price * item.quantity, 0);
      setTotal(sum);
    } catch (error) {
      console.error("Error fetching cart:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelect>) => {
    const { name, value } = e.target;
    setCheckoutData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmitOrder = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!userId) return;

    setProcessing(true);
    try {
      // Step 1: Create order
      const order = await orderAPI.createOrder(
        userId,
        checkoutData.paymentMethod,
        checkoutData.shippingAddress,
        checkoutData.customerEmail
      );

      // Step 2: Create payment record
      const payment = await paymentAPI.createPayment(
        order.id,
        userId,
        total,
        checkoutData.paymentMethod
      );

      // Step 3: Handle different payment methods
      if (checkoutData.paymentMethod === "CASH_ON_DELIVERY") {
        // For cash on delivery, automatically complete the order
        await paymentAPI.confirmPayment(payment.id, `COD-${order.id}`, true);
        await orderAPI.confirmOrder(order.id);

        // Clear cart and redirect
        await cartAPI.clearCart(userId);
        router.push(`/order-confirmation/${order.id}`);
      } else if (checkoutData.paymentMethod === "STRIPE") {
        // Redirect to Stripe payment page (mock implementation)
        router.push(`/payment?paymentId=${payment.id}&orderId=${order.id}&method=stripe`);
      } else if (checkoutData.paymentMethod === "PAYPAL") {
        // Redirect to PayPal payment page (mock implementation)
        router.push(`/payment?paymentId=${payment.id}&orderId=${order.id}&method=paypal`);
      }
    } catch (error) {
      console.error("Error processing order:", error);
      alert("Error processing order. Please try again.");
    } finally {
      setProcessing(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-xl text-gray-600">Loading checkout...</div>
      </div>
    );
  }

  if (cartItems.length === 0) {
    return (
      <div className="min-h-screen flex flex-col justify-center items-center">
        <h1 className="text-3xl font-bold mb-4">Your Cart is Empty</h1>
        <button
          onClick={() => router.push("/")}
          className="px-6 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg"
        >
          Continue Shopping
        </button>
      </div>
    );
  }

  const tax = total * 0.1;
  const finalTotal = total + tax;

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-6xl mx-auto px-4">
        <h1 className="text-3xl font-bold mb-8">Checkout</h1>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Checkout Form */}
          <div className="lg:col-span-2">
            <form onSubmit={handleSubmitOrder} className="space-y-6">
              {/* Shipping Information */}
              <div className="bg-white rounded-lg shadow-md p-6">
                <h2 className="text-xl font-bold mb-4">Shipping Information</h2>

                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium mb-2">Email</label>
                    <input
                      type="email"
                      name="customerEmail"
                      value={checkoutData.customerEmail}
                      onChange={handleInputChange}
                      required
                      className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium mb-2">Shipping Address</label>
                    <input
                      type="text"
                      name="shippingAddress"
                      value={checkoutData.shippingAddress}
                      onChange={handleInputChange}
                      placeholder="Street address, apartment or suite number"
                      required
                      className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-orange-500"
                    />
                  </div>
                </div>
              </div>

              {/* Payment Method */}
              <div className="bg-white rounded-lg shadow-md p-6">
                <h2 className="text-xl font-bold mb-4">Payment Method</h2>

                <div className="space-y-3">
                  {[
                    { value: "STRIPE", label: "Credit/Debit Card (Stripe)", icon: "💳" },
                    { value: "PAYPAL", label: "PayPal", icon: "🅿️" },
                    { value: "CASH_ON_DELIVERY", label: "Cash on Delivery", icon: "💵" },
                  ].map((method) => (
                    <label
                      key={method.value}
                      className={`flex items-center p-4 border-2 rounded-lg cursor-pointer transition ${
                        checkoutData.paymentMethod === method.value
                          ? "border-orange-500 bg-orange-50"
                          : "border-gray-200 hover:border-orange-300"
                      }`}
                    >
                      <input
                        type="radio"
                        name="paymentMethod"
                        value={method.value}
                        checked={checkoutData.paymentMethod === method.value}
                        onChange={handleInputChange}
                        className="mr-3"
                      />
                      <span className="mr-2 text-xl">{method.icon}</span>
                      <span>{method.label}</span>
                    </label>
                  ))}
                </div>
              </div>

              {/* Order Items Summary */}
              <div className="bg-white rounded-lg shadow-md p-6">
                <h2 className="text-xl font-bold mb-4">Order Summary</h2>

                <div className="space-y-2 mb-4">
                  {cartItems.map((item) => (
                    <div key={item.id} className="flex justify-between text-sm">
                      <span>
                        {item.productName} x {item.quantity}
                      </span>
                      <span>${(item.price * item.quantity).toFixed(2)}</span>
                    </div>
                  ))}
                </div>

                <div className="border-t pt-4 space-y-2">
                  <div className="flex justify-between">
                    <span>Subtotal</span>
                    <span>${total.toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Tax (10%)</span>
                    <span>${tax.toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between font-bold text-lg border-t pt-2">
                    <span>Total</span>
                    <span>${finalTotal.toFixed(2)}</span>
                  </div>
                </div>
              </div>

              {/* Submit Button */}
              <button
                type="submit"
                disabled={processing}
                className="w-full px-6 py-3 bg-orange-500 hover:bg-orange-600 disabled:bg-gray-400 text-white font-bold rounded-lg text-lg transition"
              >
                {processing ? "Processing..." : `Place Order - $${finalTotal.toFixed(2)}`}
              </button>
            </form>
          </div>

          {/* Order Summary Sidebar */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-md p-6 sticky top-4">
              <h2 className="text-xl font-bold mb-4">Order Details</h2>

              <div className="space-y-3 mb-6">
                <div>
                  <p className="text-sm text-gray-600">Items</p>
                  <p className="font-semibold">{cartItems.length} product(s)</p>
                </div>

                <div>
                  <p className="text-sm text-gray-600">Subtotal</p>
                  <p className="font-semibold">${total.toFixed(2)}</p>
                </div>

                <div>
                  <p className="text-sm text-gray-600">Tax</p>
                  <p className="font-semibold">${tax.toFixed(2)}</p>
                </div>

                <div className="border-t pt-3">
                  <p className="text-sm text-gray-600">Total Amount</p>
                  <p className="text-2xl font-bold text-orange-600">${finalTotal.toFixed(2)}</p>
                </div>
              </div>

              <button
                onClick={() => router.push("/cart")}
                className="w-full px-4 py-2 text-center text-gray-600 border border-gray-300 rounded-lg hover:bg-gray-50"
              >
                Back to Cart
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
