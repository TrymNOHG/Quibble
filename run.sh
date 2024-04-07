#!/bin/bash

# Start the database and redirect output to a log file
echo "Starting the database..."
docker-compose up db --build > db.log 2>&1 &

# Function to check the log file for a success message
check_db_started() {
  # Define a timeout in seconds
  local timeout=90
  local startTime=$(date +%s)

  while :; do
    # Check if the success message is in the log
    if grep -q "Database started successfully" db.log; then
      echo "Database has started."
      return 0
    fi
    # Check for timeout
    local currentTime=$(date +%s)
    if (( currentTime - startTime > timeout )); then
      echo "Timed out waiting for the database to start."
      return 1
    fi
    sleep 1
  done
}

# Wait for the database to initialize
echo "Waiting for the database to initialize..."
if check_db_started; then
  echo "Proceeding with backend startup."
else
  echo "Database did not start correctly. Check db.log for details."
  exit 1
fi

# Run the backend in the background
echo "Starting the backend..."
cd backend || exit
mvn spring-boot:run &

# Start the frontend
echo "Starting the frontend..."
cd ../frontend || exit
npm install
npm run dev