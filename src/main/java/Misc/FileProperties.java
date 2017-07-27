package Misc;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static Misc.Print.print;

public class FileProperties {
    public static String getOutputFileName(String inputFileName) {
        String outputFileName;
        if (inputFileName.substring(inputFileName.length() - 3, inputFileName.length()).equals("zip")) {
            outputFileName = inputFileName.substring(0, inputFileName.length() - 4) + "_unzipped";
        } else {
            outputFileName = inputFileName + "_converted";
       }
        return outputFileName;
    }
    public static String getOutputFileName(String inputFileName, int level) {
        String outputFileName;
        if (inputFileName.substring(inputFileName.length() - 3, inputFileName.length()).equals("zip")) {
            outputFileName = inputFileName.substring(0, inputFileName.length() - 4) + "_unzipped";
        } else {
            outputFileName = inputFileName + "_converted";
        }
        return outputFileName;
    }


    public static String getOutputFileName(String inputFileName, String subStr) {
        String outputFileName;
        if (inputFileName.substring(inputFileName.length() - 3, inputFileName.length()).equals("zip")) {

            outputFileName = inputFileName.substring(0, inputFileName.length() - 4) + "_unzipped";
        } else {
            outputFileName = inputFileName + "_"+subStr;
        }
        return outputFileName;
    }

    public static String getInputFileType(String inputFileName) {
        print(inputFileName);
        if (inputFileName.substring(inputFileName.length() - 3, inputFileName.length()).equals("zip")) {
            return "zip";
        } else
            return "txt";
    }
    public static String getDateUnzip(String inputFileName) {
        FileInputStream fstream = null;
        String date = "";
        BufferedReader br = null;
        try {
            fstream = new FileInputStream(inputFileName);
            br = new BufferedReader(new InputStreamReader(fstream));
            String line = br.readLine();
            date = line.substring(2, 10);
            br.close();
        } catch (FileNotFoundException e) {
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        print(date);
        return date;

    }
    public static String getDate(String inputZipFileName) {
        String date = "";
        BufferedReader br = null;
        try {
            ZipFile zf = new ZipFile(inputZipFileName);
            Enumeration entries = zf.entries();

            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                print(ze.getName());
                long size = ze.getSize();
                if (size > 0) {
                    System.out.println("Length is " + size);
                    br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line = br.readLine();
                    date = line.substring(2, 10);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (br != null)
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

        }
        return date;
    }

    public static String extractYear(String inputFileName) {
        String date="";
        if (getInputFileType(inputFileName).equals("zip"))
            date = getDate(inputFileName);
        else
            date = getDateUnzip(inputFileName);
        String year = date.substring(4, 8);
        String month = date.substring(2, 4);
        int dateNumber = Integer.parseInt(year + month);
        return analyzeYear(dateNumber);
    }

    public static String analyzeYear(int date) {
        if (date >= 201006 && date <= 201207)
            return "2010";
        if (date >= 201208 && date <= 201302)
            return "2012";
        if (date >= 201305 && date <= 201312)
            return "2013";
        if (date >= 201506 && date <= 201607)
            return "2015";
        if (date >= 201607 && date <= 201707)
            return "2016";
        else
            return "None";
    }

    public static List<String> wordCollect(JavaSparkContext sc, String inputFileName) {
        JavaRDD<String> textFile = sc.textFile(inputFileName);
        List<String[]> wordListArr = textFile.map(e -> e.split("[\\s,;.]+")).collect();
        List<String> wordList = new ArrayList<>();

        for (String[] word : wordListArr) {
            for (int i = 0; i < word.length; i++) {
                wordList.add(word[i]);
            }
        }
        int k = 0;
        for (String word : wordList) {
            k++;
        }
        print("Number of Tickers selected : " + k);
        print("Finding the following Tickers");
        for (String word : wordList) {
            System.out.print(word + " ");

        }
        print("\n");
        return wordList;
    }

    public static List<Integer> columnSelect(JavaSparkContext sc, String inputFileName) {
        JavaRDD<String> textFile = sc.textFile(inputFileName);
        List<String[]> columnListArr = textFile.map(e -> e.split("[\\s,;.]+")).collect();
        List<Integer> columnList = new ArrayList<>();

        for (String[] col : columnListArr) {
            for (int i = 0; i < col.length; i++) {
                try {
                    columnList.add(Integer.parseInt(col[i]));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        int k = 0;
        for (int column : columnList) {
            k++;
        }
        print("Number of columns selected : " + k);
        print("Finding the following columns");
        for (int column : columnList) {
            System.out.print(column + " ");

        }
        print("\n");
        return columnList;
    }
    public static List<String> wordCollect(String wordListStr) {
        List<String> wordList = new ArrayList<>(Arrays.asList(wordListStr.split("[\\s,;.]+")));
        print("Number of Tickers selected : " + wordList.size());
        print("Finding the following Tickers");
        for (String word : wordList) {
            System.out.print(word + " ");

        }
        print("\n");
        return wordList;
    }
    public static List<Integer> columnSelect(String columns) {
        List<String> columnListStr = new ArrayList<>(Arrays.asList(columns.split("[\\s,;.]+")));
        List<Integer> columnList = new ArrayList<>();

        for (String column : columnListStr) {
            columnList.add(Integer.valueOf(column));
        }
        print("Number of columns selected : " + columnList.size());
        print("Finding the following columns");
        for (int column : columnList) {
            System.out.print(column + " ");

        }
        print("\n");
        return columnList;
    }
    public static boolean isFile(String str){
        File file = new File(str);
        boolean isFile =      file.isFile();
        if (isFile)
            return true;
        else return false;
    }
    public static boolean isDirectory(String str){
        File file = new File(str);
        boolean isDirectory = file.isDirectory();
        if (isDirectory)
            return true;
        else return false;
    }
    public static boolean FileorDirExists(String str){
        File file = new File(str);
        boolean exists =      file.exists();
        if (exists)
            return true;
        else return false;
    }
}
