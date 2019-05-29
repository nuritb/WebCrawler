package fetcher;

import exceptions.InvalidMimeTypeException;
import org.apache.http.entity.ContentType;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utils.RatioUtils;
import webpage.WebPage;
import utils.FileUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Downloader {

    //TODO: read from properties file
    private static final int TIMEOUT = 3 * 1000;
    private static final String A_HREF = "a[href]";
    private static final String HREF_ATTRIBUTE = "abs:href";

    WebPage downloadPage(String url) throws InvalidMimeTypeException, IOException {
        Connection.Response res = Jsoup.connect(url).timeout(TIMEOUT).execute();
        String contentType = res.contentType().split(";")[0];

        if (isTextContentType(contentType)) {
            FileUtils.writeToFile(res.body(), url, false);
            return createWebPage(res, url);
        } else {
            throw new InvalidMimeTypeException("Invalid mime type: " + contentType);
        }
    }

    private boolean isTextContentType(String contentType) {
        return ContentType.TEXT_HTML.getMimeType().equals(contentType);
    }

    private WebPage createWebPage(Connection.Response res, String page) throws IOException {
        Document document = res.parse();
        Set<String> linksInPage = getPageLinks(document);
        return new WebPage(new Date(), RatioUtils.calcRatio(page, linksInPage), linksInPage);
    }

    private Set<String> getPageLinks(Document document) throws IOException {
        Elements linkElements = document.select(A_HREF);

        return linkElements.stream()
                .map(element -> element.attr(HREF_ATTRIBUTE))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
