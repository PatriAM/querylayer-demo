#!/bin/bash
set -ex

while ! [ $(curl --write-out %{http_code} --silent --output /dev/null http://$ELASTIC_HOST:9200/_cat/health?h=st) = 200 ]; do
	echo "elasticsearch not up"
	sleep 1
done


echo "add mapping to elasticsearch"
curl -XDELETE "$ELASTIC_HOST:9200/rss_store"
curl -XPUT "$ELASTIC_HOST:9200/rss_store" -d @mapping_item.json

