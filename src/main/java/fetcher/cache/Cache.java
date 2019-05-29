package fetcher.cache;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import webpage.WebPage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Cache {
    private static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    private CacheBackupManager cacheBackupManager;
    private Map<String, WebPage> cache;

    public Cache(CacheBackupManager cacheBackupManager) {
        this.cacheBackupManager = cacheBackupManager;
        initCache();
    }

    private void initCache() {
        cache = new HashMap<>();
        cacheBackupManager.loadFromDisk(cache);
        cacheBackupManager.configPersistToDisk(cache);
    }

    //TODO: normalize the URL before the lookup (for example - remove last slash)
    public Optional<WebPage> get(String url){
        if (cache.containsKey(url) && isWebPageUpdated(url)) {
            return Optional.of(cache.get(url));
        }

        return Optional.empty();
    }

    //TODO: normalize the URLS before inserting to cache
    public void put(String url, WebPage webPage) {
        cache.put(url, webPage);
    }

    private boolean isWebPageUpdated(String url) {
        try {
            HttpGet request = new HttpGet(url);
            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            Header lastModified = response.getFirstHeader(HttpHeaders.LAST_MODIFIED);
            return lastModified != null && isModifiedAfterDownload(url, lastModified.getValue());
        } catch (IOException | ParseException e) {
            return false;
        }
    }

    private boolean isModifiedAfterDownload(String url, String lastModified) throws ParseException {
        return lastModified != null && cache.get(url).getDownloadDate().after(format.parse(lastModified));
    }
}
