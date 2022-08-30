package scanner;

import java.io.*;

/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab
 * exercise 1. The scanner will separate a passed in file into a stream tokens.
 * 
 * @author Dawson Chen
 * @version 9/9/19
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;

    /**
     * Scanner constructor for construction of a scanner that uses an InputStream
     * object for input. Usage: FileInputStream inStream = new FileInputStream(new
     * File(<file name>); Scanner lex = new Scanner(inStream);
     * 
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream) 
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }

    /**
     * Scanner constructor for constructing a scanner that scans a given input
     * string. It sets the end-of-file flag an then reads the first character of the
     * input string into the instance field currentChar. Usage: Scanner lex = new
     * Scanner(input_string);
     * 
     * @param inString the string to scan
     */
    public Scanner(String inString) 
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Method: getNextChar
     */
    private void getNextChar() 
    {
        try 
        {
            int inp = in.read();
            if (inp == -1)
                eof = true;
            else
                currentChar = (char) inp;
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Method: eat
     * 
     * @throws ScanErrorException when expected is not currentChar
     * @param expected the expected current char to be eaten
     */

    private void eat(char expected) throws ScanErrorException 
    {
        if (expected == currentChar)
            getNextChar();
        else 
        {
            throw new ScanErrorException(
                    "Illegal character - expected " + expected + " and found " + currentChar + ".");
        }
    }

    /**
     * Method: hasNext
     * 
     * @return true if not at end of file; otherwise false
     */
    public boolean hasNext() 
    {
        return !(isEnd(currentChar));
    }

    /**
     * Method: nextToken
     * @return "END" if at end of file or the next lexeme skipping all white spaces
     * @throws ScanErrorException if next token is not a valid lexeme
     */
    public String nextToken() throws ScanErrorException
    {   
        while(isWhiteSpace(currentChar))
            getNextChar();
        if(isEnd(currentChar))
            return ".";
        if(isDigit(currentChar))
            return scanNumber();
        else if(isLetter(currentChar))
            return scanIdentifier();
        else if(isOperator(currentChar))
            return scanOperator();
        else if(isSeparator(currentChar))
            return scanSeparator();
        else if(isSpecial(currentChar))
            return scanSpecial();
        else
            throw new ScanErrorException("No lexeme found!" + " CurrentChar: " + currentChar);
    }
    /**
     * Method: isSeparator
     * 
     * @param input is the char to be checked whether it is a separator
     * @return whether the char parameter is a semicolon
     */
    public static boolean isSeparator(char input) 
    {
        return input == ';' || input == ',';
    }

    /**
     * Method: isEnd
     * 
     * @param input is the char to be checked whether it is a period(end of file)
     * @return whether the char parameter is a period
     */
    public static boolean isEnd(char input) 
    {
        return input == '.';
    }

    /**
     * Method: isDigit
     * 
     * @param input is the char to be checked whether it is a digit
     * @return whether the char parameter is a digit from 0-9
     */
    public static boolean isDigit(char input) 
    {
        return '0' <= input && input <= '9';
    }

    /**
     * Method: isDigit
     * @param input is the char to be checked whether it is a digit
     * @return whether the char parameter is a digit from 0-9
     */
    public static boolean isOperator(char input) 
    {
        return (input == '=' || input == '+' || input == '-' || 
            input == '*' || input == '/' || input == '%' || 
            input == '(' || input == ')' || input == ':' ||
            input == '>' || input == '<');
    }

    /**
     * Method: isLetter
     * 
     * @param input is the char to be checked whether it is a letter
     * @return whether the char parameter is a letter [a-z A-Z]
     */
    public static boolean isLetter(char input) 
    {
        return 'a' <= input && input <= 'z' || 'A' <= input && input <= 'Z';
    }

    /**
     * Method: isWhiteSpace
     * 
     * @param input is the char to be checked whether it is a white space
     * @return whether the char parameter is a white space [' ' '\t' '\r' '\n']
     */
    public static boolean isWhiteSpace(char input) 
    {
        return input == ' ' || input == '\t' || input == '\n' || input == '\r';
    }

    /**
     * Method: isSpecial
     * 
     * @param input is the char to be checked whether it is a period
     * @return whether the char to be checked is a period
     */
    public static boolean isSpecial(char input) 
    {
        return input == '{' || input == '}' || 
            input == '<' || input == '>';
    }

    /**
     * Method: scanNumber
     * 
     * @return the digit token if it is the next token; otherwise, throw
     *         ScanErrorException
     * @throws ScanErrorException if current character is not a number
     */
    private String scanNumber() throws ScanErrorException 
    {
        String ret = "";
        while (isDigit(currentChar)) 
        {
            ret += currentChar;
            eat(currentChar);
        }
        if (ret.equals(""))
            throw new ScanErrorException("No lexeme found! Unknown lexeme: " + currentChar);
        return ret;
    }

    /**
     * Method: scanIdentifier
     * 
     * @return the identifier token if it is the next token; otherwise, throw
     *         ScanErrorException
     * @throws ScanErrorException if current character is not an Identifier
     */
    private String scanIdentifier() throws ScanErrorException 
    {
        String ret = "";
        if (isLetter(currentChar)) 
        {
            ret += currentChar;
            eat(currentChar);
        }
        while (isDigit(currentChar) || isLetter(currentChar))
        {
            ret += currentChar;
            eat(currentChar);
        }
        if (ret.equals(""))
            throw new ScanErrorException("No lexeme found! Unknown lexeme: " + currentChar);
        return ret;
    }

    /**
     * Method: scanOperator
     * 
     * @return the operand token if it is the next token; otherwise, throw
     *         ScanErrorException
     * @throws ScanErrorException if current character is not an operand
     */
    private String scanOperator() throws ScanErrorException 
    {
        String ret = "";
        if (currentChar == ')' || currentChar == '(') 
        {
            ret += currentChar;
            eat(currentChar);
        }
        else if (currentChar == '+' || currentChar == '-' || currentChar == '*' || 
                currentChar == '/' || currentChar == '%' || currentChar == ':' || 
                currentChar == '=' || currentChar == '>') 
        {
            ret += currentChar;
            eat(currentChar);
            if (currentChar == '=') 
            {
                ret += currentChar;
                eat(currentChar);
            }
        }
        else if (currentChar == '<')
        {
        	ret += currentChar;
        	eat(currentChar);
        	if(currentChar == '>' || currentChar == '=')
        	{
        		ret += currentChar;
        		eat(currentChar);
        	}
        }
        if (ret.equals("")) 
        {
            throw new ScanErrorException("No lexeme found! Unknown lexeme: " + currentChar);
        }
        return ret;
    }

    /**
     * Method: scanSeparater
     * 
     * @return the separator token if it is the next token; otherwise throw
     *         ScanErrorException
     * @throws ScanErrorException if current character is not a separator
     */
    private String scanSeparator() throws ScanErrorException 
    {
        String ret = "";
        if (currentChar == ';' || currentChar == ',') 
        {
            ret += currentChar;
            eat(currentChar);
        }
        if (ret.equals(""))
            throw new ScanErrorException("No lexeme found! Unknown lexeme: " + currentChar);
        return ret;
    }

    /**
     * Method: scanSpecial
     * 
     * @return the special token if it is the next token; otherwise throw
     *         ScanErrorException
     * @throws ScanErrorException if current character is not a special character
     */
    private String scanSpecial() throws ScanErrorException 
    {
        String ret = "";
        if (currentChar == '{' || currentChar == '}') 
        {
            ret += currentChar;
            eat(currentChar);
        }
        if (ret.equals(""))
            throw new ScanErrorException("No lexeme found! Unknown lexeme: " + currentChar);
        return ret;
    }

    /**
     * Method: main Executes the program, testing with files to return tokens.
     * 
     * @throws Exception if any of the used methods cannot find their expected
     *                   lexemes
     * @param args the arguments needed to execute the main method
     */
    public static void main(String[] args) throws Exception 
    {
        Scanner sn = new Scanner(new FileInputStream(new File("./src/parserTest10.txt")));
        while (sn.hasNext())
        {
            System.out.println(sn.nextToken());
        }
    }
}