import fetcher.Fetcher;
import fetcher.cache.Cache;
import fetcher.cache.CacheBackupManager;
import fetcher.Downloader;
import crawler.Crawler;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Cache cache = new Cache(new CacheBackupManager());
        Fetcher fetcher = new Fetcher(cache, new Downloader());
        Crawler crawler = new Crawler(fetcher);

        if(isValidArgs(args)) {
            crawler.crawl(args[0].toLowerCase(), Integer.valueOf(args[1]));
        }
    }

    private static boolean isValidArgs(String[] args) {
        if(args.length != 2) {
            System.err.println("Missing argument. expecting <url, maxDepth>");
            return false;
        }

        Integer maxDepth;
        try {
            maxDepth = Integer.valueOf(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid max depth. should be an Integer");
            return false;
        }

        if(maxDepth < 1) {
            System.err.println("Invalid max depth. should be > 0");
            return false;
        }

        return true;
    }
}
