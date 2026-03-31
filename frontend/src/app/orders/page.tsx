"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@clerk/nextjs";
import Link from "next/link";
import { orderAPI } from "@/lib/api";

interface OrderItem {
  productName: string;
  price: number;
  quantity: number;
}

interface Order {
  id: string;
  items: OrderItem[];
  totalAmount: number;
  status: string;
  paymentStatus: string;
  paymentMethod: string;
  createdAt: string;
}

export default function OrdersPage() {
  const router = useRouter();
  const { userId } = useAuth();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!userId) {
      router.push("/sign-in");
      return;
    }

    fetchOrders();
  }, [userId, router]);

  const fetchOrders = async () => {
    try {
      const userOrders = await orderAPI.getUserOrders(userId!);
      setOrders(userOrders);
    } catch (error) {
      console.error("Error fetching orders:", error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status: string) => {
    switch (status.toUpperCase()) {
      case "COMPLETED":
        return "bg-green-100 text-green-800";
      case "PROCESSING":
        return "bg-blue-100 text-blue-800";
      case "PENDING":
        return "bg-yellow-100 text-yellow-800";
      case "CANCELLED":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="text-xl text-gray-600">Loading your orders...</div>
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="min-h-screen flex flex-col justify-center items-center">
        <h1 className="text-3xl font-bold mb-4">No Orders Yet</h1>
        <p className="text-gray-600 mb-8">You haven't placed any orders yet</p>
        <Link
          href="/"
          className="px-6 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg"
        >
          Start Shopping
        </Link>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-6xl mx-auto px-4">
        <h1 className="text-3xl font-bold mb-8">My Orders</h1>

        <div className="space-y-4">
          {orders.map((order) => (
            <div
              key={order.id}
              className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition"
            >
              <div className="grid grid-cols-1 md:grid-cols-4 gap-4 mb-4">
                <div>
                  <p className="text-sm text-gray-600 mb-1">Order ID</p>
                  <p className="font-mono font-semibold truncate">{order.id}</p>
                </div>

                <div>
                  <p className="text-sm text-gray-600 mb-1">Order Date</p>
                  <p className="font-semibold">{new Date(order.createdAt).toLocaleDateString()}</p>
                </div>

                <div>
                  <p className="text-sm text-gray-600 mb-1">Status</p>
                  <span className={`inline-block px-3 py-1 rounded-full text-xs font-semibold ${getStatusColor(order.status)}`}>
                    {order.status}
                  </span>
                </div>

                <div>
                  <p className="text-sm text-gray-600 mb-1">Total Amount</p>
                  <p className="font-bold text-lg text-orange-600">${order.totalAmount.toFixed(2)}</p>
                </div>
              </div>

              <div className="border-t pt-4">
                <p className="text-sm text-gray-600 mb-2">
                  <strong>{order.items.length}</strong> item(s) • Payment: <strong>{order.paymentMethod.replace(/_/g, " ")}</strong>
                </p>

                <div className="flex gap-2">
                  <Link
                    href={`/order-confirmation/${order.id}`}
                    className="px-4 py-2 bg-orange-500 hover:bg-orange-600 text-white rounded-lg text-sm font-semibold"
                  >
                    View Details
                  </Link>
                  <button
                    onClick={() => {
                      const items = order.items
                        .map((item) => `${item.productName} (${item.quantity}x)`)
                        .join("\n");
                      alert(`Order Items:\n${items}`);
                    }}
                    className="px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-800 rounded-lg text-sm font-semibold"
                  >
                    View Items
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="mt-8">
          <Link
            href="/"
            className="text-orange-500 hover:text-orange-600 font-semibold"
          >
            ← Back to Shopping
          </Link>
        </div>
      </div>
    </div>
  );
}
