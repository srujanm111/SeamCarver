import java.util.Arrays;

public class ShowSeams {

    private static void showHorizontalSeam(SeamCarver sc) {
        Picture ep = SCUtility.toEnergyPicture(sc);
        int[] horizontalSeam = sc.findHorizontalSeam();
        Picture epOverlay = SCUtility.seamOverlay(ep, true, horizontalSeam);
        epOverlay.show();
    }

    private static int[] showVerticalSeam(SeamCarver sc) {
        Picture ep = SCUtility.toEnergyPicture(sc);
        int[] verticalSeam = sc.findVerticalSeam();
        Picture epOverlay = SCUtility.seamOverlay(ep, false, verticalSeam);
        epOverlay.show();
        sc.removeVerticalSeam(verticalSeam);
        return verticalSeam;
    }

    public static void main(String[] args) {
        Picture picture = new Picture("/Users/srujan_mupparapu/IdeaProjects/SeamCarving/src/seam/HJocean.png");
        System.out.printf("%d-by-%d image\n", picture.width(), picture.height());
        picture.show();        
        SeamCarver sc = new SeamCarver(picture);

        for (int i = 0; i < 30; i++) {
            showVerticalSeam(sc);
        }
        sc.picture().show();
    }

}
