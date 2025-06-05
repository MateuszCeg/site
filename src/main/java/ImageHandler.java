import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHandler {
    public ImageHandler(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
    private BufferedImage bufferedImage;
    public void loadImage(String filePath) {
        File file = new File(filePath);
        try {
            bufferedImage = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Zła ścieżka: " + filePath);
        }
        System.out.println("Załadowano obraz.");
    }
    public void saveImage(String filePath){
        String extension = filePath.substring(filePath.lastIndexOf("."));
        try {
            ImageIO.write(bufferedImage, extension, new File(filePath));
        } catch (IOException e) {
            System.err.println("Błąd zapisu");
        }
        System.out.println("Zapisano obraz.");
    }
    public void setBrightness(int number){
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int rgbOfPixel = bufferedImage.getRGB(x,y);
                if (rgbOfPixel+number>255) bufferedImage.setRGB(x,y,255);
                else if (rgbOfPixel+number<0) bufferedImage.setRGB(x,y,0);
                else bufferedImage.setRGB(x,y,rgbOfPixel+number);
            }
        }
    }

}
