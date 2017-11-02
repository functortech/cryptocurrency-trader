curl \
  -X GET \
  --header "x-access-token:$1" \
  "http://localhost:8081/balance"

echo -ne '\n'