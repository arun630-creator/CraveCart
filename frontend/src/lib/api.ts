const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api";

// Cart API
export const cartAPI = {
  addToCart: async (userId: string, productId: string, quantity: number) => {
    const response = await fetch(`${API_BASE_URL}/cart/add`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ userId, productId, quantity }),
    });
    return response.json();
  },

  getCart: async (userId: string) => {
    const response = await fetch(`${API_BASE_URL}/cart/${userId}`);
    return response.json();
  },

  getCartTotal: async (userId: string) => {
    const response = await fetch(`${API_BASE_URL}/cart/${userId}/total`);
    return response.json();
  },

  updateQuantity: async (itemId: string, quantity: number) => {
    const response = await fetch(`${API_BASE_URL}/cart/${itemId}?quantity=${quantity}`, {
      method: "PUT",
    });
    return response.json();
  },

  removeFromCart: async (itemId: string) => {
    await fetch(`${API_BASE_URL}/cart/${itemId}`, {
      method: "DELETE",
    });
  },

  clearCart: async (userId: string) => {
    await fetch(`${API_BASE_URL}/cart/${userId}/clear`, {
      method: "DELETE",
    });
  },
};

// Order API
export const orderAPI = {
  createOrder: async (
    userId: string,
    paymentMethod: string,
    shippingAddress: string,
    customerEmail: string
  ) => {
    const response = await fetch(`${API_BASE_URL}/orders/create`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userId,
        paymentMethod,
        shippingAddress,
        customerEmail,
      }),
    });
    return response.json();
  },

  confirmOrder: async (orderId: string) => {
    const response = await fetch(`${API_BASE_URL}/orders/${orderId}/confirm`, {
      method: "POST",
    });
    return response.json();
  },

  getOrder: async (orderId: string) => {
    const response = await fetch(`${API_BASE_URL}/orders/${orderId}`);
    return response.json();
  },

  getUserOrders: async (userId: string) => {
    const response = await fetch(`${API_BASE_URL}/orders/user/${userId}`);
    return response.json();
  },

  updateOrderStatus: async (orderId: string, status: string) => {
    const response = await fetch(`${API_BASE_URL}/orders/${orderId}/status?status=${status}`, {
      method: "PUT",
    });
    return response.json();
  },
};

// Payment API
export const paymentAPI = {
  createPayment: async (
    orderId: string,
    userId: string,
    amount: number,
    paymentMethod: string
  ) => {
    const response = await fetch(`${API_BASE_URL}/payments/create`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        orderId,
        userId,
        amount,
        paymentMethod,
      }),
    });
    return response.json();
  },

  confirmPayment: async (
    paymentId: string,
    transactionId: string,
    isSuccessful: boolean
  ) => {
    const response = await fetch(`${API_BASE_URL}/payments/${paymentId}/confirm`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        transactionId,
        isSuccessful,
      }),
    });
    return response.json();
  },

  getPayment: async (paymentId: string) => {
    const response = await fetch(`${API_BASE_URL}/payments/${paymentId}`);
    return response.json();
  },

  getPaymentByOrderId: async (orderId: string) => {
    const response = await fetch(`${API_BASE_URL}/payments/order/${orderId}`);
    return response.json();
  },
};

// Product API
export const productAPI = {
  getAllProducts: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/products`);
      if (!response.ok) throw new Error("Failed to fetch products");
      return response.json();
    } catch (error) {
      console.error("Error fetching products:", error);
      return [];
    }
  },

  getProductById: async (productId: string) => {
    const response = await fetch(`${API_BASE_URL}/products/${productId}`);
    return response.json();
  },

  getProductsByCategory: async (categoryId: string) => {
    const response = await fetch(`${API_BASE_URL}/products/category/${categoryId}`);
    return response.json();
  },
};

// Category API
export const categoryAPI = {
  getAllCategories: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/categories`);
      if (!response.ok) throw new Error("Failed to fetch categories");
      return response.json();
    } catch (error) {
      console.error("Error fetching categories:", error);
      return [];
    }
  },
};

// Data Seeder API
export const seederAPI = {
  initializeDatabase: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/seed/init`, {
        method: "POST",
      });
      return response.text();
    } catch (error) {
      console.error("Error initializing database:", error);
      throw error;
    }
  },
};
