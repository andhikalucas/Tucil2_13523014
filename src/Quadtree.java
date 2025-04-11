import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Graphics2D;

public class Quadtree {
    /* Attributes */
    private ErrorMeasurement.Method method;
    private double threshold;
    private int minBlockSize;
    private BufferedImage img;
    private QuadtreeNode root;

    /* Constructor */
    public Quadtree(ErrorMeasurement.Method method, double threshold, int minBlockSize, BufferedImage img) {
        this.method = method;
        this.threshold = threshold;
        this.minBlockSize = minBlockSize;
        this.img = img;
        Rectangle rectangle = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        this.root = new QuadtreeNode(rectangle, QuadtreeNode.calculateAvgColor(img, rectangle));
    }

    /* Getters */
    public double getThreshold() {
        return threshold;
    }

    public int getMinBlockSize() {
        return minBlockSize;
    }

    public BufferedImage getImg() {
        return img;
    }
    public QuadtreeNode getRoot() {
        return root;
    }

    public int getDepth(QuadtreeNode node) {
        if (node == null || node.isLeaf()){
            return 1;
        }
        int maxDepth = 0;
        if (node.getChildren() != null){
            for (QuadtreeNode child : node.getChildren()){
            maxDepth = Math.max(maxDepth, getDepth(child));
            }
        }
        
        return maxDepth + 1;
    }

    public int getNodeCount(QuadtreeNode node) {
        if (node == null){
            return 0;
        }
        int count = 1;
        if (!node.isLeaf() && node.getChildren() != null){
            for (QuadtreeNode child : node.getChildren()){
                if (child != null) count += getNodeCount(child);
            }
        }
        return count;
    }

    /* Methods */
    /* Calculate value of error based on the chosen method */
    private double calculateError(BufferedImage image, Rectangle area) {
        ErrorMeasurement em = new ErrorMeasurement();
        switch (this.method) {
            case VARIANCE: return em.variance(image, area);
            case MAD: return em.mad(image, area);
            case MPD: return em.mpd(image, area);
            case ENTROPY: return em.entropy(image, area);
            default: throw new IllegalArgumentException("Unknown error measurement method.");
        }
    }

    /* Determines whether an image pixel should be split further or not */
    public boolean shouldSplit(BufferedImage img, QuadtreeNode node){
        Rectangle area = node.getArea();
        int childBlockArea = area.width * area.height;

        // Condition 1: Stop if child block would be smaller than minBlockSize
        if (childBlockArea < minBlockSize){
            return false;
        }

        // Condition 2: Stop if error is not above threshold
        double error = calculateError(img, area);
        return error > threshold;
    }

    /* Recursive function for buildQuadtree */
    private void buildRecursive(QuadtreeNode node){
        if (shouldSplit(img, node)){
            node.split(img);
            for (QuadtreeNode child : node.getChildren()){
                buildRecursive(child);
            }
        }
    } 

    /* Actual function to build quadtree, starting from the root */
    public void buildQuadtree() {
        buildRecursive(root);
    }

    /* Reconstruct compressed image from quadtree */
    public BufferedImage reconstruct(){
        // Create a new image, same dimensions with the input image
        BufferedImage output = new BufferedImage(
            img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = output.createGraphics();

        // Combine quadtree with recursive function. Graphics2D draws on the output image.
        renderQuadtree(g, root);
        g.dispose();
        return output;
    }

    private void renderQuadtree(Graphics2D g, QuadtreeNode node){
        // Base
        if (node.isLeaf()){
            g.setColor(node.getAvgColor());
            Rectangle area = node.getArea();
            g.fillRect(area.x, area.y, area.width, area.height);
        } 
        // Recursion
        else {
            for (QuadtreeNode child : node.getChildren()){
                renderQuadtree(g, child);
            }
        }
    }
}