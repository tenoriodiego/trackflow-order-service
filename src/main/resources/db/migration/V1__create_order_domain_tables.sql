
-- =============================================
-- CUSTOMERS
-- =============================================

CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    document VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT uk_customers_document
        UNIQUE (document)
);

-- =============================================
-- ADDRESSES
-- =============================================

CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    street VARCHAR(150) NOT NULL,
    number VARCHAR(20) NOT NULL,
    complement VARCHAR(100),
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(2) NOT NULL
);

-- =============================================
-- ORDERS
-- =============================================

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    tracking_code VARCHAR(40) NOT NULL,
    customer_id UUID NOT NULL,
    origin_address_id UUID NOT NULL,
    destination_address_id UUID NOT NULL,
    created_by UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    description VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT uk_orders_tracking_code
        UNIQUE (tracking_code),

    CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id),

    CONSTRAINT fk_orders_origin_address
        FOREIGN KEY (origin_address_id)
        REFERENCES addresses(id),

    CONSTRAINT fk_orders_destination_address
        FOREIGN KEY (destination_address_id)
        REFERENCES addresses(id),

    CONSTRAINT chk_orders_status
        CHECK (status IN (
            'CREATED',
            'COLLECTED',
            'IN_TRANSIT',
            'OUT_FOR_DELIVERY',
            'DELIVERED',
            'CANCELLED'
        ))
);

CREATE INDEX idx_orders_customer
    ON orders(customer_id);

CREATE INDEX idx_orders_created_by
    ON orders(created_by);

CREATE INDEX idx_orders_status
    ON orders(status);

-- =============================================
-- ORDER STATUS HISTORY
-- =============================================

CREATE TABLE order_status_history (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    description VARCHAR(500),
    location VARCHAR(150),
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_order_history_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE RESTRICT,

    CONSTRAINT chk_order_history_status
        CHECK (status IN (
            'CREATED',
            'COLLECTED',
            'IN_TRANSIT',
            'OUT_FOR_DELIVERY',
            'DELIVERED',
            'CANCELLED'
        ))
);

CREATE INDEX idx_order_history_order
    ON order_status_history(order_id);

CREATE INDEX idx_order_history_occurred_at
    ON order_status_history(occurred_at);
