# CraveCart - Complete Implementation Guide

## Overview
CraveCart is a full-stack e-commerce application with a complete cart → payment → order flow.

## Architecture

```
Customer Flow:
1. Browse Products
2. Add to Cart
3. View Cart
4. Proceed to Checkout
5. Select Payment Method
6. Process Payment
7. Order Confirmation
8. View Order History
```

## Backend Implementation

### Technology Stack
- **Framework**: Spring Boot 4.0.5
- **Database**: MongoDB
- **Language**: Java 17
- **Build Tool**: Maven

### New Models Created

#### 1. **Order (Enhanced)**
```java
- id: String (UUID)
- userId: String
- items: List<CartItem>
- totalAmount: Double
- status: String (PENDING, PROCESSING, COMPLETED, CANCELLED)
- paymentMethod: String (STRIPE, PAYPAL, CASH_ON_DELIVERY)
- paymentStatus: String (PENDING, COMPLETED, FAILED)
- transactionId: String
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
- shippingAddress: String
- customerEmail: String
```

#### 2. **Payment (New)**
```java
- id: String (UUID)
- orderId: String
- userId: String
- amount: Double
- paymentMethod: String
- status: String (PENDING, COMPLETED, FAILED)
- transactionId: String
- paymentGatewayResponse: String
- createdAt: LocalDateTime
- updatedAt: LocalDateTime
```

#### 3. **CartItem (Enhanced)**
```java
- id: String (UUID)
- userId: String
- productId: String
- quantity: Integer
- price: Double (Price at time of adding to cart)
- productName: String
- productImage: String
```

#### 4. **Product (Enhanced)**
```java
- id: String (UUID)
- name: String
- description: String
- price: Double
- categoryId: String
- brand: String
- imageUrl: String
- rating: Double
- reviews: Integer
```

### Service Layer

#### **CartService**
- `addToCart()` - Add product to cart with stock validation
- `getCart()` - Get all cart items for user
- `updateQuantity()` - Update item quantity
- `removeFromCart()` - Remove item from cart
- `clearCart()` - Clear entire cart
- `getCartTotal()` - Calculate cart total

#### **OrderService**
- `createOrder()` - Create order from cart (status: PENDING)
- `confirmOrder()` - Confirm order after payment (deduct inventory, clear cart)
- `getOrder()` - Get order details
- `getUserOrders()` - Get user's order history
- `updateOrderStatus()` - Update order status

#### **PaymentService**
- `createPayment()` - Create payment record (status: PENDING)
- `processPayment()` - Process payment (complete/fail)
- `getPayment()` - Get payment details
- `getPaymentByOrderId()` - Get payment for an order

### API Endpoints

#### **Cart Endpoints**
```
POST   /api/cart/add                    - Add item to cart
GET    /api/cart/{userId}              - Get user's cart
GET    /api/cart/{userId}/total        - Get cart total
PUT    /api/cart/{itemId}              - Update quantity
DELETE /api/cart/{itemId}              - Remove from cart
DELETE /api/cart/{userId}/clear        - Clear cart
```

#### **Order Endpoints**
```
POST   /api/orders/create              - Create order
POST   /api/orders/{orderId}/confirm   - Confirm order (after payment)
GET    /api/orders/{orderId}           - Get order details
GET    /api/orders/user/{userId}       - Get user's orders
PUT    /api/orders/{orderId}/status    - Update order status
```

#### **Payment Endpoints**
```
POST   /api/payments/create            - Create payment record
POST   /api/payments/{paymentId}/confirm - Confirm payment
GET    /api/payments/{paymentId}       - Get payment details
GET    /api/payments/order/{orderId}   - Get payment for order
```

## Frontend Implementation

### Technology Stack
- **Framework**: Next.js 16.2.1
- **Language**: TypeScript
- **Styling**: Tailwind CSS 4
- **Authentication**: Clerk
- **State Management**: React Hooks

### API Integration Utilities (`lib/api.ts`)
- `cartAPI` - Cart operations
- `orderAPI` - Order operations
- `paymentAPI` - Payment operations

### Pages Created

#### 1. **Cart Page** (`/cart`)
- View all cart items
- Update quantities
- Remove items
- View cart total
- Proceed to checkout

#### 2. **Checkout Page** (`/checkout`)
- Enter shipping address
- Select payment method:
  - Credit/Debit Card (Stripe)
  - PayPal
  - Cash on Delivery
- Review order summary
- Place order

#### 3. **Payment Page** (`/payment`)
- Process payment through selected method
- Handle payment success/failure
- Redirect to order confirmation

#### 4. **Order Confirmation Page** (`/order-confirmation/[orderId]`)
- Display order details
- Show order items
- Display shipping information
- Show order status and payment status

#### 5. **Orders History Page** (`/orders`)
- View all user orders
- Filter by status
- View order details
- Quick links to order confirmation

## Complete Flow Diagram

```
┌─────────────────┐
│  Add to Cart    │ (CartService.addToCart)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  View Cart      │ (GET /api/cart/{userId})
└────────┬────────┘
         │
         ▼
┌─────────────────────┐
│  Checkout Page      │ (Collect shipping & payment method)
└────────┬────────────┘
         │
         ▼
┌──────────────────────────────────┐
│  Create Order (PENDING status)   │ (POST /api/orders/create)
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│  Create Payment (PENDING status) │ (POST /api/payments/create)
└────────┬─────────────────────────┘
         │
         ├─────────────────────────┬─────────────────────────┐
         │                         │                         │
    Stripe/PayPal          Cash on Delivery            Other Methods
         │                         │
         ▼                         ▼
┌──────────────────┐    ┌─────────────────────┐
│  Process Payment │    │ Auto-confirm (COD)  │
└────────┬─────────┘    └─────────┬───────────┘
         │                        │
         ▼                        ▼
┌──────────────────────────────────────────────┐
│ Confirm Payment (status: COMPLETED)          │
│ (POST /api/payments/{paymentId}/confirm)     │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│ Confirm Order                                │
│ - Deduct Inventory                           │
│ - Set status: PROCESSING                     │
│ - Clear Cart                                 │
│ (POST /api/orders/{orderId}/confirm)         │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│  Order Confirmation Page                     │
│  - Display order & items                     │
│  - Show payment status                       │
│  - Provide order tracking                    │
└──────────────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│  View Order History (/orders)                │
└──────────────────────────────────────────────┘
```

## Setup Instructions

### Backend Setup

1. **Ensure MongoDB is running**
   ```bash
   # Make sure MongoDB is installed and running on localhost:27017
   ```

2. **Build and Run**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

3. **Verify Backend is Running**
   - Backend should be available at `http://localhost:8080`

### Frontend Setup

1. **Install Dependencies**
   ```bash
   cd frontend
   npm install
   ```

2. **Configure Environment**
   ```bash
   # Copy .env.example to .env.local
   cp .env.example .env.local
   
   # Update .env.local with your values:
   NEXT_PUBLIC_API_URL=http://localhost:8080/api
   NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY=your_key
   CLERK_SECRET_KEY=your_key
   ```

3. **Run Development Server**
   ```bash
   npm run dev
   ```

4. **Access Frontend**
   - Frontend available at `http://localhost:3000`

## Payment Methods

### 1. **Cash on Delivery (COD)**
- Automatically confirms order
- No external payment processing
- Perfect for testing

### 2. **Stripe** (Integration Ready)
- Mock implementation in `/payment` page
- Ready for real Stripe integration
- Requires: `NEXT_PUBLIC_STRIPE_PUBLISHABLE_KEY`

### 3. **PayPal** (Integration Ready)
- Mock implementation in `/payment` page
- Ready for real PayPal integration
- Requires: `NEXT_PUBLIC_PAYPAL_CLIENT_ID`

## Key Features Implemented

✅ **Cart Management**
- Add items with stock validation
- Update quantities
- Remove items
- View cart total

✅ **Order Management**
- Create orders from cart
- Track order history
- Order status updates
- Order confirmation

✅ **Payment Processing**
- Multiple payment methods
- Payment status tracking
- Payment confirmation
- Transaction IDs

✅ **Inventory Management**
- Automatic stock validation
- Prevent over-ordering
- Stock deduction on order confirmation

✅ **User Experience**
- Clean, intuitive UI
- Real-time updates
- Error handling
- Order status tracking

## Database Collections

```
collections:
├── products
├── categories
├── cart_items
├── orders
├── payments
├── inventories
└── users (if added)
```

## Future Enhancements

- [ ] Real Stripe integration
- [ ] Real PayPal integration
- [ ] Email notifications
- [ ] SMS order updates
- [ ] Return management
- [ ] Wishlist feature
- [ ] Product reviews
- [ ] Recommendation engine
- [ ] Admin dashboard
- [ ] Analytics & reporting

## Error Handling

- Cart validation
- Stock availability checks
- Payment failure handling
- User authentication
- Proper error messages

## Security Considerations

- Input validation on all endpoints
- User authentication via Clerk
- CORS enabled for frontend
- Payment data encryption (ready for integration)
- Order confirmation only after successful payment
