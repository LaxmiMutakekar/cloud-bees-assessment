# cloud-bees-assessment

Cloud bees assessment 156350

For the assessment I have come up with following db schema and the db used in H2 db which is a memory db

### Product Table

| Field                        | Description                            |
|------------------------------|----------------------------------------|
| pid (Primary Key)            | Unique identifier for the product.      |
| name                         | Name of the product.                   |
| description                  | Description of the product.            |
| quantityAvailable            | Available quantity of the product.     |
| priceDetailsId (Foreign Key) | Foreign key linking to PriceDetails Table.|

### PriceDetails Table

| Field              | Description                            |
|--------------------|----------------------------------------|
| id (Primary Key)   | Unique identifier for price details.  |
| price              | Price of the product.                  |
| discountPercentage | Nullable field for discount percentage.|
| taxRate            | Nullable field for tax rate.           |
