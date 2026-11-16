import { useEffect, useMemo, useState } from 'react'
import * as api from './api'

const money = value => new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(Number(value || 0))
const blankProduct = { name:'', description:'', price:'', category:'Electronics', stock:'0', imageUrl:'' }

export default function App() {
  const [products,setProducts]=useState([]), [cart,setCart]=useState({})
  const [search,setSearch]=useState(''), [loading,setLoading]=useState(true), [error,setError]=useState('')
  const [user,setUser]=useState(()=>JSON.parse(localStorage.getItem('shopease_user')||'null'))
  const [authMode,setAuthMode]=useState('login'), [authOpen,setAuthOpen]=useState(false)
  const [auth,setAuth]=useState({name:'',email:'',password:''}), [notice,setNotice]=useState('')
  const [view,setView]=useState('shop'), [orders,setOrders]=useState([])
  const [stats,setStats]=useState(null), [adminProducts,setAdminProducts]=useState([])
  const [editing,setEditing]=useState(null), [productForm,setProductForm]=useState(blankProduct)

  async function loadProducts(term=''){setLoading(true);setError('');try{setProducts(await api.getProducts(term))}catch(e){setError(e.message)}finally{setLoading(false)}}
  useEffect(()=>{loadProducts()},[])
  const cartItems=useMemo(()=>Object.entries(cart).map(([id,quantity])=>{const p=products.find(x=>x.id===Number(id));return p?{...p,quantity}:null}).filter(Boolean),[cart,products])
  const total=cartItems.reduce((sum,p)=>sum+Number(p.price)*p.quantity,0)
  const count=cartItems.reduce((sum,p)=>sum+p.quantity,0)

  function changeQty(id,delta){setCart(old=>{const next={...old},q=(next[id]||0)+delta;if(q<=0)delete next[id];else next[id]=q;return next})}
  function logout(){localStorage.removeItem('shopease_token');localStorage.removeItem('shopease_user');setUser(null);setView('shop');setNotice('You have been signed out.')}
  async function submitAuth(e){e.preventDefault();setNotice('');try{
    if(authMode==='register'){await api.register(auth);setNotice('Account created. Please sign in.');setAuthMode('login');return}
    const result=await api.login({email:auth.email,password:auth.password})
    localStorage.setItem('shopease_token',result.token)
    const profile={id:result.id,name:result.name,email:result.email,role:result.role}
    localStorage.setItem('shopease_user',JSON.stringify(profile));setUser(profile);setAuthOpen(false);setAuth({name:'',email:'',password:''});setNotice(`Welcome, ${result.name}!`)
  }catch(e){setNotice(e.message)}}
  async function checkout(){if(!user){setAuthMode('login');setAuthOpen(true);setNotice('Please sign in before placing an order.');return}
    try{const order=await api.placeOrder(cartItems.map(p=>({productId:p.id,quantity:p.quantity})));setCart({});setNotice(`Order #${order.orderId} placed. Total: ${money(order.total)}.`);loadProducts()}catch(e){setNotice(e.message)}}
  async function openAdmin(){try{const [s,p,o]=await Promise.all([api.getAdminStats(),api.getAdminProducts(),api.getAdminOrders()]);setStats(s);setAdminProducts(p);setOrders(o);setView('admin')}catch(e){setNotice(e.message)}}
  async function refreshAdmin(){const [s,p,o]=await Promise.all([api.getAdminStats(),api.getAdminProducts(),api.getAdminOrders()]);setStats(s);setAdminProducts(p);setOrders(o)}
  async function saveProduct(e){e.preventDefault();try{const payload={...productForm,price:Number(productForm.price),stock:Number(productForm.stock)};await api.saveAdminProduct(payload,editing);setNotice('Product saved.');setEditing(null);setProductForm(blankProduct);await refreshAdmin();await loadProducts()}catch(e){setNotice(e.message)}}
  async function removeProduct(id){if(!confirm('Delete this product?'))return;try{await api.deleteAdminProduct(id);setNotice('Product deleted.');await refreshAdmin();await loadProducts()}catch(e){setNotice(e.message)}}
  async function changeStatus(id,status){try{await api.updateOrderStatus(id,status);await refreshAdmin();setNotice('Order status updated.')}catch(e){setNotice(e.message)}}

  return <div className="app">
    <header className="nav"><a className="brand" href="#" onClick={()=>setView('shop')}>ShopEase<span>.</span></a><nav><button className={view==='shop'?'active':''} onClick={()=>setView('shop')}>Shop</button>{user&&<button onClick={async()=>{try{setOrders(await api.getMyOrders());setView('myorders')}catch(e){setNotice(e.message)}}}>My orders</button>}{user?.role==='ADMIN'&&<button onClick={openAdmin}>Admin dashboard</button>}</nav><div className="nav-right">{user?<><span className="welcome">Hi, {user.name}</span><button className="outline" onClick={logout}>Sign out</button></>:<button className="dark" onClick={()=>{setAuthMode('login');setAuthOpen(true)}}>Sign in</button>}<a className="cart-pill" href="#cart">Cart ({count})</a></div></header>
    <main>
      {view==='shop'&&<><section className="hero"><div><p className="eyebrow">THE EVERYDAY STORE</p><h1>Good things.<br/>Great finds.</h1><p>Discover useful essentials for work, home and life.</p></div><div className="hero-art">S<span>.</span></div></section>
        <div className="section-head"><div><p className="eyebrow">CURATED FOR YOU</p><h2>Shop products</h2></div><form className="search" onSubmit={e=>{e.preventDefault();loadProducts(search)}}><input value={search} onChange={e=>setSearch(e.target.value)} placeholder="Search products..."/><button className="dark">Search</button></form></div>
        {error&&<p className="alert">{error}</p>}{loading?<p className="muted">Loading products...</p>:<div className="products">{products.map(p=><article className="product" key={p.id}><img src={p.imageUrl||'https://placehold.co/600x400?text=Product'} onError={e=>e.currentTarget.src='https://placehold.co/600x400?text=Product'} alt={p.name}/><div className="product-body"><small>{p.category} · {p.stock} in stock</small><h3>{p.name}</h3><p>{p.description}</p><div className="product-bottom"><strong>{money(p.price)}</strong><button disabled={p.stock===0} onClick={()=>changeQty(p.id,1)}>Add to cart</button></div></div></article>)}</div>}
        <section id="cart" className="cart"><div className="section-head"><div><p className="eyebrow">READY WHEN YOU ARE</p><h2>Your cart</h2></div><span className="muted">{count} item(s)</span></div><div className="cart-panel">{!cartItems.length?<p className="muted">Your cart is empty. Add a product to get started.</p>:cartItems.map(p=><div className="cart-row" key={p.id}><span>{p.name}</span><div className="qty"><button onClick={()=>changeQty(p.id,-1)}>−</button>{p.quantity}<button onClick={()=>changeQty(p.id,1)}>+</button></div><strong>{money(p.price*p.quantity)}</strong></div>)}<div className="cart-total"><span>Total</span><strong>{money(total)}</strong></div><button className="checkout" disabled={!count} onClick={checkout}>Place order</button></div></section>
      </>}
      {view==='myorders'&&<section className="panel"><h2>My orders</h2>{orders.length?orders.map(o=><div className="order-row" key={o.orderId}><div><b>Order #{o.orderId}</b><small>{new Date(o.createdAt).toLocaleString()}</small></div><span>{o.status}</span><strong>{money(o.total)}</strong></div>):<p className="muted">You haven't placed any orders yet.</p>}</section>}
      {view==='admin'&&<section className="admin"><div className="section-head"><div><p className="eyebrow">STORE CONTROL</p><h2>Admin dashboard</h2></div><button className="dark" onClick={()=>{setEditing(null);setProductForm(blankProduct)}}>Add product</button></div>
        {stats&&<div className="stats">{[['Products',stats.products],['Customers',Math.max(0,stats.users-1)],['Orders',stats.orders],['Revenue',money(stats.revenue)]].map(([label,value])=><div className="stat" key={label}><small>{label}</small><b>{value}</b></div>)}</div>}
        <div className="admin-grid"><div className="panel"><h3>Manage products</h3>{adminProducts.map(p=><div className="admin-row" key={p.id}><div><b>{p.name}</b><small>{money(p.price)} · Stock {p.stock}</small></div><button className="text-btn" onClick={()=>{setEditing(p.id);setProductForm({name:p.name,description:p.description,price:p.price,category:p.category,stock:p.stock,imageUrl:p.imageUrl||''})}}>Edit</button><button className="text-btn danger" onClick={()=>removeProduct(p.id)}>Delete</button></div>)}</div>
        <div className="panel"><h3>{editing?'Edit product':'Add product'}</h3><form className="product-form" onSubmit={saveProduct}>{[['name','Product name'],['description','Description'],['price','Price (INR)'],['category','Category'],['stock','Stock'],['imageUrl','Image URL']].map(([key,label])=><label key={key}>{label}<input required={['name','description','price','category','stock'].includes(key)} type={['price','stock'].includes(key)?'number':'text'} step={key==='price'?'0.01':undefined} min={['price','stock'].includes(key)?'0':undefined} value={productForm[key]} onChange={e=>setProductForm({...productForm,[key]:e.target.value})}/></label>)}<div className="form-actions"><button className="dark">{editing?'Save changes':'Create product'}</button>{editing&&<button type="button" className="outline" onClick={()=>{setEditing(null);setProductForm(blankProduct)}}>Cancel</button>}</div></form></div></div>
        <div className="panel orders-panel"><h3>Recent orders</h3>{orders.length?orders.map(o=><div className="admin-row" key={o.orderId}><div><b>#{o.orderId} · {o.customerName}</b><small>{o.customerEmail} · {money(o.total)}</small></div><select value={o.status} onChange={e=>changeStatus(o.orderId,e.target.value)}>{['PLACED','PROCESSING','SHIPPED','DELIVERED','CANCELLED'].map(s=><option key={s}>{s}</option>)}</select></div>):<p className="muted">No orders yet.</p>}</div>
      </section>}
      {notice&&<div className="toast"><span>{notice}</span><button onClick={()=>setNotice('')}>×</button></div>}
    </main>
    {authOpen&&<div className="modal-backdrop" onClick={()=>setAuthOpen(false)}><form className="modal" onSubmit={submitAuth} onClick={e=>e.stopPropagation()}><button type="button" className="close" onClick={()=>setAuthOpen(false)}>×</button><p className="eyebrow">SHOPEASE ACCOUNT</p><h2>{authMode==='login'?'Welcome back':'Create your account'}</h2>{authMode==='register'&&<label>Full name<input required value={auth.name} onChange={e=>setAuth({...auth,name:e.target.value})}/></label>}<label>Email<input required type="email" value={auth.email} onChange={e=>setAuth({...auth,email:e.target.value})}/></label><label>Password<input required minLength="8" type="password" value={auth.password} onChange={e=>setAuth({...auth,password:e.target.value})}/></label><button className="dark full">{authMode==='login'?'Sign in':'Create account'}</button><p className="switch">{authMode==='login'?"Don't have an account?":"Already registered?"} <button type="button" onClick={()=>setAuthMode(authMode==='login'?'register':'login')}>{authMode==='login'?'Register':'Sign in'}</button></p></form></div>}
    <footer>ShopEase · Java Full Stack portfolio project · Demo checkout (no real payment)</footer>
  </div>
}
