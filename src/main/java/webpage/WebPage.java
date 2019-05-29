package webpage;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class WebPage implements Serializable {
    private Date downloadDate;
    private float ratio;
    private Set<String> linksInPage;

    public WebPage(Date downloadDate, float ratio, Set<String> linksInPage) {
        this.downloadDate = downloadDate;
        this.ratio = ratio;
        this.linksInPage = linksInPage;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public float getRatio() {
        return ratio;
    }

    public Set<String> getLinksInPage() {
        return linksInPage;
    }
}
