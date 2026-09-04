// ISP Practice Exercise — starter code. OldPrinter is forced to fake scan()
// and fax(). Split MultiFunctionDevice into focused interfaces instead.
// See README.md in this folder for the full task and requirements.

interface MultiFunctionDevice {
    void print(String document);
    void scan(String document);
    void fax(String document);
}

class OldPrinter implements MultiFunctionDevice {
    public void print(String document) {
        System.out.println("Printing: " + document);
    }

    public void scan(String document) {
        throw new UnsupportedOperationException("This printer can't scan");
    }

    public void fax(String document) {
        throw new UnsupportedOperationException("This printer can't fax");
    }
}

public class Starter {
    public static void main(String[] args) {
        MultiFunctionDevice printer = new OldPrinter();
        printer.print("resume.pdf");
        printer.scan("resume.pdf"); // 💥 throws at runtime
    }
}
