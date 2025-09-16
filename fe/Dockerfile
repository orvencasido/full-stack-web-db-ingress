# Use lightweight Nginx image
FROM nginx:alpine

# Remove default Nginx content
RUN rm -rf /usr/share/nginx/html/*

# Copy all frontend files (HTML, CSS, JS) into Nginx html directory
COPY . /usr/share/nginx/html/

# Expose port 80
EXPOSE 80

# Run Nginx in the foreground
CMD ["nginx", "-g", "daemon off;"]
