const services = [
  {
    title: "Pizza",
    emoji: "🍕",
    description:
      "Choose from classic Margherita, loaded Pepperoni, BBQ Chicken, Veggie Supreme, and more. Every pizza is hand-tossed with our signature dough, topped with premium ingredients, and baked to perfection in stone ovens.",
    items: ["Margherita", "Pepperoni", "BBQ Chicken", "Veggie Supreme", "Four Cheese"],
  },
  {
    title: "Cold Drinks",
    emoji: "🥤",
    description:
      "Refresh yourself with our selection of ice-cold beverages. From classic sodas to freshly squeezed juices and smoothies — the perfect companion for every meal.",
    items: ["Cola", "Lemon Soda", "Mango Smoothie", "Iced Tea", "Fresh Orange Juice"],
  },
  {
    title: "Breads",
    emoji: "🍞",
    description:
      "Freshly baked every day — our bread selection ranges from buttery garlic bread to cheesy stuffed breadsticks. Great as a side or a snack on its own.",
    items: ["Garlic Bread", "Cheese Breadsticks", "Focaccia", "Herb Rolls", "Stuffed Crust Bites"],
  },
];

export default function ServicesPage() {
  return (
    <div className="max-w-5xl mx-auto px-4 py-16">
      <h1 className="text-3xl font-bold text-center mb-4">Our Services</h1>
      <p className="text-center text-gray-500 mb-12 max-w-2xl mx-auto">
        We specialize in three categories — each crafted with care and delivered
        fresh to your doorstep.
      </p>

      <div className="space-y-10">
        {services.map((svc) => (
          <div
            key={svc.title}
            className="border border-gray-200 rounded-xl p-6 sm:p-8"
          >
            <div className="flex items-center gap-3 mb-4">
              <span className="text-4xl">{svc.emoji}</span>
              <h2 className="text-2xl font-semibold">{svc.title}</h2>
            </div>
            <p className="text-gray-600 mb-4 leading-relaxed">
              {svc.description}
            </p>
            <div>
              <h4 className="text-sm font-semibold text-gray-800 mb-2">
                Popular Picks
              </h4>
              <div className="flex flex-wrap gap-2">
                {svc.items.map((item) => (
                  <span
                    key={item}
                    className="bg-orange-50 text-orange-700 text-xs font-medium px-3 py-1 rounded-full"
                  >
                    {item}
                  </span>
                ))}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
