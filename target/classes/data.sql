-- Sample data for testing - Updated with correct schema and Indian cities
-- Users (Fixed column names based on actual schema)
INSERT INTO users (username, email, password, first_name, last_name, phone_number, role, is_enabled, created_at) VALUES
('admin', 'admin@realestate.com', '$2a$10$8C3g6TQPcPd/.F8ZdWw0qOZPxeqVR7KZf0c1/yKoZaLlDt8K2VDLq', 'Admin', 'User', '+91-9876543210', 'ADMIN', true, NOW()),
('john_investor', 'john@example.com', '$2a$10$8C3g6TQPcPd/.F8ZdWw0qOZPxeqVR7KZf0c1/yKoZaLlDt8K2VDLq', 'John', 'Doe', '+91-9876543211', 'INVESTOR', true, NOW()),
('jane_owner', 'jane@example.com', '$2a$10$8C3g6TQPcPd/.F8ZdWw0qOZPxeqVR7KZf0c1/yKoZaLlDt8K2VDLq', 'Jane', 'Smith', '+91-9876543212', 'PROPERTY_OWNER', true, NOW());

-- Properties - Updated with 6 properties for each of the 4 cities
INSERT INTO properties (title, description, price, location, city, state, property_type, status, area, area_unit, bedrooms, bathrooms, year_built, owner_id, created_at, updated_at) VALUES
-- Pune Properties (6 properties)
('Premium 3BHK in Koregaon Park', 'Luxurious 3BHK apartment in prestigious Koregaon Park with modern amenities and great connectivity', 15000000.00, 'Koregaon Park, Pune', 'Pune', 'Maharashtra', 'APARTMENT', 'AVAILABLE', 1800.00, 'SQFT', 3, 2, 2020, 3, NOW(), NOW()),
('Spacious Villa in Baner', 'Independent 4BHK villa with garden and parking in prime Baner location', 25000000.00, 'Baner, Pune', 'Pune', 'Maharashtra', 'VILLA', 'AVAILABLE', 2500.00, 'SQFT', 4, 3, 2019, 3, NOW(), NOW()),
('Modern 2BHK in Hinjewadi', 'Contemporary 2BHK flat in IT hub Hinjewadi with excellent amenities', 8500000.00, 'Hinjewadi, Pune', 'Pune', 'Maharashtra', 'APARTMENT', 'AVAILABLE', 1200.00, 'SQFT', 2, 2, 2021, 3, NOW(), NOW()),
('Luxury Penthouse Wakad', 'Exclusive penthouse with terrace garden in upscale Wakad area', 35000000.00, 'Wakad, Pune', 'Pune', 'Maharashtra', 'APARTMENT', 'AVAILABLE', 3000.00, 'SQFT', 4, 4, 2022, 3, NOW(), NOW()),
('Elegant 3BHK in Aundh', 'Sophisticated 3BHK apartment in sought-after Aundh with premium facilities', 18000000.00, 'Aundh, Pune', 'Pune', 'Maharashtra', 'APARTMENT', 'AVAILABLE', 1650.00, 'SQFT', 3, 3, 2021, 3, NOW(), NOW()),
('Heritage Bungalow Pune Cantonment', 'Beautiful colonial-style bungalow in prestigious Pune Cantonment area', 42000000.00, 'Pune Cantonment, Pune', 'Pune', 'Maharashtra', 'RESIDENTIAL', 'AVAILABLE', 3500.00, 'SQFT', 5, 4, 2015, 3, NOW(), NOW()),

-- Mumbai Properties (6 properties)
('Sea View 4BHK in Bandra West', 'Luxurious sea-facing apartment in premium Bandra West with stunning Arabian Sea views', 65000000.00, 'Bandra West, Mumbai', 'Mumbai', 'Maharashtra', 'APARTMENT', 'AVAILABLE', 2200.00, 'SQFT', 4, 3, 2020, 3, NOW(), NOW()),
('Skyscraper Penthouse Lower Parel', 'Ultra-modern penthouse in iconic Lower Parel tower with panoramic city views', 95000000.00, 'Lower Parel, Mumbai', 'Mumbai', 'Maharashtra', 'APARTMENT', 'AVAILABLE', 3800.00, 'SQFT', 4, 4, 2022, 3, NOW(), NOW()),
('Compact 2BHK in Andheri East', 'Well-designed 2BHK apartment near metro and airport in buzzing Andheri East', 22000000.00, 'Andheri East, Mumbai', 'Mumbai', 'Maharashtra', 'APARTMENT', 'AVAILABLE', 950.00, 'SQFT', 2, 2, 2021, 3, NOW(), NOW()),
('Heritage Apartment in Fort', 'Charming heritage apartment in historic Fort area with colonial architecture', 38000000.00, 'Fort, Mumbai', 'Mumbai', 'Maharashtra', 'APARTMENT', 'AVAILABLE', 1650.00, 'SQFT', 3, 2, 2018, 3, NOW(), NOW()),
('Modern 3BHK in Powai', 'Contemporary apartment in IT hub Powai with lake views and tech park proximity', 28000000.00, 'Powai, Mumbai', 'Mumbai', 'Maharashtra', 'APARTMENT', 'AVAILABLE', 1450.00, 'SQFT', 3, 2, 2019, 3, NOW(), NOW()),
('Luxury Villa in Juhu', 'Exclusive independent villa near Juhu Beach with private garden and pool', 120000000.00, 'Juhu, Mumbai', 'Mumbai', 'Maharashtra', 'VILLA', 'AVAILABLE', 4500.00, 'SQFT', 5, 5, 2021, 3, NOW(), NOW()),

-- Delhi Properties (6 properties)
('Elite Apartment in Connaught Place', 'Premium 3BHK in central Delhi with excellent connectivity and amenities', 28000000.00, 'Connaught Place, Delhi', 'Delhi', 'Delhi', 'APARTMENT', 'AVAILABLE', 1900.00, 'SQFT', 3, 2, 2019, 3, NOW(), NOW()),
('Spacious Home in South Extension', 'Large 4BHK independent house in upscale South Extension area', 35000000.00, 'South Extension, Delhi', 'Delhi', 'Delhi', 'RESIDENTIAL', 'AVAILABLE', 2800.00, 'SQFT', 4, 3, 2016, 3, NOW(), NOW()),
('Modern Condo in Dwarka', 'Contemporary 2BHK flat in well-planned Dwarka with metro connectivity', 11000000.00, 'Dwarka, Delhi', 'Delhi', 'Delhi', 'APARTMENT', 'AVAILABLE', 1300.00, 'SQFT', 2, 2, 2020, 3, NOW(), NOW()),
('Luxury Penthouse in Vasant Kunj', 'Exclusive penthouse with panoramic views in premium Vasant Kunj', 55000000.00, 'Vasant Kunj, Delhi', 'Delhi', 'Delhi', 'APARTMENT', 'AVAILABLE', 3500.00, 'SQFT', 4, 4, 2022, 3, NOW(), NOW()),
('Commercial Space CP', 'Prime commercial office space in the heart of Connaught Place', 40000000.00, 'Connaught Place, Delhi', 'Delhi', 'Delhi', 'COMMERCIAL', 'AVAILABLE', 2000.00, 'SQFT', 0, 2, 2018, 3, NOW(), NOW()),
('Designer 3BHK in Greater Kailash', 'Architect-designed luxury apartment in posh Greater Kailash with premium finishes', 32000000.00, 'Greater Kailash, Delhi', 'Delhi', 'Delhi', 'APARTMENT', 'AVAILABLE', 2100.00, 'SQFT', 3, 3, 2021, 3, NOW(), NOW()),

-- Bangalore Properties (6 properties)
('IT Hub Apartment Whitefield', 'Modern 3BHK apartment in IT corridor Whitefield with tech park proximity', 16000000.00, 'Whitefield, Bangalore', 'Bangalore', 'Karnataka', 'APARTMENT', 'AVAILABLE', 1700.00, 'SQFT', 3, 2, 2021, 3, NOW(), NOW()),
('Garden Villa in HSR Layout', 'Beautiful villa with garden in family-friendly HSR Layout', 32000000.00, 'HSR Layout, Bangalore', 'Bangalore', 'Karnataka', 'VILLA', 'AVAILABLE', 2600.00, 'SQFT', 4, 3, 2019, 3, NOW(), NOW()),
('Startup Office in Koramangala', 'Contemporary office space perfect for startups in vibrant Koramangala', 22000000.00, 'Koramangala, Bangalore', 'Bangalore', 'Karnataka', 'OFFICE', 'AVAILABLE', 1500.00, 'SQFT', 0, 3, 2020, 3, NOW(), NOW()),
('Luxury Flat in Brigade Road', 'Premium 2BHK apartment in shopping district Brigade Road', 18000000.00, 'Brigade Road, Bangalore', 'Bangalore', 'Karnataka', 'APARTMENT', 'AVAILABLE', 1400.00, 'SQFT', 2, 2, 2022, 3, NOW(), NOW()),
('Tech Park Villa Electronic City', 'Spacious villa near Electronic City tech parks with excellent connectivity', 28000000.00, 'Electronic City, Bangalore', 'Bangalore', 'Karnataka', 'VILLA', 'AVAILABLE', 2400.00, 'SQFT', 4, 3, 2020, 3, NOW(), NOW()),
('Premium 4BHK in Indiranagar', 'Upscale 4BHK apartment in trendy Indiranagar with rooftop amenities', 35000000.00, 'Indiranagar, Bangalore', 'Bangalore', 'Karnataka', 'APARTMENT', 'AVAILABLE', 2300.00, 'SQFT', 4, 3, 2021, 3, NOW(), NOW());

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