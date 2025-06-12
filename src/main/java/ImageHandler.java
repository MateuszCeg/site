import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageHandler {
    private BufferedImage image;
    private class AdjustBrightnessByThreads extends Thread {
        private int start;
        private int end;
        private int value;

        public AdjustBrightnessByThreads(int start, int end, int value) {
            this.start = start;
            this.end = end;
            this.value = value;
        }

        @Override
        public void run() {
            // Iteracja po pikselach obrazu i zmiana jasności
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = start; y < end; y++) {
                    int rgb = image.getRGB(x, y);
                    int alpha = (rgb >> 24) & 0xFF;
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    // Zmiana jasności dla każdego kanału koloru
                    red = clamp(red + value);
                    green = clamp(green + value);
                    blue = clamp(blue + value);

                    int newRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                    image.setRGB(x, y, newRGB);
                }
            }
        }
    }

    // Metoda do wczytania obrazu z podanej ścieżki
    public void loadImage(String path) {
        try {
            File file = new File(path);
            image = ImageIO.read(file);
            System.out.println("Obraz został wczytany pomyślnie.");
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas wczytywania obrazu: " + e.getMessage());
        }
    }

    // Metoda do zapisania obrazu z pola klasy do podanej ścieżki
    public void saveImage(String path) {
        try {
            File file = new File(path);
            String format = path.substring(path.lastIndexOf('.') + 1);
            ImageIO.write(image, format, file);
            System.out.println("Obraz został zapisany pomyślnie.");
        } catch (IOException e) {
            System.out.println("Wystąpił błąd podczas zapisywania obrazu: " + e.getMessage());
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Niepoprawna ścieżka, brak rozszerzenia obrazu.");
        }catch (IllegalArgumentException e) {
            System.out.println("Brak wczytanego obrazu do zapisania.");
        }
    }

    // Metoda do pobrania obiektu BufferedImage (jeśli potrzebne)
    public BufferedImage getImage() {
        return image;
    }

    // Metoda do zwiększenia jasności obrazu o podaną stałą
    public void adjustBrightness(int value) {
        if (image == null) {
            System.out.println("Brak wczytanego obrazu.");
            return;
        }

        // Iteracja po pikselach obrazu i zmiana jasności
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Zmiana jasności dla każdego kanału koloru
                red = clamp(red + value);
                green = clamp(green + value);
                blue = clamp(blue + value);

                int newRGB = (alpha << 24) | (red << 16) | (green << 8) | blue;
                image.setRGB(x, y, newRGB);
            }
        }

        //graphics.dispose();
    }
        public void adjustBrightnessThreads(int value, int threads) throws InterruptedException {
            if (image == null) {
                System.out.println("Brak wczytanego obrazu.");
                return;
            }
            Thread[] threadsTable = new Thread[threads];
            int start; int end; int high = image.getHeight()/threads;
            for (int i = 0; i < threads; i++) {
                start=i*high;
                if (i+1 == threads) end = image.getHeight();
                else end = start+high;
                threadsTable[i] = new AdjustBrightnessByThreads(start,end,value);
                threadsTable[i].start();
            }
            for (int i = 0; i < threads; i++) {
                threadsTable[i].join();
            }
        }
        public void adjustBrightnessPoolThreads(int value) {
            if (image == null) {
                System.out.println("Brak wczytanego obrazu.");
                return;
            }
            ExecutorService executorService = Executors.newFixedThreadPool(image.getHeight());
            for (int i = 0; i < image.getHeight(); i++) {
                executorService.execute(new AdjustBrightnessByThreads(i,i+1,value));
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    // Metoda pomocnicza do ograniczenia wartości do zakresu 0-255
    private int clamp(int value) {
        return Math.min(Math.max(0, value), 255);
    }




}