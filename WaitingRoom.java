import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {

    private final Queue<Patient> queue;
    private final Semaphore empty;
    private final Semaphore full;
    private final Semaphore mutex;

    public WaitingRoom(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Waiting room capacity must be greater than zero");
        }
        queue = new LinkedList<>();
        empty = new Semaphore(capacity);
        full = new Semaphore(0);
        mutex = new Semaphore(1);
    }

    public void enter(Patient patient) throws InterruptedException {
        if (!empty.tryAcquire(0)) {
            Hospital.log("The waiting room is full.");
            Hospital.log(patient.getName() + " is waiting outside for an available seat.");
            empty.acquire();
        }
        addToQueue(patient);
    }

    public Patient takePatient() throws InterruptedException {
        full.acquire();
        mutex.acquire();
        Patient patient;
        try {
            patient = queue.remove();
        } finally {
            mutex.release();
        }
        empty.release();
        return patient;
    }

    public void returnPatient(Patient patient) throws InterruptedException {
        if (!empty.tryAcquire(0)) {
            Hospital.log("The waiting room is full.");
            Hospital.log(patient.getName() + " is waiting outside for an available seat.");
            empty.acquire();
        }
        addToQueue(patient);
    }

    private void addToQueue(Patient patient) throws InterruptedException {
        mutex.acquire();
        try {
            queue.add(patient);
        } finally {
            mutex.release();
        }
        full.release();
    }
}