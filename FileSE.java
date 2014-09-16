import java.io.*;
import java.util.*;

public class FileSE implements SEServer.SearchService {
	// hashmap to store a word and an array of lines where the word is present
	// main data structure
	HashMap<String, ArrayList<Integer>> wordLines = new HashMap<String, ArrayList<Integer>>();
	// used for the Stopwatch time measurements
	double timeSpent;

	/***********************************
	 * The constructor will read the data from the file, convert the individual
	 * lines to lowerCase(), split a line into individual words (which will be
	 * the keys in the wordLines hashmap), iterate over the the array of words,
	 * creating an ArrayList of lines where a specific word is present. After
	 * this process is done the word (string) and the lineNumbers(Integer
	 * ArrayList) are put into the wordLines hashmap the words being keys and
	 * the values being the ArrayList of line numbers
	 ***********************************/
	
	public FileSE(String fileName) throws IOException {

		// line number count
		int lineNumber = 0;

		// Read in the file in a buffer
		BufferedReader r = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName), "UTF-8"));

		// timer for building the wordLines hashmap
		Stopwatch hashmapCreate = new Stopwatch();

		while (true) {
			// Read line
			String line = r.readLine();
			// No more lines - end loop
			if (line == null) {
				break;
			}
			// convert a line to lowercase
			line = line.toLowerCase();
			// split the line into individual words
			String[] words = line.split("\\W+");
			// iterate over the array of words creating an array list of the
			// lines where the word is present
			// and putting back the actual word and the array list of line
			// numbers into wordLines hashmap that takes a string (word) and an
			// arraylist of integers to follow the line numbers
			// once we are ready with a specific line we go to the next and
			// increment the lineNumber variable
			for (String word : words) {
				ArrayList<Integer> results = wordLines.get(word);
				if (!wordLines.containsKey(word)) {
					ArrayList<Integer> lineNumbers = new ArrayList<Integer>();
					lineNumbers.add(lineNumber);
					wordLines.put(word, lineNumbers);
				} else if (results.get(results.size() - 1) != lineNumber) {
					wordLines.get(word).add(lineNumber);
				}
			}
			lineNumber++;
		}

		// stop the timer and print out the time it took for indexing
		timeSpent = hashmapCreate.elapsedTime();
		System.out.println("The indexing took: " + timeSpent + " sec");
		System.out.println("The number of unique entries in the hashmap: "
				+ wordLines.size());
		System.out.println("***************************************");
	}

	// This function is the main search function
	public ArrayList<Integer> search(String queryLine) {
		System.out.println("***************************************");
		// Used for returned values
		ArrayList<Integer> lineNumbers = new ArrayList();
		// Set query to lowercase
		queryLine = queryLine.toLowerCase();
		// Split query in separate words and put them in an array
		String queryWords[] = queryLine.split("\\W+");
		// We have 2 cases! Case 1: is if the search word is only one where we
		// only
		// will have to go through the arraylist of the line numbers that
		// correspond to this word
		if (queryWords.length == 1) {
			// start measuring the single word query
			long startSingleTime = System.nanoTime();
			if (wordLines.containsKey(queryLine)) {
				lineNumbers = wordLines.get(queryLine);
			}
			// stop the timer and print out the time it took to search for the
			// word
			long stopSingleTime = System.nanoTime();
			long time = stopSingleTime - startSingleTime;
			System.out.println("Searching for this specific word \""
					+ queryLine + "\" took: " + time + " nanosecs");
			// Case 2: if the query contains more than 1 word, then we need to
			// go through each word and figure out on what lines all the words
			// are present
		} else {
			// timer for multiple words queries
			Stopwatch multipleWords = new Stopwatch();

			// go through all the query words
			if (wordLines.containsKey(queryWords[0])) {
				lineNumbers = wordLines.get(queryWords[0]);
				// the first word occures on these lines
				System.out.println("Unique lines for \"" + queryWords[0]
						+ "\": " + wordLines.get(queryWords[0]).size());
			}
			for (int i = 1; i < queryWords.length; i++) {
				if (wordLines.containsKey(queryWords[i])) {
					// We get the lines where all query words are
					// present by passing the lines where one word is present
					// and the lines where the second is present and so on
					// to the compareLines() method that will return an
					// array list of the lines where all words appear
					lineNumbers = compareLines(lineNumbers,
							wordLines.get(queryWords[i]));
				}
				//print out the lines where the i word appears
				System.out.println("Unique lines for \"" + queryWords[i]
						+ "\": " + wordLines.get(queryWords[i]).size());
			}
			// stop the timer and print out the time it took to do the search for all the words
			timeSpent = multipleWords.elapsedTime();
			System.out.println("Search time for all the query words took: "
					+ timeSpent + " secs");
		}
		//return the array list of line numbers where all query words are present
		return lineNumbers;
	}
	//compare and return the lines where all words appear
	public ArrayList<Integer> compareLines(ArrayList<Integer> arrayOne,
			ArrayList<Integer> arrayTwo) {
		ArrayList<Integer> sameLineNumbers = new ArrayList<Integer>();
		for (int i : arrayOne) {
			// uses binary search to compare the lines
			// we check if an entry in array one is present in array 2 and if
			// yes we add it to the sameLineNumbers array list
			if (findSameLines(i, arrayTwo) != -1) {
				sameLineNumbers.add(i);
			}
		}
		// returns a list of all the lines where all query words are present
		return sameLineNumbers;
	}

	// binary search compare lines
	private int findSameLines(int key, ArrayList<Integer> array) {
		int lo = 0;
		int hi = array.size() - 1;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			if (key < array.get(mid)) {
				hi = mid - 1;
			} else if (key > array.get(mid)) {
				lo = mid + 1;
			} else {
				return mid;
			}
		}
		return -1;
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Please give a single file name as argument.");
			System.exit(2);
		}
		String fileName = args[0];

		FileSE engine = new FileSE(fileName);
		SEServer server = new SEServer(8888, engine);
		server.run();
	}
}