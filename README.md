##LSDM-BloomFilter

This project implements counting 1s by Bloom Filter. It takes the size of input stream(no. of tweets in this case) and the size of array. Additionally, you can provide the keys and also define the number of keys you want to consider for counting 1s.

#Build-details
The project is build with:
java version 8

#Parameters for input stream are as follows:
1. Path to the compressed input dataset
2. Size of bucket/ size of array (n)
3. Length of stream (m)/ No. of tweets to be considered
4. Keys seperated by " "

#Running guide
You can run the jar file directly
```
java -jar BloomFilter.jar ebola.json.gz 1000 8000 ebola ebolafever
```
Or you can run 
```
./run.sh ebola.json.gz 1000 8000 ebola ebolafever
```
