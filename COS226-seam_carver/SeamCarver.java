import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.IndexMinPQ;
import java.awt.Color;
import java.util.Comparator;

public class SeamCarver {

    Picture picture; // Picture of this class
    double[][] energy; // energy[col][lin] = energy of the pixel

    // Easy acess
    int w;
    int h;

    // Create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

        if (picture == null)
            throw new IllegalArgumentException("argument is null");

        w = picture.width();
        h = picture.height();

        this.picture = new Picture(picture); // DeepCopying
        energy = new double[w][h];

        // Calculating energy for the whole image
        for (int col = 0; col < w; col++) {
            for (int lin = 0; lin < h; lin++) {

                // Delta values
                int dc = componentDelta((col+1+w)%w, lin, (col-1+w)%w, lin);
                int dl = componentDelta(col, (lin+1+h)%h, col, (lin-1+h)%h);

                energy[col][lin] = Math.sqrt(Double.valueOf(dc) + Double.valueOf(dl));
            }
        }
    }

    // Return the difference of each component of the color
    private int componentDelta (int col1, int lin1, int col2, int lin2) {

        Color cor1 = picture.get(col1, lin1);
        Color cor2 = picture.get(col2, lin2);

        int r = cor1.getRed() - cor2.getRed();
        int g = cor1.getGreen() - cor2.getGreen();
        int b = cor1.getBlue() - cor2.getBlue();

        return (r*r + g*g + b*b);
    }

    // Current picture
    public Picture picture () {
        return new Picture(picture);
    }

    // Width of current picture
    public int width () {
        return picture.width();
    }

    // Height of current picture
    public int height () {
        return picture.height();
    }

    // Energy of pixel at column x and row y
    public double energy (int x, int y) {

        if (!isInside(x, y))
            throw new IllegalArgumentException("x-coordinate or y-coordinate outside its prescribed range.");

        return energy[x][y];
    }

    // Sequence of indices for horizontal seam
    public int[] findHorizontalSeam () {

        // This will run dijkstra algorithm from top to bottom
        int[] seam = new int[w];
        int[] pathTo = new int[h*w];

        // Visited pixels
        boolean[][] vis = new boolean[w][h];

        // IndexedMinPQ for dijkstra, where de key will be the energy of such
        // cell and the coordinate will be mapped to 1D
        IndexMinPQ<Double> pq = new IndexMinPQ<>(h*w);

        for (int root = 0; root < h; root++) {
            pq.insert(root*w, energy(0, root));
            vis[0][root] = true;
            pathTo[root*w] = -1;
        }

        // For path retrieval
        int lastIndex = 0;
        double lastVal = Double.MAX_VALUE;

        while (!pq.isEmpty()) {

            // Actual coodinate
            int lin = pq.minIndex() / w;
            int col = pq.minIndex() % w;
            double val = pq.minKey();

            // Salving last pixel of dijkstra
            if (col == w-1 && val < lastVal) {
                lastIndex = pq.minIndex();
                lastVal = val;
            }

            vis[col][lin] = true;
            pq.delete(pq.minIndex());

            for (int ac = -1; ac <= 1; ac++) {

                // New possibilities
                int auxl = lin + ac;
                int auxc = col + 1;
                int index = auxl*w + auxc;

                if (auxc >= w || auxl < 0 || auxl >= h)
                    continue;

                double newEne = energy(auxc, auxl) + val;

                // Puting in pq
                if (!pq.contains(index) && vis[auxc][auxl] == false){
                    pq.insert(index, newEne);
                    pathTo[index] = lin*w + col;
                }

                // Updating value
                else if (pq.contains(index) && newEne < pq.keyOf(index)) {
                    pq.changeKey(index, newEne);
                    pathTo[index] = lin*w + col;
                }
            }
        }

        // Reconstructing path
        int aux = lastIndex;
        while (aux > 0) {
            int lin = aux / w;
            int col = aux % w;

            seam[col] = lin;
            aux = pathTo[aux];
        }

        return seam;
    }

    // Sequence of indices for vertical seam
    public int[] findVerticalSeam () {

        // This will run dijkstra algorithm from top to bottom
        int[] seam = new int[h];
        int[] pathTo = new int[h*w];

        // IndexedMinPQ for dijkstra, where de key will be the energy of such
        // cell and the coordinate will be mapped to 1D
        IndexMinPQ<Double> pq = new IndexMinPQ<>(h*w);

        // Visited pixels
        boolean[][] vis = new boolean[w][h];

        for (int root = 0; root < w; root++) {
            pq.insert(root, energy(root, 0));
            vis[root][0] = true;
            pathTo[root] = -1;
        }

        // For path retrieval
        int lastIndex = 0;
        double lastVal = Double.MAX_VALUE;

        while (!pq.isEmpty()) {

            // Actual coodinate
            int lin = pq.minIndex() / w;
            int col = pq.minIndex() % w;
            double val = pq.minKey();

            // Salving last pixel of dijkstra
            if (lin == h-1 && val < lastVal) {
                lastIndex = pq.minIndex();
                lastVal = val;
            }

            vis[col][lin] = true;
            pq.delete(pq.minIndex());

            for (int ac = -1; ac <= 1; ac++) {

                // New possibilities
                int auxl = lin + 1;
                int auxc = col + ac;
                int index = auxl*w + auxc;

                if (auxl >= h || auxc < 0 || auxc >= w)
                    continue;

                double newEne = energy(auxc, auxl) + val;

                // Puting in pq
                if (!pq.contains(index) && vis[auxc][auxl] == false){
                    pq.insert(index, newEne);
                    pathTo[index] = lin*w + col;
                }

                // Updating value
                else if (pq.contains(index) && newEne < pq.keyOf(index)) {
                    pq.changeKey(index, newEne);
                    pathTo[index] = lin*w + col;
                }
            }
        }

        // Reconstructing path
        int aux = lastIndex;
        while (aux > 0) {
            int lin = aux / w;
            int col = aux % w;

            seam[lin] = col;
            aux = pathTo[aux];
        }

        return seam;
    }

    // Remove horizontal seam from current picture
    public void removeHorizontalSeam (int[] seam) {

        if (seam == null)
            throw new IllegalArgumentException("argument is null");

        if (seam.length != w)
            throw new IllegalArgumentException("seam has wrong length");

        if (seam.length == 1)
            throw new IllegalArgumentException("width = 1");

        for (int i = 0; i < w; i++) {
            if (!isInside(i, seam[i]))
                throw new IllegalArgumentException("seam is invalid - 1");

            if (i < w-1 && Math.abs(seam[i] - seam[i+1]) > 1){
                throw new IllegalArgumentException("seam is invalid - 2");
            }
        }

        Picture pic = new Picture(w, h-1);
        energy = new double[w][h-1];
        for (int col = 0; col < w; col++){
            for (int lin = 0, rlin = 0; lin < h; lin++){

                if (seam[col] == lin) continue;

                pic.set(col, rlin++, picture.get(col, lin));
            }
        }

        h--;

        // Calculating energy for the whole image
        for (int col = 0; col < w; col++) {
            for (int lin = 0; lin < h; lin++) {

                // Delta values
                int dc = componentDelta((col+1+w)%w, lin, (col-1+w)%w, lin);
                int dl = componentDelta(col, (lin+1+h)%h, col, (lin-1+h)%h);

                energy[col][lin] = Math.sqrt(Double.valueOf(dc) + Double.valueOf(dl));
            }
        }

        picture = new Picture(pic);
    }

    // Remove vertical seam from current picture
    public void removeVerticalSeam (int[] seam) {

        if (seam == null)
            throw new IllegalArgumentException("argument is null");

        if (seam.length != h)
            throw new IllegalArgumentException("seam has wrong length");

        if (seam.length == 1)
            throw new IllegalArgumentException("height = 1");

        for (int i = 0; i < h; i++) {
            if (!isInside(seam[i], i))
                throw new IllegalArgumentException("seam is invalid - 1");


            if (i < h-1 && Math.abs(seam[i] - seam[i+1]) > 1)
                throw new IllegalArgumentException("seam is invalid - 2");
        }

        Picture pic = new Picture(w-1, h);
        energy = new double[w-1][h];
        for (int lin = 0; lin < h; lin++){
            for (int col = 0, rcol = 0; col < w; col++){

                if (seam[lin] == col) continue;

                pic.set(rcol++, lin, picture.get(col, lin));
            }
        }

        w--;

        // Calculating energy for the whole image
        for (int col = 0; col < w; col++) {
            for (int lin = 0; lin < h; lin++) {

                // Delta values
                int dc = componentDelta((col+1+w)%w, lin, (col-1+w)%w, lin);
                int dl = componentDelta(col, (lin+1+h)%h, col, (lin-1+h)%h);

                energy[col][lin] = Math.sqrt(Double.valueOf(dc) + Double.valueOf(dl));
            }
        }

        picture = new Picture(pic);
    }

    // Check is a coordinate is inside image
    private boolean isInside (int x, int y) {

        if (x < 0 || x >= w || y < 0 || y >= h)
            return false;

        return true;
    }

    // Unit testing (required)
    public static void main (String[] args) {
        StdOut.println("oi");
    }
}
