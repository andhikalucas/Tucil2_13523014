import java.awt.image.BufferedImage;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;

public class Main {
    public static void main(String[] args){
        IOHandler io = new IOHandler();

        /* 1. Input absolute image path */
        SimpleEntry<BufferedImage, String> inputMap= io.inputImage();
        BufferedImage image = inputMap.getKey();
        String inputPath = inputMap.getValue();

        /* 2. Input error measurement method */
        ErrorMeasurement.Method method = io.inputErrorMethod();

        /* 3. Input threshold */
        double threshold = io.inputThreshold(method);

        /* 4. Input minBlockSize */
        int minBlockSize = io.inputMinBlockSize();

        /* 5. Input Target Compression (bonus) */

        /* 6. Input Output Compressed Image Path */
        String outputPath = io.inputOutputPath();
        File outputFile = new File(outputPath);

        /* 7. Input Output GIF Path */

        /* 8-14. Output Image and Stats */
        long startTime = System.nanoTime();
        Quadtree quadtree = new Quadtree(method, threshold, minBlockSize, image);
        quadtree.buildQuadtree();
        BufferedImage compressedImage = quadtree.reconstruct();
        long endTime = System.nanoTime();

        io.outputImage(compressedImage, outputPath);
        io.outputStats(startTime, endTime, inputPath, outputFile, quadtree);

        io.close();
    }
}
