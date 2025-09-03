-- Sample data for testing
-- Users
INSERT INTO users (username, email, password, first_name, last_name, phone, role, enabled, created_at) VALUES
('admin', 'admin@realestate.com', '$2a$10$YourHashedPasswordHere', 'Admin', 'User', '123-456-7890', 'ROLE_ADMIN', true, NOW()),
('john_investor', 'john@example.com', '$2a$10$YourHashedPasswordHere', 'John', 'Doe', '123-456-7891', 'ROLE_USER', true, NOW()),
('jane_owner', 'jane@example.com', '$2a$10$YourHashedPasswordHere', 'Jane', 'Smith', '123-456-7892', 'ROLE_PROPERTY_OWNER', true, NOW());

-- Properties
INSERT INTO properties (title, description, price, location, address, property_type, status, area, bedrooms, bathrooms, year_built, image_url, created_at) VALUES
('Luxury Manhattan Apartment', 'Stunning 3-bedroom apartment in the heart of Manhattan with panoramic city views', 1500000, 'New York', '123 5th Avenue, Manhattan, NY 10001', 'Apartment', 'Available', 2000, 3, 2, 2015, '/images/property1.jpg', NOW()),
('Modern Brooklyn Condo', 'Newly renovated 2-bedroom condo with modern amenities', 850000, 'Brooklyn', '456 Park Slope, Brooklyn, NY 11215', 'Condo', 'Available', 1200, 2, 2, 2020, '/images/property2.jpg', NOW()),
('Spacious Family House', 'Beautiful 4-bedroom house in quiet neighborhood', 750000, 'Queens', '789 Forest Hills, Queens, NY 11375', 'House', 'Available', 2500, 4, 3, 2010, '/images/property3.jpg', NOW()),
('Investment Property Bronx', 'Multi-family home perfect for rental income', 650000, 'Bronx', '321 Riverdale Ave, Bronx, NY 10463', 'Multi-Family', 'Available', 3000, 6, 4, 2005, '/images/property4.jpg', NOW()),
('Downtown Studio', 'Cozy studio apartment perfect for young professionals', 450000, 'Manhattan', '555 Wall Street, Manhattan, NY 10005', 'Studio', 'Available', 500, 0, 1, 2018, '/images/property5.jpg', NOW());

-- Market Data
INSERT INTO market_data (location, avg_price_per_sqft, growth_rate, rental_rates, last_updated) VALUES
('New York', 1200, 5.5, 4500, NOW()),
('Brooklyn', 850, 6.2, 3200, NOW()),
('Queens', 650, 4.8, 2800, NOW()),
('Bronx', 450, 7.1, 2200, NOW()),
('Manhattan', 1500, 4.2, 5000, NOW());

-- Investment Calculations (sample)
INSERT INTO investment_calculations (property_id, roi, rental_yield, appreciation_rate, calculated_at) VALUES
(1, 6.5, 3.8, 5.2, NOW()),
(2, 7.2, 4.5, 6.1, NOW()),
(3, 5.8, 4.2, 4.5, NOW());