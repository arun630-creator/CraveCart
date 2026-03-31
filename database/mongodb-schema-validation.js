// MongoDB schema validation examples for CraveCart
// Run from the Mongo shell or MongoDB Atlas Data Explorer.

const dbName = 'cravecart';
const db = connect(`127.0.0.1:27017/${dbName}`);

function createUsers() {
  db.createCollection('users', {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        required: ['email', 'passwordHash', 'name', 'role', 'status', 'createdAt', 'updatedAt'],
        properties: {
          email: { bsonType: 'string', description: 'must be an email string' },
          passwordHash: { bsonType: 'string' },
          name: { bsonType: 'string' },
          phone: { bsonType: 'string' },
          role: { enum: ['customer', 'admin', 'staff'] },
          status: { enum: ['active', 'inactive', 'blocked'] },
          addresses: {
            bsonType: 'array',
            items: {
              bsonType: 'object',
              required: ['label', 'line1', 'city', 'state', 'postalCode', 'country'],
              properties: {
                label: { bsonType: 'string' },
                line1: { bsonType: 'string' },
                line2: { bsonType: ['string', 'null'] },
                city: { bsonType: 'string' },
                state: { bsonType: 'string' },
                postalCode: { bsonType: 'string' },
                country: { bsonType: 'string' },
                instructions: { bsonType: ['string', 'null'] }
              }
            }
          },
          loyalty: {
            bsonType: 'object',
            properties: {
              points: { bsonType: 'int' },
              tier: { bsonType: 'string' },
              lastUpdated: { bsonType: 'date' }
            }
          },
          emailVerified: { bsonType: 'bool' },
          preferences: { bsonType: 'object' }
        }
      }
    }
  });
  db.users.createIndex({ email: 1 }, { unique: true });
  db.users.createIndex({ role: 1 });
}

function createProducts() {
  db.createCollection('products', {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        required: ['name', 'slug', 'brandId', 'categoryIds', 'price', 'available', 'createdAt', 'updatedAt'],
        properties: {
          name: { bsonType: 'string' },
          slug: { bsonType: 'string' },
          description: { bsonType: 'string' },
          brandId: { bsonType: 'objectId' },
          categoryIds: {
            bsonType: 'array',
            items: { bsonType: 'objectId' }
          },
          packagingId: { bsonType: 'objectId' },
          sku: { bsonType: ['string', 'null'] },
          price: { bsonType: 'double' },
          discountPrice: { bsonType: ['double', 'null'] },
          tags: { bsonType: 'array', items: { bsonType: 'string' } },
          images: { bsonType: 'array', items: { bsonType: 'string' } },
          available: { bsonType: 'bool' },
          isFeatured: { bsonType: 'bool' },
          metadata: { bsonType: 'object' }
        }
      }
    }
  });
  db.products.createIndex({ slug: 1 }, { unique: true });
  db.products.createIndex({ brandId: 1 });
  db.products.createIndex({ categoryIds: 1 });
  db.products.createIndex({ available: 1 });
}

function createOrders() {
  db.createCollection('orders', {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        required: ['userId', 'orderNumber', 'items', 'totals', 'status', 'orderDate', 'createdAt', 'updatedAt'],
        properties: {
          userId: { bsonType: 'objectId' },
          cartId: { bsonType: ['objectId', 'null'] },
          orderNumber: { bsonType: 'string' },
          items: {
            bsonType: 'array',
            minItems: 1,
            items: {
              bsonType: 'object',
              required: ['productId', 'name', 'price', 'quantity', 'lineTotal'],
              properties: {
                productId: { bsonType: 'objectId' },
                name: { bsonType: 'string' },
                sku: { bsonType: ['string', 'null'] },
                price: { bsonType: 'double' },
                quantity: { bsonType: 'int' },
                packagingId: { bsonType: ['objectId', 'null'] },
                lineTotal: { bsonType: 'double' }
              }
            }
          },
          billingAddress: { bsonType: 'object' },
          shippingAddress: { bsonType: 'object' },
          payment: {
            bsonType: 'object',
            required: ['method', 'status'],
            properties: {
              method: { bsonType: 'string' },
              status: { bsonType: 'string' },
              transactionId: { bsonType: ['string', 'null'] }
            }
          },
          status: { bsonType: 'string' },
          promotionsApplied: {
            bsonType: 'array',
            items: {
              bsonType: 'object',
              properties: {
                promotionId: { bsonType: 'objectId' },
                code: { bsonType: 'string' },
                discountAmount: { bsonType: 'double' }
              }
            }
          },
          loyaltyUsed: {
            bsonType: 'object',
            properties: {
              pointsSpent: { bsonType: 'int' },
              pointsValue: { bsonType: 'double' }
            }
          },
          totals: {
            bsonType: 'object',
            required: ['subtotal', 'discount', 'tax', 'deliveryFee', 'grandTotal'],
            properties: {
              subtotal: { bsonType: 'double' },
              discount: { bsonType: 'double' },
              tax: { bsonType: 'double' },
              deliveryFee: { bsonType: 'double' },
              grandTotal: { bsonType: 'double' }
            }
          },
          orderDate: { bsonType: 'date' },
          deliveryDate: { bsonType: ['date', 'null'] },
          events: {
            bsonType: 'array',
            items: {
              bsonType: 'object',
              required: ['timestamp', 'status'],
              properties: {
                timestamp: { bsonType: 'date' },
                status: { bsonType: 'string' },
                note: { bsonType: ['string', 'null'] }
              }
            }
          },
          emailConfirmationStatus: { bsonType: 'string' }
        }
      }
    }
  });
  db.orders.createIndex({ orderNumber: 1 }, { unique: true });
  db.orders.createIndex({ userId: 1, orderDate: -1 });
  db.orders.createIndex({ status: 1 });
}

function createPromotions() {
  db.createCollection('promotions', {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        required: ['name', 'code', 'type', 'discountValue', 'validFrom', 'validUntil', 'enabled', 'createdAt', 'updatedAt'],
        properties: {
          name: { bsonType: 'string' },
          code: { bsonType: 'string' },
          type: { bsonType: 'string' },
          discountValue: { bsonType: 'double' },
          minimumSpend: { bsonType: ['double', 'null'] },
          applicableTo: {
            bsonType: 'object',
            properties: {
              categories: { bsonType: 'array', items: { bsonType: 'objectId' } },
              products: { bsonType: 'array', items: { bsonType: 'objectId' } },
              brands: { bsonType: 'array', items: { bsonType: 'objectId' } }
            }
          },
          validFrom: { bsonType: 'date' },
          validUntil: { bsonType: 'date' },
          maxUses: { bsonType: ['int', 'null'] },
          usesPerUser: { bsonType: ['int', 'null'] },
          enabled: { bsonType: 'bool' }
        }
      }
    }
  });
  db.promotions.createIndex({ code: 1 }, { unique: true });
  db.promotions.createIndex({ enabled: 1, validFrom: 1, validUntil: 1 });
}

function createInventory() {
  db.createCollection('inventory', {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        required: ['productId', 'quantityAvailable', 'quantityReserved', 'reorderThreshold', 'lastUpdated'],
        properties: {
          productId: { bsonType: 'objectId' },
          sku: { bsonType: 'string' },
          warehouse: { bsonType: 'string' },
          quantityAvailable: { bsonType: 'int' },
          quantityReserved: { bsonType: 'int' },
          reorderThreshold: { bsonType: 'int' },
          lastUpdated: { bsonType: 'date' }
        }
      }
    }
  });
  db.inventory.createIndex({ productId: 1 });
  db.inventory.createIndex({ sku: 1 });
}

function createCarts() {
  db.createCollection('carts', {
    validator: {
      $jsonSchema: {
        bsonType: 'object',
        required: ['userId', 'items', 'subtotal', 'tax', 'discount', 'total', 'createdAt', 'updatedAt'],
        properties: {
          userId: { bsonType: 'objectId' },
          items: {
            bsonType: 'array',
            minItems: 1,
            items: {
              bsonType: 'object',
              required: ['productId', 'name', 'price', 'quantity', 'lineTotal'],
              properties: {
                productId: { bsonType: 'objectId' },
                name: { bsonType: 'string' },
                sku: { bsonType: ['string', 'null'] },
                price: { bsonType: 'double' },
                quantity: { bsonType: 'int' },
                packagingId: { bsonType: ['objectId', 'null'] },
                lineTotal: { bsonType: 'double' }
              }
            }
          },
          subtotal: { bsonType: 'double' },
          tax: { bsonType: 'double' },
          discount: { bsonType: 'double' },
          total: { bsonType: 'double' }
        }
      }
    }
  });
  db.carts.createIndex({ userId: 1 }, { unique: true });
}

function main() {
  createUsers();
  createProducts();
  createInventory();
  createCarts();
  createOrders();
  createPromotions();
  print('MongoDB collections created with schema validation.');
}

main();
