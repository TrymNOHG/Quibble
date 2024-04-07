# build stage
FROM node:lts-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

EXPOSE 80
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]


## production stage
#FROM nginx:stable-alpine as production-stage
#COPY --from=build-stage /app/dist /usr/share/nginx/html
#COPY nginx.conf /etc/nginx/nginx.conf
#
#EXPOSE 80
#CMD ["nginx", "-g", "daemon off;"]

#
#FROM node:lts-alpine as production-stage
#COPY --from=build-stage /app/dist /app
#RUN npm install -g serve
#
#EXPOSE 80
#CMD ["serve", "-s", "/app", "-v"]
