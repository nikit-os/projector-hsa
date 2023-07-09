# Projector HSA Homework_5

## How to build java-service
```sh
cd java-service
./mvnw clean package docker:build
```

## How to run all services
```sh
docker compose up
```

## Tests

`siege --concurrent=10 --time=1M --file=siege_urls.txt`
```
Transactions:		       31634 hits
Availability:		      100.00 %
Elapsed time:		       61.00 secs
Data transferred:	        2.74 MB
Response time:		        0.02 secs
Transaction rate:	      518.59 trans/sec
Throughput:		        0.04 MB/sec
Concurrency:		        9.97
Successful transactions:       31634
Failed transactions:	           0
Longest transaction:	        0.87
Shortest transaction:	        0.00
```

`siege --concurrent=25 --time=1M --file=siege_urls.txt`
```
Transactions:		       32698 hits
Availability:		      100.00 %
Elapsed time:		       60.89 secs
Data transferred:	        2.83 MB
Response time:		        0.04 secs
Transaction rate:	      537.00 trans/sec
Throughput:		        0.05 MB/sec
Concurrency:		       22.73
Successful transactions:       32698
Failed transactions:	           0
Longest transaction:	       19.57
Shortest transaction:	        0.00
```

`siege --concurrent=50 --time=1M --file=siege_urls.txt`
```
Transactions:		       32770 hits
Availability:		      100.00 %
Elapsed time:		       60.38 secs
Data transferred:	        2.84 MB
Response time:		        0.08 secs
Transaction rate:	      542.73 trans/sec
Throughput:		        0.05 MB/sec
Concurrency:		       43.99
Successful transactions:       32770
Failed transactions:	           0
Longest transaction:	       13.31
Shortest transaction:	        0.00
```

`siege --concurrent=100 --time=1M --file=siege_urls.txt`
```
Transactions:		       32822 hits
Availability:		      100.00 %
Elapsed time:		       60.05 secs
Data transferred:	        2.84 MB
Response time:		        0.16 secs
Transaction rate:	      546.58 trans/sec
Throughput:		        0.05 MB/sec
Concurrency:		       87.74
Successful transactions:       32822
Failed transactions:	           0
Longest transaction:	       19.76
Shortest transaction:	        0.00
```

`siege --concurrent=150 --time=1M --file=siege_urls.txt`
```
Transactions:		       32890 hits
Availability:		       99.71 %
Elapsed time:		       60.50 secs
Data transferred:	        2.85 MB
Response time:		        0.17 secs
Transaction rate:	      543.64 trans/sec
Throughput:		        0.05 MB/sec
Concurrency:		       92.34
Successful transactions:       32890
Failed transactions:	          97
Longest transaction:	       20.06
Shortest transaction:	        0.00
```