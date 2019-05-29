package fetcher;

import exceptions.InvalidMimeTypeException;
import fetcher.cache.Cache;
import webpage.WebPage;

import java.io.IOException;

public class Fetcher {
    private Cache cache;
    private Downloader downloader;

    public Fetcher(Cache cache, Downloader downloader) {
        this.cache = cache;
        this.downloader = downloader;
    }

    public WebPage fetch(String url) throws InvalidMimeTypeException, IOException {
        WebPage webPage = cache.get(url).orElse(downloader.downloadPage(url));
        cache.put(url, webPage);
        return webPage;
    }
}
