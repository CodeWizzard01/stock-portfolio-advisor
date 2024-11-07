CREATE TABLE IF NOT EXISTS stock_orders (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    order_type VARCHAR(4) NOT NULL CHECK (order_type IN ('BUY', 'SELL')),
    created_at TIMESTAMP NOT NULL
);
