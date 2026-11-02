
import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {

    // طابور المرضى الموجودين في غرفة الانتظار
    private final Queue<Patient> queue;

    // عدد الأماكن الفاضية في غرفة الانتظار
    private final Semaphore empty;

    // عدد المرضى المتاحين للدكاترة
    private final Semaphore full;

    // حماية الـ Queue من التعديل بواسطة أكثر من Thread في نفس الوقت
    private final Semaphore mutex;

    // إنشاء غرفة الانتظار وتحديد سعتها
    public WaitingRoom(int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                "Waiting room capacity must be greater than zero"
            );
        }

        queue = new LinkedList<>();

        // في البداية كل الأماكن فاضية
        empty = new Semaphore(capacity);

        // في البداية مفيش أي مرضى في الغرفة
        full = new Semaphore(0);

        // السماح لـ Thread واحد فقط بتعديل الـ Queue في نفس الوقت
        mutex = new Semaphore(1);
    }

    // محاولة دخول المريض إلى غرفة الانتظار
    public void enter(Patient patient)
            throws InterruptedException {

        addPatient(patient);
    }

    // الدكتور يأخذ أول مريض موجود في الطابور
    public Patient takePatient()
            throws InterruptedException {

        // الانتظار لحد ما يكون فيه مريض متاح
        full.acquire();

        // الحصول على قفل حماية الـ Queue
        try {
            mutex.acquire();
        } catch (InterruptedException e) {

            // إعادة تصريح المريض لو حصلت مقاطعة
            full.release();
            throw e;
        }

        Patient patient;

        try {
            // سحب أول مريض دخل الطابور
            patient = queue.remove();

        } finally {
            // تحرير القفل للسماح لباقي الـ Threads بالدخول
            mutex.release();
        }

        // أصبح فيه مكان فاضي بعد خروج المريض
        empty.release();

        return patient;
    }

    // إعادة المريض إلى غرفة الانتظار لو لم يجد الدكتور غرفة علاج
    public void returnPatient(Patient patient)
            throws InterruptedException {

        addPatient(patient);
    }

    // دالة مشتركة لإضافة مريض إلى الطابور
    private void addPatient(Patient patient)
            throws InterruptedException {

        if (patient == null) {
            throw new IllegalArgumentException(
                "Patient cannot be null"
            );
        }

        // الانتظار لحد ما يكون فيه مكان فاضي
        empty.acquire();

        // الحصول على قفل حماية الـ Queue
        try {
            mutex.acquire();
        } catch (InterruptedException e) {

            // إعادة تصريح المكان الفاضي لو حصلت مقاطعة
            empty.release();
            throw e;
        }

        try {
            // إضافة المريض في نهاية الطابور
            queue.add(patient);

        } finally {
            // تحرير القفل بعد الانتهاء من تعديل الطابور
            mutex.release();
        }

        // إعلام الدكاترة بوجود مريض جديد متاح
        full.release();
    }
}
