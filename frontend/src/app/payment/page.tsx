"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { paymentAPI, orderAPI } from "@/lib/api";

interface PaymentStatus {
  loading: boolean;
  success: boolean;
  error: string;
  method: string;
}

export default function PaymentPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [status, setStatus] = useState<PaymentStatus>({
    loading: true,
    success: false,
    error: "",
    method: searchParams.get("method") || "unknown",
  });

  const paymentId = searchParams.get("paymentId");
  const orderId = searchParams.get("orderId");

  useEffect(() => {
    processPayment();
  }, []);

  const processPayment = async () => {
    try {
      if (!paymentId || !orderId) {
        throw new Error("Missing payment information");
      }

      const method = searchParams.get("method");

      // Simulate payment processing
      // In a real app, this would integrate with Stripe or PayPal APIs
      await new Promise((resolve) => setTimeout(resolve, 2000));

      // Mock success - in production, verify with payment provider
      const mockTransactionId = `TXN-${Date.now()}`;

      // Confirm payment
      await paymentAPI.confirmPayment(paymentId, mockTransactionId, true);

      // Confirm order (deduct inventory and clear cart)
      await orderAPI.confirmOrder(orderId);

      setStatus({
        loading: false,
        success: true,
        error: "",
        method,
      });

      // Redirect to order confirmation
      setTimeout(() => {
        router.push(`/order-confirmation/${orderId}`);
      }, 2000);
    } catch (error) {
      console.error("Payment error:", error);
      setStatus({
        loading: false,
        success: false,
        error: error instanceof Error ? error.message : "Payment processing failed",
        method: searchParams.get("method") || "unknown",
      });
    }
  };

  const handleRetry = () => {
    setStatus((prev) => ({ ...prev, loading: true, error: "" }));
    processPayment();
  };

  if (status.loading) {
    return (
      <div className="flex justify-center items-center min-h-screen bg-gray-50">
        <div className="text-center">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-orange-100 rounded-full mb-4">
            <svg
              className="w-8 h-8 text-orange-600 animate-spin"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
              />
            </svg>
          </div>
          <h1 className="text-2xl font-bold mb-2">Processing Payment</h1>
          <p className="text-gray-600">
            Please wait while we process your {status.method} payment...
          </p>
        </div>
      </div>
    );
  }

  if (status.success) {
    return (
      <div className="flex justify-center items-center min-h-screen bg-gray-50">
        <div className="text-center">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-green-100 rounded-full mb-4">
            <svg className="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
            </svg>
          </div>
          <h1 className="text-2xl font-bold mb-2">Payment Successful!</h1>
          <p className="text-gray-600 mb-8">
            Your order has been confirmed. Redirecting to order details...
          </p>
          <div className="w-full bg-gray-200 rounded-full h-2">
            <div className="bg-green-600 h-2 rounded-full animate-pulse"></div>
          </div>
        </div>
      </div>
    );
  }

  if (status.error) {
    return (
      <div className="flex justify-center items-center min-h-screen bg-gray-50">
        <div className="text-center max-w-md">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-red-100 rounded-full mb-4">
            <svg className="w-8 h-8 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </div>
          <h1 className="text-2xl font-bold mb-2">Payment Failed</h1>
          <p className="text-gray-600 mb-8">{status.error}</p>

          <div className="space-y-3">
            <button
              onClick={handleRetry}
              className="w-full px-6 py-3 bg-orange-500 hover:bg-orange-600 text-white font-bold rounded-lg"
            >
              Try Again
            </button>
            <button
              onClick={() => router.push("/checkout")}
              className="w-full px-6 py-3 bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold rounded-lg"
            >
              Back to Checkout
            </button>
          </div>
        </div>
      </div>
    );
  }

  return null;
}
