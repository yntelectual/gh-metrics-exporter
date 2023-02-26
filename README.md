# gh-metrics

Poll Github API for workflow runs of specific repo and publish performance metrics in prometheus format.
The data is stored in memory or in DynamoDB table


## How to use

`docker run -p 8080:8080 -e GITHUB_API_TOKEN=your_token -e GITHUB_ORG=your_org -e GITHUB_REPO=your_repo yntelectual/gh-metrics`