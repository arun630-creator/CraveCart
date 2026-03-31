export default function AboutPage() {
  return (
    <div className="max-w-4xl mx-auto px-4 py-16">
      <h1 className="text-3xl font-bold text-center mb-10">About Us</h1>

      {/* Story */}
      <section className="mb-12">
        <h2 className="text-xl font-semibold mb-3">Our Story</h2>
        <p className="text-gray-600 leading-relaxed">
          CraveCart was born from a simple idea — everyone deserves access to
          delicious, freshly prepared food without the hassle. What started as a
          small neighborhood kitchen has grown into a trusted retail ordering
          platform serving Pizza, Cold Drinks, and Breads to thousands of happy
          customers.
        </p>
      </section>

      {/* Mission */}
      <section className="mb-12">
        <h2 className="text-xl font-semibold mb-3">Our Mission</h2>
        <p className="text-gray-600 leading-relaxed">
          We aim to make food ordering seamless, secure, and enjoyable. By
          combining quality ingredients with efficient technology, we ensure
          every order is a great experience — from browsing to the first bite.
        </p>
      </section>

      {/* Values */}
      <section>
        <h2 className="text-xl font-semibold mb-6">What We Stand For</h2>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
          {[
            {
              icon: "⭐",
              title: "Quality",
              text: "Only the freshest ingredients and recipes.",
            },
            {
              icon: "🚀",
              title: "Speed",
              text: "Fast, reliable delivery you can count on.",
            },
            {
              icon: "🔒",
              title: "Security",
              text: "Secure payments and data protection.",
            },
          ].map((v) => (
            <div
              key={v.title}
              className="border border-gray-200 rounded-xl p-5 text-center"
            >
              <span className="text-3xl block mb-3">{v.icon}</span>
              <h3 className="font-semibold mb-1">{v.title}</h3>
              <p className="text-gray-500 text-sm">{v.text}</p>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
