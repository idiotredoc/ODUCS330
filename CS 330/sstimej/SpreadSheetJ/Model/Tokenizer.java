package SpreadSheetJ.Model;

import java.util.Vector;

//Tokenizer provides a mechanism for dividing a string into "Tokens",
//units that we want to treat as single elements during parsing or 
//structured inputs. This Tokenizer (a.k.a., "scanner" or "lexical 
//analyzer") is oriented towards recognizing
//1) numbers
//2) string literals
//3) reserved words
//4) non-reserved words
//5) comments
//6) operators (any non-whitespace character not contributing to one
//of the above token categories
//The Tokenizer indicates both the kind of token found and the "lexeme", the
//precise string of characters contributing to that token.

//Most of the operations of the Tokenizer class are for tweaking the 
//character set that can appear in words, operators, whitespace, etc.
//and for predefining a set of reserved words.

class Tokenizer {

	// CharacterTypes
	static public final int Whitespace = 0;
	static public final int WordChars = 1;
	static public final int Quoting = 2;
	static public final int Escape = 3;

	// TokenTypes
	static public final int EndOfInput = 0;
	static public final int IntegerConstant = 1;
	static public final int TimeConstant = 2;
	static public final int RealConstant = 3;
	static public final int Word = 4;
	static public final int Operator = 5;
	static public final int Reserved = 6;
	static public final int StringLiteral = 7;
	static public final int InvalidToken = 8;


	Tokenizer (String str)
	{
		buffer = str;
		caseSensitive = true;
		lexeme = new String();
		reservedWordIndex = -1;
		unsignedNumbers = true;
		characterCodes = new boolean[Escape+1][];
		reservedWords = new Vector();
		commentStarters = new Vector();
		commentEnders = new Vector();

		setDefaultCodes();
	}


	/*************  Setup  **********************/

	// Assign a character or a range of consecutive characters to a
	//  character type.
	void setCharType (char c, int characterType)
	{
		characterCodes[characterType][c] = true;
	}

	void setCharType (char cstart, char cstop, int characterType)
	{
		for (char c = cstart; c <= cstop; ++c)
			characterCodes[characterType][c] = true;
	}


	// Remove a character or a range of consecutive characters from a
	//  character type.
	void clearCharType (int characterType)
	{
		for (int i = 0; i < 256; ++i)
			characterCodes[characterType][i] = false;
	}

	void clearCharType (char c, int characterType)
	{
		characterCodes[characterType][c] = false;
	}

	void clearCharType (char cstart, char cstop, int characterType)
	{
		for (char c = cstart; c <= cstop; ++c)
			characterCodes[characterType][c] = false;
	}

	// Are reserved words case sensitive?
	void setCaseSensitive(boolean sensitive)
	{
		caseSensitive = sensitive;
	}

	// Can numbers have + - signs in front?
	void setNumbersAreUnsigned (boolean noSigns)
	{
		unsignedNumbers = noSigns;
	}


	// Declare a string as a reserved word. The return value is a unique
	// reserved word "ID" that you can use to identify the word if it is
	// found in the input.
	int setReserved (String reserved)
	{
		reservedWords.add(reserved);
		return reservedWords.size();
	}


	// Declare a pair of strings that begin and end comments.
	void setComment (String commentStart, String commentStop)
	{
		commentStarters.add(commentStart);
		commentEnders.add(commentStop);
	}




	/*************  Input Processing  **********************/

	// Get a token, discarding all info about the previous token (if any).
	int getNextToken()
	{
		tokenType = InvalidToken;
		lexeme = "";

		// Discard whitespace and comments
		boolean discarding = true;
		while (buffer.length() > 0 && discarding) {
			discarding = false;
			// skip initial white space
			int i = 0;
			while (i < buffer.length()
					&& characterCodes[Whitespace][buffer.charAt(i)]) {
				discarding = true;
				++i;
			}
			buffer = buffer.substring(i);


			// Check for comments
			i = 0;
			while (i < commentStarters.size()
					&& !matchPrefix((String)commentStarters.elementAt(i))) {
				++i;
			}
			if (i < commentStarters.size()) {
				// We found the start of a comment.
				// Discard everythin till its end
				String cstarter = (String)commentStarters.elementAt(i);
				discarding = true;
				buffer = buffer.substring(cstarter.length());
				String cender = (String)commentEnders.elementAt(i);
				while (buffer.length() > 0
						&& !matchPrefix(cender)) {
					buffer = buffer.substring(1);
				}
				if (buffer.length() > 0)
					buffer = buffer.substring(cender.length());
			}
		}

		// Is there anything left in the buffer?
		if (buffer.length() == 0)
			return (tokenType = EndOfInput);

		// Check for quoted strings
		if (characterCodes[Quoting][buffer.charAt(0)]) {
			char quoteChar = buffer.charAt(0);
			lexeme = "" + quoteChar;
			buffer = buffer.substring(1);
			while (buffer.length() > 0 && buffer.charAt(0) != quoteChar) {
				if (characterCodes[Escape][buffer.charAt(0)]) {
					lexeme = lexeme + buffer.charAt(1);
					buffer = buffer.substring(2);
				}
				else {
					lexeme = lexeme + buffer.charAt(0);
					buffer = buffer.substring(1);
				}
			}
			if (buffer.charAt(0) == quoteChar) {
				lexeme = lexeme + quoteChar;
				buffer = buffer.substring(1);
				tokenType = StringLiteral;
				return StringLiteral;
			}
			else
				return InvalidToken;
		}

		// Check for Numbers
		if (((!unsignedNumbers) &&
				(buffer.charAt(0) == '+' || buffer.charAt(0) == '-'))
				|| (buffer.charAt(0) >= '0' && buffer.charAt(0) <= '9')) {
			int matchedLength = 0;
			if ((!unsignedNumbers)
					&& (buffer.charAt(0) == '+' || buffer.charAt(0) == '-'))
				matchedLength = 1;
			tokenType = InvalidToken;
			while (matchedLength < buffer.length() &&
					buffer.charAt(matchedLength) >= '0'
						&& buffer.charAt(matchedLength) <= '9') {
				tokenType = IntegerConstant;
				++matchedLength;
			}
			int lastAcceptedLength = matchedLength;
			if (matchedLength < buffer.length() &&
					(buffer.charAt(matchedLength) == '.' ||
							buffer.charAt(matchedLength) == 'E' ||
							buffer.charAt(matchedLength) == 'e')) {
				if (buffer.charAt(matchedLength) == '.') {
					++matchedLength;
					tokenType = RealConstant;
					while (matchedLength < buffer.length() &&
							buffer.charAt(matchedLength) >= '0'
								&& buffer.charAt(matchedLength) <= '9') {
						++matchedLength;
					}
					lastAcceptedLength = matchedLength;
				}
				if (matchedLength < buffer.length() &&
						(buffer.charAt(matchedLength) == 'E' ||
								buffer.charAt(matchedLength) == 'e')) {
					++matchedLength;
					if (buffer.charAt(matchedLength) == '+' ||
							buffer.charAt(matchedLength) == '-') {
						++matchedLength;
					}
					while (matchedLength < buffer.length() &&
							buffer.charAt(matchedLength) >= '0'
								&& buffer.charAt(matchedLength) <= '9') {
						++matchedLength;
						tokenType = RealConstant;
						lastAcceptedLength = matchedLength;
					}
				}
			} else if (matchedLength < buffer.length() &&
					buffer.charAt(matchedLength) == ':') {
				int parts = 2;
				++matchedLength;
				while (matchedLength < buffer.length() &&
						((buffer.charAt(matchedLength) >= '0'
							&& buffer.charAt(matchedLength) <= '9') 
							|| buffer.charAt(matchedLength) == ':')) {
					if (buffer.charAt(matchedLength) == ':')
						++parts;
					++matchedLength;
				}
				if (parts < 5 && buffer.charAt(matchedLength-1) >= 0 && buffer.charAt(matchedLength-1) <= '9') {
					tokenType = TimeConstant;
					lastAcceptedLength = matchedLength;
				}
			}
			if (tokenType != InvalidToken) {
				lexeme = buffer.substring(0, lastAcceptedLength);
				buffer = buffer.substring(lastAcceptedLength);
				return tokenType;
			}
		}

		// Check for reserved words
		reservedWordIndex = 0;
		while (reservedWordIndex < reservedWords.size()
				&& !matchPrefix((String)reservedWords.elementAt(reservedWordIndex))) {
			++reservedWordIndex;
		}
		if (reservedWordIndex < reservedWords.size()) {
			tokenType = Reserved;
		}

		// Check for Words
		{
			int matchedLength = 0;
			while (matchedLength < buffer.length() &&
					characterCodes[WordChars][buffer.charAt(matchedLength)]) {
				++matchedLength;
			}
			if (tokenType == Reserved) {
				String rword = (String)reservedWords.elementAt(reservedWordIndex);
				if (matchedLength > rword.length()) {
					tokenType = Word;
					lexeme = buffer.substring(0, matchedLength);
					buffer = buffer.substring(matchedLength);
					return tokenType;
				}
				else {
					lexeme = buffer.substring (0, rword.length());
					buffer = buffer.substring (rword.length());
					return tokenType;
				}
			}
			else if (matchedLength > 0)  {
				tokenType = Word;
				lexeme = buffer.substring(0, matchedLength);
				buffer = buffer.substring(matchedLength);
				return tokenType;
			}
		}


		// Anything else is an operator
		lexeme = buffer.substring(0, 1);
		buffer = buffer.substring(1);
		tokenType = Operator;
		return tokenType;
	}

	// What is the type of the current token?
	public int getTokenType()
	{
		return tokenType;
	}

	// What is the lexeme of the current token?
	public String     getLexeme()
	{
		return lexeme;
	}


	// If the current token is an IntegerConstant, what is its value?
	public int        getIntValue()
	{
		try {
			Integer i = new Integer(lexeme);
			return i.intValue();
		} catch (NumberFormatException e) {return 0;}
	}


	// If the current token is a RealConstant, what is its value?
	public double     getRealValue()
	{
		try {
			Double d = new Double(lexeme);
			return d.doubleValue();
		} catch (NumberFormatException e) {return 0.0;}
	}

	// If the current token is a Reserved word, what is its reserved word ID?
	public int        getReservedWord()
	{
		return reservedWordIndex+1;
	}


	//////////////////////

	private String buffer;
	private int tokenType;
	private String lexeme;

	private int reservedWordIndex;

	private boolean caseSensitive;
	private boolean unsignedNumbers;

	boolean[][] characterCodes;
	Vector reservedWords;
	Vector commentStarters;
	Vector commentEnders;

	private void setDefaultCodes()
	{
		for (int i = 0; i <= Escape; ++i) {
			characterCodes[i] = new boolean[256];
			clearCharType (i);
		}

		// Default Whitespace: all characters <= ' '
		for (char c = 1; c <= ' '; ++c)
			characterCodes[Whitespace][c] = true;

		// Default WordChars: A-Z, a-z, 0-9
		for (char c = 'A'; c <= 'Z'; ++c)
			characterCodes[WordChars][c] = true;
		for (char c = 'a'; c <= 'z'; ++c)
			characterCodes[WordChars][c] = true;
		for (char c = '0'; c <= '9'; ++c)
			characterCodes[WordChars][c] = true;

		// Default EscapeChars: \  
		characterCodes[Escape]['\\'] = true; 
	}



	boolean matchPrefix(String str)
	// Compare str agaisnt the start of the buffer
	{
		for (int i = 0; i < str.length(); ++i) {
			if (buffer.charAt(i) != str.charAt(i)) {
				if (!caseSensitive) {
					char bc = buffer.charAt(i);
					char sc = str.charAt(i);
					if (bc >= 'A' && bc <= 'Z')
						bc += ('a' - 'A');
					if (sc >= 'A' && sc <= 'Z')
						sc += ('a' - 'A');
					if (bc != sc)
						return false;
				}
				else
					return false;
			}
		}
		return true;
	}

}
