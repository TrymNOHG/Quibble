sh ./backend/generate-keys.sh
docker-compose up --build -d
cd frontend || exit
npm install
npm run dev