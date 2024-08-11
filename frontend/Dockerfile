# Stage 1: Build the Angular application
FROM node:alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

# Stage 2: Serve the Angular application
FROM nginx:stable-perl

COPY default.conf /etc/nginx/conf.d

COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80