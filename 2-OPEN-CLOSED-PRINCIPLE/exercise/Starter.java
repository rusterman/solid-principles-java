interface DiscountPolicy {
    double apply(double amount);
}

class RegularCustomerDiscount implements DiscountPolicy {
    public double apply(double amount){
        return amount - (amount * 0.0);
    }
}

class PremiumCustomerDiscount implements DiscountPolicy {
    public double apply(double amount){
        return amount - (amount * 0.1);
    }
}

class VipCustomerDiscount implements DiscountPolicy {
    public double apply(double amount){
       return amount - (amount * 0.2);
    }
}

class PricingService {
    void process(DiscountPolicy discount, double amount){
        discount.apply(amount);
        System.out.println(discount.apply(amount));
    }
}

class Main {
    public static void main(String[] args) {
        PricingService ser = new PricingService();
        ser.process(new VipCustomerDiscount(), 20);

        DiscountPolicy StudentCustomerDiscount = new DiscountPolicy() {
            public double apply(double amount){
               return amount - (amount * 0.15);
            }
        };

        PricingService ser2 = new PricingService();
        ser2.process(StudentCustomerDiscount, 20);
    }
}
