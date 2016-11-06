import java.io.IOException;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This is the crawler class. 
 * Connects to Alexa website, parses it and outputs list of required link names as strings
 */

/**
 * @author mkunaparaju
 * 
 */
public class AlexaCrawler {
	/**
	 * @param baseURL URL to start the crawl from
	 * @param pageCount the number of pages we send request to
	 * @param topN keeps track of how many websites yet to add to linksList
	 * @param URLQueue stores the urls required to crawl through
	 * @param linksList stores the website names to be printed names after parsing the page
	 * 
	 */
	
	String baseURL;
	int pageCount;
	int topN;
	Queue<String> URLQueue;
	List<String> linksList;
	AlexaCrawler(int topN)
	{
		this.baseURL = "http://www.alexa.com/topsites/global;";
		this.pageCount = 0;
		this.topN = topN;
		linksList = new ArrayList();
		URLQueue = new LinkedList();
		URLQueue.add(baseURL + pageCount);
	}
	
	/** 
	 * This method connects to the Alexa Website 
	 * connection times out in 5secs if the server does not respond as required
	 * @return document object
	 */
	Document getDocFromURL(String url)
	{
		Document alexaDoc = null; 
		try 
		{
			alexaDoc = Jsoup.connect(url).timeout(5000).get();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return alexaDoc;
	}

	/** 
	 * constructs the URLQueue
	 * calls the method which adds links to lists
	 * the thread sleeps for 20 seconds between calls to the website;
	 * Note: during the course of writing this program, I had accidentally gone into an infinite loop, which sent too many requests to the website
	 * as a result I no longer have access to the website from my IP address
	 * thread.sleep will avoid this presently for anyone else executing the program
	 * 
	 * @return document object
	 */
	void constructLinkListfromDoc() 
	{
		while(!URLQueue.isEmpty())
		{
			String url = URLQueue.poll();
			Document alexaDoc = getDocFromURL(url);
			Elements elemList = alexaDoc.getElementsByClass("site-listing");
			
			if(topN <= elemList.size())
			{
				addLinkstoList(elemList);
				return;
			} 
			
			addLinkstoList(elemList);
			pageCount = pageCount+1;
			topN = topN - elemList.size();
			URLQueue.add(baseURL+pageCount);
			
			System.out.println("Hold on, the program is still running");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * parses the html and adds the website names to a list
	 */
	
	void addLinkstoList(Elements elemList)
	{
		int printCount = 0;
		
		for(Element link : elemList)
			{
				if(printCount == topN) return;
				Elements siteTags = link.getElementsByTag("a");
				for(Element website: siteTags )
				{
					if(website.hasAttr("href"))
						linksList.add(website.text());
						//System.out.println(website.text());
				}		
				printCount++;		
			}
	}
		
	/*
	 * 
	 * @return ArrayList of website names to be printed 
	 */
	List<String> getLinksList()
	{
		return linksList;
	}

}
