import axios from 'axios';

const api = axios.create({
  baseURL: '/api', // ★ 改成同源路徑，交給 Nginx 代理到 backend
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
});

export default api;
