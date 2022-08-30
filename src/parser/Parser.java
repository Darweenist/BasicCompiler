package parser;

import ast.*;
import ast.Number;
import emitter.Emitter;

import java.util.*;

import java.io.*;
import scanner.*;

/**
 * Parses through the stream of tokens and produces a parse tree.
 * @author Dawson Chen
 * @version 9/24/2019
 */
public class Parser 
{
    private scanner.Scanner sn;
    private String currentToken;
    // private Environment env;
    /**
     * Constructs a Parser instance
     * @param sn the scanner that provides the stream of tokens
     * @throws ScanErrorException when the next token is not the expected
     */
    public Parser(scanner.Scanner sn) throws ScanErrorException
    {
        this.sn = sn;
        this.currentToken = sn.nextToken();
        // env = new Environment();
    }
    /**
     * Method: eat
     * Advances the current token, throwing an error if there is an unexpected character
     * @throws ScanErrorException when expected is not currentChar
     * @param expected the expected current char to be eaten
     */
    private void eat(String expected) throws ScanErrorException
    {
        currentToken = sn.nextToken();
    }
    /**
    * Method: parseNumber
    * precondition: current token is an integer
    * postcondition: number token has been eaten
    * @throws ScanErrorException when eat doesn't eat the expected char
    * @return the value of the parsed integer
    */
    private Expression parseNumber() throws ScanErrorException
    {
        String s = currentToken;
        eat(s);
        return new Number(Integer.parseInt(s));
    }
    
    /**
     * Method: parseFactor
     * Parses a factor, accounting for negatives, precedance and variables
     * @throws ScanErrorException when eat doesn't eat the expected char
     * @return the integer value of the parsed factor
     */
    public Expression parseFactor() throws ScanErrorException
    {

        if(currentToken.equals("-"))
        {
            //recursive case
            eat("-");
            return new BinOp("*", new Number(-1), parseFactor());
        }
        else if(currentToken.equals("("))
        {
            eat("(");
            Expression ret = parseExpression();
            eat(")");
            return ret;
        }
        else if(scanner.Scanner.isDigit(currentToken.charAt(0)))
        {
            //base case 1 - it's a number 
            return parseNumber();
        }
        else
        {
            String retID = currentToken;
            eat(currentToken);
            if(currentToken.equals("("))
            {
            	eat("(");
            	List<Expression> params = new ArrayList<Expression>();
            	while(!currentToken.equals(")"))
                {
                    params.add(parseExpression());
                    if (currentToken.equals(","))
                    	eat(",");
                }
            	eat(")");
            	return new ProcedureCall(retID, params);
            }
            //find ret from retID and return it
            // System.out.println(retID);
            return new Variable(retID);
        }
    }
    /**
     * Method: parseTerm
     * Parses the term and evaluates the quotient or product of two possible factors.
     * @throws ScanErrorException when eat doesn't eat the expected char
     * @return the integer value of the parsed term
     */
    public Expression parseTerm() throws ScanErrorException
    {
        Expression bo = parseFactor();      
        while(currentToken.equals("*") || currentToken.equals("/") || currentToken.equals("mod"))
        {
            if(currentToken.equals("*"))
            {
                eat("*");
                bo = new BinOp("*", bo, parseFactor());
            }
            else if(currentToken.equals("mod"))
            {
                eat("mod");
                bo = new BinOp("mod", bo, parseFactor());
            }
            else
            {
                eat("/");
                bo = new BinOp("/", bo, parseFactor());
            }
        }
        return bo;
    }
    /**
     * Method: parseExpression
     * Parses the expression, accounting for addition and subtraction, then
     * parsing the operands as terms.
     * @throws ScanErrorException when eat doesn't eat the expected char
     * @return the integer value of the parsed Expression
     */
    public Expression parseExpression() throws ScanErrorException
    {
        Expression term = parseTerm();
        while(currentToken.equals("+") || currentToken.equals("-"))
        {
            if(currentToken.equals("+"))
            {
                eat("+");
                term = new BinOp("+", term, parseTerm());
            }
            else
            {
                eat("-");
                term = new BinOp("-", term, parseTerm());
            }
        }
        return term;
    }
    /**
     * Method: parseStatement
     * Parses statements and executes corresponding actions, returning the specific
     * type of statement parsed, subclasses of Statement.
     * @throws ScanErrorException when eat doesn't eat the expected char
     * @return the statement parsed
     */
    public Statement parseStatement() throws ScanErrorException
    {
        if (currentToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression exp = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(exp);
        }       
        else if(currentToken.equals("IF"))
        {
            eat("IF");
            Expression exp1 = parseExpression();
            String relop = currentToken;
            eat(relop);
            Expression exp2 = parseExpression();
            eat("THEN");
            return new If(new Condition(exp1, exp2, relop), parseStatement());
        }
        else if(currentToken.equals("WHILE"))
        {
            eat("WHILE");
            Expression exp1 = parseExpression();
            String relop = currentToken;
            eat(relop);
            Expression exp2 = parseExpression();
            eat("DO");
            return new While(new Condition(exp1, exp2, relop), parseStatement());
        }
        // else if(currentToken.equals("READLN"))
        // {
        //     eat("READLN");
        //     eat("(");
        //     java.util.Scanner jsn = new java.util.Scanner(System.in);
        //     int val = Integer.parseInt(jsn.nextLine());
        //     String var = currentToken;
        //     vars.put(var, val);
        //     eat(currentToken);
        //     eat(")");
        //     eat(";");
        //     jsn.close();
        // }
        else if(currentToken.equals("BEGIN"))
        {
            List<Statement> exp = new ArrayList<Statement>();
            Block block = new Block(exp);
            eat("BEGIN");
            while(!currentToken.equals("END") && !currentToken.equals("."))
            {
                exp.add(parseStatement());
                System.out.println("currentToken: " + currentToken);
            }
            eat("END");
            eat(";");
            return block;
        } 
        // else if(!currentToken.equals("mod") && !currentToken.equals("IF") &&
        //         !currentToken.equals("WHILE") && !currentToken.equals("THEN") &&
        //         !currentToken.equals("DO") && !currentToken.equals("BEGIN") &&
        //         !currentToken.equals("END") &&
        //         (currentToken.compareTo("A") >= 0 && currentToken.compareTo("[") < 0 ||
        //         currentToken.compareTo("a") >= 0 && currentToken.compareTo("{") < 0))
        else 
        {
            //id:=expr;
            String id = currentToken;
            eat(id);
            eat(":=");
            //vars.put(id, parseExpression());
            Expression val = parseExpression();
            eat(";");
            return new Assignment(id, val);
        }
        // else
        // {
        //     throw new ScanErrorException("Not a statement!");
        // }
    }
    /**
     * Method: parseProgram
     * Parses the entire program, procedure declarations and then the statement 
     * @throws ScanErrorException when eat doesn't eat the expected char
     * @return the Program parsed
     */
    public Program parseProgram() throws ScanErrorException
    {
    	List<Variable> vars = new ArrayList<Variable>();
        if(currentToken.equals("VAR"))
        {
        	eat(currentToken);
        	while(!currentToken.equals(";"))
        	{
        		vars.add(new Variable(currentToken));
        		eat(currentToken);
        		if(currentToken.equals(","))
        			eat(",");
        	}
        	eat (";");
        }
        List<ProcedureDeclaration> procDecs = new ArrayList<ProcedureDeclaration>();
        while(currentToken.equals("PROCEDURE")) 
        {
            List<String> params = new ArrayList<String>();
            eat(currentToken);
            String id = currentToken;
            eat(id);
            eat("(");
            while(!currentToken.equals(")"))
            {
                params.add(currentToken);
                eat(currentToken);
                if (currentToken.equals(","))
                	eat(",");
            }
            eat(")");
            eat(";");
            Statement st = parseStatement();
            procDecs.add(new ProcedureDeclaration(st, id, params));
        }
        Statement body = parseStatement();
        return new Program(procDecs, body, vars);
    }
    /**
     * Method: main, tests the parseStatement method, which also tests 
     * all other parser methods in turn.
     * @param args the arguments used to run the class
     * @throws ScanErrorException when eat doesn't eat the expected token
     * @throws FileNotFoundException when the file passed into the scanner is unreal
     */
    public static void main(String[] args) throws ScanErrorException, FileNotFoundException
    { 
        scanner.Scanner sn = new scanner.Scanner(new FileInputStream(new File("./parserTest10.txt")));
        Parser p = new Parser(sn);
//        Environment env = new Environment(null);
        Emitter e = new Emitter("testingYale.asm");
//        while(sn.hasNext())
//        {
        Program prog = p.parseProgram();
        prog.compile(e);
//            s.exec(env);
//        }
    }
}