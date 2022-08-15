package me.yizhang.annotationlogger;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class LogWriter extends Thread {
    
    private final ArrayBlockingQueue<LogEntry> queue;
    private ArrayList<LogStorage> storage;

    public LogWriter(ArrayBlockingQueue<LogEntry> queue) {
        super("LogWriter");
        this.queue = queue;
    }

    public void updateStorage(ArrayList<LogStorage> storage) {
        this.storage = storage;
    }

    public void run() {
        while (true) {
            log();
        }
    }

    public void log() {
        if (queue.isEmpty()) {
            return;
        }
        
        LogEntry entry;
        try {
            entry = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        LogStorage store = storage.get(entry.getTarget());

        store.writeData(entry.getValues());
        flush();
    }

    public void flush() {
        for (int i = 0; i < storage.size(); i++) {
            storage.get(i).flush();
        }
    }

    public void close() {
        for (int i = 0; i < storage.size(); i++) {
            storage.get(i).close();
        }
    }
}