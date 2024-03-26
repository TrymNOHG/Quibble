mkdir backend/src/main/resources/certificates

cd backend/src/main/resources/certificates || exit

# Check if the file exists
if [ -f "keypair.pem" ]; then
  rm keypair.pem
fi

if [ -f "publicKey.pem" ]; then
  rm publicKey.pem
fi

if [ -f "privateKey.pem" ]; then
  rm privateKey.pem
fi

openssl genrsa -out keypair.pem 2048

openssl rsa -in keypair.pem -pubout -out publicKey.pem

openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out privateKey.pem