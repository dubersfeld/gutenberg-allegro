#!/bin/bash

ADDRESS=$1

if [ -z $ADDRESS ]; then
  ADDRESS="localhost:9200"
fi

# Check that Elasticsearch is running
curl -s "http://$ADDRESS" 2>&1 > /dev/null
if [ $? != 0 ]; then
    echo "Unable to contact Elasticsearch at $ADDRESS"
    echo "Please ensure Elasticsearch is running and can be reached at http://$ADDRESS/"
    exit -1
fi

echo "WARNING, this script will delete the 'gutenberg-categories' index and re-index all data!"
echo "Press Control-C to cancel this operation."
echo
echo "Press [Enter] to continue."
read

echo $(dirname $0)

# Delete the old index, swallow failures if it doesn't exist
curl -s -XDELETE "$ADDRESS/gutenberg-categories" > /dev/null

# Create the new index using init.json
echo "Creating 'gutenberg-categories' index..."
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-categories" -d@$(dirname $0)/initCategories.json

# Wait for index to become yellow
curl -s "$ADDRESS/gutenberg-categories/_health?wait_for_status=yellow&timeout=10s" > /dev/null
echo
echo "Done creating 'gutenberg-categories' index."


echo
echo "Indexing data..."


echo
echo "Indexing data..."

echo "Indexing categories..."
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-categories/_doc/1" -d'{
  "slug": "books",
  "name": "Books",
  "description": "All books",
  "children": ["2", "3", "5" ],
  "ancestors": [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-categories/_doc/2" -d'{
  "slug": "textbooks",
  "name": "Textbooks",
  "description" : "Textbooks for professionals",
  "parentId": "1",
  "children": [ "4" ],
  "ancestors": [
  {
    "name": "Books",
    "id": "1",
    "slug": "books"
  }
]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-categories/_doc/3" -d'{
  "slug": "biographies",
  "name": "Biographies",
  "description": "All about the life of famous people",
  "parentId": "1",
  "children": [ ],
  "ancestors": [
    {
      "name": "Books",
      "id": "1",
      "slug": "books"
    }
  ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-categories/_doc/4" -d'{
  "slug" : "computer-science",
  "name" : "Computer science",
  "description" : "Latest trends in computer science",
  "parentId" : "2",
  "children" : [ ],
  "ancestors" : [
    {
      "name" : "Textbooks",
      "id" : "2",
      "slug" : "textbooks"
    },
    {
      "name" : "Books",
      "id" : "1",
      "slug" : "books"
    }
  ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-categories/_doc/5" -d'{
  "slug" : "fiction",
  "name" : "Fiction",
  "description" : "Most popular novels.",
  "parentId" : "1",
  "children" : [ ],
  "ancestors" : [
    {
      "name" : "Books",
      "id" : "1",
      "slug" : "books"
    }
  ]
}'

echo
echo

# Refresh so data is available
curl -s -XGET "$ADDRESS/gutenberg-categories/_refresh"

echo
echo
