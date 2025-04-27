# Use a base image that has Chrome + full JDK
FROM seleniarm/standalone-chromium:latest

# Switch to root user
USER root

# First fix any potentially broken dependencies
RUN apt-get update && apt-get -y --fix-broken install

# Install GTK and ATK dependencies first
RUN apt-get update && apt-get install -y \
    libgtk-3-0 \
    libatk1.0-0 \
    libcups2 \
    libasound2 \
    libatk-bridge2.0-0 \
    && apt-get clean

# Now install Java and development tools
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    gradle \
    && apt-get clean

# Install remaining utilities
RUN apt-get update && apt-get install -y \
    curl \
    unzip \
    zip \
    libx11-dev \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    libnss3 \
    libgdk-pixbuf2.0-0 \
    libxss1 \
    libnss3-dev \
    fonts-liberation \
    xdg-utils \
    && apt-get clean

# Specifically avoid libappindicator3-1 which is causing issues
# Use libayatana-appindicator3-1 instead if needed
# RUN apt-get update && apt-get install -y libayatana-appindicator3-1 && apt-get clean

# Set JAVA_HOME explicitly (important for Gradle and Kotlin builds)
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
ENV PATH=$JAVA_HOME/bin:$PATH

# Set working directory
WORKDIR /app

# Copy project files into the container
COPY . .

# Make sure gradlew has executable permissions
RUN chmod +x ./gradlew

# Default command to run tests
CMD ["./gradlew", "clean", "test"]