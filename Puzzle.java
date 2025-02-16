package il.ac.tau.cs.sw1.ex4;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class WordPuzzle {
	public static final char HIDDEN_CHAR = '_';

	/*
	 * @pre: template is legal for word
	 */
	public static char[] createPuzzleFromTemplate(String word, boolean[] template) { // Q - 1
		char[] riddle = new char[word.length()];
		for (int i = 0; i < word.length(); i++) {
			if (template[i]) {
				riddle[i] = HIDDEN_CHAR;
			} else {
				riddle[i] = word.charAt(i);
			}
		}
		return riddle;
	}

	public static boolean checkLegalTemplate(String word, boolean[] template) { // Q - 2
		if (word.length() != template.length) {
			return false;
		}
		boolean[] alltrue = new boolean[template.length];
		boolean[] allfalse = new boolean[template.length];
		Arrays.fill(alltrue, true);
		if ((Arrays.equals(template, alltrue)) || (Arrays.equals(template, allfalse))) {
			return false;
		}
		int counter = 1;
		char[] wordArray = word.toCharArray();
		for (int i = 0; i < wordArray.length - 1; i++) {
			int firstIndex = i;
			int[] wordIndex = new int[word.length()];
			Arrays.fill(wordIndex, -1);
			for (int j = i + 1; j < wordArray.length; j++) {
				if (wordArray[i] == wordArray[j]) {
					wordIndex[i] = i;
					wordIndex[j] = j;
					counter++;
				}
			}
			if (counter == word.length()) {
				return false;
			}
			Boolean CurrBool = template[firstIndex];
			for (int t = 0; t < wordIndex.length; t++) {
				if (wordIndex[t] == -1) {
					continue;
				} else {
					boolean isEqual = (CurrBool == template[t]);
					if (!isEqual) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * @pre: 0 < k < word.lenght(), word.length() <= 10
	 */
	public static boolean[][] getAllLegalTemplates(String word, int k) {  // Q - 3
		double size = Math.pow(2, word.length());
		boolean[][] allPossibleTemp = new boolean[(int) size][word.length()];
		int count = 0;
		for (int i = 0; i < size; i++) {
			String binaryString =String.format("%" + word.length() + "s", Integer.toBinaryString(i)).replaceAll(" ", "0");
			int oneBit = Integer.bitCount(i);
			if (oneBit == k) {
				boolean[] convertedArray = convertFromStringToBooleanArray(binaryString);
				boolean checkOneTemp = checkLegalTemplate(word, convertedArray);
				if (checkOneTemp) {
					allPossibleTemp[count] = convertedArray;
					count++;
					}
				}
			}
		boolean[][] legalTemp = Arrays.copyOf(allPossibleTemp, count);
		return legalTemp;
	}

	protected static boolean[] convertFromStringToBooleanArray(String binaryWord) {
		char[] charArray = binaryWord.toCharArray();
		boolean[] boolArray = new boolean[binaryWord.length()];
		for (int i = 0; i < binaryWord.length(); i++) {
			if (charArray[i] == '1') {
				boolArray[i] = true;
			}
		}
		return boolArray;
	}

	//
//
//	/*
//	 * @pre: puzzle is a legal puzzle constructed from word, guess is in [a...z]
//	 */
	public static int applyGuess(char guess, String word, char[] puzzle) { // Q - 4
		int counter = 0;
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess) {
				if (puzzle[i] == HIDDEN_CHAR) {
					counter += 1;
					puzzle[i] = guess;
				}
			}
		}
		return counter;
	}

	//
//
//	/*
//	 * @pre: puzzle is a legal puzzle constructed from word
//	 * @pre: puzzle contains at least one hidden character.
//	 * @pre: there are at least 2 letters that don't appear in word, and the user didn't guess
//	 */
	public static char[] getHint(String word, char[] puzzle, boolean[] already_guessed) { // Q - 5
		char rightchar = ' ';
		char wrongchar = ' ';
		char[] wordArray = new char[2];
		Random rad = new Random();
		while (rightchar == ' ' || wrongchar == ' ') {
			int index = rad.nextInt(26);
			if (!already_guessed[index]) {
				char currchar = (char) ('a' + index);
				int indexInWord = word.indexOf(currchar);
				if (indexInWord == -1) {
					wrongchar = currchar;
					wordArray[0] = wrongchar;
				} else {
					if (puzzle[indexInWord] == HIDDEN_CHAR)
					rightchar = currchar;
					wordArray[1] = rightchar;
				}
			}
		}
		Arrays.sort(wordArray);
		return wordArray;
	}
	protected static boolean[] convertFromStringtoBooleanArray (String [] temp) {
		boolean [] boolArray = new boolean[temp.length];
		for (int i =0; i< temp.length; i++){
			if (temp[i].equals("_")) {
				boolArray[i] = true;
			}
		}
		return boolArray;
	}

	public static char[] mainTemplateSettings(String word, Scanner inputScanner) { // Q - 6
		printSettingsMessage();
		boolean Flag = true;
		char [] puzzle = new char[1];
		search :
			while (Flag) {
				printSelectTemplate();
				int num = inputScanner.nextInt();
				if (num == 1) {
					printSelectNumberOfHiddenChars();
					int currK = inputScanner.nextInt();
					boolean[][] Alltemp = getAllLegalTemplates(word, currK);
					if (Alltemp.length <= 0) {
						printWrongTemplateParameters();
						continue search;
					}
					int RandomIndex = new Random().nextInt(Alltemp.length);
					boolean[] choosenTemp = Alltemp[RandomIndex];
					Flag= false;
					return createPuzzleFromTemplate(word,choosenTemp );

				}
				if (num == 2) {
					printEnterPuzzleTemplate();
					String [] currTemp = inputScanner.next().split(",");
					System.out.println(currTemp);
					boolean[] BoolTemp = convertFromStringtoBooleanArray(currTemp);
					if (!checkLegalTemplate(word, BoolTemp)) {
						printWrongTemplateParameters();
						continue search;
					}
					return createPuzzleFromTemplate(word, BoolTemp );
				}
				}
			return null;
	}
	public static boolean contains (char [] puzzle) {
		for (int element : puzzle) {
			if (element == HIDDEN_CHAR) {
				return false;
			}
		}
		return true;
	}
	public static int NumOfhiddenChar (char [] puzzle) {
		int counter = 0;
		for (int element : puzzle) {
			if (element == HIDDEN_CHAR) {
				counter++;
			}
		}
		return counter;
	}
	public static void mainGame(String word, char[] puzzle, Scanner inputScanner) { // Q - 7
		printGameStageMessage();
		printEnterYourGuessMessage();
		int NumofHints = NumOfhiddenChar(puzzle) + 3;
		boolean[] already_guessed = new boolean[26];
		while (NumofHints > 0) {
			char input = inputScanner.next().charAt(0);
			printPuzzle(puzzle);
			if (input == 'H') {
				char[] hints = getHint(word, puzzle, already_guessed);
				int Firstindex = hints[0]-'a';
				int Secondindex = hints[1]-'a';
				already_guessed[Firstindex] = true;
				already_guessed[Secondindex]= true;
				printHint(hints);
			}
			else {
				int updatePuzzle = applyGuess(input, word,puzzle);
				if (updatePuzzle > 0){
					if (contains(puzzle)){
						printWinMessage();
						break;
					}
					else {
						printCorrectGuess(--NumofHints);
					}
				}
				else{
					printWrongGuess(--NumofHints);
				}
				}
			}
			printGameOver();
		}


/*************************************************************/
/********************* Don't change this ********************/
	/*************************************************************/

	public static void main(String[] args) throws Exception {
		if (args.length < 1){
			throw new Exception("You must specify one argument to this program");
		}
		String wordForPuzzle = args[0].toLowerCase();
		if (wordForPuzzle.length() > 10){
			throw new Exception("The word should not contain more than 10 characters");
		}
		Scanner inputScanner = new Scanner(System.in);
		char[] puzzle = mainTemplateSettings(wordForPuzzle, inputScanner);
		mainGame(wordForPuzzle, puzzle, inputScanner);
		inputScanner.close();
	}


	public static void printSettingsMessage() {
		System.out.println("--- Settings stage ---");
	}

	public static void printEnterWord() {
		System.out.println("Enter word:");
	}

	public static void printSelectNumberOfHiddenChars(){
		System.out.println("Enter number of hidden characters:");
	}
	public static void printSelectTemplate() {
		System.out.println("Choose a (1) random or (2) manual template:");
	}

	public static void printWrongTemplateParameters() {
		System.out.println("Cannot generate puzzle, try again.");
	}

	public static void printEnterPuzzleTemplate() {
		System.out.println("Enter your puzzle template:");
	}


	public static void printPuzzle(char[] puzzle) {
		System.out.println(puzzle);
	}


	public static void printGameStageMessage() {
		System.out.println("--- Game stage ---");
	}

	public static void printEnterYourGuessMessage() {
		System.out.println("Enter your guess:");
	}

	public static void printHint(char[] hist){
		System.out.println(String.format("Here's a hint for you: choose either %s or %s.", hist[0] ,hist[1]));

	}
	public static void printCorrectGuess(int attemptsNum) {
		System.out.println("Correct Guess, " + attemptsNum + " guesses left.");
	}

	public static void printWrongGuess(int attemptsNum) {
		System.out.println("Wrong Guess, " + attemptsNum + " guesses left.");
	}

	public static void printWinMessage() {
		System.out.println("Congratulations! You solved the puzzle!");
	}

	public static void printGameOver() {
		System.out.println("Game over!");
	}

}
