curl \
  -X POST \
  -d "{\"login\":\"$1\",\"password\":\"$2\"}" \
  "http://localhost:8081/access"

echo -ne '\n'