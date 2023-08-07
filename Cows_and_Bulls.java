import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.net.*;
/**
 * This class is made to play a game of cows and bulls. It needs EnglishDictionary.txt and ComEngWords.txt to be accessible to function properly.
 * @author Vvaidya
 *
 */
public class Cows_and_Bulls {
	private ArrayList<String> engdict = new ArrayList<String>();
	private ArrayList<String> common = new ArrayList<String>();
	private String word = null;
	private Scanner scan;
	private int numguesses = 0;
	private Integer limit = null;
	private String ip = "127.0.0.1";
	private int port = 3333;
	private ServerSocket server;
	private Socket client;
	private PrintWriter out;
    private BufferedReader in;
    boolean online = false;
	
	/**
	 * This constructor takes both text files and feeds them into the
	 * "engdict" and "common" ArrayLists respectively. It also initializes
	 * the scanner "scan".
	 */
	public Cows_and_Bulls() {
			scan = new Scanner(System.in);
			File f = new File("EnglishDictionary.txt");
			try {
				Scanner input = new Scanner(f);
				while(input.hasNextLine()){
					engdict.add(input.nextLine());
	        	}
	        	input.close();
	        }
	        catch (FileNotFoundException e) {
	        	System.out.println("Error finding or reading English Dictionary Text file. Make Sure it exists and is in the same directory as this file.");
			}
			//Source: https://www.ef.edu/english-resources/english-vocabulary/top-3000-words/
			File f1 = new File("ComEngWords.txt");
			try {
				Scanner input = new Scanner(f1);
				while(input.hasNextLine()){
					common.add(input.nextLine());
	        	}
	        	input.close();
	        	//System.out.println(common.toString());
	        }
	        catch (FileNotFoundException e) {
	        	System.out.println("Error finding or reading Common English words text file. Make Sure it exists and is in the same directory as this file.");
			}
	}
	
	/**
	 * This method checks if a word is valid by making sure
	 * it is a valid word in the english dictionary, and that the length of it
	 * is the same as the answer word.
	 * @param guess The guess that will be checked
	 * @return Boolean True False depending on whether guess is valid or not
	 */
	public boolean isValid(String guess) {
		
		return (guess.length() == word.length() && engdict.contains(guess));
	}
	/**
	 * This method finds a random word in the English dictionary by
	 * shuffling it and then finding the first occurrence of a word
	 * that size, and sets it to the instance variable "word". If there
	 * is no such word of that length, "word" will be set to null.
	 * @param length The length that you want the word to be.
	 */
	public void findWord(int length) {
		Collections.shuffle(engdict);
		String randword = null;
		for (int i = 1; i < engdict.size(); i++) {
			if (engdict.get(i).length() == length) {
				randword = engdict.get(i);
				break;
			}
		}
		
		word = randword;
		word = word.toLowerCase();
	}
	
	/**
	 * This method finds a random word in a set of more common words by
	 * shuffling it and then finding the first occurrence of a word
	 * that size, and sets it to the instance variable "word". If there
	 * is no such word of that length, "word" will be set to null.
	 * (Common Words Source: https://www.ef.edu/english-resources/english-vocabulary/top-3000-words/\r\n)
	 * @param length The length that you want the word to be.
	 */
	public void findWordC(int length) {
		Collections.shuffle(common);
		boolean correctsize = false;
		String randword = null;
		for (int i = 1; i < common.size() && correctsize == false; i++) {
			if (common.get(i).length() == length) {
				randword = common.get(i);
				correctsize = true;
			}
		}
		
		word = randword;
		word = word.toLowerCase();
	}
	/**
	 * Sets "word" manually. If the word isn't valid, it will be set to null.
	 * @param newword the word that "word" is being set to.
	 */
	public void manualSetWord(String newword) {
		if (engdict.contains(newword)) {
			word = newword.toLowerCase();
		}
		else {
			word = null;
		}
	}
	/**
	 * Returns the instance variable "word".
	 * @return the instance variable "word".
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Finds the number of Bulls (letters that are correct and in the right spot)
	 * and the number of Cows (letters that are correct but not in the right spot).
	 * A bull cannot be a cow.
	 * @param guess the guess of of which the number of cows and bulls is being calculated.
	 * @return The number of Bulls and Cows in an int array.
	 */
	public int[] numCowsandBulls(String guess) {
		guess = guess.toLowerCase();
		boolean[] bullindex = new boolean[guess.length()];
		boolean[] cowindex = new boolean[guess.length()];
		int[] cowsandbulls = new int[2];
		if (this.isValid(guess)) {
			for (int i = 0; i < guess.length(); i++) {
				if (word.charAt(i) == guess.charAt(i)) {
					bullindex[i] = true;
					cowsandbulls[0]++;
				}
			}
			for (int i = 0; i < guess.length(); i++) {
				if (guess.indexOf(word.charAt(i)) != -1) {
					if (bullindex[guess.indexOf(word.charAt(i))] == false && bullindex[i] == false && cowindex[guess.indexOf(word.charAt(i))] == false) {
						cowsandbulls[1]++;
						cowindex[guess.indexOf(word.charAt(i))] = true;
					}
				}
			}
		}
		return cowsandbulls;
	}
	/**
	 * Sets the number guesses allowed before you lose.
	 * @param newlimit the number guesses allowed before you lose
	 */
	public void setLimit(Integer newlimit) {
		limit = newlimit;
	}
	
	/**
	 * Sets Port
	 * @param newport the new port
	 */
	public void setPort(int newport) {
		port = newport;
	}
	
	/**
	 * Sets IP
	 * @param newip the new IP
	 */
	public void setIP(String newip) {
		ip = newip;
	}
	
	public void chooseInit() {
		//server
		String word;
		System.out.println("Server started, waiting for client");
		try {
			server = new ServerSocket(port);
			//server.setReuseAddress(true);
			client = server.accept();
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			online = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Client Connected");
		String ans;
		while (true) {
			System.out.println("Type the word");
			ans = scan.nextLine().toLowerCase();
			if (engdict.contains(ans)) {
				break;
			}
			else {
				System.out.println("That is not a valid word");
			}
		}
		out.println(ans);
		System.out.println("Type the guess limit, type a negative number to leave it unlimited.");
		out.println(Integer.parseInt(scan.nextLine()));
		try {
			ans = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(ans);
		String ans2 = "a";
		while (!ans.equals("Word was guessed correctly") && !ans.equals("Guess Limit was Exceeded. Terminating connection.")) {
			try {
				ans2 = in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!ans2.equals(ans) && !ans2.equals(null)) {
				System.out.println(ans2);
				ans = ans2;
			}
		}
		
		
	}
	
	public void guessInit() {
		//client
		System.out.println("Searching for server");
		online = true;
		try {
			client = new Socket(ip, port);
			out = new PrintWriter(client.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Connection Failed, check IP and port");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//out.println("Client Connected");
			System.out.println("Found Server");
			this.manualSetWord(in.readLine());
			Integer num = Integer.parseInt(in.readLine());
			if (num <= 0) {
				num = null;
			}
			else {
				System.out.println("Guess limit is: " + num);
				System.out.println();
			}
			//
			this.setLimit(num);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopServer() {
        try {
			in.close();
			out.close();
	        client.close();
	        server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	
	/**
	 * Plays the game
	 */
	public void play() {
		String guess = word + "a";
		String ans;
		boolean limitexceeded = false;
		while((!guess.equals(word) && !guess.equals("!") && limitexceeded == false)) {
			System.out.println("The word is " + word.length() + " letters long. (Enter ! to exit)");
			guess = scan.nextLine();
			if (online) {
				out.println("Guess was " + guess);
			}
			int[] cowsbulls = new int[2];
			if (!guess.equals("!")) {
				System.out.println("The word you entered is " + guess);
			}
			else {
				System.out.println("Press y to view the word, press anything else to continue.");
				ans = scan.nextLine();
				if (ans.toLowerCase().equals("y")) {
					System.out.println("The word was " + word);
					System.out.println();
				}
				this.stopServer();
			}
			if (this.isValid(guess)) {
				cowsbulls = this.numCowsandBulls(guess);
				System.out.println(cowsbulls[0] + " bulls");
				System.out.println(cowsbulls[1] + " cows");
				if (cowsbulls[0] == word.length()) {
					System.out.println("You got the word correct!");
				}
				if (online) {
					out.println(cowsbulls[0] + " bulls");
					out.println(cowsbulls[1] + " cows");
					if (cowsbulls[0] == word.length()) {
						out.println("Word was guessed correctly");
					}
				}
				numguesses++;
				//System.out.println(numguesses);
				//System.out.println(limit);
				if (limit != null) {
					if (numguesses >= limit) {
						System.out.println("You have exceeded the guess limit");
						if (online) {
							out.println("Guess Limit was Exceeded. Terminating connection.");
							this.stopServer();
						}
						System.out.println("Press y to view the word, press anything else to continue.");
						ans = scan.nextLine();
						if (ans.toLowerCase().equals("y")) {
							System.out.println("The word was " + word);
							System.out.println();
							limitexceeded = true;
						}
					}
				}
			}
			else if (!guess.equals("!")) {
				System.out.println("That word either isn't a valid English word or was not the correct length.");
				if (online) {
					out.println("Word either isn't a valid English word or isn't the correct length");
				}
			}
		}
	}

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		Cows_and_Bulls game = new Cows_and_Bulls();
		//game.stopServer();
		Scanner scan = new Scanner(System.in);
		String ans = "aaaaaaaaaa";
		String guess = null;
		Integer limit = null;
		String message;
		//int port =
		while (!ans.equals("!")) {
			boolean limitexceeded = false;
			int numguesses = 0;
			System.out.println("Cows and Bulls");
			System.out.println("Type 1 for singleplayer, 2 for multiplayer, 3 for lan play, l to set a guess limit, desc for info on the game, and ! to quit.");
			ans = scan.nextLine();
			ans = ans.toLowerCase();
			if (ans.equals("l")) {
				System.out.println("Type the guess limit, type a negative number to leave it unlimited.");
				limit = Integer.parseInt(scan.nextLine());
				if (limit <= 0) {
					limit = null;
				}
				game.setLimit(limit);
				System.out.println();
			}
			if (ans.equals("desc")) {
				System.out.println("Cows and Bulls is a word game similar to Wordle.");
				System.out.println("A word is picked, and you guess a word of the same length.");
				System.out.println("The game will then return the numbers of cows and bulls. ");
				System.out.print("Bulls are letters that are correct and in the right spot, ");
				System.out.println("and cows that are letter that are right but in the wrong spot.");
				System.out.println("A bull cannot also be a cow.");
				System.out.print("The main difference between Wordle and Cows and Bulls is that ");
				System.out.println("in Cows and Bulls you don't know which letter is a cow or which letter is a Bull.");
				System.out.println("Singleplayer mode picks a random word of a chosen length from a dictionary.");
				System.out.print("Multiplayer mode involves a person inputting a word as a command line argument ");
				System.out.println("and then giving it to another person for them to guess.");
				System.out.println();
			}
			if (ans.equals("2")) {
				try {
					game.manualSetWord(args[0]);
					//System.out.println(args[0]);
					guess = game.getWord() + "a";
					if (game.isValid(game.getWord()) == false) {
						throw new ArrayIndexOutOfBoundsException();
					}
					game.play();
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("There is no word in the command line arguments, or the word is not a valid English word.");
				}
			}
			if (ans.equals("1")) {
				System.out.println("How long would you like the autogenerated word to be?");
				int size = scan.nextInt();
				scan.nextLine();
				while (!ans.equals("!")){
					ans = "aaaaaa";
					boolean wordfound = false;
					System.out.println("type 1 to search for a word in the regular dictionary.");
					System.out.println("type 2 to search for one in a set of more commmon words.");
					System.out.println("type desc to learn about the difference");
					System.out.println("type ! to quit");
					ans = scan.nextLine();
					//scan.nextLine();
					if (ans.equals("1")) {
						game.findWord(size);
						wordfound = true;
						
					}
					else if (ans.equals("2")) {
						game.findWordC(size);
						wordfound = true;
						
					}
					else if (ans.equals("desc")) {
						System.out.println("Number 1 will search the regular dictionary.");
						System.out.println("This option is much harder, since there are many obscure words you will likely run into.");
						System.out.println();
						System.out.println("Number 2 will search a list of more common words that will be easier to guess.");
						System.out.println("(Source: https://www.ef.edu/english-resources/english-vocabulary/top-3000-words/\\r\\n)");
					}
					if (wordfound) {
						game.play();
					}
				}
			}
			if (ans.equals("3")) {
				System.out.println("Would you like to create the word or guess it? Type 1 for create, and 2 for guess");
				ans = scan.nextLine().toLowerCase();
				if (ans.equals("1")) {
					System.out.println("Change Port? Default is and 3333 [Y/n]");
					ans = scan.nextLine().toLowerCase();
					if (ans.equals("y")) {
						System.out.println("Set Port");
						game.setPort(Integer.parseInt(scan.nextLine()));
					}
					game.chooseInit();
					//Add a while loop that keeps checking messages until the client wins or loses
					
				}
				else if (ans.equals("2")) {
					System.out.println("Change IP and port? Defaults are 127.0.0.1 (localhost) and 3333 [Y/n]");
					ans = scan.nextLine().toLowerCase();
					if (ans.equals("y")) {
						System.out.println("Set IP");
						game.setIP(scan.nextLine());
						System.out.println("Set Port");
						game.setPort(Integer.parseInt(scan.nextLine()));
					}
					game.guessInit();
					game.play();
				}
			}
		}
		System.out.println("Thank you for playing");
		scan.close();
	}

}