package lsdm.BloomFilter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class Hashing{
	int noOfHashFunct = 0;
	int bitArraySize=0;
	ArrayList<Integer> a = new ArrayList<Integer>();
	ArrayList<Integer> b = new ArrayList<Integer>();
	public void defineHashFunc(int noOfHashFunc, int i) {
		this.noOfHashFunct = noOfHashFunc;
		this.bitArraySize = i;
		
		hashFunc(noOfHashFunc, bitArraySize);
		}
	private void hashFunc(int noOfHashFunc, int size) {
		//hash functions of type ax+bmodc; a and b should be odd and c=size of bit array
		//convert string to key
		
		for(int i=0; i<noOfHashFunc; i++){
			Random rand = new Random();
			int numA = create(rand);
			numA=checkContains(true, numA, a);
			a.add(numA);
			int numB = create(rand);
			numB=checkContains(true, numA, a);
			b.add(numB);
			
		}
	
		
	}
	private int checkContains(boolean contains, int num, ArrayList<Integer> a2) {
		while(contains==true){
			boolean check=false;
			for(int s: a2){
				if(s==num){
					check=true;
				}
				
			}
			if(check==false){
				contains=false;
			}
				else{
					Random rand = new Random();
					num=create(rand);
				}
			
		}
		return num;
	}
	private int create(Random rand) {
		int numA = rand.nextInt();
		if(numA%2==0){
			numA++;
		}
		return numA;
		
	}
	public HashMap<String, int[]> hashKeys(String key, ArrayList<String> keys) {
		
		
		HashMap<String, int[]> KeyArray = new HashMap<String, int[]>();
		
			long kInt=converttoInt(key);
			//int i = keys.indexOf(key);
			
			int[] bitArray=new int[bitArraySize];
		for(int i=0; i<a.size(); i++){
			int position = Math.abs((int) (((a.get(i)*key.toLowerCase().hashCode())+b.get(i))%bitArraySize));
			
			bitArray[position]=1;
		}
			KeyArray.put(key, bitArray);
		
		return KeyArray;
		
	}
	private long converttoInt(String key) {
		key=key.toLowerCase();
		byte[] keyBytes = key.getBytes();
		String binary="";
		for(byte b : keyBytes){
			binary= binary+Integer.toBinaryString(b);
		}
		long c = new BigInteger(binary, 2).longValue();
		return c;
	}
	public int[] hashString(String word) {
		long hInt = converttoInt(word.toLowerCase());
		int[] h = new int[bitArraySize];
		for(int i =0; i<a.size(); i++){
			int position = Math.abs((int) (((a.get(i)*word.toLowerCase().hashCode())+b.get(i))%bitArraySize));
			h[position]=1;
		}
		return h;
		
	}
	public int CheckIfExist(int[] wordHash, HashMap<String, int[]> keysMap) {
		int test = 1;
		int count=0;
		
		for(String key : keysMap.keySet()){
			if(test==0)
				test=1;
			int[] hashKey = keysMap.get(key);
			
			for(int i = 0; i<bitArraySize; i++){
				if(hashKey[i]!=wordHash[i] || wordHash[i]!=hashKey[i]){
					
						test=0;
						break;
					}
				
			}
			if(test!=0){
				count++;
			}
			//return test;
			
		}
		return count;
		
	}
	
	

}