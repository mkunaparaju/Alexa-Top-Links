/**
 * This class takes an integer input and retrieves the list websites in ascending order of rank as listed in Alexa website 
 */

/**
 * @author mkunaparaju
 *
 */
import java.util.List;
import java.util.Scanner;

public class TopNLinks {

	/**
	 * @param args
	 */
	
	/**
	 * The main method; takes the input from stdin and prints output to stdout 
	 */
	public static void main(String[] args) {
		//takes input from command line
		System.out.println("Please enter the number of Websites you want listed:");
		Scanner scan = new Scanner(System.in);
		
		int topN = scan.nextInt();
		if(topN > 500)
		{
			System.out.println("Max number of sites shown in Alexa is 500. Please choose a number blow that");
			return;
		}
		
		AlexaCrawler getAlexaLinks = new AlexaCrawler(topN);
		getAlexaLinks.constructLinkListfromDoc();
		List<String> alexaLinks = getAlexaLinks.getLinksList();
		
		for(String link : alexaLinks)
		{
			System.out.println(link);
		}	

	}

}
