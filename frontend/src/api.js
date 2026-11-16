const API = 'http://localhost:8080/api'
export async function getProducts(search = '') {
  const url = new URL(`${API}/products`)
  if (search.trim()) url.searchParams.set('search', search.trim())
  const response = await fetch(url)
  if (!response.ok) throw new Error('Could not load products. Is the backend running?')
  return response.json()
}
export async function placeOrder(payload) {
  const response = await fetch(`${API}/orders`, {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload)
  })
  const data = await response.json()
  if (!response.ok) throw new Error(data.error || 'Unable to place order')
  return data
}
