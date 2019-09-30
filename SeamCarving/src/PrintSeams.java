public class PrintSeams {
    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;

    private static void printSeam(SeamCarver carver, int[] seam, boolean direction) {
        double totalSeamEnergy = 0.0;

        for (int row = 0; row < carver.height(); row++) {
            for (int col = 0; col < carver.width(); col++) {
                double energy = carver.energy(col, row);
                String marker = " ";
                if ((direction == HORIZONTAL && row == seam[col]) ||
                        (direction == VERTICAL && col == seam[row])) {
                    marker = "*";
                    totalSeamEnergy += energy;
                }
                System.out.printf("%7.2f%s ", energy, marker);
            }
            System.out.println();
        }
        // System.out.println();
        System.out.printf("Total energy = %f\n", totalSeamEnergy);
        System.out.println();
        System.out.println();
    }

    public static void main(String[] args) {
        String file = "HJocean.png";
        Picture picture = new Picture("/Users/srujan_mupparapu/IdeaProjects/SeamCarving/src/seam/" + file);
        System.out.printf("%s (%d-by-%d image)\n", file, picture.width(), picture.height());
        System.out.println();
        System.out.println("The table gives the dual-gradient energies of each pixel.");
        System.out.println("The asterisks denote a minimum energy vertical or horizontal seam.");
        System.out.println();

        SeamCarver carver = new SeamCarver(picture);

        System.out.printf("Vertical seam: { ");
        int[] verticalSeam = carver.findVerticalSeam();
        for (int x : verticalSeam)
            System.out.print(x + " ");
        System.out.println("}");
        printSeam(carver, verticalSeam, VERTICAL);

        System.out.printf("Horizontal seam: { ");
        int[] horizontalSeam = carver.findHorizontalSeam();
        for (int y : horizontalSeam)
            System.out.print(y + " ");
        System.out.println("}");
        printSeam(carver, horizontalSeam, HORIZONTAL);

    }

}