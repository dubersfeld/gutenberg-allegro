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

echo "WARNING, this script will delete the 'gutenberg-users' index and re-index all data!"
echo "Press Control-C to cancel this operation."
echo
echo "Press [Enter] to continue."
read

echo $(dirname $0)

# Delete the old index, swallow failures if it doesn't exist
curl -s -XDELETE "$ADDRESS/gutenberg-users" > /dev/null

# Create the new index using init.json
echo "Creating 'gutenberg-users' index..."
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users" -d@$(dirname $0)/initUsers.json

# Wait for index to become yellow
curl -s "$ADDRESS/gutenberg-users/_health?wait_for_status=yellow&timeout=10s" > /dev/null
echo
echo "Done creating 'gutenberg-users' index."


echo
echo "Indexing data..."

curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/1" -d'{
  "username": "Carol",
  "hashedPassword": "{bcrypt}$2a$10$IbWmP8ViaXfNcaKbSfEkyOKDWGzIgLJ90oJ43nphlYEYzomlhOaSm",
  "accountNonExpired": true,
  "accountNonLocked": true,
  "credentialsNonExpired": true,
  "enabled": true,
  "authorities": [ {
      "authority": "ROLE_USER"
    }
  ],
  "addresses": [ {
      "street": "Main Street 2233",
      "city": "Dallas",
      "zip": "75215",
      "state": "TX",
      "country": "USA"
    }
  ],
  "paymentMethods": [ {
      "cardNumber": "1234567813572468",
      "name": "Carol Baker"
    }
  ],
  "mainPayMeth": 0,
  "mainShippingAddress": 0
}'


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/2" -d'{
  "username": "Albert",
  "hashedPassword": "{bcrypt}$2a$10$2amyAvl.aoVidnbd/8AUWOl7LDqkcWHZF/z29WFd6jQ6ZR78sffCi",
  "accountNonExpired": true,
  "accountNonLocked": true,
  "credentialsNonExpired": true,
  "enabled": true,
  "authorities": [ {
      "authority":  "ROLE_USER"
    }
  ],
  "addresses": [ {
    "street": "Plumber Street 22",
    "city": "London",
    "zip": "WC2N 5DU",
    "state": "",
    "country": "UK"
    }
  ],
  "paymentMethods": [ {
      "cardNumber": "1357246813572468",
      "name": "Albert Degroot"
    }
  ],
  "mainPayMeth": 0,
  "mainShippingAddress": 0
}'


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/3" -d'{
  "username": "Werner",
  "hashedPassword": "{bcrypt}$2a$10$4D/68H.9Yesn32FldAQs2OVpF2AFsjosjxIiNx0hDX./3FOvYmZGa",
  "accountNonExpired": true,
  "accountNonLocked": true,
  "credentialsNonExpired": true,
  "enabled": true,
  "authorities": [ {
      "authority":  "ROLE_USER"
    }
  ],
  "addresses": [ {
      "street": "Hannoverstr. 22",
      "city": "Berlin",
      "zip": "10315",
      "state": "",
      "country": "DE"
    }
  ],
  "paymentMethods": [ {
      "cardNumber": "4321987643219876",
      "name": "Werner Stolz"
    }
  ],
  "mainPayMeth": 0,
  "mainShippingAddress": 0
}'


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/4" -d'{
  "username": "Alice",
  "hashedPassword": "{bcrypt}$2a$10$Ip8KBSorI9R39m.KQBk3nu/WhjekgPSmfmpnmnf5yCL3aL9y.ITVW",
  "accountNonExpired": true,
  "accountNonLocked": true,
  "credentialsNonExpired": true,
  "enabled": true,
  "authorities": [ {
      "authority":  "ROLE_USER"
    }
  ],
  "addresses": [ {
      "street": "42 rue Amélie Poulain",
      "city": "Paris",
      "zip": "75018",
      "state": "",
      "country": "FR"
    }, {
      "street": "42 rue Amélie Nothomb",
      "city": "Paris",
      "zip": "75018",
      "state": "",
      "country": "FR"
    }
  ],
  "paymentMethods": [ {
      "cardNumber": "6789432167894321",
            "name": "Alice Carrol"
    }, {
      "cardNumber": "6789432167891234",
      "name": "Alice Carrol"
    }
  ],
  "mainPayMeth": 0,
  "mainShippingAddress": 0
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/5" -d'{
  "username": "Richard",
  "hashedPassword": "{bcrypt}$2a$10$LBMYa2KlGqKyjUg6UPSx8.SqV99/mWTryFwl1sY4.x0UKKqnab9ru",
  "accountNonExpired": true,
  "accountNonLocked": true,
  "credentialsNonExpired": true,
  "enabled": true,
  "authorities": [ {
      "authority":  "ROLE_USER"
    }
  ],
  "addresses": [ {
      "street": "Avenue de la Gare 55",
      "city": "Lausanne",
      "zip": "1022",
      "state": "",
      "country": "CH"
    }
  ],
  "paymentMethods": [ {
      "cardNumber": "9876123498761234",
      "name": "Richard Brunner"
    }, {
      "cardNumber": "9876123498761235",
      "name": "Richard Brenner"
    }
  ],
  "mainPayMeth": 0,
  "mainShippingAddress": 0
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/6" -d'{
  "username" : "Sator",
  "hashedPassword" : "{bcrypt}$2a$10$bMiqHtOacryfa90U9ddokelGe3xlEmVVuZ1UDre3ArINmspjjsIGC",
  "accountNonExpired" : true,
  "accountNonLocked" : true,
  "credentialsNonExpired" : true,
  "enabled" : true,
  "authorities" : [{"authority"  :  "ROLE_USER"}],
  "mainPayMeth" : 0,
  "mainShippingAddress" : 0
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/7" -d'{
  "username" : "Arepo",
  "hashedPassword" : "{bcrypt}$2a$10$5YYBC/bq85.z8.cUbDzi5euUotdU1kHgqsuDGAiDNByiXMG3zEoMi",
  "accountNonExpired" : true,
  "accountNonLocked" : true,
  "credentialsNonExpired" : true,
  "enabled" : true,
  "authorities" : [{"authority"  :  "ROLE_USER"}],
  "mainPayMeth" : 0,
  "mainShippingAddress" : 0
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/8" -d'{
  "username" : "Tenet",
  "hashedPassword" : "{bcrypt}$2a$10$Dr0/hk5Zy8xilHh.fdoc.OTsT/sDMraT1i4IMVpW39pzbu2w6ZVMC",
  "accountNonExpired" : true,
  "accountNonLocked" : true,
  "credentialsNonExpired" : true,
  "enabled" : true,
  "authorities" : [{"authority"  :  "ROLE_USER"}],
  "mainPayMeth" : 0,
  "mainShippingAddress" : 0
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/9" -d'{
  "username" : "Opera",
  "hashedPassword" : "{bcrypt}$2a$10$VwntQlZvYt4g7e3M7QadG.91SkLd/MW1vrhab2Qj.0VkTdGcjVrnm",
  "accountNonExpired" : true,
  "accountNonLocked" : true,
  "credentialsNonExpired" : true,
  "enabled" : true,
  "authorities" : [{"authority"  :  "ROLE_USER"}],
  "mainPayMeth" : 0,
  "mainShippingAddress" : 0
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-users/_doc/10" -d'{
  "username" : "Rotas",
  "hashedPassword" : "{bcrypt}$2a$10$zICb16aMAVQyJ6180HUpDuvQAbZX.yGPmjUC7FSD7otSRSoBdbnjG",
  "accountNonExpired" : true,
  "accountNonLocked" : true,
  "credentialsNonExpired" : true,
  "enabled" : true,
  "authorities" : [{"authority"  :  "ROLE_USER"}],
  "mainPayMeth" : 0,
  "mainShippingAddress" : 0
}'


echo
echo
# Refresh so data is available
curl -s -XGET "$ADDRESS/gutenberg-users/_refresh"

echo

