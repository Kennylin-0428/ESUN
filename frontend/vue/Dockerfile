# ========== Build Stage ==========
FROM node:20-alpine AS builder
WORKDIR /app

# 先安裝依賴（利用快取）
COPY package*.json ./
RUN npm ci

# 複製原始碼並建置
COPY . .
ENV NODE_ENV=production
RUN npm run build  # 產物在 /app/dist

# ========== Runtime (Nginx) ==========
FROM nginx:1.27-alpine

# 放入我們的站台設定（含 /api 反向代理與 SPA fallback）
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 複製前端靜態檔
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
