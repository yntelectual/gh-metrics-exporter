# gh-metrics

Poll Github API for workflow runs of specific repo and publish performance metrics in prometheus format.
The data is stored in memory or in DynamoDB table

## How to use

`docker run -p 8080:8080 -e GITHUB_API_TOKEN=your_token -e GITHUB_ORG=your_org -e GITHUB_REPO=your_repo yntelectual/gh-metrics-exporter`

If you want to use dynamo DB as store, set env var `REPO_TYPE` to value `dynamodb` and configure the dynamo client based on https://quarkiverse.github.io/quarkiverse-docs/quarkus-amazon-services/dev/amazon-dynamodb.html#_configuring_dynamodb_clients