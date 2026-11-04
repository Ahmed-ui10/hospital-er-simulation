public class Doctor extends Thread {

    private final int doctorId;
    private final WaitingRoom waitingRoom;
    private final TreatmentRoomManager roomManager;
    private final Hospital hospital;

    public Doctor(int doctorId, WaitingRoom waitingRoom, TreatmentRoomManager roomManager, Hospital hospital) {
        this.doctorId = doctorId;
        this.waitingRoom = waitingRoom;
        this.roomManager = roomManager;
        this.hospital = hospital;
    }

    public void run() {
        try {
            while (true) {
                Patient patient = waitingRoom.takePatient();

                Hospital.log("Doctor " + doctorId + " is assigned to " + patient.getName() + ".");
                Hospital.log("Doctor " + doctorId + " is trying to acquire a treatment room.");

                boolean roomAcquired = roomManager.acquireRoom(3000);

                if (roomAcquired) {
                    try {
                        Hospital.log("Doctor " + doctorId + " acquired a treatment room.");
                        Hospital.log("Doctor " + doctorId + " begins treating " + patient.getName() + ".");

                        int treatmentTime = (int) (Math.random() * 4000) + 1000;
                        Thread.sleep(treatmentTime);

                        Hospital.log("Doctor " + doctorId + " finished treating " + patient.getName() + ".");
                    } finally {
                        roomManager.releaseRoom();
                        Hospital.log("Doctor " + doctorId + " released the treatment room.");
                    }

                    Hospital.log(patient.getName() + " left.");
                    hospital.patientTreated();

                } else {
                    Hospital.log("Doctor " + doctorId + " could not acquire a treatment room.");
                    Hospital.log("Doctor " + doctorId + " is sending " + patient.getName() + " back to the waiting room.");
                    waitingRoom.returnPatient(patient);
                }

                Hospital.log("Doctor " + doctorId + " is now available.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Hospital.log("Doctor " + doctorId + " stopped.");
        }
    }
}