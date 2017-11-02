curl \
  -X DELETE \
  --header "x-access-token:$1" \
  "http://localhost:8081/access"

echo -ne '\n'