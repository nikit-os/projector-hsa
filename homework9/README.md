# Projector HSA Homework_9

## Run MySQL
```
docker-compose up
```

## Generate test data

```sh
cd data_generator
python3 -m venv
source ./bin/activate
pip install Faker
pip install mysql-connector-python

python users_data_generator.py users
python users_data_generator.py users_btree
```

## Dump test data
```sh
docker exec homework9-mysql-1 sh -c 'exec mysqldump homework9_db users -uroot -p"password"' > ./sql/users-dump.sql
docker exec homework9-mysql-1 sh -c 'exec mysqldump homework9_db users_btree -uroot -p"password"' > ./sql/users_btree-dump.sql
```

## Connect to instance with mysql client
```sh
docker run --network homework9_default -it --rm mysql mysql -hhomework9-mysql-1 -uroot -ppassword
```

## Select comparsion

```sql
use homework9_db;
show tables;

select * from users where birthday > '2000-01-01';
353307 rows in set (0.65 sec)

select * from users_btree where birthday > '2000-01-01';
354190 rows in set (0.71 sec)

select * from users where birthday = '2004-08-04';
164 rows in set (0.61 sec)

select * from users_btree where birthday = '2004-08-04';
187 rows in set (0.02 sec)

select * from users where birthday between '2000-01-01' and '2002-01-01';
127286 rows in set (1.26 sec)

select * from users_btree where birthday between '2000-01-01' and '2002-01-01';
127532 rows in set (0.47 sec)
```

**Result table:**

| where clause                                   | users    | users_btree |
| ---------------------------------------------- | -------- | ----------- |
| birthday > '2000-01-01'                        | 0.65 sec | 0.71 sec    |
| birthday = '2004-08-04'                        | 0.61 sec | 0.02 sec    |
| birthday between '2000-01-01' and '2002-01-01' | 1.26 sec | 0.47 sec    |
|                                                |          |             |

## Insert comparsions

```sh
cd data_generator
python3 -m venv
source ./bin/activate
pip install Faker
pip install mysql-connector-python

python insert_tester.py users
innodb_flush_log_at_trx_commit=0: 13.8957 seconds
innodb_flush_log_at_trx_commit=1: 17.0763 seconds
innodb_flush_log_at_trx_commit=2: 14.4523 seconds

python insert_tester.py users_btree
innodb_flush_log_at_trx_commit=0: 14.3180 seconds
innodb_flush_log_at_trx_commit=1: 17.5376 seconds
innodb_flush_log_at_trx_commit=2: 14.5750 seconds
```

| innodb_flush_log_at_trx_commit = | 0       | 1       | 2       |
| -------------------------------- | ------- | ------- | ------- |
| users                            | 13.8957 | 17.0763 | 14.4523 |
| users_btree                      | 14.3180 | 17.5376 | 14.5750 |
|                                  |         |         |         |