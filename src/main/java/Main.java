public class Main {
    public static void main(String[] args) {
        ImageHandler newImage = new ImageHandler(null);
        newImage.loadImage("src/main/resources/testImage.png");
        newImage.setBrightness(10);
        newImage.saveImage("CopyOfImage.png");
    }
}
