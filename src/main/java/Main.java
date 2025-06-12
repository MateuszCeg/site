public class Main {
    public static void main(String[] args) {
        ImageHandler handler = new ImageHandler();
        //handler.saveImage("obrazKopia.jpg");

        // Wczytanie obrazu
        handler.loadImage("src/main/java/obraz.jpg");

        // Zwiększenie jasności obrazu o 100
        long startTimeParallel = System.currentTimeMillis();
        handler.adjustBrightness(100);
        long endTimeParallel = System.currentTimeMillis();
        System.out.println("Czas wykonania jeden wątek: " + (endTimeParallel - startTimeParallel) + " ms");

        handler.saveImage("src/main/java/obrazKopia1.jpg");


        try {
            handler.loadImage("src/main/java/obraz.jpg");
            long startTimeParallel2 = System.currentTimeMillis();
            handler.adjustBrightnessThreads(100, Runtime.getRuntime().availableProcessors());
            long endTimeParallel2 = System.currentTimeMillis();
            System.out.println("Czas wykonania jeden wątek: " + (endTimeParallel2 - startTimeParallel2) + " ms");
            handler.saveImage("src/main/java/obrazKopia2.jpg");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        handler.loadImage("src/main/java/obraz.jpg");
        long startTimeParallel3 = System.currentTimeMillis();
        handler.adjustBrightnessPoolThreads(100);
        long endTimeParallel3 = System.currentTimeMillis();
        System.out.println("Czas wykonania jeden wątek: " + (endTimeParallel3 - startTimeParallel3) + " ms");
        handler.saveImage("src/main/java/obrazKopia3.jpg");
    }
}