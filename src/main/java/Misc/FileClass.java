package Misc;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static Misc.Debug.debug;
import static Misc.Print.print;

public class FileClass {

    public static void printFileLine(String inputFileName, int lineCount) {
        FileInputStream fstream = null;
        BufferedReader br = null;
        try {
            fstream = new FileInputStream(inputFileName);
            br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            int k = 0;
            StringBuilder str = new StringBuilder();
            while ((strLine = br.readLine()) != null) {
                if (k >= 0) {
                    System.out.println(strLine);
                    str.append(strLine + "\n");
                    lineCount--;
                    if (lineCount < 0)
                        break;
                }
                k++;
            }
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

    }

    public static File[] readDirectory(String directory) {

        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        return listOfFiles;
    }

    public static void deleteFileorDir(String fileOrDirStr) {
        File fileOrDir = new File(fileOrDirStr);
        if (fileOrDir.exists()) {
            System.out.println("Do you want to delete file : " + fileOrDirStr);
            Scanner scan = new Scanner(System.in);
            String s = scan.next();
            if (s.equals("y")) {
                if (fileOrDirStr.substring(fileOrDirStr.length() - 1, fileOrDirStr.length()).equals("/")) {
                    File[] files = fileOrDir.listFiles();
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            if (files[i].isDirectory()) {
                                deleteFileorDir(files[i].getAbsolutePath());
                                System.out.println("Deleted Directory : " + files[i].getAbsolutePath());

                            } else {
                                files[i].delete();
                                System.out.println("Deleted File : " + files[i].getAbsolutePath());

                            }
                        }
                    }
                    fileOrDir.delete();
                } else {
                    fileOrDir.delete();
                    System.out.println("Deleted Single File : " + fileOrDirStr);
                }
            }
        }
    }

    public static String getFirstLineUnzipped(String inputFileName) {
        FileInputStream fstream = null;
        String firstLine = "";
        BufferedReader br = null;
        try {
            fstream = new FileInputStream(inputFileName);
            br = new BufferedReader(new InputStreamReader(fstream));
            firstLine = br.readLine();
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
        return firstLine;
    }

    public static String getFirstLineZip(String inputZipFileName) {
        String line = "";
        BufferedReader br = null;
        try {
            ZipFile zf = new ZipFile(inputZipFileName);
            Enumeration entries = zf.entries();

            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                long size = ze.getSize();
                if (size > 0) {
                    System.out.println("Length is " + size);
                    br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    line = br.readLine();

                }
            }
            br.close();
        } catch (IOException e) {
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();

        }
        return line;

    }

    public static void FileSampleWrite(String inputFileName, String count) {
        FileInputStream inputStream;
        String outputFileName;
        File outputFile;
        PrintStream outputStream;
        BufferedReader br;
        try {
            inputStream = new FileInputStream(inputFileName);
            outputFileName = inputFileName + "_sample";
            outputFile = new File(outputFileName);
            outputStream = new PrintStream(outputFile);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String strLine;
            int lastindex = Integer.parseInt(count);
            int k = 0;
            StringBuilder stringBuilder = new StringBuilder();
            int bufferSize = 100000;
            while ((strLine = br.readLine()) != null) {

                if (k > lastindex)
                    break;
                else {
                    stringBuilder.append(strLine);
                    if (k < lastindex)
                        stringBuilder.append('\n');
                    k++;
                    if (k > bufferSize) {
                        outputStream.print(stringBuilder);
                        outputStream.flush();
                        stringBuilder.setLength(0);
                        if (k % bufferSize == 0)
                            print(k);
                    }
                }
            }
            outputStream.print(stringBuilder);
            outputStream.flush();
            stringBuilder.setLength(0);
            inputStream.close();
            br.close();
            outputStream.close();
            debug("end");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileType(String directory) {
        if (directory.toLowerCase().contains("nbbo"))
            return "nbbo";
        else if (directory.toLowerCase().contains("quote"))
            return "quote";
        else if (directory.toLowerCase().contains("trade"))
            return "trade";
        else return "None";
    }
    public static String getParentDir(String inputFileName) {
        File file = new File(inputFileName);
        File parentDir = file.getParentFile(); // to get the parent dir
        String parentDirName = file.getParent();
        return parentDirName;
    }
    public static String getParentDir(String inputFileName, int level) {
        File file = new File(inputFileName);
        File parentDir=null;
        for(int i = 0; i<level; i++){
            parentDir = file.getParentFile(); // to get the parent dir
            file = new File(String.valueOf(parentDir));
        }
        String parentDirName = file.getParent();
        return parentDirName;
    }
    public static void mkdir(String dirName) {
        File dir = new File(dirName);
        boolean successful = dir.mkdir();
        if (successful) {
            System.out.println("directory was created successfully");
        } else {
            System.out.println("failed trying to create the directory");
        }
    }
    public static void mkdir(String inputFileName, String dirName) {
        String dirLoc = getParentDir(inputFileName, 1);
        File dir = new File(dirLoc + "/"+dirName);
        boolean successful = dir.mkdir();
        if (successful) {
            System.out.println("directory was created successfully");
        } else {
            System.out.println("failed trying to create the directory");
        }
    }
    public static void mkdir(String inputFileName, String dirName, int level) {
        String dirLoc = getParentDir(inputFileName, level);
        File dir = new File(dirLoc + "/"+dirName);
        boolean successful = dir.mkdir();
        if (successful) {
            System.out.println("directory was created successfully");
        } else {
            System.out.println("failed trying to create the directory");
        }
    }

    public static String unZip(String zipFile, String outputFileName) {
        BufferedReader br = null;
        PrintWriter outputStream = null;
        String sizeStr = "";
        try {
            ZipFile zf = new ZipFile(zipFile);
            File outputFile = new File(outputFileName);
            if (outputFile.exists()) {
                System.out.println("File already unzipped\nDo you want to unzip again?");
                Scanner scan = new Scanner(System.in);
                String s = scan.next();
                if (s.equals("y"))
                    outputFile.delete();
                else
                    return "0";
            }
            outputStream = new PrintWriter(outputFile);
            StringBuilder strBuilder = new StringBuilder();
            Enumeration entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                long size = ze.getSize();
                sizeStr = readableFileSize(size);
                if (size > 0) {
                    br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line;
                    int k = 1;
                    line = br.readLine();
                    print("Extracting zip file...");
                    while (line != null) {
                        strBuilder.append(line);
                        strBuilder.append("\r\n");
                        if (k % 5000000 == 0) {
                            outputStream.print(strBuilder);
                            outputStream.flush();
                            strBuilder.setLength(0);
                            print("On Line: " + k);
                        }
                        k++;
                        line = br.readLine();
                    }
                    outputStream.print(strBuilder);
                    outputStream.flush();
                    strBuilder.setLength(0);
                    print("Total Lines : " + k);
                    br.close();
                    outputStream.close();

                }
            }

        } catch (Exception e) {

            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            outputStream.close();
            System.out.println(e);
        }

        return sizeStr;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    public static void main(String[] args){
        String dirName = "Temp";
        mkdir("/home/anjana/Downloads/DATA/bulk/EQY_US_ALL_TRADE_20100506.zip", "aa");

    }
}
