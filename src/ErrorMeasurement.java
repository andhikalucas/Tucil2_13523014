import java.awt.*;
import java.awt.image.BufferedImage;
public class ErrorMeasurement {
    public enum Method {
        VARIANCE, MAD, MPD, ENTROPY
    }
    
    /* 1. Variance (σ²) */
    public double variance(BufferedImage img, Rectangle area) {
        // Edge case for empty areas
        if (area.width <= 0 || area.height <= 0) {
            return 0; // Return 0 for empty areas
        }
        Color avgRGB = QuadtreeNode.calculateAvgColor(img, area);
        double varianceR = 0;
        double varianceG = 0;
        double varianceB = 0;
        int pixelCount = 0;

        /* Count variance for individual R, G, and B values */
        for (int y = area.y; y < area.y + area.height; y++) {
            for (int x = area.x; x < area.x + area.width; x++) {

                if (x >= img.getWidth() || y >= img.getHeight()) continue;

                Color pixelRGB = new Color(img.getRGB(x, y));
                varianceR += Math.pow(pixelRGB.getRed() - avgRGB.getRed(), 2);
                varianceG += Math.pow(pixelRGB.getGreen() - avgRGB.getGreen(), 2);
                varianceB += Math.pow(pixelRGB.getBlue() - avgRGB.getBlue(), 2);
                pixelCount++;
            }
        }
        /* Edge case if no valid pixels were iterated */
        if (pixelCount == 0) return 0;

        /* Return the final variance of RGB */
        return (varianceR + varianceG + varianceB) / (3 * pixelCount);
    }

    /* 2. Mean Absolute Deviation (MAD) */
    public double mad(BufferedImage img, Rectangle area) {
        // Edge case for empty areas
        if (area.width <= 0 || area.height <= 0) {
            return 0; // Return 0 for empty areas
        }
        Color avgRGB = QuadtreeNode.calculateAvgColor(img, area);
        double madR = 0;
        double madG = 0;
        double madB = 0;
        int pixelCount = 0;

        /* Count MAD for individual R, G, and B values */
        for (int y = area.y; y < area.y + area.height; y++) {
            for (int x = area.x; x < area.x + area.width; x++) {

                if (x >= img.getWidth() || y >= img.getHeight()) continue;

                Color pixelRGB = new Color(img.getRGB(x, y));
                madR += Math.abs(pixelRGB.getRed() - avgRGB.getRed());
                madG += Math.abs(pixelRGB.getGreen() - avgRGB.getGreen());
                madB += Math.abs(pixelRGB.getBlue() - avgRGB.getBlue());
                pixelCount++;
            }
        }
        /* Edge case if no valid pixels were iterated */
        if (pixelCount == 0) return 0;

        /* Return the final MAD of RGB */
        return (madR + madG + madB) / (3 * pixelCount);
    }

    /* 3. Maximum Pixel Difference (MPD) */
    public double mpd(BufferedImage img, Rectangle area){
        // Edge case for empty areas
        if (area.width <= 0 || area.height <= 0) {
            return 0; // Return 0 for empty areas
        }
        double maxR = 0;
        double minR = 0;
        double maxG = 0;
        double minG = 0;
        double maxB = 0;
        double minB = 0;
        int pixelCount = 0;

        /* Update the max and min value for each color channel */
        for (int y = area.y; y < area.y + area.height; y++){
            for (int x = area.x; x < area.x + area.width; x++){
                if (x >= img.getWidth() || y >= img.getHeight()) continue;

                Color pixelRGB = new Color(img.getRGB(x, y));

                if (pixelRGB.getRed() > maxR) maxR = pixelRGB.getRed();
                if (pixelRGB.getRed() < minR) minR = pixelRGB.getRed();
                if (pixelRGB.getGreen() > maxG) maxG = pixelRGB.getGreen();
                if (pixelRGB.getGreen() < minG) minG = pixelRGB.getGreen();
                if (pixelRGB.getBlue() > maxB) maxB = pixelRGB.getBlue();
                if (pixelRGB.getBlue() < minB) minB = pixelRGB.getBlue();
                pixelCount++;
            }
        }
        /* Edge case if no valid pixels were iterated */
        if (pixelCount == 0) return 0;

        /* Return the difference in RGB */
        return ((maxR - minR) + (maxG - minG) + (maxB - minB)) / 3;
    }

    /* 4. Entropy */
    public double entropy(BufferedImage img, Rectangle area){
        // Edge case for empty areas
        if (area.width <= 0 || area.height <= 0) {
            return 0; // Return 0 for empty areas
        }
        int pixelCount = 0;

        // Plot the frequency of each color channel value in histogram
        int[][] histogram = new int[3][256]; // R, G, B

        for (int y = area.y; y < area.y + area.height; y++){
            for (int x = area.x; x < area.x + area.width; x++){
                if (x >= img.getWidth() || y >= img.getHeight()) continue;

                Color pixelRGB = new Color(img.getRGB(x, y));
                histogram[0][pixelRGB.getRed()]++;
                histogram[1][pixelRGB.getGreen()]++;
                histogram[2][pixelRGB.getBlue()]++;
                pixelCount++;
            }
        }

        /* Edge case if no valid pixels were iterated */
        if (pixelCount == 0) return 0;

        // Count entropy value per each RGB channel
        double[] channelEntropy = new double[3];
        for (int i = 0; i < 3; i++){ // RGB
            double entropy = 0;
            for (int j = 0; j < 256; j++){ // 0 - 255 channel values
                if (histogram[i][j] > 0){
                    double probability = (double) histogram[i][j] / pixelCount;
                    entropy -= probability * Math.log(probability) / Math.log(2); // log base 2
                }
            }
            channelEntropy[i] = entropy;
        }
    // Return the final entropy for RGB
    return (channelEntropy[0] + channelEntropy[1] + channelEntropy[2]) / 3;   
    }
}
