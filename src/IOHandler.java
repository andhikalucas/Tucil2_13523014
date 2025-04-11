import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.util.AbstractMap.SimpleEntry;

public class IOHandler {
    /* Attributes */
    private Scanner scanner;
    
    /* Constructor */
    public IOHandler() {
        this.scanner = new Scanner(System.in);
    }

    /* Methods */
    /* 1. Input absolute image path */
    public SimpleEntry<BufferedImage, String> inputImage(){
        while (true){
        System.out.print("\nMasukkan alamat absolut gambar yang akan dikompresi: ");
        String path = scanner.nextLine();
        try {
            BufferedImage img = ImageIO.read(new File(path));
            if (img == null) {
                System.out.println("Gambar tidak ditemukan pada alamat tersebut. Silahkan coba lagi!");
            } else{
                return new SimpleEntry<>(img, path);
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat membaca gambar pada path tersebut. Silahkan coba lagi!");
        }
        }
    }

    /* 2. Input error measurement method */
    public ErrorMeasurement.Method inputErrorMethod(){
        System.out.println("Metode Pengukuran Error:\n1. Variance\n2. Mean Absolute Deviation (MAD)\n3. Maximum Pixel Difference (MPD)\n4. Entropy\n");
        System.out.print("Pilih metode pengukuran error (1-4): ");
        while (true){
            try{
                int input = Integer.parseInt(scanner.nextLine());
                if (input < 1 || input > 4) {
                    System.out.println("Pilihan metode di luar batas angka 1-4!");
                  } else {
                    return ErrorMeasurement.Method.values()[input - 1];
                  }
            } catch (NumberFormatException e){
                System.out.println("Masukkan pilihan metode yang valid berupa angka 1-4!");
            }
        }
    }

    /* 3. Input threshold */
    public double inputThreshold(ErrorMeasurement.Method method){
        double bottomRange = 0;
        double topRange = 0;
        switch(method){
            case VARIANCE: 
                System.out.println("Threshold Metode Variance: (100-1000)"); 
                bottomRange = 100; 
                topRange = 1000;
                break;
            case MAD: 
                System.out.println("Threshold Metode MAD: (5-50)");
                bottomRange = 5; 
                topRange = 50;
                break;
            case MPD: 
                System.out.println("Threshold Metode MPD: (10-200)"); 
                bottomRange = 10; 
                topRange = 200;
                break;
            case ENTROPY: 
                System.out.println("Threshold Metode Entropy: (0.5-5)"); 
                bottomRange = 0.5; 
                topRange = 5;
                break;
        }
        
        while (true){
            try{
                System.out.print("Masukkan nilai threshold yang di antara batas: ");
                double input = Double.parseDouble(scanner.nextLine());
                if (input < bottomRange || input > topRange) {
                    System.out.println("Nilai threshold di luar batas threshold!");
                } else {
                    return input;
                }
            } catch (NumberFormatException e){
                System.out.println("Masukkan nilai threshold yang valid!");
            }
        }
    }

    /* 4. Input minimum block size */
    public int inputMinBlockSize() {
        while(true){
            try {
                System.out.print("Masukkan ukuran blok minimum dalam integer (Contoh: 800 untuk blok 40x20): ");
                int input = Integer.parseInt(scanner.nextLine());
                if (input < 1) {
                    System.out.println("Ukuran blok minimum tidak valid! Silahkan coba lagi.");
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println("Masukkan ukuran blok minimum yang valid!");
            }
        }
    }

    /* 6. Input result absolute path */
    public String inputOutputPath() {
        while (true) {
            try {
                System.out.print("Masukkan alamat absolut untuk menyimpan gambar hasil kompresi: ");
                String path = scanner.nextLine().trim();
                File file = new File(path);
                String parent = file.getParent();
    
                if (parent == null || parent.isEmpty()) {
                    throw new IllegalArgumentException("Path absolut tidak valid! Pastikan input alamat direktori yang lengkap.");
                } else { 
                return path;
                }

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan yang tidak terduga: " + e.getMessage());
            }
        }
    }

    /* 8-13. Output exec time, before and after image size, compression percentage, tree depth and node count */
    public void outputStats(long startTime, long endTime, String inputPath, File outputFile, Quadtree quadtree) {
        double executionTime = (endTime - startTime) / 1_000_000;
        long originalSize = new File(inputPath).length();
        long compressedSize = outputFile.length();

        if (originalSize == 0){
            System.out.println("Ukuran gambar asli adalah 0. Tidak dapat menghitung rasio kompresi.");
            return;
        }

        double compressionRatio = ((double) (originalSize - compressedSize) / originalSize) * 100;
        int depth = quadtree.getDepth(quadtree.getRoot());
        int nodeCount = quadtree.getNodeCount(quadtree.getRoot());

        System.out.println("\nStatistik Kompresi Gambar");
        System.out.println("Waktu eksekusi: " + executionTime + " detik");
        System.out.println("Ukuran gambar sebelum: " + originalSize + " bytes");
        System.out.println("Ukuran gambar setelah: " + compressedSize + " bytes");
        System.out.println("Persentase kompresi: " + compressionRatio + "%");
        System.out.println("Kedalaman pohon: " + depth);
        System.out.println("Banyak simpul pada pohon: " + nodeCount);
    }

    /* 14. Output the compressed image at given output path */
    public void outputImage(BufferedImage image, String outputPath) {
        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "jpg", outputFile);
            System.out.println("Gambar hasil kompresi berhasil disimpan di: " + outputPath);
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan saat menyimpan gambar hasil kompresi: " + e.getMessage());
        }
    }

    public void close(){
        scanner.close();
    }
}
