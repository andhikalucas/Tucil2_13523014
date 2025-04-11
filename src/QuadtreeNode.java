import java.awt.*;
import java.awt.image.BufferedImage;

public class QuadtreeNode {
    /* Attributes  */
    private Rectangle area; // x, y, width, height
    private Color avgColor;
    private QuadtreeNode[] children; // top-left, top-right, bottom-left, bottom-right

    /* Constructor */
    public QuadtreeNode(Rectangle area, Color avgColor){
        this.area = area;
        this.avgColor = avgColor;
        this.children = null;
    }

    /* Getters */
    public Rectangle getArea(){
        return this.area;
    }

    public Color getAvgColor(){
        return this.avgColor;
    }

    public QuadtreeNode[] getChildren(){
        return this.children;
    }

    public boolean isLeaf(){
        return this.children == null;
    }

    /*Functions */
    /* Calculate the average color of a node (area of pixel) */
    public static Color calculateAvgColor(BufferedImage img, Rectangle area){
        int red = 0;
        int green = 0;
        int blue = 0;
        int pixelCount = 0;

        // Iterate box of pixels in the image to sum RGB values
        for (int y = area.y; y < area.y + area.height; y++){
            for (int x = area.x; x < area.x + area.width; x++){
                int RGB = img.getRGB(x, y);
                Color color = new Color(RGB);
                red += color.getRed();
                green += color.getGreen();
                blue += color.getBlue();
                pixelCount++;
            }
        }

        // Edge case for invalid iteration
        if (pixelCount == 0){
            return new Color(0, 0, 0);
        }

        // Create new Color object with average RGB values and return
        int avgRed = (int) (red / pixelCount);
        int avgGreen = (int) (green / pixelCount);
        int avgBlue = (int) (blue / pixelCount);

        return new Color(avgRed, avgGreen, avgBlue);
    }
    
    /* Splits given image section into 4 boxes. */
    public void split(BufferedImage img){
        this.children = new QuadtreeNode[4];
        int halfWidth = area.width / 2;
        int halfHeight = area.height / 2;
        int remainingWidth = area.width - halfWidth;
        int remainingHeight = area.height - halfHeight;

        children = new QuadtreeNode[4];
        // Top-Left
        Rectangle topLeft = new Rectangle(area.x, area.y, remainingWidth, remainingHeight);
        children[0] = new QuadtreeNode(topLeft, QuadtreeNode.calculateAvgColor(img, topLeft));

        // Top-Right
        Rectangle topRight = new Rectangle(area.x + halfWidth, area.y, remainingWidth, remainingHeight);
        children[1] = new QuadtreeNode(topRight, QuadtreeNode.calculateAvgColor(img, topRight));

        // Bottom-Left
        Rectangle bottomLeft = new Rectangle(area.x, area.y + halfHeight, remainingWidth, remainingHeight);
        children[2] = new QuadtreeNode(bottomLeft, QuadtreeNode.calculateAvgColor(img, bottomLeft));

        // Bottom-Right
        Rectangle bottomRight = new Rectangle(area.x + halfWidth, area.y + halfHeight, remainingWidth, remainingHeight);
        children[3] = new QuadtreeNode(bottomRight, QuadtreeNode.calculateAvgColor(img, bottomRight));
    }
}
