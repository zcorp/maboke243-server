package com.zcore.mabokeserver.common.file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCommon {

    public static boolean createFile(String filePath) throws IOException {
        PrintWriter writer;
        Path myPath = Paths.get(filePath);
        if(Files.exists(myPath)) {
            try {
                writer = new PrintWriter(filePath);
                writer.print("");
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("clear exist file");
            return true;
        } else {
            Files.createFile(myPath);
            System.out.println("File created");
            return false;
        }
    }
    
    public static void writeFormattedFile(String fileName, String content) {
        FileWriter fileWriter;
        PrintWriter printWriter;

        try {
            fileWriter = new FileWriter(fileName);
            printWriter = new PrintWriter(fileWriter);
            printWriter.print("Some String");
            printWriter.printf("Product name is %s and its price is %d $", "iPhone", 1000);
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public static void appendFile(String fileName, String content) {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String fileName, String content) {
        FileOutputStream fos;
        DataOutputStream outStream;

        try {
            fos = new FileOutputStream(fileName);
            outStream = new DataOutputStream(new BufferedOutputStream(fos));
            outStream.writeUTF(content);
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static File writeTmpFile(String fileName, String fileContent) {
        File tmpFile = null;
        FileWriter writer;

        try {
            tmpFile = File.createTempFile(fileName, ".tmp");
            writer = new FileWriter(tmpFile);
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tmpFile;
    }

    public static void writeLargeFile(String fileName, String fileContent) {
        byte[] strBytes;
        ByteBuffer buffer;
        FileChannel channel;
        RandomAccessFile stream;

        try {
            stream = new RandomAccessFile(fileName, "rw");
            channel = stream.getChannel();
            strBytes = fileContent.getBytes();
            buffer = ByteBuffer.allocate(strBytes.length);
            buffer.put(strBytes);
            buffer.flip();
            channel.write(buffer);
            stream.close();
            channel.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName
     * @throws IOException
     */
    public static void writeFileWithLock(String fileName, String fileContent) {
        FileChannel channel;
        FileLock lock = null;
        RandomAccessFile stream;

        try {
            stream = new RandomAccessFile(fileName, "rw");
            channel = stream.getChannel();

            lock = channel.tryLock();
            stream.writeChars(fileContent);
            lock.release();
            stream.close();
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readTmpFile(String fileName) {
        File tmpFile;
        BufferedReader reader ;
        try {
            tmpFile = File.createTempFile(fileName, ".tmp");
            reader = new BufferedReader(new FileReader(tmpFile));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }

        return resultStringBuilder.toString();
    }

    public static String getFileContent(String fileName) {
        Class clazz;
        String res = null;
        InputStream inputStream;

        try {
            clazz = FileCommon.class;
            inputStream = clazz.getResourceAsStream(fileName);
            res = readFromInputStream(inputStream);
            //File file = new File(classLoader.getResource("fileTest.txt").getFile());
            //inputStream = new FileInputStream(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static void readFileContent(String fileName) {  
        String currentLine;   
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            currentLine = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void readEncodedFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader
        (new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String currentLine = reader.readLine();
        reader.close();
    }

    /*public static void readFilesLines_thenFileData() {
        String expectedData = "Hello, world!"; 
        Path path = Paths.get(getClass().getClassLoader()
          .getResource("fileTest.txt").toURI());
        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
             
    }*/

    /*public static void readLargeFile(String fileName) throws IOException {
        String expected_value = "Hello, world!";
        Path path = Paths.get(fileName);
        BufferedReader reader = Files.newBufferedReader(path);
        String line = reader.readLine();
    }*/

    /*public void readSmallFile(String fileName) throws IOException {
        String expected_value = "Hello, world!";
        Path path = Paths.get(fileName);
        String read = Files.readAllLines(path).get(0);
    }*/

    public static void getFile(String filePath, String content) {
        File file = null;

        try {
            file = new File(filePath);
            file.createNewFile();
             //FileOutputStream oFile = new FileOutputStream(file, false); 
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}
