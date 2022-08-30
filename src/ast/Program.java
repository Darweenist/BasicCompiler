//hello
package ast;

import java.util.*;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents the entire program in the grammar
 * 
 * @author Dawson Chen
 * @version 10/22/2019
 */
public class Program extends Statement 
{
	private List<ProcedureDeclaration> procDec;
	private List<Variable> vars;
	private Statement st;

	/**
	 * Instantiates the list of procedure declarations and the statement body
	 * 
	 * @param pd the list of procedure declarations
	 * @param s  the body of code to be executed, represented by a single statement
	 */
	public Program(List<ProcedureDeclaration> pd, Statement s, List<Variable> v) 
	{
		procDec = pd;
		st = s;
		vars = v;
	}

	@Override
	public void exec(Environment env) 
	{
		for (ProcedureDeclaration p : procDec)
			p.exec(env);
		st.exec(env);
	}

	/**
	 * Uses an emitter to write SPIM code to a given file.
	 * @param fname the file to be written to
	 */
	@Override
	public void compile(Emitter e) 
	{
		e.emit(".text");
		e.emit(".globl main");
		e.emit("main:");
		st.compile(e);
		e.emit("li $v0 10");
		e.emit("syscall # halt");
		//compile procDecs
		for(ProcedureDeclaration pd : procDec)
		{
			pd.compile(e);
		}
		e.emit(".data");
		e.emit("nextline: .asciiz \"\\n\"");
		for(Variable var : vars)
		{
			e.emit("var" + var.getName() + ":" + " .word " +0);
		}
		
	}
}