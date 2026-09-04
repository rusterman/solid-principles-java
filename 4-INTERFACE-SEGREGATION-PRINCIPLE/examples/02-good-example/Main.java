// ✅ GOOD: split the fat interface into small, focused ones.
// Each class implements only what it can genuinely support.
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

class Human implements Workable, Eatable {
    public void work() {
        System.out.println("Working");
    }

    public void eat() {
        System.out.println("Eating");
    }
}

class Robot implements Workable {
    public void work() {
        System.out.println("Working");
    }
    // No eat() to fake — Robot simply isn't Eatable.
}

public class Main {
    static void putToWork(Workable worker) {
        worker.work();
    }

    public static void main(String[] args) {
        putToWork(new Human());
        putToWork(new Robot());

        Eatable human = new Human();
        human.eat();
        // Eatable robot = new Robot(); // ❌ won't compile — Robot isn't Eatable
    }
}

/*
 * Why this is better:
 *  - No class is forced to implement a method that doesn't apply to it
 *  - Interfaces describe real capabilities: Workable, Eatable — nothing more
 *  - Adding Sleepable later only affects classes that actually sleep
 */
