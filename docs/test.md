---
id: test
title: okay testing this
---

## Quick start

A self-contained Docker image is provided which starts a Stacks 2.05 blockchain and API instance.

Ensure Docker is installed, then run the command:

```shell
docker run -p 3999:3999 hirosystems/stacks-blockchain-api-standalone
```

Similarly, a "mocknet" instance can be started. This runs a local node, isolated from the testnet/mainnet:

```shell
docker run -p 3999:3999 -e STACKS_NETWORK=mocknet hirosystems/stacks-blockchain-api-standalone
```

Once the blockchain has synced with network, the API will be available at:
[http://localhost:3999](http://localhost:3999)
