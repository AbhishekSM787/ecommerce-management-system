import { useEffect, useMemo, useState } from 'react'
import { getProducts, placeOrder } from './api'

const money = value => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(value)

export default function App() {
  const [products, setProducts] = useState([])
  const [cart, setCart] = useState({})
  const [search, setSearch] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [customerName, setCustomerName] = useState('')
  const [customerEmail, setCustomerEmail] = useState('')
  const [message, setMessage] = useState('')

  async function loadProducts(term = '') {
    setLoading(true); setError('')
    try { setProducts(await getProducts(term)) }
    catch (err) { setError(err.message) }
    finally { setLoading(false) }
  }
  useEffect(() => { loadProducts() }, [])

  const cartItems = useMemo(() => Object.entries(cart).map(([id, quantity]) => {
    const product = products.find(item => item.id === Number(id))
    return product ? { ...product, quantity } : null
  }).filter(Boolean), [cart, products])
  const total = cartItems.reduce((sum, item) => sum + Number(item.price) * item.quantity, 0)
  const count = cartItems.reduce((sum, item) => sum + item.quantity, 0)

  function changeQuantity(id, delta) {
    setCart(current => {
      const next = { ...current }
      const quantity = (next[id] || 0) + delta
      if (quantity <= 0) delete next[id]
      else next[id] = quantity
      return next
    })
    setMessage('')
  }

  async function checkout(event) {
    event.preventDefault(); setMessage('')
    if (!cartItems.length) return setMessage('Add at least one product to your cart.')
    try {
      const result = await placeOrder({
        customerName, customerEmail,
        items: cartItems.map(item => ({ productId: item.id, quantity: item.quantity }))
      })
      setMessage(`Order #${result.orderId} placed successfully. Total: ${money(result.total)}. This is a demo order; no payment was taken.`)
      setCart({}); setCustomerName(''); setCustomerEmail('')
    } catch (err) { setMessage(err.message) }
  }

  return <div className="page">
    <header className="topbar"><a className="brand" href="#">ShopEase<span>.</span></a><span className="tagline">Simple shopping, made easy</span><a className="cart-link" href="#cart">Cart ({count})</a></header>
    <main>
      <section className="hero"><div><p className="eyebrow">WELCOME TO SHOPEASE</p><h1>Find your everyday<br/>essentials.</h1><p className="hero-copy">A simple demo store built with React and Spring Boot.</p></div><div className="hero-mark">S<span>.</span></div></section>
      <div className="section-head"><div><p className="eyebrow">OUR COLLECTION</p><h2>Featured products</h2></div><form className="search" onSubmit={e => { e.preventDefault(); loadProducts(search) }}><input value={search} onChange={e => setSearch(e.target.value)} placeholder="Search products..." aria-label="Search products"/><button>Search</button></form></div>
      {error && <p className="notice error">{error}</p>}
      {loading ? <p className="muted">Loading products...</p> : <div className="products">
        {products.map(product => <article className="product" key={product.id}><div className="product-image"><img src={product.imageUrl} alt={product.name} onError={e => { e.currentTarget.src = 'https://placehold.co/600x420?text=Product' }}/></div><div className="product-info"><span className="category">{product.category}</span><h3>{product.name}</h3><p>{product.description}</p><div className="product-bottom"><strong>{money(product.price)}</strong><button onClick={() => changeQuantity(product.id, 1)}>Add to cart</button></div><small>{product.stock} in stock</small></div></article>)}
        {!products.length && <p className="muted">No products found. Try another search.</p>}
      </div>}
      <section className="cart-section" id="cart"><div className="section-head"><div><p className="eyebrow">YOUR SELECTION</p><h2>Shopping cart</h2></div><span className="muted">{count} item(s)</span></div>
        <div className="cart-layout"><div className="cart-list">
          {!cartItems.length ? <p className="empty">Your cart is empty. Add a product to get started.</p> : cartItems.map(item => <div className="cart-row" key={item.id}><div><strong>{item.name}</strong><small>{money(item.price)} each</small></div><div className="quantity"><button onClick={() => changeQuantity(item.id, -1)} aria-label="Decrease quantity">−</button><span>{item.quantity}</span><button onClick={() => changeQuantity(item.id, 1)} aria-label="Increase quantity">+</button></div><strong>{money(Number(item.price) * item.quantity)}</strong></div>)}
          <div className="total"><span>Order total</span><strong>{money(total)}</strong></div>
        </div>
        <form className="checkout" onSubmit={checkout}><h3>Demo checkout</h3><p>Enter your details to place a sample order.</p><label>Full name<input required value={customerName} onChange={e => setCustomerName(e.target.value)} placeholder="Your name"/></label><label>Email address<input required type="email" value={customerEmail} onChange={e => setCustomerEmail(e.target.value)} placeholder="you@example.com"/></label><button className="checkout-btn" disabled={!count}>Place demo order</button>{message && <p className="notice">{message}</p>}</form></div>
      </section>
    </main>
    <footer>ShopEase · Student portfolio project · Demo store only</footer>
  </div>
}
