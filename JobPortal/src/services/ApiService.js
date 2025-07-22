import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8888',
  withCredentials: true,
})

let isRefreshing = false
let failedQueue = []

const processQueue = (error = null) => {
  failedQueue.forEach(prom => {
    if (error) prom.reject(error)
    else prom.resolve()
  })
  failedQueue = []
}


api.interceptors.response.use(
  res => res,
  async err => {
    const originalRequest = err.config

    if (err.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject })
        }).then(() => api(originalRequest))
      }

      isRefreshing = true

      try {
        await api.post('/auth/refresh-token') 
        processQueue(null)
        return api(originalRequest)
      } catch (refreshErr) {
        processQueue(refreshErr)
        //window.location.href = '/userLogin'
        return Promise.reject(refreshErr)
      } finally {
        isRefreshing = false
      }
    }

    return Promise.reject(err)
  }
)

export default {
  get: (url, config = {}) => api.get(url, config),
  post: (url, data, config = {}) => api.post(url, data, config),
  put: (url, data, config = {}) => api.put(url, data, config),
  delete: (url, config = {}) => api.delete(url, config),
  patch: (url, data, config = {}) => api.patch(url, data, config),
}
