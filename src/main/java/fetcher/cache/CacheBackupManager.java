package fetcher.cache;

import webpage.WebPage;

import java.io.*;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheBackupManager {
    private static final String FILE_NAME = "cache.ser";

    void loadFromDisk(Map<String, WebPage> cache) {
        try {
            File f = new File(FILE_NAME);
            if (f.exists() && !f.isDirectory()) {
                FileInputStream fis = new FileInputStream(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                cache.putAll((Map<String, WebPage>) ois.readObject());
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load cache from disk. " + e.getMessage());
        }
    }

    void configPersistToDisk(Map<String, WebPage> cache) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new cacheSerializer(cache), 1, 1, TimeUnit.MINUTES);
    }

    class cacheSerializer implements Runnable {
        private Map<String, WebPage> cache;

        cacheSerializer(Map<String, WebPage> cache) {
            this.cache = cache;
        }

        @Override
        public void run() {
            try {
                FileOutputStream fos = new FileOutputStream(FILE_NAME);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(cache);
                oos.close();
            } catch (IOException e) {
                System.err.println("Failed to persist cache to disk. " + e.getMessage());
            }
        }
    }

}
