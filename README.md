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

### Orders Endpoints

All endpoints in this section require a valid JWT token in the `Authorization` header:

`Authorization: Bearer <jwt-token>`

#### Create Order

```http
POST /orders
```

**Description:**
Creates a new order for the authenticated user. The customer ID is automatically extracted from the JWT token.

**Request Body:**
```json
{
  "itemName": "iPhone 14 Pro",
  "itemCondition": "Screen cracked",
  "repairDetails": "Replace screen and check internal components",
  "serviceDate": "2025-06-01T10:00:00",
  "technicianId": "b1234567-8901-2345-6789-012345678901",
  "paymentMethodId": "c2345678-9012-3456-7890-123456789012",
  "couponId": "d3456789-0123-4567-8901-234567890123",
  "estimatedCompletionTime": "2025-06-03T16:00:00",
  "estimatedPrice": 250000
}
```

**Response:**
```json
{
  "order": {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "customerId": "a3dda38c-0819-4ee0-ba4f-f124180219e9",
    "itemName": "iPhone 14 Pro",
    "itemCondition": "Screen cracked",
    "repairDetails": "Replace screen and check internal components",
    "serviceDate": "2025-06-01T10:00:00",
    "technicianId": "b1234567-8901-2345-6789-012345678901",
    "paymentMethodId": "c2345678-9012-3456-7890-123456789012",
    "couponId": "d3456789-0123-4567-8901-234567890123",
    "estimatedCompletionTime": "2025-06-03T16:00:00",
    "estimatedPrice": 250000,
    "finalPrice": null,
    "completedAt": null,
    "createdAt": "2025-05-27T10:30:00"
  },
  "message": "Order created successfully"
}
```

#### Get Order History

```http
GET /orders
```

**Description:**
Retrieves all orders for the authenticated user.

**Response:**
```json
{
  "orders": [
    {
      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "customerId": "a3dda38c-0819-4ee0-ba4f-f124180219e9",
      "itemName": "iPhone 14 Pro",
      "itemCondition": "Screen cracked",
      "repairDetails": "Replace screen and check internal components",
      "serviceDate": "2025-06-01T10:00:00",
      "estimatedPrice": 250000,
      "finalPrice": 230000,
      "status": "COMPLETED",
      "createdAt": "2025-05-27T10:30:00",
      "completedAt": "2025-06-03T15:45:00"
    }
  ]
}
```

#### Get Order Detail

```http
GET /orders/{orderId}
```

**Description:**
Retrieves detailed information for a specific order. Users can only access their own orders.

**Path Parameter:**
| Parameter       | Type   | Description                      |
| --------------- | ------ | -------------------------------- |
| `orderId` | UUID  | **Required**. ID of the order to retrieve. |

**Response:**
```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "customerId": "a3dda38c-0819-4ee0-ba4f-f124180219e9",
  "itemName": "iPhone 14 Pro",
  "itemCondition": "Screen cracked",
  "repairDetails": "Replace screen and check internal components",
  "serviceDate": "2025-06-01T10:00:00",
  "technicianId": "b1234567-8901-2345-6789-012345678901",
  "paymentMethodId": "c2345678-9012-3456-7890-123456789012",
  "couponId": "d3456789-0123-4567-8901-234567890123",
  "estimatedCompletionTime": "2025-06-03T16:00:00",
  "estimatedPrice": 250000,
  "finalPrice": 230000,
  "completedAt": "2025-06-03T15:45:00",
  "createdAt": "2025-05-27T10:30:00"
}
```


#### Update Order

```http
PUT /orders/{orderId}
```

**Description:**
Updates an existing order. Users can only update their own orders. Only non-null fields in the request body will be updated.

**Path Parameter:**
| Parameter       | Type   | Description                      |
| --------------- | ------ | -------------------------------- |
| `orderId` | UUID  | **Required**. ID of the order to update. |

**Request Body:**
json{
  "itemName": "iPhone 14 Pro Max",
  "repairDetails": "Replace screen, battery, and check internal components",
  "estimatedPrice": 300000
}

**Response:**
```json
{
  "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "customerId": "a3dda38c-0819-4ee0-ba4f-f124180219e9",
  "itemName": "iPhone 14 Pro Max",
  "itemCondition": "Screen cracked",
  "repairDetails": "Replace screen, battery, and check internal components",
  "serviceDate": "2025-06-01T10:00:00",
  "technicianId": "b1234567-8901-2345-6789-012345678901",
  "paymentMethodId": "c2345678-9012-3456-7890-123456789012",
  "couponId": "d3456789-0123-4567-8901-234567890123",
  "estimatedCompletionTime": "2025-06-03T16:00:00",
  "estimatedPrice": 300000,
  "finalPrice": null,
  "completedAt": null,
  "createdAt": "2025-05-27T10:30:00"
}
```


#### Cancel Order

```http
DELETE /orders/{orderId}
```

**Description:**
Cancels/deletes an order. Users can only cancel their own orders.

**Path Parameter:**
| Parameter       | Type   | Description                      |
| --------------- | ------ | -------------------------------- |
| `orderId` | UUID  | **Required**. ID of the order to cancel. |

Response:
A successful response returns an HTTP 204 (No Content) status. If the order is not found or the user is not authorized to cancel it, an error status is returned.

#### Get All Orders (Admin)
Requires ADMIN role in addition to a valid JWT token.

```http
ET /orders/admin
```

**Description:**
Retrieves all orders in the system. This endpoint is only accessible by users with ADMIN role.

**Response:**
```json
{
  "orders": [
    {
      "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
      "customerId": "a3dda38c-0819-4ee0-ba4f-f124180219e9",
      "itemName": "iPhone 14 Pro",
      "itemCondition": "Screen cracked",
      "repairDetails": "Replace screen and check internal components",
      "serviceDate": "2025-06-01T10:00:00",
      "estimatedPrice": 250000,
      "finalPrice": 230000,
      "status": "COMPLETED",
      "createdAt": "2025-05-27T10:30:00",
      "completedAt": "2025-06-03T15:45:00"
    },
    {
      "id": "c9bf9e57-1685-4c89-bafb-ff5af830be8a",
      "customerId": "b4eeb28c-1820-5ff1-cb5f-g235291320f0",
      "itemName": "Samsung Galaxy S24",
      "itemCondition": "Battery draining fast",
      "repairDetails": "Replace battery",
      "serviceDate": "2025-06-02T14:00:00",
      "estimatedPrice": 150000,
      "finalPrice": null,
      "status": "IN_PROGRESS",
      "createdAt": "2025-05-28T09:15:00",
      "completedAt": null
    }
  ]
}
```

**Error Responses**
All order endpoints may return the following error responses:
- 403 Forbidden: User does not have permission to access the resource
- 404 Not Found: The requested order does not exist
- 500 Internal Server Error: An unexpected error occurred


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

### Monitoring
![Monitoring](images/monitoring/monitoring1.png)
![Monitoring](images/monitoring/monitoring2.png)
![Monitoring](images/monitoring/monitoring3.png)
![Monitoring](images/monitoring/monitoring4.png)

### Profilling
![call tree](images/profiling/calltree.png)
![flamecgraph](images/profiling/flamegraph.png)
![method list](images/profiling/methodlist.png)

Profil performa menunjukkan beberapa bottleneck utama dalam aplikasi. Pertama, overhead refleksi yang tinggi terlihat pada pemanggilan java.lang.reflect.Method.invoke dan DirectMethodHandleAccessor.Invoke. Kedua, proses startup Spring Boot, seperti refreshContext dan createBean, menghabiskan banyak waktu, menunjukkan perlunya optimasi inisialisasi. Ketiga, aktivitas RestartLauncher.run mengindikasikan bahwa fitur DevTools yang digunakan dalam pengembangan mungkin tidak perlu dijalankan di lingkungan produksi. Selain itu, manajemen thread dan garbage collection juga memerlukan perhatian, terutama pada Thread.run dan ForkJoinWorkerThread. Untuk meningkatkan performa, terdapat beberapa peningkatan, seperti menonaktifkan DevTools di produksi akan menghilangkan overhead yang tidak diperlukan. Kemudian, optimasi manajemen thread dan memori, seperti menggunakan thread pooling dan mengurangi alokasi objek sementara, akan membantu meningkatkan efisiensi.