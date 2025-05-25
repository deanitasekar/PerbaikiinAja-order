# PerbaikiinAja - Order Service

This project is responsible for managing the full process of placing and handling repair service requests, from initial order creation to final completion. Users can place new repair orders, specifying item details, desired repairs, choosing a payment method, and optionally selecting their preferred technician. Technicians receive these orders, provide cost and time estimates, and update the order status as repairs progress. After technicians mark repairs as completed, users receive notifications and can view, manage, or delete these notifications to stay informed without clutter.

## API Reference

### Notifications Endpoints

All endpoints in this section require a valid JWT token in the `Authorization` header:

`Authorization: Bearer <jwt-token>`

#### Get Notifications

```http
GET /notifications
```

**Description:**  
Retrieves a list of notifications for the authenticated user. When the notifications are retrieved, their status will be updated so that each notification’s `isRead` flag is set to `true`.

**Response:**

```json
[
  {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "userId": "a3dda38c-0819-4ee0-ba4f-f124180219e9",
    "title": "Repair Completed",
    "message": "Your repair request #12345 has been completed.",
    "isRead": true,
    "createdAt": "2025-04-09T20:54:40"
  },
  {
    "id": "c9bf9e57-1685-4c89-bafb-ff5af830be8a",
    "userId": "a3dda38c-0819-4ee0-ba4f-f124180219e9",
    "title": "Repair Completed",
    "message": "Your repair request #67890 has been completed.",
    "isRead": true,
    "createdAt": "2025-04-09T21:00:10"
  }
]
```

---

#### Delete Notification

```http
DELETE /notifications/{notificationId}
```

**Description:**  
Deletes a single notification by its ID if it belongs to the authenticated user.

**Path Parameter:**

| Parameter       | Type   | Description                      |
| --------------- | ------ | -------------------------------- |
| `notificationId` | UUID  | **Required**. ID of the notification to delete. |

**Response:**  
A successful response returns an HTTP 200 status. If the notification is not found or the user is not authorized to delete it, an error message is returned.

---

# PerbaikiinAja - Order Service

This project is responsible for managing the full process of placing and handling repair service requests, from initial order creation to final completion. Users can place new repair orders, specifying item details, desired repairs, choosing a payment method, and optionally selecting their preferred technician. Technicians receive these orders, provide cost and time estimates, and update the order status as repairs progress. After technicians mark repairs as completed, users receive notifications and can view, manage, or delete these notifications to stay informed without clutter.

## API Reference

---


## Coupon API

Endpoints for managing discount coupons: creation, retrieval, update, deletion, application, and previewing.

---

### Create a Coupon

```http
POST /coupons
````

**Description**
Creates a new coupon with the given type, discount amount, usage limit, and active period.

**Request Body**

```json
{
  "couponType": "PERCENTAGE",
  "discount_amount": 20,
  "max_usage": 100,
  "start_date": "2025-05-22T00:00:00",
  "end_date": "2025-06-01T23:59:59"
}
```

**Response**
Status `201 Created`

```json
{
  "id": "uuid",
  "code": "PERCENTAGE-ABC",
  "couponType": "PERCENTAGE",
  "discount_amount": 20,
  "max_usage": 100,
  "start_date": "2025-05-22T00:00:00",
  "end_date": "2025-06-01T23:59:59"
}
```

---

### Get All Coupons

```http
GET /coupons
```

**Description**
Retrieves every coupon, including those that may have expired or reached usage limits.

**Response**
Status `200 OK`

```json
{
  "coupons": [
    {
      "id": "uuid",
      "code": "FIXED-XYZ",
      "couponType": "FIXED",
      "discount_amount": 10000,
      "max_usage": 5,
      "start_date": "2025-05-22T00:00:00",
      "end_date": "2025-06-01T23:59:59"
    }
  ],
  "total": 1
}
```

---

### Get Valid Coupons

```http
GET /coupons/valid
```

**Description**
Returns only coupons that are still valid (not expired, not deleted, and below the usage limit).

**Response**
Status `200 OK`

```json
{
  "coupons": [
    {
      "id": "uuid",
      "code": "PERCENTAGE-ABC",
      "couponType": "PERCENTAGE",
      "discount_amount": 20,
      "max_usage": 100,
      "start_date": "2025-05-22T00:00:00",
      "end_date": "2025-06-01T23:59:59"
    }
  ],
  "total": 1
}
```

---

### Get Coupon by ID

```http
GET /coupons/{id}
```

**Path Parameters**

| Name | Type | Description                     |
| ---- | ---- | ------------------------------- |
| `id` | UUID | Unique identifier of the coupon |

**Description**
Fetches detailed information about a specific coupon.

**Response**
Status `200 OK`

```json
{
  "id": "uuid",
  "code": "FIXED-XYZ",
  "couponType": "FIXED",
  "discount_amount": 10000,
  "max_usage": 5,
  "start_date": "2025-05-22T00:00:00",
  "end_date": "2025-06-01T23:59:59"
}
```

---

### Update Coupon

```http
PUT /coupons/{id}
```

**Path Parameters**

| Name | Type | Description         |
| ---- | ---- | ------------------- |
| `id` | UUID | Coupon ID to update |

**Description**
Updates coupon fields such as type, discount amount, and usage limits.

**Request Body**

```json
{
  "couponType": "FIXED",
  "discount_amount": 10000,
  "max_usage": 5,
  "start_date": "2025-05-22T00:00:00",
  "end_date": "2025-06-01T23:59:59"
}
```

**Response**
Status `200 OK`

```json
{
  "id": "uuid",
  "code": "FIXED-XYZ",
  "couponType": "FIXED",
  "discount_amount": 10000,
  "max_usage": 5,
  "start_date": "2025-05-22T00:00:00",
  "end_date": "2025-06-01T23:59:59"
}
```

---

### Delete Coupon

```http
DELETE /coupons/{id}
```

**Path Parameters**

| Name | Type | Description         |
| ---- | ---- | ------------------- |
| `id` | UUID | Coupon ID to delete |

**Description**
Permanently deletes a coupon.

**Response**
Status `200 OK`

```json
{
  "success": true,
  "message": "Coupon deleted successfully"
}
```

---

### Apply Coupon

```http
POST /coupons/{id}/apply
```

**Path Parameters**

| Name | Type | Description        |
| ---- | ---- | ------------------ |
| `id` | UUID | Coupon ID to apply |

**Description**
Applies the coupon to a given price and increases its usage count.

**Request Body**

```json
{
  "original_price": 100000
}
```

**Response**
Status `200 OK`

```json
{
  "original_price": 100000,
  "discounted_price": 80000,
  "coupon_code": "PERCENTAGE-XYZ",
  "valid": true
}
```

---

### Preview Coupon

```http
POST /coupons/{id}/preview
```

**Path Parameters**

| Name | Type | Description          |
| ---- | ---- | -------------------- |
| `id` | UUID | Coupon ID to preview |

**Description**
Returns the calculated discount without increasing the coupon’s usage.

**Request Body**

```json
{
  "original_price": 100000
}
```

**Response**
Status `200 OK`

```json
{
  "original_price": 100000,
  "discounted_price": 80000,
  "coupon_code": "PERCENTAGE-XYZ",
  "valid": true
}
```


---


## Environment Configuration

Before running the application, create a `.env` file in the project root with the following example settings. Replace these dummy values with your actual configuration as needed.

```dotenv
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/perbaikiinaja
DATABASE_USERNAME=your_db_username
DATABASE_PASSWORD=your_db_password

# JWT
# The decoded secret must be at least 512 bits (64 bytes) in length.
JWT_SECRET="your_jwt_secret_key"
```
