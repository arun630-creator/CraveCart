"use client";

import { useState } from "react";

const faqs = [
  {
    q: "How do I place an order?",
    a: "Simply sign up or log in, browse our menu under Services, add items to your cart, and proceed to checkout. You\u2019ll receive a confirmation once your order is placed.",
  },
  {
    q: "What payment methods do you accept?",
    a: "We accept all major credit and debit cards, UPI, and popular digital wallets. All transactions are secured with industry-standard encryption.",
  },
  {
    q: "How long does delivery take?",
    a: "Most orders are delivered within 30\u201345 minutes, depending on your location and order size.",
  },
  {
    q: "Can I cancel or modify my order?",
    a: "You can cancel or modify your order within 5 minutes of placing it. After that, the kitchen begins preparation and changes may not be possible.",
  },
  {
    q: "Do you offer discounts or promotions?",
    a: "Yes! We regularly run seasonal offers, coupon codes, and a loyalty points program. Check the promotions section after signing in.",
  },
  {
    q: "Is my personal data secure?",
    a: "Absolutely. We use Clerk for authentication and follow best practices for data security, including encrypted storage and secure API endpoints.",
  },
];

function FAQItem({ q, a }: { q: string; a: string }) {
  const [open, setOpen] = useState(false);

  return (
    <div className="border border-gray-200 rounded-lg">
      <button
        onClick={() => setOpen(!open)}
        className="w-full flex justify-between items-center px-5 py-4 text-left cursor-pointer"
      >
        <span className="font-medium text-gray-800">{q}</span>
        <svg
          className={`w-5 h-5 text-gray-400 transition-transform ${
            open ? "rotate-180" : ""
          }`}
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M19 9l-7 7-7-7"
          />
        </svg>
      </button>
      {open && (
        <div className="px-5 pb-4 text-gray-600 text-sm leading-relaxed">
          {a}
        </div>
      )}
    </div>
  );
}

export default function FAQsPage() {
  return (
    <div className="max-w-3xl mx-auto px-4 py-16">
      <h1 className="text-3xl font-bold text-center mb-4">
        Frequently Asked Questions
      </h1>
      <p className="text-center text-gray-500 mb-10">
        Have a question? Find answers to the most common ones below.
      </p>

      <div className="space-y-3">
        {faqs.map((faq) => (
          <FAQItem key={faq.q} q={faq.q} a={faq.a} />
        ))}
      </div>
    </div>
  );
}
