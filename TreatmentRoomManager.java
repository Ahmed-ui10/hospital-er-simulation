import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TreatmentRoomManager {

    // عدد غرف العلاج ا
    private static final int TREATMENT_ROOMS_COUNT = 2;

    // Semaphore للتحكم في دخول غرف العلاج
    private final Semaphore treatmentRoomsSemaphore;

    public TreatmentRoomManager() {
        // true تعني أن الدخول يكون حسب ترتيب الانتظار
        this.treatmentRoomsSemaphore = new Semaphore(TREATMENT_ROOMS_COUNT, true);
    }

    // محاولة حجز غرفة خلال وقت محدد
    public boolean acquireRoom(long timeoutMillis) throws InterruptedException {
        return treatmentRoomsSemaphore.tryAcquire(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    // حجز غرفة والانتظار حتى تصبح غرفة متاحة
    public void acquireRoom() throws InterruptedException {
        treatmentRoomsSemaphore.acquire();
    }

    // تحرير غرفة العلاج بعد الانتهاء منها
    public void releaseRoom() {
        treatmentRoomsSemaphore.release();
    }

    // إرجاع عدد الغرف المتاحة حالياً
    public int getAvailableRoomsCount() {
        return treatmentRoomsSemaphore.availablePermits();
    }
}
