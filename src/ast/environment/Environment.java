package ast.environment;

import java.util.*;

import ast.ProcedureDeclaration;

/**
 * Represents an environment where variables are stored.
 * @author Dawson Chen
 * @version 10/1/19
 */
public class Environment
{
    private Map<String, Integer> vars;
    private Map<String, ProcedureDeclaration> proc;
    private Environment parent;
    /**
     * Instantiates a new environment, initializing vars to an empty map
     */
    public Environment(Environment p)
    {
        vars = new HashMap<String, Integer>();
        proc = new HashMap<String, ProcedureDeclaration>();
        parent = p;
    }
    /**
     * Declare a variable to a value
     * @param variable name to be declared
     * @param value value to be assigned
     */
    public void declareVariable(String variable, int value) 
    {
        // System.out.println("variable to set: " + variable);
        // System.out.println("value to set: " + value);
        vars.put(variable, value);
        // System.out.println(vars);
    }
    /**
     * Sets a variable to a value
     * @param variable selected to be changed or created
     * @param value the value that the variable is to be set to
     */
    public void setVariable(String variable, int value)
    {
    	if(vars.containsKey(variable) || parent == null)
    	{
    		vars.put(variable, value);
    	}
    	else
    	{
    		parent.setVariable(variable, value);
    	}
    }
    /**
     * Returns the variable value requested
     * @param variable the variable whose value is requested
     * @return value the requested variable
     */
    public int getVariable(String variable) 
    {
    	if(vars.containsKey(variable))
    		return vars.get(variable);
    	else if(parent == null)
    		return -1000;
    	else
    		return parent.getVariable(variable);
    }
    /**
     * Sets the procedure declaration to a procedure name
     * @param pro is the name of the procedure
     * @param dec is the procedure's declaration
     */
    public void setProcedure(String pro, ProcedureDeclaration dec)
    {
        proc.put(pro, dec);
    }
    /**
     * Returns the procedure declaration associated with the given name
     * @param name the name of the procedure to be found
     * @return the procedure associated with the name passed in
     */
    public ProcedureDeclaration getProcedure(String name)
    {
        return proc.get(name);
    }
}