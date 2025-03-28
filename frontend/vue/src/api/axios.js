import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api', 
  timeout: 5000, 
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
});

export default api;
