package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 
    	 ** to this method - you just need to fill them in.
    	 **/
          
    	StringTokenizer st = new StringTokenizer(expr, delims); //creating st tok object
    	String tk = st.nextToken(); //first token

    	while (st.hasMoreElements()) 
    	{
    		String Check = expr.substring(expr.indexOf(tk)+tk.length(), expr.indexOf(tk)+tk.length()+1); 

    		if (Character.isDigit(tk.charAt(0)) == true) 
    		{
    			tk = st.nextToken();
    			continue;
    		}

    		if (Check.equals("[")) 
    		{
    			Array arr = new Array(tk);
    			arrays.add(arr);
    		}
    		
    		else
    		{
    			Variable var = new Variable(tk);
    			vars.add(var);
    		}

    		tk= st.nextToken();    		
    	}

    	Variable var = new Variable(tk);
		vars.add(var);	
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	expr=expr.replaceAll("\\s"," ");//removes all the spaces from the expression
    	 expr=expr.replaceAll("\\s","");//removes all the spaces from the expression
         
         Stack<String> operators=new Stack<String>();//initialize stacks
         Stack<Float> oprnd=new Stack<Float>();
         
         String tk;//stores the token in the String tokenizer
         
         
         float result=0;//gives the final result
                 
         int i=0;//gives the index of the token      
         
         StringTokenizer st=new StringTokenizer(expr,delims,true);
         while(st.hasMoreTokens())
         {
             tk=st.nextToken();
             float temp=0,temp1=0;
             
           if ( tk.matches("[a-z A-Z]+"))//check for any variable
           {
      
              if(vars.contains(new Variable(tk)))
              {
               
                 Variable tempV=new Variable(tk);
                
                  int index = vars.indexOf(tempV);
                  oprnd.push ((float)(vars.get(index).value));
                  
                 
              }
              else if(arrays.contains(new Array(tk)))
              {
                  Array  tempA=new Array(tk);
                  int index=arrays.indexOf(tempA);
                  
                  int j=0;
               String sub="";
               while(st.hasMoreTokens())
               {
                    tk=st.nextToken();
                  if(tk.equals("["))
                  {
                      j++;
                  }
                  if(tk.equals("]"))
                  {
                       j--;
                  }
                  
                  if(j==0)
                  {
                      break;
                  }
                  sub=sub+tk;
                }
                  
                 float res= arrays.get(index).values[((int)(evaluate(sub,vars,arrays)))];
               oprnd.push(res);
              }
        
             }
           else if(tk.matches("[0-9]+") )
            {
               oprnd.push(Float.parseFloat(tk));
               
             }
           else if(tk.equals("("))
           {
               int j=1;
               String sub="";
               while(st.hasMoreTokens())
               {
                    tk=st.nextToken();
                  if(tk.equals("("))
                  {
                      j++;
                  }
                  if(tk.equals(")"))
                  {
                       j--;
                  }
                  if(j==0)
                  {
                      break;
                  }
                  sub=sub+tk;
                }
                  
                oprnd.push(evaluate(sub,vars,arrays));
                
              }
              
             if(operators.isEmpty()==false)
           {
              
               if(operators.peek().equals("/") )
             {
                   
                 temp=oprnd.pop();
                 
                 temp1=oprnd.pop();
                 result=temp1/temp;
                 oprnd.push(result);
                 
                 operators.pop();
                 i=i+1;
                 continue;
              }
             else if( operators.peek().equals("*"))
              {
                  temp=oprnd.pop();
                  
                  temp1=oprnd.pop();
                 result=temp*temp1;
                 oprnd.push(result);
                 
                 operators.pop();
                 i=i+1;
                 continue;
              }
              
            }   
                             
            if(tk.equals("+") || tk.equals("-") || tk.equals("*") || tk.equals("/"))
            {
                  operators.push(tk);
                  
            }
             
             
            i=i+1; 
          }
          
          Stack<Float> oprnd_rev=new Stack<Float>();
          Stack<String> operators_rev=new Stack<String>();
          
          while(!oprnd.isEmpty()) {

		oprnd_rev.push(oprnd.pop());
	}
	while(!operators.isEmpty()) {

		operators_rev.push(operators.pop());
	}
          while(!operators_rev.isEmpty())
          {
              float a=0,b=0,ans=0;
              a=oprnd_rev.pop();
              b=oprnd_rev.pop();
              String c=operators_rev.pop();
              switch(c)
              {
                  case"+":
                  { ans=a+b;
                      oprnd_rev.push(ans);
                   break;
                    }
      
                 case"-":
                 { ans=a-b;
                     oprnd_rev.push(ans);
                  break;
                 }
                 
               
              }
              
          }
          
         result=oprnd_rev.pop();
      
      
   return result;
   
    }
    	
    
}
