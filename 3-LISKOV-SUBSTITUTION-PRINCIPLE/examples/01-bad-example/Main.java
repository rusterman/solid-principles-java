// ❌ BAD: Penguin IS-A Bird, but it cannot honor Bird's contract.
// Anywhere code expects "a Bird that can fly()", a Penguin blows it up.
class Bird {
    public void fly() {
        System.out.println("Flying");
    }
}

class Penguin extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguins can't fly!");
    }
}

public class Main {
    static void letItFly(Bird bird) {
        bird.fly(); // works for Bird and Sparrow-like birds... but not for Penguin
    }

    public static void main(String[] args) {
        letItFly(new Bird());
        letItFly(new Penguin()); // 💥 throws at runtime
    }
}

/*
 * Problems with this design:
 *  - Penguin is substitutable in the TYPE SYSTEM but not in BEHAVIOR
 *  - Any code written against Bird now has to special-case Penguin
 *  - The inheritance relationship models "what a Penguin technically is",
 *    not "what a Penguin can actually do"
 */
