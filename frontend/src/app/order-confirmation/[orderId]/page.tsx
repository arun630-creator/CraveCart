"use client";

import { useEffect, useState } from "react";
import { useRouter, useParams } from "next/navigation";
import { orderAPI } from "@/lib/api";

interface OrderItem {
  id: string;
  productName: string;
  price: number;
  quantity: number;
}

interface Order {
  id: string;
  userId: string;
  items: OrderItem[];
  totalAmount: number;
  status: string;
  paymentMethod: string;
  paymentStatus: string;
  shippingAddress: string;
  customerEmail: string;
  createdAt: string;
}

export default function OrderConfirmationPage() {
  const router = useRouter();
  const params = useParams();
  const orderId = params.orderId as string;

  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!orderId) return;

    fetchOrder();
  }, [orderId]);

  const fetchOrder = async () => {
    try {
      const orderData = await orderAPI.getOrder(orderId);
      setOrder(orderData);
    } catch (err) {
      console.error("Error fetching order:", err);
      setError("Could not load order details");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-xl text-gray-600">Loading order details...</div>
      </div>
    );
  }

  if (error || !order) {
    return (
      <div className="min-h-screen flex flex-col justify-center items-center">
        <h1 className="text-3xl font-bold mb-4">Error</h1>
        <p className="text-gray-600 mb-8">{error || "Order not found"}</p>
        <button
          onClick={() => router.push("/")}
          className="px-6 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg"
        >
          Return to Home
        </button>
      </div>
    );
  }

  const normalizeStatus = (status: unknown) => {
    if (typeof status === "string") {
      return status.toUpperCase();
    }

    if (status === null || status === undefined) {
      return "PENDING";
    }

    return String(status).toUpperCase();
  };

  const getStatusColor = (status: unknown) => {
    const normalizedStatus = normalizeStatus(status);
    switch (normalizedStatus) {
      case "COMPLETED":
      case "PROCESSING":
        return "text-green-600 bg-green-50";
      case "PENDING":
        return "text-yellow-600 bg-yellow-50";
      case "CANCELLED":
        return "text-red-600 bg-red-50";
      default:
        return "text-gray-600 bg-gray-50";
    }
  };

  const getPaymentStatusColor = (status: unknown) => {
    const normalizedStatus = normalizeStatus(status);
    switch (normalizedStatus) {
      case "COMPLETED":
        return "text-green-600";
      case "PENDING":
        return "text-yellow-600";
      case "FAILED":
        return "text-red-600";
      default:
        return "text-gray-600";
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 py-12">
      <div className="max-w-4xl mx-auto px-4">
        {/* Success Message */}
        <div className="text-center mb-12">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-green-100 rounded-full mb-4">
            <svg className="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
            </svg>
          </div>
          <h1 className="text-3xl font-bold mb-2">Order Confirmed!</h1>
          <p className="text-gray-600">Thank you for your purchase</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Main Order Details */}
          <div className="lg:col-span-2">
            {/* Order Header */}
            <div className="bg-white rounded-lg shadow-md p-6 mb-6">
              <div className="grid grid-cols-2 gap-6">
                <div>
                  <p className="text-sm text-gray-600 mb-1">Order ID</p>
                  <p className="font-mono font-semibold text-lg">{order.id}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 mb-1">Order Date</p>
                  <p className="font-semibold">
                    {new Date(order.createdAt).toLocaleDateString()}
                  </p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 mb-1">Status</p>
                  <span
                    className={`inline-block px-3 py-1 rounded-full text-sm font-semibold ${getStatusColor(
                      order.status
                    )}`}
                  >
                    {normalizeStatus(order.status)}
                  </span>
                </div>
                <div>
                  <p className="text-sm text-gray-600 mb-1">Payment Status</p>
                  <span className={`font-semibold ${getPaymentStatusColor(order.paymentStatus)}`}>
                    {normalizeStatus(order.paymentStatus)}
                  </span>
                </div>
              </div>
            </div>

            {/* Order Items */}
            <div className="bg-white rounded-lg shadow-md p-6 mb-6">
              <h2 className="text-xl font-bold mb-4">Order Items</h2>

              <div className="space-y-4">
                {order.items.map((item) => (
                  <div key={item.id} className="flex justify-between pb-4 border-b last:border-b-0">
                    <div>
                      <p className="font-semibold">{item.productName}</p>
                      <p className="text-sm text-gray-600">Quantity: {item.quantity}</p>
                    </div>
                    <p className="font-semibold">${(item.price * item.quantity).toFixed(2)}</p>
                  </div>
                ))}
              </div>
            </div>

            {/* Shipping Information */}
            <div className="bg-white rounded-lg shadow-md p-6">
              <h2 className="text-xl font-bold mb-4">Shipping Information</h2>

              <div className="space-y-4">
                <div>
                  <p className="text-sm text-gray-600 mb-1">Shipping Address</p>
                  <p className="font-semibold">{order.shippingAddress}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600 mb-1">Email</p>
                  <p className="font-semibold">{order.customerEmail}</p>
                </div>
              </div>
            </div>
          </div>

          {/* Order Summary Sidebar */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-lg shadow-md p-6 sticky top-4">
              <h2 className="text-xl font-bold mb-4">Order Summary</h2>

              <div className="space-y-3 mb-6">
                <div>
                  <p className="text-sm text-gray-600">Subtotal</p>
                  <p className="font-semibold">
                    ${(order.totalAmount / 1.1).toFixed(2)}
                  </p>
                </div>

                <div>
                  <p className="text-sm text-gray-600">Tax (10%)</p>
                  <p className="font-semibold">
                    ${(order.totalAmount - order.totalAmount / 1.1).toFixed(2)}
                  </p>
                </div>

                <div className="border-t pt-3">
                  <p className="text-sm text-gray-600">Total Amount</p>
                  <p className="text-2xl font-bold text-orange-600">
                    ${order.totalAmount.toFixed(2)}
                  </p>
                </div>
              </div>

              <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
                <p className="text-sm text-blue-800">
                  <strong>Payment Method:</strong> {order.paymentMethod.replace(/_/g, " ")}
                </p>
              </div>

              <div className="space-y-3">
                <button
                  onClick={() => router.push("/orders")}
                  className="w-full px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg font-semibold"
                >
                  View All Orders
                </button>
                <button
                  onClick={() => router.push("/")}
                  className="w-full px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-800 rounded-lg font-semibold"
                >
                  Continue Shopping
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
