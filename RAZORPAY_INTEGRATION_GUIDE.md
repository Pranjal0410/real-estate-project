# Razorpay Integration Guide

## 🚀 Complete Working Example

This project includes a fully functional Razorpay payment integration using Spring Boot and JSP.

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/realestate/
│   │       ├── controller/
│   │       │   └── RazorpayController.java     # REST endpoints for payment
│   │       ├── service/
│   │       │   └── RazorpayService.java        # Razorpay business logic
│   │       ├── model/entity/
│   │       │   └── Payment.java                # Payment entity (updated)
│   │       └── repository/
│   │           └── PaymentRepository.java      # Database operations
│   ├── resources/
│   │   └── application.yml                     # Configuration with Razorpay keys
│   └── webapp/WEB-INF/jsp/payment/
│       └── checkout.jsp                        # Payment page with Razorpay Checkout
```

## 🔧 Configuration

### 1. Dependencies (Already Added to pom.xml)
```xml
<dependency>
    <groupId>com.razorpay</groupId>
    <artifactId>razorpay-java</artifactId>
    <version>1.4.5</version>
</dependency>
```

### 2. Application Configuration (application.yml)
```yaml
razorpay:
  key:
    id: ${RAZORPAY_KEY_ID:rzp_test_XXXXXXXXXXX}
    secret: ${RAZORPAY_KEY_SECRET:test_secret_XXXXXXXXXXX}
```

### 3. Environment Variables (.env file)
Create a `.env` file in project root:
```
RAZORPAY_KEY_ID=your_actual_test_key_id
RAZORPAY_KEY_SECRET=your_actual_test_secret
```

## 🎯 API Endpoints

### 1. Create Order
- **URL**: `/api/payment/create-order`
- **Method**: POST
- **Response**:
```json
{
    "success": true,
    "orderId": "order_xxxxxxxxxxxxx",
    "amount": 10000,
    "currency": "INR",
    "keyId": "rzp_test_XXXXXXXXXXX"
}
```

### 2. Verify Payment
- **URL**: `/api/payment/verify-payment`
- **Method**: POST
- **Request Body**:
```json
{
    "orderId": "order_xxxxxxxxxxxxx",
    "paymentId": "pay_xxxxxxxxxxxxx",
    "signature": "signature_string"
}
```
- **Response**:
```json
{
    "success": true,
    "message": "Payment verified successfully",
    "paymentId": 123
}
```

### 3. Payment Page
- **URL**: `/api/payment/payment-page`
- **Method**: GET
- **Description**: Returns the JSP page with Razorpay checkout integration

## 🧪 Testing the Integration

1. **Start the Application**:
   ```bash
   mvn spring-boot:run
   ```

2. **Access the Payment Page**:
   ```
   http://localhost:9090/api/payment/payment-page
   ```

3. **Test Card Details**:
   - Card Number: `4111 1111 1111 1111`
   - Expiry: Any future date (e.g., 12/25)
   - CVV: Any 3 digits (e.g., 123)
   - OTP: Not required in test mode

4. **Payment Flow**:
   1. Click "Pay Now" button
   2. Backend creates Razorpay order
   3. Razorpay Checkout popup opens
   4. Enter test card details
   5. Payment is processed
   6. Backend verifies the signature
   7. Success/failure message is displayed

## 🔄 Switching from Test to Live Mode

### Step 1: Get Live Credentials
1. Login to [Razorpay Dashboard](https://dashboard.razorpay.com)
2. Complete KYC verification
3. Navigate to Settings → API Keys
4. Generate Live Key ID and Secret

### Step 2: Update Configuration

#### Option A: Using Environment Variables (Recommended)
Update your `.env` file:
```
RAZORPAY_KEY_ID=rzp_live_XXXXXXXXXXX
RAZORPAY_KEY_SECRET=live_secret_XXXXXXXXXXX
```

#### Option B: Direct in application.yml (Not Recommended for Production)
```yaml
razorpay:
  key:
    id: rzp_live_XXXXXXXXXXX
    secret: live_secret_XXXXXXXXXXX
```

### Step 3: Update Webhook URL (Optional but Recommended)
1. In Razorpay Dashboard, go to Settings → Webhooks
2. Add webhook endpoint: `https://yourdomain.com/api/payment/webhook`
3. Select events to listen (payment.captured, payment.failed, etc.)

### Step 4: Security Considerations for Production
1. **Never commit live keys to version control**
2. Use environment variables or secure vault
3. Enable webhook signature verification
4. Implement rate limiting on payment endpoints
5. Add CSRF protection for payment forms
6. Use HTTPS in production
7. Implement proper logging and monitoring

## 📊 Database Schema

The Payment entity stores:
- Order ID (Razorpay order ID)
- Payment ID (Razorpay payment ID)
- Signature (for verification)
- Amount (in Rupees)
- Status (PENDING, COMPLETED, FAILED)
- Timestamps

## 🔒 Security Features Implemented

1. **Signature Verification**: All payments are verified using Razorpay's signature
2. **Amount Validation**: Backend controls the payment amount
3. **Status Tracking**: Payment status stored in database
4. **Error Handling**: Comprehensive error handling for failures

## 📝 Additional Notes

1. **Amount Convention**: Razorpay accepts amount in paise (₹1 = 100 paise)
2. **Currency**: Currently set to INR, can be changed in service
3. **Timeout**: Orders expire after 30 minutes by default
4. **Refunds**: Can be implemented using Razorpay Refund API

## 🐛 Troubleshooting

### Common Issues:

1. **"Invalid Key ID"**
   - Check if keys are properly set in application.yml or .env
   - Ensure you're using test keys for test mode

2. **"Signature Verification Failed"**
   - Ensure secret key matches the key ID
   - Check if all three parameters (order_id, payment_id, signature) are sent

3. **CORS Issues**
   - Add appropriate CORS configuration in Spring Security

4. **Payment Not Reflecting**
   - Check database connection
   - Verify Payment entity mappings
   - Check application logs

## 📚 References

- [Razorpay Documentation](https://razorpay.com/docs/)
- [Razorpay Java SDK](https://github.com/razorpay/razorpay-java)
- [Test Cards](https://razorpay.com/docs/payments/payments/test-card-upi-details/)
- [Webhook Integration](https://razorpay.com/docs/webhooks/)

## 💡 Next Steps

1. Implement webhook handler for async payment updates
2. Add email notifications on successful payment
3. Implement refund functionality
4. Add payment history page
5. Implement subscription/recurring payments
6. Add more payment methods (UPI, NetBanking, Wallets)

---

**Note**: Remember to replace test keys with actual Razorpay test/live credentials before testing!