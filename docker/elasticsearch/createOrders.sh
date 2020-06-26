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

echo "WARNING, this script will delete the 'gutenberg-orders' index and re-index all data!"
echo "Press Control-C to cancel this operation."
echo
echo "Press [Enter] to continue."
read

echo $(dirname $0)

# Delete the old index, swallow failures if it doesn't exist
curl -s -XDELETE "$ADDRESS/gutenberg-orders" > /dev/null

# Create the new index using init.json
echo "Creating 'gutenberg-orders' index..."
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders" -d@$(dirname $0)/initOrders.json

# Wait for index to become yellow
curl -s "$ADDRESS/gutenberg-orders/_health?wait_for_status=yellow&timeout=10s" > /dev/null
echo
echo "Done creating 'gutenberg-orders' index."


echo
echo "Indexing data..."


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/1" -d'{
  "userId" : "7",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "2",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {    
  },
  "subtotal" : 4400,
  "date" : "2017-01-24 00:00"
}'


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/2" -d'{
  "userId" : "8",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "1",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {        
  },
  "subtotal" : 6600,
  "date" : "2017-02-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/3" -d'{
  "userId" : "9",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "2",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {        
  },
  "subtotal" : 7700,
  "date" : "2017-03-24 00:00"
}' 

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/4" -d'{
  "userId" : "10",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "3",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
        "street" : "",
        "city" : "",
        "zip" : "",
        "state" : "",
        "country" : ""
  },
  "paymentMethod" : {        
  },
  "subtotal" : 8800,
  "date" : "2017-04-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/5" -d'{
  "userId" : "6",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "5",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {        
  },
  "subtotal" : 9900,
  "date" : "2017-05-24 00:00"
}'



echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/6" -d'{
  "userId" : "7",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "15",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
        
  },
  "subtotal" : 2200,
  "date" : "2017-06-24 00:00"
}'



echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/7" -d'{
    "userId" : "7",
    "state" : "SHIPPED",
    "lineItems" : [
      {
        "bookId" : "9",
        "quantity" : 1
      }
    ],
    "shippingAddress" : {
      "street" : "",
      "city" : "",
      "zip" : "",
      "state" : "",
      "country" : ""
    },
    "paymentMethod" : {
    },
    "subtotal" : 3300,
    "date" : "2017-07-24 00:00"
}'



echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/8" -d'{
  "userId" : "7",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "7",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {        
  },
  "subtotal" : 4400,
  "date" : "2017-08-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/9" -d'{
  "userId" : "4",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "3",
      "quantity" : 1
    },
    {
      "bookId" : "15",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
        "street" : "",
        "city" : "",
        "zip" : "",
        "state" : "",
        "country" : ""
  },
  "paymentMethod" : {
        
  },
  "subtotal" : 5500,
  "date" : "2017-09-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/10" -d'{
  "userId" : "7",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "8",
      "quantity" : 1
    },
    {
      "bookId" : "3",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {      
  },
  "subtotal" : 6600,
  "date" : "2017-10-24 00:00"
}'


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/11" -d'
{
  "userId" : "10",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "12",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
  },
  "subtotal" : 7700,
  "date" : "2017-11-24 00:00"
}
'


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/12" -d'{
  "userId" : "10",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "14",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
    
  },
  "subtotal" : 8800,
  "date" : "2017-12-24 00:00"
}'


echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/13" -d'{
  "userId" : "4",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "14",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
    
  },
  "subtotal" : 9900,
  "date" : "2017-08-14 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/14" -d'{
  "userId" : "6",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "14",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
    
  },
  "subtotal" : 2200,
  "date" : "2017-07-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/15" -d'{
  "userId" : "6",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "14",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
    
  },
  "subtotal" : 3300,
  "date" : "2017-07-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/16" -d'{
  "userId" : "42",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "14",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
    
  },
  "subtotal" : 3300,
  "date" : "2017-07-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/17" -d'{
  "userId" : "43",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "14",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
    
  },
  "subtotal" : 3300,
  "date" : "2017-07-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/18" -d'{
  "userId" : "43",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "5",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
    
  },
  "subtotal" : 9900,
  "date" : "2017-07-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/19" -d'{
  "userId" : "44",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "14",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
    
  },
  "subtotal" : 3300,
  "date" : "2017-07-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/20" -d'{
  "userId" : "44",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "14",
      "quantity" : 1
    },
    {
      "bookId" : "5",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {
    
  },
  "subtotal" : 9900,
  "date" : "2017-07-24 00:00"
}'

echo
echo
curl -s -XPUT -H 'Content-Type: application/json' "$ADDRESS/gutenberg-orders/_doc/21" -d'{
  "userId" : "7",
  "state" : "SHIPPED",
  "lineItems" : [
    {
      "bookId" : "5",
      "quantity" : 1
    }
  ],
  "shippingAddress" : {
    "street" : "",
    "city" : "",
    "zip" : "",
    "state" : "",
    "country" : ""
  },
  "paymentMethod" : {    
  },
  "subtotal" : 4400,
  "date" : "2017-01-24 00:00"
}'

echo
echo
# Refresh so data is available
curl -s -XGET "$ADDRESS/gutenberg-orders/_refresh"

echo

