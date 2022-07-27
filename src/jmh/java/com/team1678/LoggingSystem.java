package com.team1678;

import java.util.ArrayList;

import java.io.FileWriter;

public class LoggingSystem {
    private static LoggingSystem mInstance; 
    //  set original directory path. Will be added to in LoggingSystem() when new directories are created inside /tmp/
    public static String mDirectory = "./Output Logs";
    ArrayList<ILoggable> loggableItems = new ArrayList<ILoggable>();

    ArrayList<FileWriter> loggableFiles = new ArrayList<FileWriter>();

    /* 
        Create a for loop that goes over all the current files and subdirectories in mDirectories.
        If the directory is empty (when the max number is 0), start a new subdirectory at 1.
        Whenever the logging system reboots, function will scan over all the exhisting files and subdirectories and find the largest one.
        New subdirectory is created by adding one (1) to the max file number.
    */
    public LoggingSystem() {

    }
    public synchronized static LoggingSystem getInstance() {
        if (mInstance == null) {
            mInstance = new LoggingSystem();
        }
        return mInstance; 
    }
    //  start function that opens file
    public void register(ILoggable newLoggable, String fileName) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(mDirectory + "/" + fileName);
        } catch (Exception e) {
            System.err.println("Couldn't register new File");  //  Exception Handling 
        }
        ArrayList<String> itemNames = newLoggable.getItemNames();
        loggableFiles.add(fileWriter);
        //  Write names to file
        try {
            for (int h=0; h < itemNames.size(); h++) {
                fileWriter.write(itemNames.get(h));
                if (h!= itemNames.size()) {
                    fileWriter.write(",");
                }
            }
            fileWriter.write("\n");
            //  Adding Loggable to loggableItems list
            loggableItems.add(newLoggable);
        } catch (Exception e) {
            System.err.println("Couldn't write to file");
        }
    }
    //  Logging Function
    //  gets called when main begins logging
    public void Log() {
        try{
            for (int i=0; i < loggableItems.size(); i++) {
               ArrayList<ArrayList<Double>> items = loggableItems.get(i).getItems();
               //  get object fileWriter from the list 
               FileWriter fileWriter = loggableFiles.get(i);

               //  write to files
               for (int j=0; j < items.size(); j++) {
                   ArrayList<Double> data = items.get(j);
                   for (int m=0; m < data.size(); m++){
                        fileWriter.write(data.get(m).toString());
                        if (m != data.size()){
                            fileWriter.write(",");
                        }
                    }
                fileWriter.write("\n");
                }
            }
        } catch (Exception e) {
            System.err.println("Couldn't get object and/or log it");
            e.printStackTrace();
        }
    }
    //  Close Logging System
    public void Close() {
        try {
            //  Get final logs
            Log();
            //  Close files 
            for (int i=0; i< loggableFiles.size(); i++) {
                FileWriter fileWriter = loggableFiles.get(i);
                fileWriter.close();
            }
        } catch (Exception e) {
            System.err.println("Couldn't close file");
        }
    }
}
