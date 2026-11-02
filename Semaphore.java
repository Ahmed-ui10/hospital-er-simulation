public class Semaphore {

    // عدد التصاريح المتاحة
    private int permits;

    // بنحدد عدد التصاريح في البداية
    public Semaphore(int permits) {

        if (permits < 0) {
            throw new IllegalArgumentException("Invalid permits number");
        }

        this.permits = permits;
    }

    // بنحاول ناخد تصريح ولو مفيش بنستنى
    public synchronized void acquire() throws InterruptedException {

        while (permits == 0) {
            wait();
        }

        permits--;
    }

    // بنرجع تصريح بعد ما نخلص استخدامه
    public synchronized void release() {

        permits++;

        notifyAll();
    }

    // بنحاول ناخد تصريح خلال وقت معين
    public synchronized boolean tryAcquire(long time)
            throws InterruptedException {

        if (time < 0) {
            throw new IllegalArgumentException("Invalid timeout");
        }

        long start = System.nanoTime();

        long total = time * 1_000_000L;

        while (permits == 0) {

            long passed = System.nanoTime() - start;

            long left = total - passed;

            if (left <= 0) {
                return false;
            }

            long millis = left / 1_000_000L;
            int nanos = (int) (left % 1_000_000L);

            wait(millis, nanos);
        }

        permits--;

        return true;
    }
}
