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

echo "WARNING, this script will delete the 'gutenberg-books' index and re-index all data!"
echo "Press Control-C to cancel this operation."
echo
echo "Press [Enter] to continue."
read

echo $(dirname $0)

# Delete the old index, swallow failures if it doesn't exist
curl -s -XDELETE "$ADDRESS/gutenberg-books" > /dev/null

# Create the new index using init.json
echo "Creating 'gutenberg-books' index..."
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books" -d@$(dirname $0)/initBooks.json

# Wait for index to become yellow
echo
echo "Waiting on $ADDRESS"
curl -s "$ADDRESS/gutenberg-books/_health?wait_for_status=yellow&timeout=10s" > /dev/null
echo
echo "Done creating 'gutenberg-books' index."


echo
echo "Indexing data..."

curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/1" -d'{
  "slug": "mess-harefaq-1542",
  "title": "Messaging with HareFAQ",
  "publisher": "Gutemberg",
  "authors": [
    "Paul Bunyan"
  ],
  "description" : "A new enterprise messaging implementation.",
  "price" : 2339,
  "categoryId" : "4",
  "tags" : [
    "java",
    "spring",
    "messaging"
  ],
  "ratings": [
    5
  ],
  "boughtWith": [
    {"bookId": "5", "quantity": 3}
  ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/2" -d'{
  "slug": "malware-begin-666",
  "title": "Malware for beginners",
  "publisher": "O'\''Rourke",
  "authors": [
     "Marc Dutroux",
     "George Besse"
  ],
  "description": "How to crash your entreprise servers.",
  "price": 3339,
  "categoryId": "4",
  "tags": [
    "malware",
    "system",
    "blackhat"
  ],
  "ratings" : [
    4
  ],
  "boughtWith": [
    {"bookId": "5", "quantity": 3}, 
    {"bookId": "10", "quantity": 8}
  ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/3" -d'{
  "slug": "emerald-ultimate-421",
  "title": "The Ultimate Emerald Reference",
  "publisher": "O'\''Rourke",
  "authors": [
    "Nivü Nikkonü"
  ],
  "description": "Much easier to master and more efficient than Ruby.",
  "price": 3539,
  "categoryId": "4",
  "tags": [
    "software",
    "web",
    "database"
  ],
  "ratings": [
    5
  ],
  "boughtWith": [
    {"bookId": "15", "quantity": 3}
  ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/4" -d'{
  "slug": "donkey-action-1234",
  "title": "Donkey in Action",
  "publisher": "Chebyshev",
  "authors": [
    "Grace Deveneux"
  ],
  "description": "An introduction to the newest microservice framework.",
  "price": 3839,
  "categoryId": "4",
  "tags": [
    "microservice",
    "web",
    "restful"
  ],
  "ratings": [
    4
  ],
 "boughtWith": [
   
  ]
}'

echo 
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/5" -d'{
  "slug": "tangodb-depth-5678",
  "title": "TangoDB in Depth",
  "publisher": "Notch",
  "authors": [
    "Norman Bates"
  ],
  "description": "A comprehensive introduction to the latest schemaless database.",
  "price" : 4339,
  "categoryId": "4",
  "tags": [
    "database",
    "schemaless",
    "nosql"
  ],
  "ratings": [
    5
  ],
  "boughtWith": [
    {"bookId": "1", "quantity": 4}, 
    {"bookId": "2", "quantity": 5}, 
    {"bookId": "15", "quantity": 6}
  ]
}'

echo
echo

curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/6" -d'{
  "slug": "jvonneumann-1945",
  "title": "The Incredible Life of John von Neumann",
  "publisher": "Grouble",
  "authors": [
    "Albert Schweitzer"
  ],
  "description": "A founding father of computer science.",
  "price": 4439,
  "categoryId": "3",
  "tags": [
    "computer",
    "system",
    "mathematics"
  ],
  "ratings" : [
    3
  ],
 "boughtWith": [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/7" -d'{
  "slug": "heisenberg-1923",
  "title": "Heisenberg, a Life in Uncertainty",
  "publisher": "Grouble",
  "authors": [
    "Isabel Spengler"
  ],
  "description": "A founding father of quantum physics. His entire life he had to cope with uncertainty and most probably was not awarded the Nobel prize.",
  "price": 4539,
  "categoryId": "3",
  "tags": [
    "biography",
    "science",
    "history"
  ],
  "ratings" : [
    5,
    1,
    2
  ],
  "boughtWith": [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/8" -d'{
  "slug": "jsmouche-1900",
  "title": "Jean-Sébastien Mouche, from Paris with Love",
  "publisher": "Grouble",
  "authors": [
    "André Malraux"
  ],
  "description": "He created the popular Bateaux-Mouche where visitors from around the world can enjoy a romantic dinner on the river Seine.",
  "price": 2939,
  "categoryId" : "3",
    "tags" : [
    "biography",
    "science",
    "history"
  ],
  "ratings" : [
    5
  ],
  "boughtWith": [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/9" -d'{
  "slug": "marbront-1902",
  "title": "Eleanor Brontë and the Blank Page Challenge",
  "publisher": "Spivakov",
  "authors": [
    "Hu Xiao-Mei"
  ],
  "description": "The only Brontë sister who never wrote anything.",
  "price": 2739,
  "categoryId": "3",
  "tags": [
    "biography",
    "literature",
    "women"
  ],
  "ratings": [
    2
  ],
  "boughtWith": [ ]
}'


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/10" -d'{
  "slug": "nostradamus-42",
  "title": "Nostradamus",
  "publisher": "Springfield",
  "authors": [
    "Helmut von Staubsauger"
  ],
  "description": "Everybody has heard of him, now it'\''s time to read about his true story.",
  "price": 4439,
  "categoryId": "3",
  "tags": [
    "biography",
    "literature",
    "medicine"
  ],
  "ratings" : [
    4
  ],
  "boughtWith": [
    {"bookId": "2", "quantity": 3}, 
    {"bookId": "15", "quantity": 3}
  ]
}'


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/11" -d'{
  "slug": "bourne-shell-1542",
  "title": "The Bourne Shell Legacy",
  "publisher": "MacNamara",
  "authors": [
    "Robert Bedlam"
  ],
  "description": "A nail-biting thriller featuring JSON Bourne.",
  "price": 4539,
  "categoryId": "5",
  "tags" : [
    "thriller",
    "crime",
    "spying"
  ],
  "ratings" : [
    5
  ],
  "boughtWith": [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/12" -d'{
  "slug": "raiders-pattern-3190",
  "title": "Raiders of the Lost Pattern",
  "publisher": "Atkinson-Wembley",
  "authors": [
    "Evert Edepamuur"
  ],
  "description": "Two geeks on the track of an elusive pattern that escaped the attention of the Gang of Four.",
  "price": 3639,
  "categoryId": "5",
  "tags": [
    "thriller",
    "crime",
    "software"
  ],
  "ratings": [
    5
  ],
  "boughtWith": [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/13" -d'{
  "slug": "dining-philosophers-1542",
  "title": "The Dining Philosophers",
  "publisher": "Dyson",
  "authors": [
    "Paul Enclume"
  ],
  "description": "Five philosophers decide to have a dinner together. They have to cope with a lack of forks and knives.",
  "price": 3839,
  "categoryId": "5",
  "tags": [
    "home",
    "life",
    "food"
  ],
  "ratings": [
    4
  ],
  "boughtWith": [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/14" -d'{
  "slug": "walking-planck-3141",
  "title": "Walking the Planck Constant",
  "publisher": "Hanning",
  "authors": [
    "Laetitia Haddad"
  ],
  "description": "A Caribbean pirate captain falls into a quantum entanglement trap. Only the Schroedinger Cat could rescue him but nobody knows whether the Cat is alive or not",
  "price": 5339,
  "categoryId": "5",
  "tags": [
    "piracy",
    "science-fiction",
    "gold"
  ],
  "ratings" : [
    5
  ],
  "boughtWith": [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-books/_doc/15" -d'{
  "slug": "apes-wrath-4153",
  "title": "Apes of Wrath",
  "publisher": "Butterworth",
  "authors": [
    "Boris Cyrulnik"
  ],
  "description": "A gorilla keeper in San Diego Zoo struggles to keep his job during the Great Depression.",
  "price": 6839,
  "categoryId": "5",
  "tags": [
    "apes",
    "life",
    "depression"
  ],
  "ratings": [
    4,
    4,
    4
  ],
  "boughtWith": [
    {"bookId": "5", "quantity": 3},
    {"bookId": "3", "quantity": 3},
    {"bookId": "10", "quantity": 3}
  ]
}'


echo
echo
# Refresh so data is available
curl -s -XGET "$ADDRESS/gutenberg-books/_refresh"

echo


