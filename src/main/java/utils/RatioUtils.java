package utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

public class RatioUtils {

    public static float calcRatio(String page, Set<String> linksInPage) throws MalformedURLException {
        URL pageUrl = new URL(page);

        long subDomainLinks = linksInPage.stream()
                .filter(link -> isLinkSubDomain(pageUrl, link))
                .count();

        return linksInPage.isEmpty() ? 0 : (float) subDomainLinks / linksInPage.size();
    }

    private static boolean isLinkSubDomain(URL pageUrl, String link) {
        try {
            URL linkUrl = new URL(link);
            return Objects.equals(linkUrl.getHost(), (pageUrl.getHost()));
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
