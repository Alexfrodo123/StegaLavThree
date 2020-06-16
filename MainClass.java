import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) throws IOException {
        int i, j, DlinaSlova;
        char chr;
        String StringKodSimvol, InputStroka, BinarMessToString;
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


        Scanner option = new Scanner(System.in);
        System.out.println ("Selected action:");
        System.out.println("1. To embed the information");
        System.out.println("2. To retrieve information");
        System.out.println("3. PSNR");
        int nubmer = option.nextInt();
        switch (nubmer) {
            case 1: {
                try {
                    File inputFile = new File("adf.bmp");
                    BufferedImage inputImage = ImageIO.read(inputFile);
                    int mpix = inputImage.getWidth(), npix = inputImage.getHeight();
                    BufferedImage outputImage = new BufferedImage(mpix, npix, inputImage.getType());
                    System.out.println("Enter what you want to place:");
                    Scanner scanNextLine = new Scanner(System.in);
                    InputStroka = scanNextLine.nextLine();
                    InputStroka += '@';
                    DlinaSlova = InputStroka.length();
                    StringBuffer BinarMess = new StringBuffer("");
                    char[] chars = InputStroka.toCharArray();
                    for (i = 0; i < DlinaSlova; i++) {
                        BinarMess.insert(i * 7, hashmap.get(chars[i]));
                    }
                    BinarMessToString = BinarMess.toString();
                    char[] charl = BinarMessToString.toCharArray();
                    i = 0;
                    int nolodin;
                    for (int y = 0; y < npix; y++) {
                        for (int x = 0; x < mpix; x++) {
                            Color pixel = new Color(inputImage.getRGB(x, y));
                            int pixelRed = pixel.getRed();
                            int pixelBlue = pixel.getBlue();
                            int pixelGreen   = pixel.getGreen();

                            if (i < BinarMessToString.length()) {
                                if (charl[i] == '0') nolodin = 0;
                                else nolodin = 1;

                                if (pixelGreen % 2 < nolodin) {
                                    pixelGreen += 1;
                                } else if (pixelGreen % 2 > nolodin) {
                                    pixelGreen -= 1;
                                }
                                i++;
                            }
                            Color outputPixel = new Color(pixelRed, pixelGreen, pixelBlue);
                            outputImage.setRGB(x, y, outputPixel.getRGB());
                        }
                    }
                    File outputFiles = new File("adk1.bmp");
                    ImageIO.write(outputImage, "bmp", outputFiles);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2: {
                try {
                    StringBuffer lp = new StringBuffer("");
                    File in = new File("adk1.bmp");
                    BufferedImage cont = ImageIO.read(in);
                    int m = cont.getWidth(), n = cont.getHeight();
                    int tr = 0;
                    String str = "";
                    char ck = '\0';
                    StringBuilder result = new StringBuilder();
                    for (int y = 0; y < n; y++) {
                        if (ck == '@') break;
                        for (int x = 0; x < m; x++) {
                            if (ck == '@') break;
                            Color pixel = new Color(cont.getRGB(x, y));
                            int greenCol = pixel.getGreen() % 2;
                            str += greenCol;
                            if (str.length() ==  7) {
                                lp.insert(tr, getKeyFromValue(hashmap, str));
                                ck = lp.charAt(tr);
                                if (ck != '@') result.append(ck);
                                else ck = '@';
                                str = "";
                                tr++;

                            }
                        }
                    }
                    System.out.println(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
            case 3: {
                File inputFileOne = new File("adf.bmp");
                BufferedImage first = ImageIO.read(inputFileOne);
                File inputFileTwo = new File("adk1.bmp");
                BufferedImage second = ImageIO.read(inputFileTwo);
                double MSERED = 0;
                double MSEGREEN = 0;
                double MSEBLUE = 0;
                double MSE;
                int m = first.getWidth();
                int n = first.getHeight();
                for (i = 0; i < m; i++) {
                    for (j = 0; j < n; j++) {
                        Color I = new Color(first.getRGB(i, j));
                        Color K = new Color(second.getRGB(i, j));
                        MSERED += (1.0 / (m * n)) * Math.pow(Math.abs((I.getRed() - K.getRed())), 2);
                        MSEGREEN += (1.0 / (m * n)) * Math.pow(Math.abs((I.getGreen() - K.getGreen())), 2);
                        MSEBLUE += (1.0 / (m * n)) * Math.pow(Math.abs((I.getBlue() - K.getBlue())), 2);
                    }
                }
                MSE = (MSERED + MSEGREEN + MSEBLUE) / 3.0;
                double PSNR = Math.round(10 * Math.log10(Math.pow(255, 2) / MSE) * 100) / 100.0;
                System.out.println("PSNR = " + PSNR + "dB");
                break;
            }
        }
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

