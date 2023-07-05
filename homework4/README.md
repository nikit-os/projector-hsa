# Projector HSA Homework_4

## How to start worker

1. Create `.env` file with content (paste your values)
```
COIN_API_TOKEN=
COIN_API_ASSET_BASE=
COIN_API_ASSET_QUOTE=
GAMP_CLIENT_ID=
GAMP_API_SECRET=
GAMP_MEASUREMENT_ID=
```
2. Run docker container
```
docker run -d --env-file .env -p 8000:8000 nikit0s/projector-hsa-homework4
```

Dashboard with jobs accessible by url - `http://localhost:8000`

## How to build worker container
```
cd currency-worker
./mvnw clean package docker:build
```