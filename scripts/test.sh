#!/bin/bash
set -ex

ELASTIC_HOST=127.0.0.1

curl -H "Content-Type: application/json" -XPOST "http://localhost:9200/rss_store/item/optionalUniqueId" -d @test.json
