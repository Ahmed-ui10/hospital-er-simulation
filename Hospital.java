import java.util.Scanner;

public class Hospital {
    private final int numDoctors;
    private final int numPatients;
    private final WaitingRoom waitingRoom;
    private final TreatmentRoomManager roomManager;
    private final Doctor[] doctors;
    private final Patient[] patients;
    
    private int treatedPatientsCount = 0;
    private final int totalPatients;

    public Hospital(int capacity, int numDoctors, int numPatients) {
        this.numDoctors = numDoctors;
        this.numPatients = numPatients;
        this.totalPatients = numPatients;
        
        this.waitingRoom = new WaitingRoom(capacity);
        this.roomManager = new TreatmentRoomManager();
        this.doctors = new Doctor[numDoctors];
        this.patients = new Patient[numPatients];
    }

    public static synchronized void log(String message) {
        System.out.println(message);
    }

    public synchronized void patientTreated() {
        treatedPatientsCount++;
        if (treatedPatientsCount == totalPatients) {
            notifyAll();
        }
    }

    public void startSimulation() {
        Hospital.log("Patients arriving: P1 P2 P3 P4 P5");
        
        for (int i = 0; i < numDoctors; i++) {
            doctors[i] = new Doctor(i + 1, waitingRoom, roomManager, this);
            doctors[i].start();
        }

        for (int i = 0; i < numPatients; i++) {
            patients[i] = new Patient("P" + (i + 1), waitingRoom);
            new Thread(patients[i]).start();
        }

        synchronized (this) {
            try {
                while (treatedPatientsCount < totalPatients) {
                    wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        for (int i = 0; i < numDoctors; i++) {
            doctors[i].interrupt();
        }
        
        // ننتظر الدكاترة يقفلوا خالص قبل ما نطبع النهاية
        for (int i = 0; i < numDoctors; i++) {
            try {
                doctors[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        Hospital.log("All patients have been treated.");
        Hospital.log("Simulation completed.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Waiting room capacity: ");
        int capacity = scanner.nextInt();
        
        System.out.print("Number of doctors: ");
        int numDoctors = scanner.nextInt();
        
        System.out.print("Number of patients: ");
        int numPatients = scanner.nextInt();
        
        Hospital hospital = new Hospital(capacity, numDoctors, numPatients);
        hospital.startSimulation();
        
        scanner.close();
    }
}