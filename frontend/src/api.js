const API = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
const getToken = () => localStorage.getItem('shopease_token')
async function request(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) }
  if (getToken()) headers.Authorization = `Bearer ${getToken()}`
  const response = await fetch(`${API}${path}`, { ...options, headers })
  const data = response.status === 204 ? null : await response.json().catch(() => ({}))
  if (!response.ok) throw new Error(data?.error || data?.message || `Request failed (${response.status})`)
  return data
}
export const getProducts = (search = '') => request(`/products${search ? `?search=${encodeURIComponent(search)}` : ''}`)
export const register = payload => request('/auth/register', { method: 'POST', body: JSON.stringify(payload) })
export const login = payload => request('/auth/login', { method: 'POST', body: JSON.stringify(payload) })
export const placeOrder = items => request('/orders', { method: 'POST', body: JSON.stringify({ items }) })
export const getMyOrders = () => request('/orders/my')
export const getAdminStats = () => request('/admin/stats')
export const getAdminProducts = () => request('/admin/products')
export const saveAdminProduct = (payload, id) => request(id ? `/admin/products/${id}` : '/admin/products', {
  method: id ? 'PUT' : 'POST', body: JSON.stringify(payload)
})
export const deleteAdminProduct = id => request(`/admin/products/${id}`, { method: 'DELETE' })
export const getAdminOrders = () => request('/admin/orders')
export const updateOrderStatus = (id, status) => request(`/admin/orders/${id}/status`, {
  method: 'PATCH', body: JSON.stringify({ status })
})
