import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class main {
    public static double pi = 3.142857;

    public static void main(String[] args) throws Exception {
        int[][] twoDimArray = new int[8][8];



        int i, j, DlinaSlova;
        char chr;
        String StringKodSimvol, InputStroka;
        StringBuffer BinarKodSimvol = new StringBuffer("");
        HashMap<Character, String> hashmap = new HashMap<>();

        for (i = 1040; i < 1104; i++) {
            j = i - 1040;
            StringKodSimvol = Integer.toBinaryString(j);
            BinarKodSimvol.insert(0, StringKodSimvol);
            while (BinarKodSimvol.length() != 7) BinarKodSimvol.insert(0, '0');
            chr = (char) i;
            hashmap.put(chr, BinarKodSimvol.toString());
            BinarKodSimvol.setLength(0);

        }
        for (i = 32; i < 63; i++) {
            j = i + 33;
            StringKodSimvol = Integer.toBinaryString(j);
            BinarKodSimvol.insert(0, StringKodSimvol);
            while (BinarKodSimvol.length() != 7) BinarKodSimvol.insert(0, '0');
            chr = (char) i;
            hashmap.put(chr, BinarKodSimvol.toString());
            BinarKodSimvol.setLength(0);
        }
        hashmap.put('@', "1111111");
        System.out.println(hashmap);


        File inputFile = new File("adk2.bmp");
        BufferedImage inputImage = ImageIO.read(inputFile);
        int mpix = inputImage.getWidth(), npix = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(mpix, npix, inputImage.getType());
        int mpixVocem = mpix / 8;
        int npixVocem = npix / 8;
        int q = 0;
        System.out.println(mpix / 8);
        System.out.println(npix / 8);
        int tr = 0;
        String str = "";
        char ck = '\0';
        StringBuilder result = new StringBuilder();


        System.out.println("Enter what you want to place:");
        Scanner scanNextLine = new Scanner(System.in);
        InputStroka = scanNextLine.nextLine();
        InputStroka += '@';
        StringBuffer lp = new StringBuffer("");
        DlinaSlova = InputStroka.length();
        StringBuffer BinarMess = new StringBuffer("");
        char[] chars = InputStroka.toCharArray();
        for (i = 0; i < DlinaSlova; i++) {
            BinarMess.insert(i * 7, hashmap.get(chars[i]));
        }


        for (int chechikn = 0; chechikn < 8; chechikn++) {

            for (int chechikm = 0; chechikm < 8; chechikm++) {


                for (int y = chechikn * 8; y < chechikn * 8 + 8; y++) {
                    for (int x = chechikm * 8; x < chechikm * 8 + 8; x++) {
                        Color pixel = new Color(inputImage.getRGB(x, y));
                        int pixelGreen = pixel.getGreen();
                        twoDimArray[x % 8][y % 8] = pixelGreen;


                    }
                }


                int n = 8, m = 8, k, l;

                double[][] dct = new double[m][n];


                double ci, cj, dct1, sum;



                for (i = 0; i < m; i++) {

                    for (j = 0; j < n; j++) {

                        // ci и cj зависит от частоты, а также

                        // номер строки и столбца указанной матрицы

                        if (i == 0)

                            ci = 1 / Math.sqrt(m);

                        else

                            ci = Math.sqrt(2) / Math.sqrt(m);


                        if (j == 0)

                            cj = 1 / Math.sqrt(n);

                        else

                            cj = Math.sqrt(2) / Math.sqrt(n);


                        // сумма временно сохранит сумму

                        // косинус сигналы

                        sum = 0;

                        for (k = 0; k < m; k++) {

                            for (l = 0; l < n; l++) {

                                dct1 = twoDimArray[k][l] *

                                        Math.cos((2 * k + 1) * i * pi / (2 * m)) *

                                        Math.cos((2 * l + 1) * j * pi / (2 * n));

                                sum = sum + dct1;

                            }

                        }
                        dct[i][j] = ci * cj * sum;
                       double value = Math.round(dct[i][j]);
                       int jk = (int) value;

                       int jp = jk % 2;
                        str += jp;
                        if (str.length() ==  7) {
                            lp.insert(tr, getKeyFromValue(hashmap, str));
                            ck = lp.charAt(tr);
                            if (ck != '@') result.append(ck);
                            else ck = '@';
                            str = "";
                            tr++;

                        }
                    }
                    System.out.println('\n');

                }


                if (q == 0) {
                    System.out.println(Arrays.deepToString(dct));
                    q++;
                }
            }
        }
        System.out.println("Матрешка");

    }
    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}