curl \
  -X DELETE \
  --header "x-access-token:$1" \
  "http://localhost:8081/order/$2"

echo -ne '\n'
