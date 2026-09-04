// ❌ BAD: one "fat" interface forces every implementer to deal with
// methods that make no sense for them.
interface Worker {
    void work();
    void eat();
}

class Human implements Worker {
    public void work() {
        System.out.println("Working");
    }

    public void eat() {
        System.out.println("Eating");
    }
}

class Robot implements Worker {
    public void work() {
        System.out.println("Working");
    }

    // A Robot doesn't eat — but the interface demands the method anyway.
    public void eat() {
        throw new UnsupportedOperationException("Robots don't eat!");
    }
}

public class Main {
    public static void main(String[] args) {
        Worker robot = new Robot();
        robot.work();
        robot.eat(); // 💥 throws at runtime
    }
}

/*
 * Problems with this design:
 *  - Robot is forced to implement (and either fake or blow up on) eat()
 *  - Callers holding a Worker reference can't know which methods are "real"
 *  - Any change to Worker (e.g. adding sleep()) ripples into every implementer,
 *    even ones that have nothing to do with sleeping
 */
