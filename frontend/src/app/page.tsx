import Link from "next/link";

const categories = [
  {
    title: "Pizza",
    emoji: "🍕",
    description: "Hand-tossed, oven-baked perfection with fresh toppings.",
  },
  {
    title: "Cold Drinks",
    emoji: "🥤",
    description: "Ice-cold beverages to complement every meal.",
  },
  {
    title: "Breads",
    emoji: "🍞",
    description: "Freshly baked breads, garlic sticks & more.",
  },
];

export default function Home() {
  return (
    <div>
      {/* Hero Section */}
      <section className="bg-orange-50 py-20 px-4">
        <div className="max-w-4xl mx-auto text-center">
          <h1 className="text-4xl sm:text-5xl font-bold text-gray-900 mb-4">
            Delicious Food,{" "}
            <span className="text-orange-600">Delivered Fast</span>
          </h1>
          <p className="text-lg text-gray-600 mb-8 max-w-2xl mx-auto">
            Browse our menu of freshly prepared Pizza, Cold Drinks and Breads.
            Order online and enjoy seamless delivery right to your door.
          </p>
          <Link
            href="/products"
            className="inline-block bg-orange-600 text-white px-8 py-3 rounded-lg font-medium hover:bg-orange-700 transition-colors"
          >
            Browse Products & Add to Cart
          </Link>
        </div>
      </section>

      {/* Categories */}
      <section className="py-16 px-4">
        <div className="max-w-5xl mx-auto">
          <h2 className="text-2xl font-bold text-center mb-10">
            What We Offer
          </h2>
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
            {categories.map((cat) => (
              <div
                key={cat.title}
                className="border border-gray-200 rounded-xl p-6 text-center hover:shadow-md transition-shadow"
              >
                <span className="text-5xl block mb-4">{cat.emoji}</span>
                <h3 className="text-lg font-semibold mb-2">{cat.title}</h3>
                <p className="text-gray-500 text-sm">{cat.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="bg-gray-900 text-white py-14 px-4 text-center">
        <h2 className="text-2xl font-bold mb-3">Ready to Order?</h2>
        <p className="text-gray-400 mb-6">
          Sign up today and get your first delivery on us!
        </p>
        <Link
          href="/sign-up"
          className="inline-block bg-orange-600 text-white px-8 py-3 rounded-lg font-medium hover:bg-orange-700 transition-colors"
        >
          Get Started
        </Link>
      </section>
    </div>
  );
}
