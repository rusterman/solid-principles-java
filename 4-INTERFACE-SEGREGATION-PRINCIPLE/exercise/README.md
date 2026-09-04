# ISP Practice Exercise — Office Printer Fleet

## 🎯 Goal

Split a fat interface into focused ones, so a device that can only print is never forced to
fake scanning or faxing.

## 📦 Scenario

An office-equipment app models every device with one interface, see
[`Starter.java`](Starter.java):

```java
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
```

`OldPrinter` can only print — but the interface forces it to also "implement" `scan()` and
`fax()`, and both just throw. Any code holding a `MultiFunctionDevice` reference now has to
guess (or discover at runtime) which methods are actually safe to call.

## 🛠️ Your task

Split `MultiFunctionDevice` into small, focused interfaces:

- `Printer` — `print(String document)`
- `Scanner` — `scan(String document)`
- `FaxMachine` — `fax(String document)`

Then:

1. `OldPrinter` implements only `Printer`.
2. Add a `SmartOfficeMachine` that implements all three (`Printer`, `Scanner`, `FaxMachine`).
3. Write a helper `void printDocument(Printer printer, String doc)` that works with **any**
   printer-capable device.

## ✅ Requirements / acceptance criteria

- [ ] No class has a method body that throws `UnsupportedOperationException` (or is otherwise
      a fake/empty implementation) just to satisfy an interface
- [ ] `OldPrinter` implements `Printer` only — it must be **impossible to compile** a call to
      `.scan()` or `.fax()` on an `OldPrinter` reference, not just impossible at runtime
- [ ] `SmartOfficeMachine` implements `Printer`, `Scanner`, and `FaxMachine`
- [ ] `main()` demonstrates `printDocument()` working for both `OldPrinter` and
      `SmartOfficeMachine`, plus `SmartOfficeMachine` also scanning and faxing

## 💡 Hint

You'll know you're done when the compiler itself — not a thrown exception — is what stops you
from calling `.scan()` on an `OldPrinter`.

## Submitting your solution

See [Submitting your solutions](../../README.md#submitting-your-solutions) in the root
README for the branch/commit convention. Suggested commit message:

```
exercise: ISP - office printer fleet
```
