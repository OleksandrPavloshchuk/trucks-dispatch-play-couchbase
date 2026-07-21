#!/usr/bin/env bash

JSON=$(printf '{"name":"%s","capacity":%f}' "$1" "$2")

curl -v \
   -X POST \
  -H "Content-Type: application/json" \
  -d $JSON \
  http://localhost:9120/td/truck