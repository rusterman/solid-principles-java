// ===================== Implementation =====================
class Main {
    public static void main(String[] args) {

        double amount = 1205.2;
        String email = "rustam.atakisiev@gmail.com";

        // Choose payment method (OCP)
        PaymentMethod paymentMethod = new PayPalPayment();
        PaymentService paymentService = new PaymentService(paymentMethod);

        OrderService orderService = new OrderService(
            new DiscountService(),
            paymentService,
            new OrderRepository(),
            new EmailService(),
            new LoggerService()
        );

        orderService.checkout(amount, email);
        orderService.refund("paypal", amount);
    }
}


// ===================== SRP =====================
class DiscountService {
    public double applyDiscount(double amount) {
        if (amount > 1000)
            return amount * 0.9;
        return amount;
    }
}

class Order {
    double amount;

    Order(double amount) {
        this.amount = amount;
    }
}

class OrderRepository {
    public void save(Order order) {
        System.out.println("Saving order to database with amount: " + order.amount);
    }
}

class EmailService {
    public void send(String email) {
        System.out.println("Sending email to " + email);
    }
}

class LoggerService {
    void log(String message) {
        System.out.println("Logging " + message);
    }
}


// ===================== OCP =====================
interface PaymentMethod {
    public void pay(double amount);
}

class CreditCardPayment implements PaymentMethod {
    public void pay(double amount) {
        System.out.println("Processing credit card payment: " + amount);
    }
}

class PayPalPayment implements PaymentMethod {
    public void pay(double amount) {
        System.out.println("Processing PayPal payment: " + amount);
    }
}

class BankPayment implements PaymentMethod {
    public void pay(double amount) {
        System.out.println("Processing bank transfer payment: " + amount);
    }
}

class PaymentService {
    private PaymentMethod paymenStrategy;

    PaymentService(PaymentMethod method) {
        this.paymenStrategy = method;
    }
    
    void pay(double amount) {
        paymenStrategy.pay(amount);
    }
}


// ===================== Orchestration =====================
class OrderService {

    private DiscountService discount;
    private PaymentService payment;
    private OrderRepository repository;
    private EmailService notification;
    private LoggerService logger;

    OrderService (
        DiscountService discount,
        PaymentService payment,
        OrderRepository repository,
        EmailService notification,
        LoggerService logger
    ) {
        this.discount = discount;
        this.payment = payment;
        this.repository = repository;
        this.notification = notification;
        this.logger = logger;
    }
    
    public void checkout(double amount, String email) {
        
        // Apply discount
        double finalAmount = discount.applyDiscount(amount);

        // Process payment
        payment.pay(finalAmount);

        // Save order
        Order order = new Order(amount);
        repository.save(order);

        // Send notification
        notification.send(email);

        // Log
        logger.log("Order Completed");
    }

    public void refund(String paymentType, double amount) {
        if(paymentType.equals("credit") || paymentType.equals("paypal")) {
            System.out.println("Refund processed");
        } else {
            throw new RuntimeException("Refund not supported");
        }
    }
}