
public class Doctor extends Thread {

    // رقم الدكتور
    private final int doctorId;

    // غرفة الانتظار المشتركة
    private final WaitingRoom waitingRoom;

    // مدير غرف العلاج
    private final TreatmentRoomManager roomManager;

    // إنشاء الدكتور وتحديد الموارد التي سيستخدمها
    public Doctor(int doctorId,
                  WaitingRoom waitingRoom,
                  TreatmentRoomManager roomManager) {

        this.doctorId = doctorId;
        this.waitingRoom = waitingRoom;
        this.roomManager = roomManager;
    }

    // تشغيل الدكتور
    public void run() {

        try {

            // الدكتور يفضل يستقبل المرضى
            while (true) {

                // أخذ أول مريض من غرفة الانتظار
                Patient patient = waitingRoom.takePatient();

                System.out.println(
                    "Doctor " + doctorId +
                    " is assigned to " + patient.getName() + "."
                );

                System.out.println(
                    "Doctor " + doctorId +
                    " is trying to acquire a treatment room."
                );

                // محاولة حجز غرفة علاج خلال 3 ثوانٍ
                boolean roomAcquired =
                    roomManager.acquireRoom(3000);

                if (roomAcquired) {

                    try {

                        System.out.println(
                            "Doctor " + doctorId +
                            " acquired a treatment room."
                        );

                        System.out.println(
                            "Doctor " + doctorId +
                            " begins treating " + patient.getName() + "."
                        );

                        // تحديد مدة العلاج بشكل عشوائي
                        int treatmentTime =
                            (int) (Math.random() * 4000) + 1000;

                        // محاكاة وقت العلاج
                        Thread.sleep(treatmentTime);

                        System.out.println(
                            "Doctor " + doctorId +
                            " finished treating " + patient.getName() + "."
                        );

                    } finally {

                        // تحرير غرفة العلاج
                        roomManager.releaseRoom();

                        System.out.println(
                            "Doctor " + doctorId +
                            " released the treatment room."
                        );
                    }

                    // المريض يغادر المستشفى
                    System.out.println(
                        patient.getName() + " left."
                    );

                } else {

                    System.out.println(
                        "Doctor " + doctorId +
                        " could not acquire a treatment room."
                    );

                    System.out.println(
                        "Doctor " + doctorId +
                        " is sending " + patient.getName() +
                        " back to the waiting room."
                    );

                    // إعادة المريض إلى غرفة الانتظار
                    waitingRoom.returnPatient(patient);
                }

                // الدكتور أصبح متاحًا
                System.out.println(
                    "Doctor " + doctorId +
                    " is now available."
                );
            }

        } catch (InterruptedException e) {

            // إيقاف الدكتور عند حدوث Interrupt
            Thread.currentThread().interrupt();

            System.out.println(
                "Doctor " + doctorId + " stopped."
            );
        }
    }
}
