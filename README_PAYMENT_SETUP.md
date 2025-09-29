# Razorpay Payment Integration Setup Guide

## ✅ Integration Complete!

Your Real Estate Investment Platform now has Razorpay payment gateway integrated with the following credentials configured in `.env`:

### Your Configuration
- **Razorpay Key ID**: `rzp_live_RJ3C684tGaamiF`
- **Razorpay Key Secret**: `q7W51CYEVVLJV4sPnJBFP57R`
- **Email**: `propinvestbuy@gmail.com`
- **App Name**: PropInvest

## 🚀 Quick Start

### 1. Start the Application
```bash
cd "D:\real estate project"
mvn spring-boot:run
```

### 2. Access the Application
- **Main URL**: http://localhost:9090
- **Login as Investor**: Use credentials from database or create new account

### 3. Test Payment Flow
1. Browse properties at `/properties`
2. Click "Invest Now" on any property
3. You'll be redirected to `/investor/invest/{propertyId}`
4. Enter investment amount (minimum ₹10,000)
5. Click "Proceed to Pay"
6. Razorpay checkout will open
7. Use test card details (for testing):
   - Card Number: `4111 1111 1111 1111`
   - Expiry: Any future date
   - CVV: Any 3 digits

## 📁 Important Files Created

### Backend
- `Payment.java` - Payment entity with all transaction details
- `PaymentRepository.java` - Database operations for payments
- `PaymentService.java` - Business logic and Razorpay integration
- `PaymentController.java` - REST endpoints and MVC controllers
- `PaymentException.java` - Custom exception handling
- `CustomUserDetails.java` - Spring Security user details

### Frontend (JSP)
- `/payment/invest.jsp` - Investment page with Razorpay checkout
- `/payment/history.jsp` - Payment history dashboard
- `/payment/success.jsp` - Success page with confetti
- `/payment/failure.jsp` - Failure page with troubleshooting

### Configuration
- `.env` - Your environment variables (DO NOT COMMIT)
- `.env.example` - Template for other developers
- `application-env.yml` - Spring Boot env configuration
- `EnvConfig.java` - Loads .env file on startup
- `.gitignore` - Prevents .env from being committed

## 🔐 Security Configuration

The following endpoints are secured:
- `/api/payment/**` - Requires INVESTOR role
- `/investor/**` - Requires INVESTOR role
- `/api/admin/payments/**` - Requires ADMIN role

## 📊 Payment Features

### For Investors
- Make investments on properties
- View payment history at `/investor/payments`
- Download payment receipts
- Real-time payment status updates
- Email notifications on successful payment

### For Admins
- View all payments via API
- Get top invested properties
- Payment analytics and reports

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Test Coverage
- `PaymentServiceTest.java` includes:
  - Order creation tests
  - Payment verification tests
  - Error handling tests
  - Repository operation tests

## ⚠️ Important Notes

1. **Gmail App Password**: Currently using regular password. For production:
   - Enable 2-factor authentication on Gmail
   - Generate app-specific password
   - Update `MAIL_PASSWORD` in `.env`

2. **Production Deployment**:
   - Change `HIBERNATE_DDL_AUTO` from `create` to `update` or `validate`
   - Use production Razorpay keys (not test keys)
   - Set up proper SSL certificates
   - Use environment variables instead of .env file

3. **Database Migration**:
   The Payment table will be created automatically when you run the application due to JPA/Hibernate.

## 🔄 API Endpoints

### Create Payment Order
```
POST /api/payment/create-order
Params: propertyId, amount
```

### Verify Payment
```
POST /api/payment/verify
Params: razorpay_order_id, razorpay_payment_id, razorpay_signature
```

### Get Payment History
```
GET /api/payment/history
```

### Get Investment Statistics
```
GET /api/payment/statistics
```

## 📞 Support

If you encounter any issues:
1. Check application logs in `logs/application.log`
2. Verify Razorpay credentials in `.env`
3. Ensure MySQL is running on port 3306
4. Check network connectivity for Razorpay API

## 🎉 Next Steps

1. **Configure Email**: Set up Gmail app-specific password for email notifications
2. **Add OpenAI Key**: If you want to use the chatbot feature
3. **Test Payments**: Try making test investments
4. **Monitor Logs**: Check for any errors in the application logs

Your payment gateway is now fully integrated and ready to use!