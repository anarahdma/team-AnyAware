import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.script.*;

public class notifier {
	
	Hashtable<String,String> cpcNbrhoodTable;
	static final int NHBRHOOD_COUNT = 124;
	csvTest notifyAlgo;
	
	public static void main(String[] args) { new notifier();}
	
	notifier(){
		cpcNbrhoodTable = new Hashtable<String,String>(NHBRHOOD_COUNT*2);//hoodCode | hoodeName
		notifyAlgo = new csvTest();
		run();
		}
	
	public void run(){
		buildNbrhoodTable();//build hash table
		sendNotifAlertToMap();
		}
	
	//iterates through each neighborhood and returns a true or false to notify
	public boolean sendNotifAlertToMap(){
		boolean sendNotify = false;
		Set<Entry<String, String>> set2 = cpcNbrhoodTable.entrySet();
		Iterator<Entry<String, String>> it2 = set2.iterator();
		while (it2.hasNext()) {
	    	Map.Entry<String,String> nghbrhoods = (Map.Entry) it2.next();
	    	sendNotify = notifyAlgo.notifyFor(nghbrhoods.getKey(), cpcNbrhoodTable.get(nghbrhoods.getKey()));
	    	//if(notifyFor(selector.isSelected() && nghbrhoods.getKey())){
	    		//send true to the corresponding address
	    		//}
			}
		return sendNotify;
		}
	
	private void buildNbrhoodTable(){
		String csvFile = "cpcNbrhood.csv";
		BufferedReader br = null;;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			while ((line = br.readLine()) != null){
				String[] nhbrhood = line.split(cvsSplitBy);
				cpcNbrhoodTable.put(nhbrhood[1], nhbrhood[3]);
				}
			} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			} 
		catch (IOException e) {
			e.printStackTrace();
			}
		finally {
			if (br != null) {
				try {
					br.close();
					} 
				catch (IOException e) {
					e.printStackTrace();
					}
				}
			}
		}
	}
