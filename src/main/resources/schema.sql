-- Database schema for Real Estate Investment Platform
CREATE DATABASE IF NOT EXISTS real_estate_db;
USE real_estate_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone VARCHAR(20),
    role VARCHAR(30) NOT NULL DEFAULT 'ROLE_USER',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Properties table
CREATE TABLE IF NOT EXISTS properties (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(15, 2) NOT NULL,
    location VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    property_type VARCHAR(50),
    status VARCHAR(30) DEFAULT 'Available',
    area DOUBLE,
    bedrooms INT,
    bathrooms INT,
    year_built INT,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_location (location),
    INDEX idx_price (price),
    INDEX idx_type (property_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Investment Calculations table
CREATE TABLE IF NOT EXISTS investment_calculations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    property_id BIGINT NOT NULL,
    roi DECIMAL(10, 4),
    rental_yield DECIMAL(10, 4),
    appreciation_rate DECIMAL(10, 4),
    cap_rate DECIMAL(10, 4),
    cash_flow DECIMAL(15, 2),
    break_even_years DECIMAL(10, 2),
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
    INDEX idx_property (property_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Market Data table
CREATE TABLE IF NOT EXISTS market_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    location VARCHAR(100) NOT NULL,
    avg_price_per_sqft DECIMAL(10, 2),
    growth_rate DECIMAL(10, 4),
    rental_rates DECIMAL(10, 2),
    vacancy_rate DECIMAL(10, 4),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_location (location)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Property Images table
CREATE TABLE IF NOT EXISTS property_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    property_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- User Favorites table
CREATE TABLE IF NOT EXISTS user_favorites (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_property (user_id, property_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Property Inquiries table
CREATE TABLE IF NOT EXISTS property_inquiries (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    property_id BIGINT NOT NULL,
    user_id BIGINT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    message TEXT,
    status VARCHAR(30) DEFAULT 'New',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Refresh Tokens table for JWT token rotation
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    family_id VARCHAR(36) NOT NULL,
    user_id BIGINT NOT NULL,
    issued_at DATETIME NOT NULL,
    expires_at DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_token_hash (token_hash),
    INDEX idx_family_id (family_id),
    INDEX idx_user_status (user_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Token Blacklist table for invalidated tokens
CREATE TABLE IF NOT EXISTS token_blacklist (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    jti VARCHAR(36) NOT NULL UNIQUE,
    expires_at DATETIME NOT NULL,
    blacklisted_at DATETIME NOT NULL,
    reason VARCHAR(50),
    INDEX idx_jti (jti),
    INDEX idx_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Portfolios table for investment tracking
CREATE TABLE IF NOT EXISTS portfolios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    portfolio_name VARCHAR(100) NOT NULL DEFAULT 'Default Portfolio',
    total_invested DECIMAL(18,2) DEFAULT 0,
    total_current_value DECIMAL(18,2) DEFAULT 0,
    unrealized_gains DECIMAL(18,2) DEFAULT 0,
    realized_gains DECIMAL(18,2) DEFAULT 0,
    risk_profile VARCHAR(20) DEFAULT 'MODERATE',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_portfolio (user_id, portfolio_name),
    INDEX idx_user_status (user_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Holdings table for property investments
CREATE TABLE IF NOT EXISTS holdings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    portfolio_id BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    quantity DECIMAL(18,8) NOT NULL,
    average_cost_basis DECIMAL(18,2) NOT NULL,
    total_cost_basis DECIMAL(18,2) NOT NULL,
    current_value DECIMAL(18,2) DEFAULT 0,
    unrealized_gain_loss DECIMAL(18,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (portfolio_id) REFERENCES portfolios(id) ON DELETE CASCADE,
    FOREIGN KEY (property_id) REFERENCES properties(id) ON DELETE CASCADE,
    INDEX idx_portfolio_status (portfolio_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Investment Transactions table
CREATE TABLE IF NOT EXISTS investment_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_reference VARCHAR(50) NOT NULL UNIQUE,
    portfolio_id BIGINT NOT NULL,
    holding_id BIGINT,
    property_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    quantity DECIMAL(18,8) NOT NULL,
    unit_price DECIMAL(18,2) NOT NULL,
    gross_amount DECIMAL(18,2) NOT NULL,
    net_amount DECIMAL(18,2) NOT NULL,
    platform_fee DECIMAL(12,2) DEFAULT 0,
    transaction_fee DECIMAL(12,2) DEFAULT 0,
    tax_amount DECIMAL(12,2) DEFAULT 0,
    cost_basis DECIMAL(18,2),
    realized_gain_loss DECIMAL(18,2),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    idempotency_key VARCHAR(100) UNIQUE,
    version BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (portfolio_id) REFERENCES portfolios(id),
    FOREIGN KEY (holding_id) REFERENCES holdings(id),
    FOREIGN KEY (property_id) REFERENCES properties(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_txn_user_status (user_id, status),
    INDEX idx_txn_reference (transaction_reference)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;