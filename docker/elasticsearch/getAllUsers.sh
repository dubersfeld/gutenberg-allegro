#!/bin/bash

curl -X GET "localhost:9200/gutenberg-users/_search?pretty" -H 'Content-Type: application/json' -d'
{
    "query": {
        "match_all": {}
    }
}
'


