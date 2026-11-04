public class TreatmentRoomManager {

    private static final int TREATMENT_ROOMS_COUNT = 2;
    private final Semaphore treatmentRoomsSemaphore;

    public TreatmentRoomManager() {
        this.treatmentRoomsSemaphore = new Semaphore(TREATMENT_ROOMS_COUNT);
    }

    public boolean acquireRoom(long timeoutMillis) throws InterruptedException {
        return treatmentRoomsSemaphore.tryAcquire(timeoutMillis);
    }

    public void acquireRoom() throws InterruptedException {
        treatmentRoomsSemaphore.acquire();
    }

    public void releaseRoom() {
        treatmentRoomsSemaphore.release();
    }

    public int getAvailableRoomsCount() {
        return treatmentRoomsSemaphore.availablePermits();
    }
}