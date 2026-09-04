class Main {
    public static void main(String[] args) {
        String payment = "paypal";
        String email = "rustam.atakisiev@gmail.com";
        double amount = 1205.2;

        OrderService orderService = new OrderService(
            new DiscountService(),
            new PaymentService(),
            new OrderRepository(),
            new EmailService(),
            new LoggerService()
        );

        orderService.checkout(payment, amount, email);
        orderService.refund(payment, amount);
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

class PaymentService {
    public void pay(String paymentType) {
        if(paymentType.equals("credit")) {
            System.out.println("Processing credit card");
        } else if(paymentType.equals("paypal")) {
            System.out.println("Processing PayPal");
        } else if(paymentType.equals("bank")) {
            System.out.println("Processing bank transfer");
        }
    }
}

class LoggerService {
    void log(String message) {
        System.out.println("Logging " + message);
    }
}


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
    
    public void checkout(String paymentType, double amount, String email) {
        
        // calculate discount
        double finalAmount = discount.applyDiscount(amount);

        // process payment
        payment.pay(paymentType);

        Order order = new Order(amount);

        // save order
        repository.save(order);

        // send email
        notification.send(email);

        // logging
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