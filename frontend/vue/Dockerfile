# 階段一：使用 Node.js 映像檔來建置應用程式
FROM node:16 as builder

# 設定工作目錄
WORKDIR /app

# 複製 package.json 和 package-lock.json
COPY package*.json ./

# 安裝所有依賴
RUN npm install

# 複製專案原始碼
COPY . .

# 執行 npm run build 來建置生產環境的靜態檔案
RUN npm run build

# 階段二：使用 Nginx 映像檔來提供靜態檔案服務
FROM nginx:alpine

# 複製 Nginx 預設設定檔
COPY --from=builder /app/dist /usr/share/nginx/html

# 複製自定義的 Nginx 設定檔 (可選)
# COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# 暴露 Nginx 預設埠
EXPOSE 80

# 啟動 Nginx 服務
CMD ["nginx", "-g", "daemon off;"]
