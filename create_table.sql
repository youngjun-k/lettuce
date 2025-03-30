CREATE EXTERNAL TABLE lettuce_analytics.carbon_footprint (
  id BIGINT,
  aggregate_id STRING,
  user_id BIGINT,
  image_url STRING,
  product_name STRING,
  product_category STRING,
  carbon_value DOUBLE,
  carbon_reduction DOUBLE,
  environmental_impact STRING,  
  created_at BIGINT
)
STORED AS PARQUET
LOCATION 's3://lettuce/parquet/';