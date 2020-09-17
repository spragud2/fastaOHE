import java.io.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
public class OneHotEncoding { 

    private static final String a = "A";
    private static final String t = "T";
    private static final String c = "C";
    private static final String g = "G";
    private static HashMap<Character,Integer> nucleotideMap = new HashMap<>();
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 3) {
            System.out.println("Please pass one fasta file and output file name and read length");
            System.exit(0);
        }
        // map nucleotides to 0-axis in array

        nucleotideMap.put(a.charAt(0),0);
        nucleotideMap.put(t.charAt(0),1);
        nucleotideMap.put(c.charAt(0),2);
        nucleotideMap.put(g.charAt(0),3);

        int readLength = Integer.parseInt(args[2]);

        try (Scanner sc = new Scanner(new File(args[0]))) {


            List<String> temps = new ArrayList<String>();
            List<String> headers = new ArrayList<String>();

            String seq = "";
            boolean first = true;


            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim().toUpperCase();
                if (line.charAt(0)=='>') {
                    if (first == true) {
                        headers.add(line);
                        seq = "";
                        first = false;
                    } 
                    else {
                    headers.add(line);
                    temps.add(seq);
                    seq = "";
                    }
            } else {
                    seq+=line;
                }
            }
            String[] seqArray = temps.toArray(new String[0]);
            String[] headerArr = headers.toArray(new String[0]);
            int[][][] X = CharToInt(seqArray,nucleotideMap,readLength);
            printTable(X,headerArr,args[1]);
        }
    }

    private static int[][][] CharToInt(String[] seqs,HashMap<Character,Integer> nuMap,int readLength) {
        int[][][] X = new int[seqs.length][readLength][4];
        for (int j = 0; j < seqs.length;j++) {
            String s = seqs[j];
            if (s.length() < readLength){
                // s = padRight(s, readLength-s.length()-1);
                // System.out.println(s);
                continue;
            }
            for (int i = 0; i < readLength;i++  ) {
                char c = s.charAt(i);
                if (nuMap.containsKey(c)) {
                    X[j][i][nuMap.get(c)] = 1;
                }

            }
        }
        return X;
    }

    private static void printTable(int[][][] X,String[] headers,String name) {
        try (
            PrintStream output = new PrintStream(new File(name));
        ) {
            for (int i =0; i < X.length;i++) {
                output.println(headers[i]);
                for (int j = 0; j < X[i].length;j++) {
                    String s = "";
                    for (int k = 0; k < 4;k++) {
                        s+=X[i][j][k];
                    }
                    output.println(s);
                }
            } 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);  
   }
}