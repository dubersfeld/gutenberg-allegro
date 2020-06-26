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

echo "WARNING, this script will delete the 'gutenberg-reviews' index and re-index all data!"
echo "Press Control-C to cancel this operation."
echo
echo "Press [Enter] to continue."
read

echo $(dirname $0)

# Delete the old index, swallow failures if it doesn't exist
curl -s -XDELETE "$ADDRESS/gutenberg-reviews" > /dev/null

# Create the new index using init.json
echo "Creating 'gutenberg-reviews' index..."
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews" -d@$(dirname $0)/initReviews.json

# Wait for index to become yellow
curl -s "$ADDRESS/gutenberg-reviews/_health?wait_for_status=yellow&timeout=10s" > /dev/null
echo
echo "Done creating 'gutenberg-reviews' index."


echo
echo "Indexing data..."


curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/1" -d'{
  "bookId": "1",
  "text" : "The most comprehensive source for everything CHEVAL HareFAQ.",
  "rating" : 5.0,
  "userId" : "6",
  "helpfulVotes" : 10,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/2" -d'{
  "bookId": "2",
  "text" : "Malware for beginners is a great primer on malware and what you can do to protect yourself and your organisation from it.",
  "rating" : 4.0,
  "userId" : "7",
  "helpfulVotes" : 9,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/3" -d'{
  "bookId": "3",
  "text" : "Nivü Nikkonü has assembled a comprehensive reference manual for Emerald. It is a treasure map that everyone will want to use.",
  "rating" : 5.0,
  "userId" : "8",
  "helpfulVotes" : 8,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/4" -d'{
  "bookId" : "4",
  "text" : "Donkey is fast becoming THE framework for microservices--This book shows you why and how.",
  "rating" : 4.0,
  "userId" : "9",
  "helpfulVotes" : 7,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/5" -d'{
  "bookId" : "5",
  "text" : "A thorough manual for learning, practicing, and implementing TangoDB.",
  "rating" : 5.0,
  "userId" : "10",
  "helpfulVotes" : 6,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/6" -d'{
  "bookId" : "6",
    "text" : "I recommend this book to all history of science majors.",
    "rating" : 3.0,
    "userId" : "6",
    "helpfulVotes" : 10,
    "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/7" -d'{
  "bookId" : "9",
  "text" : "Thanks to this book I don'\''t need sleeping pills anymore. It is so boring that I fall asleep after reading two pages.",
  "rating" : 2.0,
  "userId" : "7",
  "helpfulVotes" : 9,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/8" -d'{
  "bookId" : "10",
  "text" : "I was surprised to read that Nostradamus was a doctor. Moreover this book gives many interesting details about everyday life in France in XVIth century.",
  "rating" : 4.0,
  "userId" : "8",
  "helpfulVotes" : 8,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/9" -d'{
  "bookId" : "7",
  "text" : "This is the long awaited biography of the most controversial scientist of XXth century.",
  "rating" : 5.0,
  "userId" : "9",
  "helpfulVotes" : 7,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/10" -d'{
  "bookId" : "7",
  "text" : "This book left me deeply frustrated. After thoroughly reading it twice, I still don'\''t know which cat breed the Schroedinger Cat was!",
  "rating" : 1.0,
  "userId" : "8",
  "helpfulVotes" : 6,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/11" -d'{
  "bookId" : "7",
  "text" : "I was really surprised that Heisenberg chose such a desolate place as Helgoland for his spring break.",
  "rating" : 2.0,
  "userId" : "7",
  "helpfulVotes" : 10,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/12" -d'{
  "bookId" : "8",
  "text" : "I had a dinner in a Bateau-Mouche during my honeymoon in Paris a long time ago. Reading this book made me feel young again!",
  "rating" : 5.0,
  "userId" : "10",
  "helpfulVotes" : 9,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/13" -d'{
  "bookId" : "11",
  "text" : "Bedlam at his best. This is really a page-turner.",
  "rating" : 5.0,
  "userId" : "6",
  "helpfulVotes" : 8,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/14" -d'{
  "bookId" : "12",
    "text" : "An incredible exploration of the effect of virtual reality on human behavior.",
    "rating" : 5.0,
    "userId" : "6",
    "helpfulVotes" : 7,
    "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/15" -d'{
  "bookId" : "13",
  "text" : "This book shows that philosophy is not an abstract concept. It is the very foundation of social interactions.",
  "rating" : 4.0,
  "userId" : "8",
  "helpfulVotes" : 6,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/16" -d'{
  "bookId" : "14",
    "text" : "Really entertaining book. Now I hope that one of Hollywood Eight will adapt it into a movie!",
    "rating" : 5.0,
    "userId" : "9",
    "helpfulVotes" : 10,
    "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/17" -d'{
  "bookId" : "15",
  "text" : "Reading this book made me think of some novels by John Steinbeck. Here the gorillas are more human than some human characters!",
  "rating" : 4.0,
  "userId" : "10",
  "helpfulVotes" : 9,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/18" -d'{
  "bookId" : "15",
  "text" : "After reading this book I feel like a privileged citizen!",
  "rating" : 4.0,
  "userId" : "6",
  "helpfulVotes" : 9,
  "voterIds" : [ ]
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-reviews/_doc/19" -d'{
  "bookId" : "15",
  "text" : "After reading this book I prefer gorillas to my neighbors!",
  "rating" : 4.0,
  "userId" : "7",
  "helpfulVotes" : 9,
  "voterIds" : [ ]
}'





echo
echo
# Refresh so data is available
curl -s -XGET "$ADDRESS/gutenberg-reviews/_refresh"

echo

