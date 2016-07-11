package lsdm.BloomFilter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class BloomFilterLSDM{
	static String inputFile="";
	
	static int noOfTags=0;
	static boolean status = true;
	
	public static void main(String args[]){
		long start = System.currentTimeMillis();
		
		inputFile=args[0];
		GZIPInputStream file=null;
	try {
		file = new GZIPInputStream(new FileInputStream(inputFile));
	} catch (FileNotFoundException e) {
		
		e.printStackTrace();
		System.out.println("Error in input File");
	} catch (IOException e) {
		
		e.printStackTrace();
		System.out.println("Error in input file");
	}
	Reader rd = null;
	try {
		rd = new InputStreamReader(file, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch (NullPointerException e){
		e.printStackTrace();
	}
	BufferedReader br = new BufferedReader(rd);
	String tweet=null;
	int counter = Integer.parseInt(args[2]);
	ArrayList<String> hashtags = new ArrayList<String>();
	
	try {
		while((tweet=br.readLine())!=null && status){
			
			hashtags.addAll(ManageTweet(tweet, counter, hashtags.size()));
			
		}
	} catch (IOException e1) {
		
		e1.printStackTrace();
	}
	int noOfHashFunc = (int) Math.ceil(((Integer.parseInt(args[2])/Integer.parseInt(args[1]))*0.69314718056));
	int noOfKeys = args.length-3;
	ArrayList<String> keys = new ArrayList<String>();
	for(int i=0; i<noOfKeys; i++){
		keys.add(args[i+3].toLowerCase());
	}
	Hashing(hashtags, noOfHashFunc, noOfKeys, keys, Integer.parseInt(args[1]));
	long end = System.currentTimeMillis();

	System.out.println("Took : " + ((end - start) / 1000)+" seconds");
	
	
	
}
	private static void Hashing(ArrayList<String> hashtags, int noOfHashFunc, int noOfKeys, ArrayList<String> keys, int bitArraySize) {
		//Hash Keys and set the bit Array
		Hashing h = new Hashing();
		int count=0;
		h.defineHashFunc(noOfHashFunc, bitArraySize );
		HashMap<String, int[]> keysMap = new HashMap<String, int[]>();
		for(int i=0; i<noOfKeys; i++){
			keysMap.putAll(h.hashKeys(keys.get(i), keys));
		}
		for(int i=0; i<hashtags.size(); i++){
			int[] wordHash = h.hashString(hashtags.get(i));
			int test = h.CheckIfExist(wordHash, keysMap);
			if(test==1){
				count++;
				//System.out.print("1");
			}
			//else
				//System.out.print("0");
		}
		System.out.println("\r\b\bTotal no. of 1s = "+count);
		double false_pos=CalculateFP(noOfHashFunc,hashtags.size(), bitArraySize);
		System.out.println("False positive rate= "+(false_pos));
		
		
	}
	private static double CalculateFP(int noOfHashFunc, int size, int bitArraySize) {
		double power = (double) (noOfHashFunc * bitArraySize)/size;
		double fp = 1-(1/Math.exp(power));
		return Math.pow(fp, noOfHashFunc);
	}
	private static ArrayList<String> ManageTweet(String tweet, int counter, int size) {
		ArrayList<String> hashArray = new ArrayList<String>();
		JSONObject obj = null;
		try {
			obj = new JSONObject(tweet);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject entities = new JSONObject();
		if(obj.has("entities")){
		try {
			 entities = (JSONObject) obj.get("entities");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		JSONArray hashtags = new JSONArray();
		if(entities.has("hashtags"))
		try {
			hashtags = entities.getJSONArray("hashtags");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0; i<hashtags.length(); i++){
			JSONObject tag = new JSONObject();
			try {
				tag = hashtags.getJSONObject(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(tag.has("text")){
			try {
				//System.out.println(tag.getString("text"));
				hashArray.add(tag.getString("text"));
				if((size+hashArray.size())==(counter))
					status=false;
				noOfTags++;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		return hashArray;
		
		
		
	}
	
}