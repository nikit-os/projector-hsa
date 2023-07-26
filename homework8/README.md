# Projector HSA Homework_8

## Requirements:
 - Configure nginx that will cache only images, that were requested at least twice 
 - Add ability to drop nginx cache by request. 
 - You should drop cache for specific file only ( not all cache )

## How to run services
```bash
docker compose up
```

## Making requests

*First request*
Header `X-CACHE: MISS` indicates that `superman.jpg` is not in cache
```bash
http --verbose GET http://localhost:8000/images/superman.jpg

GET /images/superman.jpg HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate, zstd
Connection: keep-alive
Host: localhost:8000
User-Agent: HTTPie/3.2.2

HTTP/1.1 200 OK
Accept-Ranges: bytes
Connection: keep-alive
Content-Length: 84087
Content-Type: image/jpeg
Date: Wed, 26 Jul 2023 15:12:11 GMT
ETag: "64c01291-14877"
Last-Modified: Tue, 25 Jul 2023 18:21:05 GMT
Server: openresty/1.21.4.2
X-CACHE: MISS
```

*Second request*
Header `X-CACHE: HIT` indicates that `superman.jpg` is in cache
```bash
http --verbose GET http://localhost:8000/images/superman.jpg
GET /images/superman.jpg HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate, zstd
Connection: keep-alive
Host: localhost:8000
User-Agent: HTTPie/3.2.2



HTTP/1.1 200 OK
Accept-Ranges: bytes
Connection: keep-alive
Content-Length: 84087
Content-Type: image/jpeg
Date: Wed, 26 Jul 2023 15:14:15 GMT
ETag: "64c01291-14877"
Last-Modified: Tue, 25 Jul 2023 18:21:05 GMT
Server: openresty/1.21.4.2
X-CACHE: HIT
```

*Third request*
Clear cahce for `superman.jpg`
```bash
http --verbose POST http://localhost:8000/purge_cache <<< '"/images/superman.jpg"'

POST /purge_cache HTTP/1.1
Accept: application/json, */*;q=0.5
Accept-Encoding: gzip, deflate, zstd
Connection: keep-alive
Content-Length: 23
Content-Type: application/json
Host: localhost:8000
User-Agent: HTTPie/3.2.2

"/images/superman.jpg"


HTTP/1.1 204 No Content
Connection: keep-alive
Date: Wed, 26 Jul 2023 15:16:46 GMT
Server: openresty/1.21.4.2
```

*Forth request*
Header `X-CACHE: MISS` tells us that purge was successful
```bash
http --verbose GET http://localhost:8000/images/superman.jpg
GET /images/superman.jpg HTTP/1.1
Accept: */*
Accept-Encoding: gzip, deflate, zstd
Connection: keep-alive
Host: localhost:8000
User-Agent: HTTPie/3.2.2


HTTP/1.1 200 OK
Accept-Ranges: bytes
Connection: keep-alive
Content-Length: 84087
Content-Type: image/jpeg
Date: Wed, 26 Jul 2023 15:17:14 GMT
ETag: "64c01291-14877"
Last-Modified: Tue, 25 Jul 2023 18:21:05 GMT
Server: openresty/1.21.4.2
X-CACHE: MISS
```