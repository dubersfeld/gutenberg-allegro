#!/bin/bash

curl -X POST "localhost:9200/gutenberg-reviews/_search?size=50&version=true&pretty" -H 'Content-Type: application/json' -d'
{
    "query": {
        "match_all": {}
    }
}
'



