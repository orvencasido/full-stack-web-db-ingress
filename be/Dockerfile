FROM python:3.10-slim

# Install OS dependencies
RUN apt-get update && apt-get install -y \
    libaio-dev unzip wget curl \
    && rm -rf /var/lib/apt/lists/*

# Fix libaio.so.1 missing for Oracle Instant Client
RUN ln -s /usr/lib/x86_64-linux-gnu/libaio.so /usr/lib/x86_64-linux-gnu/libaio.so.1

# Install Oracle Instant Client
WORKDIR /opt/oracle
RUN wget https://download.oracle.com/otn_software/linux/instantclient/instantclient-basiclite-linuxx64.zip \
    && unzip instantclient-basiclite-linuxx64.zip \
    && rm instantclient-basiclite-linuxx64.zip

# Set environment variable for cx_Oracle
ENV LD_LIBRARY_PATH=/opt/oracle/instantclient_23_9:$LD_LIBRARY_PATH

# Copy app code
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY . .

# Run Flask app
CMD ["python", "app.py"]
