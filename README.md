Please read the entire readme before starting to work. The tasks are written down at the end.

# The product-processor: 
The Product-processor is a micro-service that accepts a product in a raw form and processes it into a standard form.

A product is just a JSON object with an id and a list of attributes. 
Each attribute is composed of a name and a list of values (strings)

For example:
```
{
  "id": 1000,
  "attributes": [
    {
      "name": "title",
      "values": [
        "Red and blue T-shirt with a chicken print. XL"
      ]
    },
    {
      "name": "color",
      "values": [
        "red",
        "blue"
      ]
    },
    {
      "name": "size",
      "values": [
        "xl"
      ]
    }
  ]
}
```

The product-processor makes the following changes to the product input:
* Normalize all attribute values except title
* Remove illegal values
* Sort the attributes by the attribute name
* Persist the processed products and fetch processed products
