#!/bin/bash

curl -X GET "localhost:9200/gutenberg-books/_search?size=50&pretty" -H 'Content-Type: application/json' -d'
{
    "query": {
        "match_all": {}
    }
}
'


