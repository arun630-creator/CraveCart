# CraveCart - Quick Start Guide

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven
- MongoDB
- Node.js 18+
- npm

### Step 1: Start MongoDB
```bash
# On Windows
net start MongoDB

# On macOS (Homebrew)
brew services start mongodb-community

# On Linux
sudo systemctl start mongod
```

### Step 2: Start Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

✅ Backend running at: `http://localhost:8080`

### Step 3: Start Frontend
```bash
cd frontend

# Install dependencies (first time only)
npm install

# Create .env.local file
cp .env.example .env.local

# Run development server
npm run dev
```

✅ Frontend running at: `http://localhost:3000`

## 📋 Testing the Flow

### 1. Add Product to Cart
```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "productId": "prod456",
    "quantity": 2
  }'
```

### 2. Get Cart
```bash
curl http://localhost:8080/api/cart/user123
```

### 3. Get Cart Total
```bash
curl http://localhost:8080/api/cart/user123/total
```

### 4. Create Order
```bash
curl -X POST http://localhost:8080/api/orders/create \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "paymentMethod": "CASH_ON_DELIVERY",
    "shippingAddress": "123 Main St, City, State",
    "customerEmail": "user@example.com"
  }'
```

### 5. Create Payment
```bash
curl -X POST http://localhost:8080/api/payments/create \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "order789",
    "userId": "user123",
    "amount": 149.99,
    "paymentMethod": "CASH_ON_DELIVERY"
  }'
```

### 6. Confirm Payment
```bash
curl -X POST http://localhost:8080/api/payments/payment123/confirm \
  -H "Content-Type: application/json" \
  -d '{
    "transactionId": "TXN-12345",
    "isSuccessful": true
  }'
```

### 7. Confirm Order
```bash
curl -X POST http://localhost:8080/api/orders/order789/confirm
```

## 🎯 Frontend Pages

| Page | URL | Purpose |
|------|-----|---------|
| Cart | `/cart` | View and manage cart items |
| Checkout | `/checkout` | Enter shipping & payment method |
| Payment | `/payment` | Payment processing |
| Order Confirmation | `/order-confirmation/[orderId]` | Order receipt & details |
| My Orders | `/orders` | Order history |

## 💳 Payment Methods

1. **Cash on Delivery** (COD)
   - Select at checkout
   - Auto-confirms order
   - Best for testing

2. **Stripe** 
   - Placeholder implementation ready
   - Requires Stripe API keys

3. **PayPal**
   - Placeholder implementation ready
   - Requires PayPal credentials

## 📦 Key Features

✅ Shopping Cart
- Add/remove items
- Update quantities
- Real-time total calculation

✅ Checkout
- Shipping address entry
- Payment method selection
- Order summary

✅ Payment Processing
- Multiple payment options
- Payment status tracking
- Error handling

✅ Order Management
- Order creation
- Payment confirmation
- Inventory deduction
- Order history

## ⚠️ Important Notes

### Environment Variables
Create `frontend/.env.local`:
```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY=your_clerk_key (if using auth)
CLERK_SECRET_KEY=your_clerk_secret (if using auth)
```

### MongoDB Sample Data
To seed the database with test products:
```bash
# Use MongoDB Compass or mongosh to insert:
db.products.insertMany([
  {
    "name": "Burger Deluxe",
    "description": "Premium beef burger",
    "price": 12.99,
    "categoryId": "cat1",
    "brand": "CraveCart",
    "imageUrl": "https://example.com/burger.jpg"
  }
])
```

## 🐛 Troubleshooting

### Backend won't start
- Ensure MongoDB is running
- Check port 8080 is available
- Check Java 17+ is installed: `java -version`

### Frontend won't connect to backend
- Ensure backend is running at `http://localhost:8080`
- Check `NEXT_PUBLIC_API_URL` in `.env.local`
- Check browser console for CORS errors

### Cart page shows empty
- Ensure cart items were added via API
- Check MongoDB has data in `cart_items` collection
- Check userId matches

## 📝 Next Steps

1. **Seed database** with products and inventory
2. **Test payment flow** using Cash on Delivery
3. **Integrate Stripe** (update PaymentController)
4. **Integrate PayPal** (update PaymentController)
5. **Add email notifications**
6. **Deploy to production**

## 📚 Documentation

See [IMPLEMENTATION_GUIDE.md](./IMPLEMENTATION_GUIDE.md) for:
- Complete API documentation
- Data models
- Service layer details
- Database schema
- Architecture overview

---

**Need help?** Check the logs:
- Backend: Terminal running `mvn spring-boot:run`
- Frontend: Browser console (F12)
- MongoDB: Check `mongod` process

Happy coding! 🎉
