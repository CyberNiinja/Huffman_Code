package mada;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Path outputMadaPath = Paths.get("output-mada.dat");
        Path decTabMadaPath = Paths.get("dec_tab-mada.txt");
        Path decompressMadaPath = Paths.get("decompress-mada.txt");
        Path inputPath = Paths.get("input.txt");
        Path outputPath = Paths.get("output.dat");
        Path decTabPath = Paths.get("dec_tab.txt");
        Path decompressPath = Paths.get("decompress.txt");

        // Encode input.txt

        decode(outputMadaPath, decompressMadaPath, decTabMadaPath);
    }

    public static String encode(Path inputPath, Path outputPath, Path decTabPath) {

        // Read the input file
        String input = "";
        try {
            input = Files.readString(inputPath);
        } catch (IOException ex) {
            System.out.println("Somehting went while reading the input file");
        }

        // generate occurance table
        int[] o = new int[128];
        for (char c : input.toCharArray()) {
            o[c]++;
        }

        // generate Leaf nodes
        List<Node> leaves = new ArrayList<>();
        for (int i = 0; i < o.length; i++) {
            if (o[i] != 0) {
                leaves.add(new Node(o[i], (char) i));
            }
        }

        // generate tree
        Node.constructTree(leaves);

        // generate Code Map
        Map<Character, String> d = new HashMap<Character, String>();
        for (Node leaf : leaves) {
            d.put(leaf.c.charAt(0), leaf.code);
        }

        // Write Code map to file
        String codemap = "";
        for (int i = 0; i < 128; i++) {
            if (d.containsKey((char) i)) {
                codemap += String.format("%1$s:%2$s-", i, d.get((char) i));
            }
        }

        codemap = codemap.substring(0, codemap.length() - 1);
        System.out.println(codemap);
        try {
            Files.writeString(decTabPath, codemap, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // encode ascii characters
        String encoded = "";
        for (char c : input.toCharArray()) {
            encoded += d.containsKey(c) ? d.get(c) : "";
        }

        // add ending
        encoded += "1";
        while (encoded.length() % 8 != 0) {
            encoded += "0";
        }
        System.out.println("encoded: " + encoded);

        // transform to byte array
        byte[] out = new byte[encoded.length() / 8];
        for (int i = 0; i < out.length; i++) {
            String b = encoded.substring(i * 8, (i + 1) * 8);
            int x = Integer.parseInt(b, 2);
            out[i] = (byte) x;
        }
        // write the byte array
        try {
            Files.write(outputPath, out, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encoded;
    }

    public static String decode(Path inputPath, Path outputPath, Path decTabPath) {

        // read input
        String binaryWithTrail = "";
        byte[] bytes = Read(inputPath.toString());
        for (byte b : bytes) {
            binaryWithTrail += String.format("%8s", Integer.toBinaryString(Byte.toUnsignedInt(b))).replace(' ', '0');
        }
        System.out.println("RECOVER: " + binaryWithTrail);

        // read code map
        String decString = "";
        try {
            decString = Files.readString(decTabPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // encoded to binary String with trailing 0
        String binary = binaryWithTrail.substring(0, binaryWithTrail.lastIndexOf("1") - 1);

        // make code map
        System.out.println(decString);
        String[] parts = decString.split(":|-");
        Map<String, Character> d = new HashMap<>();
        for (int i = 0; i < (parts.length / 2); i++) {
            d.put(parts[(i * 2) + 1], (char) Integer.parseInt(parts[i * 2]));
        }

        // decode with code map
        String result = "";
        int i = 1;
        while (binary.length() > 0 && i < binary.length() - 1) {
            String c = binary.substring(0, i);
            if (d.containsKey(c)) {
                result += d.get(c);
                binary = binary.substring(i);
                i = 0;
            } else {
                i++;
            }
        }
        try {
            Files.writeString(outputPath, result, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void Write(byte[] out, String filename) {

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(out);
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static byte[] Read(String filename) {
        try {
            File file = new File(filename);
            byte[] bFile = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(bFile);
            fis.close();
            return bFile;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
