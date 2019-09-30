public class PrintEnergy {

    public static void main(String[] args) {
        Picture picture = new Picture("/Users/srujan_mupparapu/IdeaProjects/SeamCarving/src/seam/6x5.png");
        System.out.printf("%d-by-%d image\n", picture.width(), picture.height());

        SeamCarver sc = new SeamCarver(picture);

        System.out.printf("Printing energy calculated for each pixel.\n");

        for (int row = 0; row < sc.height(); row++) {
            for (int col = 0; col < sc.width(); col++)
                System.out.printf("%9.0f ", sc.energy(col, row));
            System.out.println();
        }
    }

}
