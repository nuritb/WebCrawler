package crawler;

import fetcher.Fetcher;
import utils.FileUtils;
import webpage.WebPage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static utils.FileUtils.OUTPUT_FILE_NAME;

public class Crawler {

    private static final String OUTPUT_TITLE = "Url\tmaxDepth\tratio";
    private Set<String> linksToCrawl;
    private Fetcher fetcher;

    public Crawler(Fetcher fetcher) {
        this.fetcher = fetcher;
        this.linksToCrawl = new HashSet<>();
    }

    public void crawl(String url, int maxDepth) {
        linksToCrawl.add(url);
        FileUtils.writeToFile(OUTPUT_TITLE, OUTPUT_FILE_NAME, false);
        crawlInDepth(1, maxDepth);
    }

    private void crawlInDepth(int currentDepth, int maxDepth) {
        List<String> linksInPages = new ArrayList<>(linksToCrawl);
        linksToCrawl.clear();

        if (!linksInPages.isEmpty() && currentDepth <= maxDepth) {
            linksInPages.forEach(url -> processUrl(url, currentDepth));
            crawlInDepth(currentDepth + 1, maxDepth);
        }
    }

    private void processUrl(String url, int depth) {
        try {
            WebPage webPage = fetcher.fetch(url);
            FileUtils.writeToOutputFile(url, depth, webPage.getRatio());
            linksToCrawl.addAll(webPage.getLinksInPage());
        } catch (Exception e) {
            System.err.println("Failed to process URL " + url + ": " + e.getMessage());
        }

    }
}
