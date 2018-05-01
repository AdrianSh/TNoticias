package es.ucm.fdi.tusnoticias.extra;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ArticleRipper {
	String url;
	Document doc;

	public ArticleRipper(String url) {
		this.url = url;
		getHTMLcontent();
	}

	void getHTMLcontent() {
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Document getDocument() {
		return doc;
	}
}