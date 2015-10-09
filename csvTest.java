import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class csvTest {
	//Key | Value
	Hashtable<String,Integer> crimeCountTable;//crimeType | number of occurrences
	Hashtable<String,Integer> maxTable;//crimeType | most recent day
	Hashtable<String,Integer> minTable;//crimeType | oldest day
	Hashtable<String,Boolean> beenThruLoopTable;//crimeType | been through loop(T/F)
	Hashtable<String,Boolean> notifyTable;//crimeType | notify(T/F)
	Hashtable<String,Double> thresholdTable;//crimeType | threshold
	selection selector;
	int NHBRHOOD_COUNT = 124;
	int CRIME_TYPE_COUNT = 14;
	int INCIDENT_COUNT = 100000;

	csvTest(){
		crimeCountTable= new Hashtable<String,Integer>(CRIME_TYPE_COUNT*2);
		maxTable = new Hashtable<String,Integer>(CRIME_TYPE_COUNT*2);
		minTable = new Hashtable<String,Integer>(CRIME_TYPE_COUNT*2);
		beenThruLoopTable = new Hashtable<String,Boolean>(CRIME_TYPE_COUNT*2);
		notifyTable = new Hashtable<String,Boolean>(CRIME_TYPE_COUNT*2);
		thresholdTable = new Hashtable<String,Double>(CRIME_TYPE_COUNT*2);
		selector = new selection();
		buildCrimeCountTable();
		buildThresholdTable();
		buildMinMaxTables();
		}
	
	private void buildCrimeCountTable(){
		String csvCrimeTypeFile = "crimeTypes.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(csvCrimeTypeFile));
			while((line = br.readLine()) != null){
				String[] crimeTypeArray = line.split(cvsSplitBy);
				crimeCountTable.put(crimeTypeArray[0],0);
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
	
	private void buildThresholdTable(){
		//size 0-13
		double[] thresholds = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		String csvCrimeTypeFile = "crimeTypes.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int i =0;
		try {
			br = new BufferedReader(new FileReader(csvCrimeTypeFile));
			while((line = br.readLine()) != null){
				String[] crimeTypeArray = line.split(cvsSplitBy);
				thresholdTable.put(crimeTypeArray[0],thresholds[i]);
				i++;
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
	
 	private void buildMinMaxTables(){
		String csvCrimeTypeFile = "crimeTypes.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(csvCrimeTypeFile));
			while((line = br.readLine()) != null){
				String[] crimeType = line.split(cvsSplitBy);
				minTable.put(crimeType[0], 0);
				maxTable.put(crimeType[0], 0);
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
	
	public boolean notifyFor(String givenHoodCode, String hoodName){
		
		String csvFile = "incidents-100k.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int numberOfCrimes= 0;
		int PERCENTAGE = 100;
		boolean isFirstLoop = true;
		double timeSpan= 0;
		double threshold = 0.0;
		double crimeRate = 0;
		boolean[] beenThruLoopList = new boolean[14];
		boolean notify = false;
		
		try {
			br = new BufferedReader(new FileReader(csvFile));
			//loop through each entry in csv file, match with 
			//corresponding nieghborhood and count number of crimes
			line = br.readLine();//skip the first line
			//start adding values to the crime count table
			while ((line = br.readLine()) != null) {
				String[] crime = line.split(cvsSplitBy);
				String hoodCode = crime[14].replace("\"", "");//get the nieghborhood code
		    	String crimeType = crime[10].replace("\"", "");//get the crime type
				int day = Integer.parseInt(crime[4].replace("\"", ""));// get the day
		    	
		    	if(hoodCode.equals(givenHoodCode)){//compare whether it's the nieghborhood
		    		if(crimeCountTable.containsKey(crimeType)){//test whether table contains the crime type
		    			crimeCountTable.put(crimeType, crimeCountTable.get(crimeType)+1);//increment the specific crime category by one
		    			if(beenThruLoopTable.get(crimeType) == null){//initialize the min and max
		    				maxTable.put(crimeType, day);
		    				minTable.put(crimeType, day);
		    				beenThruLoopTable.put(crimeType, true);
		    				}
		    			//update min and max
		    			if(day > maxTable.get(crimeType))
		    				maxTable.put(crimeType, day);
		    			if(day < minTable.get(crimeType))
		    				minTable.put(crimeType, day);
		    			}
		    		}
		    	}
			//loop through crime count table and calculate the crime density
			Set<Entry<String, Integer>> crimeCountSet = crimeCountTable.entrySet();
			Iterator<Entry<String, Integer>> iter = crimeCountSet.iterator();
					Set set = crimeCountTable.entrySet();
	    Iterator crimeCountIter = set.iterator();
	    //str.replace("X", "");
	    System.out.println("---------------------------------");
	    System.out.println(givenHoodCode + " : " + hoodName);
	    System.out.println("---------------------------------");
	    
	    while (iter.hasNext() && crimeCountIter.hasNext()) {
				Map.Entry crimeCountEntry = (Map.Entry) iter.next();
				timeSpan = maxTable.get(crimeCountEntry.getKey()) - minTable.get(crimeCountEntry.getKey());
				numberOfCrimes = (Integer) crimeCountEntry.getValue();
				crimeRate = (numberOfCrimes/timeSpan) * PERCENTAGE;
				if(crimeRate > thresholdTable.get(crimeCountEntry.getKey())){
					notifyTable.put((String) crimeCountEntry.getKey(), true);
					notify = true;
					}
				
				Map.Entry entry = (Map.Entry) crimeCountIter.next();
				//System.out.println(entry.getKey() + " : " + entry.getValue() + ", time span= " + timeSpan + " days");
				System.out.format("%-25s : %-10d , time span= %-5.0f days , crime rate(crimes/time)= %-10.1f , notify= %b\n" 
						,entry.getKey(), entry.getValue(), timeSpan, crimeRate, notifyTable.get(entry.getKey()));
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
		
	    buildCrimeCountTable();
		System.out.println("Done\n");
		
		return notify;
	}

}