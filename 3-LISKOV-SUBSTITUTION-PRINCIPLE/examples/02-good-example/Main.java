// ✅ GOOD: flight is pulled out into its own capability. Bird no longer
// promises something not every bird can deliver, so every substitution is safe.
class Bird {
    // common bird behavior only: eating, making a sound, etc.
}

interface FlyingBird {
    void fly();
}

class Sparrow extends Bird implements FlyingBird {
    public void fly() {
        System.out.println("Flying");
    }
}

class Penguin extends Bird {
    // no fly() — and no obligation to have one
    public void swim() {
        System.out.println("Swimming");
    }
}

public class Main {
    // Code that needs flight asks for FlyingBird, not Bird —
    // so only birds that CAN fly are ever passed in.
    static void letItFly(FlyingBird bird) {
        bird.fly();
    }

    public static void main(String[] args) {
        letItFly(new Sparrow()); // ✔️ compiles and works
        // letItFly(new Penguin()); // ❌ won't even compile — caught before runtime!

        Penguin penguin = new Penguin();
        penguin.swim();
    }
}

/*
 * Why this is better:
 *  - Every FlyingBird passed to letItFly() is guaranteed to actually fly
 *  - Penguin is still a Bird — it just isn't forced into a contract it can't keep
 *  - The compiler catches the mistake instead of the user hitting a runtime exception
 */
