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


### Profilling
![call tree](images/profiling/calltree.png)
![flamecgraph](images/profiling/flamegraph.png)
![method list](images/profiling/methodlist.png)

Profil performa menunjukkan beberapa bottleneck utama dalam aplikasi. Pertama, overhead refleksi yang tinggi terlihat pada pemanggilan java.lang.reflect.Method.invoke dan DirectMethodHandleAccessor.Invoke. Kedua, proses startup Spring Boot, seperti refreshContext dan createBean, menghabiskan banyak waktu, menunjukkan perlunya optimasi inisialisasi. Ketiga, aktivitas RestartLauncher.run mengindikasikan bahwa fitur DevTools yang digunakan dalam pengembangan mungkin tidak perlu dijalankan di lingkungan produksi. Selain itu, manajemen thread dan garbage collection juga memerlukan perhatian, terutama pada Thread.run dan ForkJoinWorkerThread. Untuk meningkatkan performa, terdapat beberapa peningkatan, seperti menonaktifkan DevTools di produksi akan menghilangkan overhead yang tidak diperlukan. Kemudian, optimasi manajemen thread dan memori, seperti menggunakan thread pooling dan mengurangi alokasi objek sementara, akan membantu meningkatkan efisiensi.