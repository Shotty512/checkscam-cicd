# syntax=docker/dockerfile:1.7

# ============================================================
# Stage 1: Build Angular application
# ============================================================
FROM node:22-alpine AS builder

WORKDIR /app

# Keep npm cache outside node_modules so Docker can reuse it
# between builds.
ENV npm_config_cache=/root/.npm

# Copy dependency manifests first.
# This layer is invalidated only when dependencies change.
COPY package.json package-lock.json ./

# Install exactly the versions recorded in package-lock.json.
# BuildKit cache avoids downloading packages repeatedly.
RUN npm ci --no-audit --no-fund --legacy-peer-deps
# Copy application source only after dependencies are installed.
COPY . .

# Production build.
RUN npm run build

# ============================================================
# Stage 2: Production web server
# ============================================================
FROM nginx:stable-alpine AS runtime

# Remove default Nginx content.
RUN rm -rf /usr/share/nginx/html/*

# Copy only the compiled Angular application.
COPY --from=builder /app/dist/checkscam-admin/browser /usr/share/nginx/html

# SPA routing configuration.
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Nginx listens on HTTP port 80.
EXPOSE 80

# Run Nginx in foreground so Docker can manage the process.
CMD ["nginx", "-g", "daemon off;"]
