
public class Patient implements Runnable {

    // اسم المريض
    private final String name;

    // مرجع لغرفة الانتظار 
    private final WaitingRoom waitingRoom;

    // مرجع لاسم المريض وغرفة الانتظار
    public Patient(String name, WaitingRoom waitingRoom) {

        this.name = name;

        this.waitingRoom = waitingRoom;
    }
    // رجاع الاسم
    public String getName() {
        return name;
    }
    @Override
    public void run() {

        try {

            // وصول المريض
            System.out.println(name + " arrived.");

            //   محاوله المريض  دخل غرفة الانتظار
            System.out.println(name + " is trying to enter the waiting room.");
            waitingRoom.enter(this);

            // توضح أن المريض دخل غرفة الانتظار بنجاح
            System.out.println(name + " entered the waiting room.");

        } catch (InterruptedException e) {

            // إعادة ضبط حالة المقاطعة إذا تم إيقاف انتظار الـ 
            Thread.currentThread().interrupt();
        }
    }
}
