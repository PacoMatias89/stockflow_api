-- =============================================================================
-- V1: Initial schema - StockFlow API
-- =============================================================================
-- Creates the base domain tables: users, products, warehouses, stock,
-- customers, orders and stock movements.
-- =============================================================================

-- =====================
-- USERS
-- =====================
CREATE TABLE users (
                       id              BIGSERIAL PRIMARY KEY,
                       username        VARCHAR(50)  NOT NULL UNIQUE,
                       email           VARCHAR(150) NOT NULL UNIQUE,
                       password_hash   VARCHAR(255) NOT NULL,
                       full_name       VARCHAR(150) NOT NULL,
                       role            VARCHAR(20)  NOT NULL CHECK (role IN ('ADMIN', 'OPERATOR', 'READONLY')),
                       enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
                       created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                       updated_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                       created_by      VARCHAR(50),
                       updated_by      VARCHAR(50)
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role  ON users(role);

-- =====================
-- PRODUCTS
-- =====================
CREATE TABLE products (
                          id              BIGSERIAL PRIMARY KEY,
                          sku             VARCHAR(50)   NOT NULL UNIQUE,
                          name            VARCHAR(200)  NOT NULL,
                          description     TEXT,
                          category        VARCHAR(100),
                          price           NUMERIC(12,2) NOT NULL CHECK (price >= 0),
                          active          BOOLEAN       NOT NULL DEFAULT TRUE,
                          version         BIGINT        NOT NULL DEFAULT 0,
                          created_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
                          updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
                          created_by      VARCHAR(50),
                          updated_by      VARCHAR(50)
);

CREATE INDEX idx_products_sku      ON products(sku);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_active   ON products(active);

-- =====================
-- WAREHOUSES
-- =====================
CREATE TABLE warehouses (
                            id              BIGSERIAL PRIMARY KEY,
                            code            VARCHAR(20)  NOT NULL UNIQUE,
                            name            VARCHAR(150) NOT NULL,
                            address         VARCHAR(300),
                            city            VARCHAR(100),
                            country         VARCHAR(100),
                            active          BOOLEAN      NOT NULL DEFAULT TRUE,
                            created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                            updated_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                            created_by      VARCHAR(50),
                            updated_by      VARCHAR(50)
);

CREATE INDEX idx_warehouses_code ON warehouses(code);

-- =====================
-- STOCK (quantity per product and warehouse)
-- =====================
CREATE TABLE stock (
                       id              BIGSERIAL PRIMARY KEY,
                       product_id      BIGINT      NOT NULL REFERENCES products(id),
                       warehouse_id    BIGINT      NOT NULL REFERENCES warehouses(id),
                       quantity        INTEGER     NOT NULL DEFAULT 0 CHECK (quantity >= 0),
                       reserved        INTEGER     NOT NULL DEFAULT 0 CHECK (reserved >= 0),
                       version         BIGINT      NOT NULL DEFAULT 0,
                       updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       CONSTRAINT uk_stock_product_warehouse UNIQUE (product_id, warehouse_id),
                       CONSTRAINT ck_stock_reserved_lte_quantity CHECK (reserved <= quantity)
);

CREATE INDEX idx_stock_product   ON stock(product_id);
CREATE INDEX idx_stock_warehouse ON stock(warehouse_id);

-- =====================
-- STOCK MOVEMENTS (audit log)
-- =====================
CREATE TABLE stock_movements (
                                 id              BIGSERIAL PRIMARY KEY,
                                 product_id      BIGINT      NOT NULL REFERENCES products(id),
                                 warehouse_id    BIGINT      NOT NULL REFERENCES warehouses(id),
                                 movement_type   VARCHAR(20) NOT NULL CHECK (movement_type IN ('IN', 'OUT', 'ADJUSTMENT', 'RESERVATION', 'RELEASE')),
                                 quantity        INTEGER     NOT NULL,
                                 reference       VARCHAR(100),
                                 notes           TEXT,
                                 created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                 created_by      VARCHAR(50)
);

CREATE INDEX idx_stock_movements_product    ON stock_movements(product_id);
CREATE INDEX idx_stock_movements_warehouse  ON stock_movements(warehouse_id);
CREATE INDEX idx_stock_movements_created_at ON stock_movements(created_at DESC);
CREATE INDEX idx_stock_movements_reference  ON stock_movements(reference);

-- =====================
-- CUSTOMERS
-- =====================
CREATE TABLE customers (
                           id              BIGSERIAL PRIMARY KEY,
                           code            VARCHAR(20)  NOT NULL UNIQUE,
                           name            VARCHAR(200) NOT NULL,
                           email           VARCHAR(150),
                           phone           VARCHAR(30),
                           tax_id          VARCHAR(50),
                           address         VARCHAR(300),
                           city            VARCHAR(100),
                           country         VARCHAR(100),
                           active          BOOLEAN      NOT NULL DEFAULT TRUE,
                           created_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                           updated_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
                           created_by      VARCHAR(50),
                           updated_by      VARCHAR(50)
);

CREATE INDEX idx_customers_code  ON customers(code);
CREATE INDEX idx_customers_email ON customers(email);

-- =====================
-- ORDERS
-- =====================
CREATE TABLE orders (
                        id              BIGSERIAL PRIMARY KEY,
                        order_number    VARCHAR(30)   NOT NULL UNIQUE,
                        customer_id     BIGINT        NOT NULL REFERENCES customers(id),
                        warehouse_id    BIGINT        NOT NULL REFERENCES warehouses(id),
                        status          VARCHAR(20)   NOT NULL CHECK (status IN ('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED')),
                        total_amount    NUMERIC(14,2) NOT NULL DEFAULT 0 CHECK (total_amount >= 0),
                        notes           TEXT,
                        version         BIGINT        NOT NULL DEFAULT 0,
                        created_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
                        updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
                        confirmed_at    TIMESTAMPTZ,
                        shipped_at      TIMESTAMPTZ,
                        delivered_at    TIMESTAMPTZ,
                        cancelled_at    TIMESTAMPTZ,
                        created_by      VARCHAR(50),
                        updated_by      VARCHAR(50)
);

CREATE INDEX idx_orders_order_number ON orders(order_number);
CREATE INDEX idx_orders_customer     ON orders(customer_id);
CREATE INDEX idx_orders_warehouse    ON orders(warehouse_id);
CREATE INDEX idx_orders_status       ON orders(status);
CREATE INDEX idx_orders_created_at   ON orders(created_at DESC);

-- =====================
-- ORDER LINES
-- =====================
CREATE TABLE order_lines (
                             id              BIGSERIAL PRIMARY KEY,
                             order_id        BIGINT        NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                             product_id      BIGINT        NOT NULL REFERENCES products(id),
                             quantity        INTEGER       NOT NULL CHECK (quantity > 0),
                             unit_price      NUMERIC(12,2) NOT NULL CHECK (unit_price >= 0),
                             subtotal        NUMERIC(14,2) NOT NULL CHECK (subtotal >= 0),
                             line_number     INTEGER       NOT NULL,
                             CONSTRAINT uk_order_lines_order_line UNIQUE (order_id, line_number)
);

CREATE INDEX idx_order_lines_order   ON order_lines(order_id);
CREATE INDEX idx_order_lines_product ON order_lines(product_id);

-- =====================
-- INITIAL USER (admin / admin123)
-- =====================
-- BCrypt hash for 'admin123' (cost 10). For development bootstrapping only,
-- so we can log in from day one. Replace via the user creation endpoint
-- once it exists.
INSERT INTO users (username, email, password_hash, full_name, role, created_by) VALUES
    ('admin', 'admin@stockflow.local',
     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
     'System Administrator', 'ADMIN', 'system');