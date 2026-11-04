public class Patient implements Runnable {

    private final String name;
    private final WaitingRoom waitingRoom;

    public Patient(String name, WaitingRoom waitingRoom) {
        this.name = name;
        this.waitingRoom = waitingRoom;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            Hospital.log(name + " arrived.");
            waitingRoom.enter(this);
            Hospital.log(name + " entered the waiting room.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}