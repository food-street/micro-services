## Handling payments

Order service will communicate with payment service to intiate payment and the workflow will be as follows.

![pay-flow](/order-service/src/main/resources/payment-flow.png)

```mermaid
sequenceDiagram
    participant C as Client
    participant PG as Payment Gateway
    participant PS as Payment Service
    participant MB as Message Broker
    participant OS as Order Service
    participant NS as Notification Service

    C->>OS: Create Order
    OS->>PS: Initiate Payment
    PS->>PG: Get Payment URL
    PS-->>C: Return Payment URL
    C->>PG: Redirect User
    PG->>PS: Payment Webhook
    PG-->>C: Redirect to Success URL
    C-->>OS: Subscribe to Order Updates
    PS->>MB: Payment Success Event
    MB->>OS: Order Update Event
    OS-->>C: Receive Status Update
    OS->>NS: Send Notification
    NS-->>C: Push Notification
```

For simplicity, we'll skip the message broker in the initial implementation.