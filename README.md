# 📚 ESUN INTERVIEW

---

## 📝 專案簡介
`ESUN INTERVIEW` 專案是一個座位管理系統，主要功能包括：
- 查看座位分佈情況
- 分配座位給員工
- 清空座位並釋放佔用

該系統使用 **Vue 3 + Axios** 作為前端框架，後端採用 **Java Spring Boot + MySQL** 提供 API 和資料庫服務。

---

## ⚡️ 環境要求

- Node.js >= 16.x
- npm >= 8.x
- java >= 17.x
- mysql >= 8.x
- maven >= 4.x

---

## 📥 安裝步驟

### 1. Clone 專案
```bash
git clone https://github.com/Kennylin-0428/ESUN.git

cd ESUN

```

### 2. 執行DB目錄SQL

### 3. 啟動後端API服務
```bash
mvn clean package

cd target

java -jar demo-0.0.1-SNAPSHOT.jar
```
### 4. 啟動前端
```bash
cd frontend/vue

npm install

npm run dev



