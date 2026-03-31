# MongoDB Schema for CraveCart

## Database
- Database name: `cravecart`
- Primary storage: MongoDB collections for users, products, orders, cart, inventory, promotions, and loyalty.

## Collections Overview

### `users`
Stores authenticated customers and their profile data.

Fields:
- `_id`: ObjectId
- `email`: string, unique
- `passwordHash`: string
- `name`: string
- `phone`: string
- `role`: string (`customer`, `admin`, `staff`)
- `status`: string (`active`, `inactive`, `blocked`)
- `addresses`: array of embedded address objects
  - `label`, `line1`, `line2`, `city`, `state`, `postalCode`, `country`, `instructions`
- `loyalty`: object
  - `points`: number
  - `tier`: string
  - `lastUpdated`: date
- `createdAt`, `updatedAt`: date
- `orderHistoryEnabled`: boolean
- `emailVerified`: boolean
- `preferences`: object

Indexes:
- `{ email: 1 }` unique
- `{ role: 1 }`

### `brands`
Stores brand metadata for menu grouping.

Fields:
- `_id`: ObjectId
- `name`: string
- `slug`: string, unique
- `description`: string
- `logoUrl`: string
- `isActive`: boolean
- `createdAt`, `updatedAt`: date

Indexes:
- `{ slug: 1 }` unique

### `categories`
Stores menu categories such as Pizza, Cold Drinks, Breads.

Fields:
- `_id`: ObjectId
- `name`: string
- `slug`: string, unique
- `description`: string
- `parentId`: ObjectId|null
- `isActive`: boolean
- `createdAt`, `updatedAt`: date

Indexes:
- `{ slug: 1 }` unique
- `{ parentId: 1 }`

### `packaging`
Stores packaging options and metadata.

Fields:
- `_id`: ObjectId
- `name`: string
- `description`: string
- `type`: string (`box`, `bag`, `bottle`, etc.)
- `price`: number
- `isReusable`: boolean
- `createdAt`, `updatedAt`: date

### `products`
Stores all menu items with category and brand links.

Fields:
- `_id`: ObjectId
- `name`: string
- `slug`: string, unique
- `description`: string
- `brandId`: ObjectId
- `categoryIds`: array of ObjectId
- `packagingId`: ObjectId
- `sku`: string, optional
- `price`: number
- `discountPrice`: number|null
- `tags`: array of strings
- `images`: array of strings
- `available`: boolean
- `isFeatured`: boolean
- `metadata`: object
- `createdAt`, `updatedAt`: date

Indexes:
- `{ slug: 1 }` unique
- `{ brandId: 1 }`
- `{ categoryIds: 1 }`
- `{ available: 1 }`

### `inventory`
Tracks stock for each product.

Fields:
- `_id`: ObjectId
- `productId`: ObjectId
- `sku`: string
- `warehouse`: string
- `quantityAvailable`: number
- `quantityReserved`: number
- `reorderThreshold`: number
- `lastUpdated`: date

Indexes:
- `{ productId: 1 }`
- `{ sku: 1 }`

### `carts`
Stores the current shopping cart for each user.

Fields:
- `_id`: ObjectId
- `userId`: ObjectId
- `items`: array of objects
  - `productId`: ObjectId
  - `name`: string
  - `sku`: string
  - `price`: number
  - `quantity`: number
  - `packagingId`: ObjectId|null
  - `lineTotal`: number
- `subtotal`: number
- `tax`: number
- `discount`: number
- `total`: number
- `updatedAt`: date
- `createdAt`: date

Indexes:
- `{ userId: 1 }` unique

### `orders`
Stores completed and in-progress orders.

Fields:
- `_id`: ObjectId
- `userId`: ObjectId
- `cartId`: ObjectId|null
- `orderNumber`: string, unique
- `items`: array of embedded objects
  - `productId`: ObjectId
  - `name`: string
  - `sku`: string
  - `price`: number
  - `quantity`: number
  - `packagingId`: ObjectId|null
  - `lineTotal`: number
- `billingAddress`: object
- `shippingAddress`: object
- `payment`: object
  - `method`: string
  - `status`: string (`pending`, `paid`, `failed`)
  - `transactionId`: string|null
- `status`: string (`pending`, `confirmed`, `preparing`, `out_for_delivery`, `delivered`, `cancelled`, `returned`)
- `promotionsApplied`: array of objects
  - `promotionId`: ObjectId
  - `code`: string
  - `discountAmount`: number
- `loyaltyUsed`: object
  - `pointsSpent`: number
  - `pointsValue`: number
- `totals`: object
  - `subtotal`: number
  - `discount`: number
  - `tax`: number
  - `deliveryFee`: number
  - `grandTotal`: number
- `orderDate`: date
- `deliveryDate`: date|null
- `events`: array of objects
  - `timestamp`: date
  - `status`: string
  - `note`: string|null
- `emailConfirmationStatus`: string (`pending`, `sent`, `failed`)
- `createdAt`, `updatedAt`: date

Indexes:
- `{ userId: 1, orderDate: -1 }`
- `{ orderNumber: 1 }` unique
- `{ status: 1 }`

### `promotions`
Stores coupons, seasonal offers, and loyalty rules.

Fields:
- `_id`: ObjectId
- `name`: string
- `code`: string, unique
- `type`: string (`percentage`, `fixed`, `buy_x_get_y`, `free_shipping`)
- `discountValue`: number
- `minimumSpend`: number|null
- `applicableTo`: object
  - `categories`: array of ObjectId
  - `products`: array of ObjectId
  - `brands`: array of ObjectId
- `validFrom`: date
- `validUntil`: date
- `maxUses`: number|null
- `usesPerUser`: number|null
- `enabled`: boolean
- `createdAt`, `updatedAt`: date

Indexes:
- `{ code: 1 }` unique
- `{ enabled: 1, validFrom: 1, validUntil: 1 }`

### `loyalty_accounts` (optional)
If loyalty data is large or updated often, use a dedicated collection.

Fields:
- `_id`: ObjectId
- `userId`: ObjectId
- `pointsBalance`: number
- `tier`: string
- `history`: array of objects
  - `change`: number
  - `reason`: string
  - `referenceType`: string
  - `referenceId`: ObjectId
  - `createdAt`: date

Indexes:
- `{ userId: 1 }` unique

### `email_jobs` or `notifications` (optional)
Tracks email confirmations and notifications.

Fields:
- `_id`: ObjectId
- `type`: string
- `targetEmail`: string
- `referenceId`: ObjectId
- `status`: string
- `attempts`: number
- `payload`: object
- `createdAt`, `updatedAt`: date

## Design Notes

- `orders.items` and `carts.items` are embedded to preserve user choices and pricing at the time of order.
- `users.addresses` is embedded to support quick checkout and order history.
- `inventory` is separate to support real-time stock updates and avoid write contention on product documents.
- Use MongoDB transactions when confirming an order to decrement inventory and write the order atomically.
- Keep order history query-friendly by indexing `{ userId: 1, orderDate: -1 }`.
- Enable quick reorder by copying the last `orders.items` into a new `carts` document.

## Example Validation Script

A sample JSON schema validation script is available at `database/mongodb-schema-validation.js`.
Run it in the Mongo shell or MongoDB Atlas Data Explorer to create the collections with validators and recommended indexes.

## Validation Strategy

Use collection validators or application-side schema enforcement for critical collections:
- `users`: require `email`, `passwordHash`, `name`
- `products`: require `name`, `price`, `categoryIds`
- `orders`: require `userId`, `items`, `totals`, `status`
- `promotions`: require `code`, `type`, `validFrom`, `validUntil`

## Recommended Indexes
- `users.email` unique
- `products.slug` unique
- `orders.orderNumber` unique
- `orders.userId + orderDate` descending
- `inventory.productId`
- `carts.userId` unique
- `promotions.code` unique

## How This Supports Your Requirements

- Centralized portal: `brands`, `categories`, `packaging`, and `products` collections.
- Menu browsing: product documents link to categories and brands.
- Cart/order flow: `carts` + `orders` collections with embedded line items.
- Inventory sync: `inventory` updates on confirmed orders.
- Secure APIs: store users and roles in `users`; authorize by `role` and `userId`.
- Order confirmations: track `emailConfirmationStatus` and use `email_jobs`.
- Order history / quick reorder: stored in `orders` and queryable by `userId`.
- Promotions/loyalty: `promotions` plus loyalty data in `users` or `loyalty_accounts`.
