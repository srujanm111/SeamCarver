import java.awt.*;
import java.util.Arrays;
import java.util.PriorityQueue;

public class SeamCarver {

    private Picture picture;
    double[][] energy;

    public SeamCarver(String fileName) {
        this(new Picture(fileName));
    }

    public SeamCarver(Picture picture) {
        this.picture = picture;
        energy = new double[picture.height()][picture.width()];
        for (int r = 0; r < height(); r++) {
            for (int c = 0; c < width(); c++) {
                energy[r][c] = energy(c, r);
            }
        }
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return energy[0].length;
    }

    public int height() {
        return energy.length;
    }

    public double energy(int x, int y) {
        Color leftPixel = picture.get(x == 0 ? width() - 1 : x - 1, y);
        Color rightPixel = picture.get(x == width() - 1 ? 0 : x + 1, y);
        double xGradient = Math.pow(leftPixel.getRed() - rightPixel.getRed(), 2) + Math.pow(leftPixel.getBlue() - rightPixel.getBlue(), 2) + Math.pow(leftPixel.getGreen() - rightPixel.getGreen(), 2);

        Color upPixel = picture.get(x, y == 0 ? height() - 1 : y - 1);
        Color downPixel = picture.get(x, y == height() - 1 ? 0 : y + 1);
        double yGradient = Math.pow(upPixel.getRed() - downPixel.getRed(), 2) + Math.pow(upPixel.getBlue() - downPixel.getBlue(), 2) + Math.pow(upPixel.getGreen() - downPixel.getGreen(), 2);

        return Math.sqrt(xGradient + yGradient) + 1;
    }

    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;

    }

    public int[] findVerticalSeam() {
//        PriorityQueue<Coordinate> pq = new PriorityQueue<>();
//        for (int x = 0; x < width(); x++) {
//            pq.offer(new Coordinate(x, 0, energy[0][x], null));
//        }
//        while (pq.peek() != null) {
//            Coordinate coordinate = pq.poll();
//            if (coordinate.y == height() - 1) {
//                int[] seam = new int[height()];
//                while (coordinate != null) {
//                    seam[coordinate.y] = coordinate.x;
//                    coordinate = coordinate.predecessor;
//                }
//                return seam;
//            }
//            for (int i = -1; i <= 1; i++) {
//                try {
//                    pq.offer(new Coordinate(coordinate.x+i, coordinate.y + 1, coordinate.totalEnergy + energy[coordinate.y + 1][coordinate.x+i], coordinate));
//                } catch (Exception e) {
//
//                }
//            }
//        }
//        return null;
        return dpSolution();
    }

    public int[] dpSolution() {
        double[][] minEnergySums = new double[height()][width()]; // modified during dp algo
        int[] predecessors = new int[height() * width()]; // predecessor indexed values from index(x, y)
        minEnergySums[0] = energy[0].clone(); // first row of table should be same as first row energies
        // O(h * w * 3)
        for (int row = 1; row < height(); row++) { // go through each row of dg energies except for the already copied one
            for (int column = 0; column < width(); column++) { // go through each energy in each column
                for (int direction = -1; direction <= 1; direction++) { // check adjacent energy sums from previous row
                    if (column + direction < 0 || column + direction >= width()) { // avoid bounds exceptions
                        continue;
                    }
                    // min sum has not been calculated for this pixel yet
                    // or sum of this path is less than the current min energy sum of the pixel
                    if (minEnergySums[row][column] == 0 || energy[row][column] + minEnergySums[row - 1][column + direction] < minEnergySums[row][column]) {
                        relax(minEnergySums, predecessors, row, column, direction); // change the min sum and assign the predecessor
                    }
                }
            }
        }
        // O(w)
        int minIndex = 0;
        for (int col = 1; col < width(); col++) { // find the smallest total energy path by finding the min sum in the bottom row
            if (minEnergySums[height() - 1][col] < minEnergySums[height() - 1][minIndex]) {
                minIndex = col;
            }
        }
        // O(h)
        int[] seam = new int[height()];
        seam[height() - 1] = minIndex;
        for (int row = seam.length-2; row >= 0; row--) { // get the predecessors
            seam[row] = (predecessors[index(seam[row + 1], row + 1)] - row) / height();
        }

        return seam;
    }

    public void transpose(){
        double[][] transposedMatrix = new double[width()][height()];

        for(int x = 0; x < width(); x++) {
            for(int y = 0; y < height(); y++) {
                transposedMatrix[x][y] = energy[y][x];
            }
        }

        energy = transposedMatrix;
    }

    public void relax(double[][] minEnergySums, int[] predecessors, int row, int column, int direction) {
        minEnergySums[row][column] = energy[row][column] + minEnergySums[row - 1][column + direction];
        predecessors[index(column, row)] = index(column + direction, row - 1);
    }

    private int index(int x, int y) {
        return height() * x + y;
    }

    public void removeHorizontalSeam(int[] seam) {
        Picture newPicture = new Picture(width(), height() - 1);
        int height = height();
        int width = width();
        double[][] newEnergy = new double[height - 1][width];
        int r2 = 0;
        for (int c = 0; c < height - 1; c++) {
            for (int r = 0; r < height; r++) {
                if (seam[c] != r) {
                    newPicture.set(c, r2, picture.get(c, r));
                    newEnergy[r2][c] = energy[r][c];
                    r2++;
                }
            }
            r2 = 0;
        }
        picture = newPicture;
        energy = newEnergy;
    }

    public void removeVerticalSeam(int[] seam) {
        Picture newPicture = new Picture(width() - 1, height());
        int height = height();
        int width = width();
        double[][] newEnergy = new double[height][width - 1];
        int c2 = 0;
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width - 1; c++) {
                if (seam[r] != c) {
                    newPicture.set(c2, r, picture.get(c, r));
                    newEnergy[r][c2] = energy[r][c];
                    c2++;
                }
            }
            c2 = 0;
        }
        picture = newPicture;
        energy = newEnergy;
    }

}