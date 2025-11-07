// cartUtils.js
export const calculateCartTotals = (cartItems) => {
  const subtotal = cartItems.reduce((acc, item) => {
    return acc + item.foodPrice * item.quantity;
  }, 0);

  const shipping = subtotal === 0 ? 0.0 : 10;
  const tax = subtotal * 0.1;
  const total = subtotal + shipping + tax;

  return { subtotal, shipping, tax, total };
};
