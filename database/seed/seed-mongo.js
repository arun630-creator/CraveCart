// Run this script in the Mongo shell or MongoDB Atlas Data Explorer to seed sample products and inventory.

use cravecart;

const products = [
  {
    _id: ObjectId(),
    name: 'Margherita Pizza',
    description: 'Classic pizza with tomato sauce and cheese',
    price: 12.99,
    available: true
  },
  {
    _id: ObjectId(),
    name: 'Cold Cola',
    description: 'Refreshing cold drink',
    price: 2.99,
    available: true
  },
  {
    _id: ObjectId(),
    name: 'Garlic Bread',
    description: 'Freshly baked garlic bread',
    price: 4.50,
    available: true
  }
];

const inventory = products.map(product => ({
  _id: ObjectId(),
  productId: product._id,
  quantityAvailable: 50,
  quantityReserved: 0,
  lastUpdated: new Date()
}));

db.products.insertMany(products);
db.inventory.insertMany(inventory);
