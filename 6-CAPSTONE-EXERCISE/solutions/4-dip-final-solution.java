// ===================== Implementation =====================

class Main {
    public static void main(String[] args) {

        double amount = 1205.2;
        String email = "rustam.atakisiev@gmail.com";

        // Choose payment method (OCP)
        PaymentMethod paymentMethod = new PayPalPayment();
        PaymentService paymentService = new PaymentProcessor(paymentMethod);

        OrderService orderService = new OrderService(
            new DefaultDiscountService(),
            paymentService,
            new DatabaseOrderRepository(),
            new EmailService(),
            new ConsoleLogger()
        );

        orderService.checkout(amount, email);
        
        // Refund only if supported
        RefundablePayment refundable = new PayPalPayment();
        RefundService refundService  = new RefundService(refundable);

        refundService.refund(amount);
    }
}


// ===================== DOMAIN =====================
// Core business objects that represent real-world entities
// Example: Order is the main entity we're working with
// Principle: SRP - Domain objects focus only on data, not behavior

class Order {
    double amount;

    Order(double amount) {
        this.amount = amount;
    }
}


// ===================== ABSTRACTIONS (DIP) =====================
// Interfaces that services depend on - following Dependency Inversion Principle
// These allow OrderService to depend on abstractions, not concrete implementations
// Principle: DIP - High-level modules should not depend on low-level modules
// Both should depend on abstractions (interfaces)

interface DiscountPolicy {
    double applyDiscount(double amount);
}

interface OrderRepository {
    void save(Order order);
}

interface NotificationService {
    void send(String email);
}

interface Logger {
    void log(String message);
}


// ===================== SRP IMPLEMENTATIONS =====================
// Concrete implementations of DIP abstractions
// Each class has Single Responsibility: DiscountService handles discounts only,
// EmailService handles emails only, etc.
// Principle: SRP - Each class should have one reason to change

class DefaultDiscountService implements DiscountPolicy {
    public double applyDiscount(double amount) {
        if (amount > 1000)
            return amount * 0.9;
        return amount;
    }
}

class DatabaseOrderRepository implements OrderRepository {
    public void save(Order order) {
        System.out.println("Saving order to database with amount: " + order.amount);
    }
}

class EmailService implements NotificationService {
    public void send(String email) {
        System.out.println("Sending email to " + email);
    }
}

class SmsService implements NotificationService {
    public void send(String phoneNumber) {
        System.out.println("Sending SMS to " + phoneNumber);
    }
}

class ConsoleLogger implements Logger {
    public void log(String message) {
        System.out.println("Logging: " + message);
    }
}


// ===================== OCP IMPLEMENTATIONS =====================
// PaymentMethod interface allows extending payment types without modifying existing code
// New payment methods (Apple Pay, Google Pay, etc.) can be added without changing OrderService
// Principle: OCP - Open for extension, closed for modification

interface PaymentMethod {
    public void pay(double amount);
}

// ===================== LSP + ISP - IMPLEMENTATIONS =====================
// RefundablePayment interface segregates refund capability from payment
// BankPayment implements only PaymentMethod (no refunds)
// CreditCard/PayPal implement both (accepts refunds)
// Principle: ISP - Clients should depend only on the interfaces they use
// Principle: LSP - Subtypes must be substitutable without breaking behavior

interface RefundablePayment {
    public void refund(double amount);
}

class CreditCardPayment implements PaymentMethod, RefundablePayment {
    public void pay(double amount) {
        System.out.println("Processing credit card payment: " + amount);
    }

    public void refund(double amount) {
        System.out.println("Refunding credit card payment: " + amount);
    }
}

class PayPalPayment implements PaymentMethod, RefundablePayment {
    public void pay(double amount) {
        System.out.println("Processing PayPal payment: " + amount);
    }

    public void refund(double amount) {
        System.out.println("Refunding PayPal payment: " + amount);
    }
}

class BankPayment implements PaymentMethod {
    public void pay(double amount) {
        System.out.println("Processing bank transfer payment: " + amount);
    }
}


// ===================== SERVICES =====================
// ** WHY TWO PAYMENT INTERFACES? **
// PaymentMethod (OCP): Represents WHAT payment type (CreditCard, PayPal, Bank)
//   - Allows extending payment types without modifying code
//   - Used by: PaymentProcessor and RefundService
// 
// PaymentService (DIP): Represents WHO does the payment processing
//   - PaymentProcessor is the concrete implementation
//   - Allows OrderService to depend on abstraction, not concrete PaymentProcessor
//   - Decouples OrderService from payment processing details
//   - Principle: DIP - Depend on abstractions, not concrete implementations
//
// Together: Separation of concerns
//   - PaymentMethod: Strategy pattern (what payment type to use)
//   - PaymentService: Service abstraction (how to process payment)

interface PaymentService {
    void pay(double amount);
}

class PaymentProcessor implements PaymentService {
    private PaymentMethod paymentStrategy;

    PaymentProcessor(PaymentMethod method) {
        this.paymentStrategy = method;
    }
    
    public void pay(double amount) {
        paymentStrategy.pay(amount);
    }
}

class RefundService {
    private RefundablePayment refundablePayment;

    RefundService(RefundablePayment refundablePayment) {
        this.refundablePayment = refundablePayment;
    }

    void refund(double amount) {
        refundablePayment.refund(amount);
    }
}


// ===================== Orchestration =====================
// OrderService orchestrates all services to fulfill a checkout operation
// - Depends on ABSTRACTIONS (PaymentService, DiscountPolicy, etc.) via DIP
// - Does NOT know implementation details (PaymentProcessor, DatabaseOrderRepository, etc.)
// - Easy to test: can inject mock implementations
// - Easy to extend: swap implementations without changing OrderService
// Principle: DIP - All dependencies are interfaces, allowing flexibility

class OrderService {

    // All dependencies are ABSTRACTIONS (interfaces), not concrete classes
    // This allows OrderService to focus on business logic, not implementation details
    private DiscountPolicy discount;     
    private PaymentService payment;       
    private OrderRepository repository;   
    private NotificationService notification;    
    private Logger logger;                

    OrderService (
        DiscountPolicy discount,
        PaymentService payment,
        OrderRepository repository,
        NotificationService notification,
        Logger logger
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
}