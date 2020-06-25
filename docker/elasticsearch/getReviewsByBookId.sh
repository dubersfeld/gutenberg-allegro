#!/bin/bash

curl -X POST "localhost:40000/gutenberg-reviews/_search?version=true&pretty" -H 'Content-Type: application/json' -d'
{
    "query": {
        "match_all": { "bookId" : "3"}
    }
}
'



