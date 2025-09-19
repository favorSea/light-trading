# Crypto Trading (Spring Boot + H2)

## Overview
This document describes the API endpoints and example requests/responses. 

> **Base URL (example used in examples):** `http://localhost:8080`

---

## Endpoints

### 1. `GET /api/history`
Retrieve trade history.

**Request**
```bash
curl --location 'http://localhost:8080/api/history'
```

**Response (200 OK)**
```json
[
    {
        "id": 4,
        "symbol": "BTCUSDT",
        "side": "SELL",
        "price": 117110.74000000,
        "quantity": 0.05123400,
        "total": 6000.05165316,
        "user_id": 1,
        "created_at": "2025-09-19T02:29:59.849165Z"
    },
    {
        "id": 3,
        "symbol": "BTCUSDT",
        "side": "BUY",
        "price": 117110.75000000,
        "quantity": 0.10000000,
        "total": 11711.07500000,
        "user_id": 1,
        "created_at": "2025-09-19T02:29:52.762336Z"
    },
    {
        "id": 2,
        "symbol": "ETHUSDT",
        "side": "SELL",
        "price": 4592.30000000,
        "quantity": 0.78100000,
        "total": 3586.58630000,
        "user_id": 1,
        "created_at": "2025-09-19T02:29:41.818097Z"
    },
    {
        "id": 1,
        "symbol": "ETHUSDT",
        "side": "BUY",
        "price": 4592.74000000,
        "quantity": 1.00000000,
        "total": 4592.74000000,
        "user_id": 1,
        "created_at": "2025-09-19T02:29:33.182512Z"
    }
]
```

**Field descriptions**
| Field | Type | Description |
|---|---:|---|
| `id` | integer | Trade record identifier |
| `symbol` | string | Trading pair symbol (e.g. `BTCUSDT`, `ETHUSDT`) |
| `side` | string | `BUY` or `SELL` |
| `price` | number (decimal) | Fill price per unit |
| `quantity` | number (decimal) | Filled quantity |
| `total` | number (decimal) | `price * quantity` (observed) |
| `user_id` | integer | ID of the user who made the trade |
| `created_at` | string (ISO 8601) | Timestamp of the trade (UTC `Z` suffix present) |

---

### 2. `GET /api/wallet`
Retrieve wallet balances for the user.

**Request**
```bash
curl --location 'http://localhost:8080/api/wallet'
```

**Response (200 OK)**
```json
[
    {
        "id": 1,
        "currency": "USDT",
        "balance": 43282.82295316,
        "user_id": 1
    },
    {
        "id": 2,
        "currency": "BTC",
        "balance": 0.04876600,
        "user_id": 1
    },
    {
        "id": 3,
        "currency": "ETH",
        "balance": 0.21900000,
        "user_id": 1
    }
]
```

**Field descriptions**
| Field | Type | Description |
|---|---:|---|
| `id` | integer | Wallet record identifier |
| `currency` | string | Currency code (e.g. `USDT`, `BTC`, `ETH`) |
| `balance` | number (decimal) | Current balance for the currency |
| `user_id` | integer | Owner user id |

---

### 3. `POST /api/trade`
Place a trade.

**Request**
```bash
curl --location 'http://localhost:8080/api/trade' --header 'Content-Type: application/json' --data '{
  "symbol": "ETHUSDT",
  "side": "BUY",
  "quantity": 1
}'
```

**Response (201/200)**
```json
{
    "id": 1,
    "symbol": "ETHUSDT",
    "side": "BUY",
    "price": 4592.74,
    "quantity": 1,
    "total": 4592.74,
    "created_at": "2025-09-19T02:29:33.182512200Z"
}
```

**Request body fields**
| Field | Type | Required | Description |
|---|---:|:---:|---|
| `symbol` | string | yes | Trading pair symbol |
| `side` | string | yes | `BUY` or `SELL` |
| `quantity` | number | yes | Quantity to trade |

**Response fields**
| Field | Type | Description |
|---|---:|---|
| `id` | integer | New trade id |
| `symbol` | string | Trading pair |
| `side` | string | `BUY`/`SELL` |
| `price` | number | Executed price |
| `quantity` | number | Executed quantity |
| `total` | number | `price * quantity` |
| `created_at` | string (ISO 8601) | Timestamp with sub-second precision and `Z` |

---

### 4. `GET /api/price/latest?symbol={symbol}`
Get the latest aggregated best bid/ask for a symbol.

**Request**
```bash
curl --location 'http://localhost:8080/api/price/latest?symbol=BTCUSDT'
```

**Response (200 OK)**
```json
{
    "id": 1,
    "symbol": "BTCUSDT",
    "best_ask": 117110.75,
    "best_ask_src": "binance",
    "best_bid": 117110.74,
    "best_bid_src": "binance",
    "updated_at": "2025-09-19T02:29:56.620185Z"
}
```

**Field descriptions**
| Field | Type | Description |
|---|---:|---|
| `id` | integer | Price record id |
| `symbol` | string | Trading pair |
| `best_ask` | number | Current best ask price |
| `best_ask_src` | string | Source/exchange of best ask (observed `binance`) |
| `best_bid` | number | Current best bid price |
| `best_bid_src` | string | Source/exchange of best bid (observed `binance`) |
| `updated_at` | string (ISO 8601) | Last update timestamp (UTC `Z`) |

---

## Notes & Observations
- All timestamps in the samples are ISO 8601 with a `Z` suffix (UTC).
- Numeric fields use high-precision decimals (prices, quantities, balances).
- `total` fields in trade responses equal `price * quantity` in the examples.

---

## Example quick lookup (summary)
- `GET /api/history` — list of past trades.
- `GET /api/wallet` — current wallet balances.
- `POST /api/trade` — create/place a trade (JSON body: `symbol`, `side`, `quantity`).
- `GET /api/price/latest?symbol={symbol}` — latest best bid/ask and sources.

---

*End of API reference.*
