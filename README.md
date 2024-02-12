# cloud-bees-assessment

Cloud bees assessment 156350

For the assessment I have come up with following db schema and the db used in H2 db which is a memory db

I have added the PostMan collection as well in the location src/main/resources/collection for the ease of reference.

### Product Table

| Field                        | Description                            |
|------------------------------|----------------------------------------|
| pid (Primary Key)            | Unique identifier for the product.      |
| name                         | Name of the product.                   |
| description                  | Description of the product.            |
| quantityAvailable            | Available quantity of the product.     |
| priceDetailsId (Foreign Key) | Foreign key linking to PriceDetails Table.|

### PriceDetails Table

| Field              | Description                             |
|--------------------|-----------------------------------------|
| id (Primary Key)   | Unique identifier for price details.    |
| pid (Foreign Key)  | Foreign key linking to Product Table.   |
| price              | Price of the product.                   |
| discountPercentage | Nullable field for discount percentage. |
| taxRate            | Nullable field for tax rate.            |
