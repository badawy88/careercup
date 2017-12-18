package com.careercup;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import org.junit.Test;

/**
 * @author Abouads, Badawy
 *
 */
public class CheckParenthesesInFile {
	/**
	 * Arrays for Parentheses
	 */
	HashMap<Character, Character> mappedSpecialCharacters = new HashMap<>();
	Collection<Character> opens = null; // Open Parenthesis
	Set<Character> closes = null; // Close Parenthesis

	/**
	 * Initialization of Parentheses arrays
	 */

	public CheckParenthesesInFile() {
		mappedSpecialCharacters.put('}', '{');
		mappedSpecialCharacters.put(')', '(');
		mappedSpecialCharacters.put(']', '[');
		closes = mappedSpecialCharacters.keySet();
		opens = mappedSpecialCharacters.values();

	}

	public boolean check(String filePath) throws IOException {
		File file = new File(filePath);
		RandomAccessFile current = new RandomAccessFile(file, "r");
		// System.out.println(file.);
		File tempFile = new File(file.getParent() + "//output.temp");
		tempFile.deleteOnExit();
		RandomAccessFile temp = new RandomAccessFile(tempFile, "rw");

		long mainFilePointer = 0;
		long tempFilePointer = 0;
		boolean isValid = true;
		while ((mainFilePointer = current.read()) != -1) {
			char currentCharacter = (char) mainFilePointer;
			if (opens.contains(currentCharacter)) {
				temp.seek(tempFilePointer);
				temp.write(currentCharacter);
				tempFilePointer++;
			} else if (closes.contains(currentCharacter)) {
				tempFilePointer--;
				if (tempFilePointer >= 0) {
					temp.seek(tempFilePointer);
					char lastChar = (char) temp.read();
					if (mappedSpecialCharacters.get(currentCharacter) == lastChar) {
						temp.seek(tempFilePointer);
						temp.write(' ');
					} else {
						isValid = false;
						break;
					}
				} else {
					isValid = false;
					break;
				}

			}
		}
		current.close();
		return isThereParenthesesInFile(temp) && isValid;
	}

	/**
	 * Check if the file has any open Parentheses or any close Parentheses
	 * 
	 * @param tempFile
	 * @return
	 * @throws IOException
	 */
	private boolean isThereParenthesesInFile(final RandomAccessFile file) throws IOException {
		file.seek(0);
		int read = 0;
		boolean isValid = true;
		while ((read = file.read()) != -1) {
			char ch = (char) read;
			if (opens.contains(ch) || closes.contains(ch)) {
				isValid = false;
				break;
			}
		}
		file.close();
		return isValid;
	}

	@Test
	public void testParenthesis() {
		try {
			// check valid files
			assertTrue(check("validateFiles/correctLargefile1.txt")); // file contains [{()()}]
			assertTrue(check("validateFiles//correctLargefile2.txt")); // file contains [{(32)(44)565}23232][]{}()
			assertTrue(check("validateFiles//correctLargefile3.txt")); // file contains this java class inside it, with valid comments in testParenthesis method
			assertTrue(check("validateFiles//correctLargefile4.txt")); // file contains (){}[]{{{}}}[()[]]
			
			// invalid files
			assertFalse(check("validateFiles//incorrectLargefile1.txt"));// file contains [{()()}[
			assertFalse(check("validateFiles//incorrectLargefile2.txt"));// file contains [{()()}][{(}()}]
			assertFalse(check("validateFiles//incorrectLargefile3.txt")); // file contains this java class inside it, with invalid comments in testParenthesis method
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public static void main(String args[]) {
		
	}
}