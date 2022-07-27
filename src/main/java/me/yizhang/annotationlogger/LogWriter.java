package me.yizhang.annotationlogger;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class LogWriter extends Thread {
    
    private final ArrayDeque<LogEntry> queue;
    private ArrayList<LogStorage> storage;

    private boolean running = false;

    public LogWriter(ArrayDeque<LogEntry> queue) {
        super("LogWriter");
        this.queue = queue;
    }

    public void updateStorage(ArrayList<LogStorage> storage) {
        this.storage = storage;
    }

    public void run() {

        running = true;

        while (true) {
            if (!running && queue.isEmpty()) {
                this.interrupt();
            }

            if (isInterrupted()) {
                close();
                break;
            }
            
            log();
        }
    }

    public void end() {
        running = false;
    }

    public void log() {
        if (queue.isEmpty()) {
            return;
        }
        
        LogEntry entry = queue.pop();

        LogStorage store = storage.get(entry.getTarget());

        store.writeData(entry.getValues());
    }

    public void close() {
        System.out.println("Closing writer");
        for (int i = 0; i < storage.size(); i++) {
            storage.get(i).flush();
        }
    }
}
