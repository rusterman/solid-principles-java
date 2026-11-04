class InvoicePrint {
    
    private String customerName;
    private double[] itemPrices;
    private InvoiceCalculate calculate;
    
    public InvoicePrint(String customerName, double[] itemPrices) {
        this.customerName = customerName;
        this.itemPrices = itemPrices;
        this.calculate = new InvoiceCalculate(itemPrices);
    }
    
    public void printInvoice() {
        System.out.println("Invoice for: " + customerName);
        for (double price : itemPrices) {
            System.out.println(" - $" + price);
        }
        System.out.println("Total: $" + calculate.calculateTotal());
    }
}

class InvoiceCalculate {
    private double[] itemPrices;

    public InvoiceCalculate(double[] itemPrices) {
        this.itemPrices = itemPrices;
    }

    public double calculateTotal() {
        double total = 0;
        for (double price : itemPrices) {
            total += price;
        }
        return total;
    }
    
}

class SaveInvoiceFile {
    public void saveToFile(String filename) {
        System.out.println("Saving invoice to " + filename);
    }
}

class EmailInvoice {
    public void emailInvoice(String emailAddress) {
        System.out.println("Emailing invoice to " + emailAddress);
    }
}

class Invoice {
    private String customerName;
    private double[] itemPrices;
    
    public Invoice(String customerName, double[] itemPrices) {
        this.customerName = customerName;
        this.itemPrices = itemPrices;
    }

    public double[] getPrices(){
        return this.itemPrices;
    }
    
    public String getCostomerName(){
        return this.customerName;
    }
}

public class Main {
    public static void main(String[] args) {
        Invoice invoice = new Invoice("Jane Doe", new double[] { 19.99, 42.50, 5.00 });
        InvoicePrint print = new InvoicePrint(invoice.getCostomerName() , invoice.getPrices());
        print.printInvoice();
        SaveInvoiceFile save = new SaveInvoiceFile();
        save.saveToFile("invoice.txt");
        EmailInvoice email = new EmailInvoice();
        email.emailInvoice("example@.com");
    }
}