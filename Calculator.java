/**
 * Program that takes a regular mathematical expression, turns it into RPN and evaluates it to get a result.
 *
 *
 *
 *
 *
 *
 * @author Antonio Munoz
 * @version HW 6, #3
 * @bugs eveything works perfectly until parenthesis are used. for some reason, the stack pushes the left parenthesis
 *       and works up to the point were it reads the right parenthesis. Then it keeps poping from the stack until it
 *       peeks that the next is a left parenthesis in the stack. The problem is that when I try to pop the left paren,
 *       it is gone for some reason and gives me an error of unbalanced parenthesis :(
 *       left peaces of code to show how much I tried debugging
 */


package oop.amunoz.hw6.thee;


import oop.amunoz.hw6.two.Operator;
import oop.amunoz.hw6.two.RPN;
import oop.amunoz.hw6.two.Token;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

    public static void main(String args[]){

        //while loop to keep asking for input
        while(true){
            //scanning input
            Scanner in = new Scanner(System.in);
            String s = in.nextLine();

            String[] array = s.split(" ");
            ArrayList<Token> tokens = new ArrayList<Token>();

            //going through all the input
            //consideing that parenthesis don't have a space between the token and itself
            for (int i=0; i<array.length; i++){

                //checking if there is a left paenthesis
                if (array[i].charAt(0)=='('){
                    tokens.add(Token.parseToken("("));
                    String sub = array[i].toString().substring(1);
                    tokens.add(Token.parseToken(sub));
                }
                //checking if there is a right parenthesis
                else if ( array[i].charAt(array[i].length()-1) == ')' ){
                    String sub = array[i].toString().substring(0,array[i].length()-1);
                    tokens.add(Token.parseToken(sub));
                    tokens.add(Token.parseToken(")"));
                }
                //otherwise reading token as if there were no parenthesis
                else {
                    tokens.add(Token.parseToken(array[i]));
                }
            }

            //used this to debug a little bit
//            for(int i = 0; i<tokens.size(); i++){
//                if(tokens.get(i).isOperator()){
//                    System.out.println(tokens.get(i).getOperator().toString());
//                }
//                else{
//                    System.out.println(tokens.get(i).getNumber().toString());
//                }
//            }

            //converting expression to RPN
            ArrayList<Token> newArray = toRPN(tokens);

            //more debugging nightmare
//            for(int i = 0; i<newArray.size(); i++){
////                if(newArray.get(i).isOperator()){
////                    System.out.println(newArray.get(i).getOperator().toString());
////                }
////                else{
////                    System.out.println(newArray.get(i).getNumber().toString());
////                }
////            }

            //showing result
            System.out.println(RPN.eval(newArray));
        }
    }

    /**function that converts a regular math expression into a RPN expression
     *
     * @param tkns array that contains the regular math expression tokens
     * @return array that contains the tokens in a RPN order
     */

    public static ArrayList<Token> toRPN(ArrayList<Token> tkns){
        ArrayList<Token> output;

        output= new ArrayList<Token>();

        Stack<Operator> stk;

        stk = new Stack<Operator>();

        //going thtough the array of tokens
        for (int i = 0; i < tkns.size(); i++){

            Token tk = tkns.get(i);
            //following instructions from PDF
            if (!tk.isOperator()){
                output.add(tk);
                //System.out.println(tk.getNumber());
            }
            else if ( tk.getOperator() != Operator.LPAREN && tk.getOperator() != Operator.RPAREN ){
                Operator op = tk.getOperator();
                while( stk.size()>0 && (stk.peek().getPrecedence()>op.getPrecedence() || (stk.peek()==Operator.EXPONENT && op==Operator.EXPONENT ) )){

                    output.add(new Token(stk.pop()));
                }
                stk.push(tk.getOperator());
                //System.out.println(tk.getOperator().toString());
            }
            else if( tk.getOperator() == Operator.LPAREN ){
                //System.out.println(tk.getOperator().toString());
                stk.push(Operator.LPAREN);
               // System.out.println("LPAREn:"+stk.peek().toString());
            }
            else if( tk.getOperator() == Operator.RPAREN ){
                //System.out.println(tk.getOperator().toString());
                //System.out.println(stk.peek().toString());
                System.out.println(stk.peek().toString());
                while(stk.size()>0 && stk.peek() != Operator.LPAREN ){

                    output.add(new Token(stk.pop()));
                    System.out.println(stk.peek().toString());
                }
                if ( stk.size()>0 && stk.peek() == Operator.LPAREN ){
                    stk.pop();
                }
                else{
                    //throwing exception when there is not balance on the parenthesis
                    throw new ArithmeticException("The expression contains mismatched parentheses");
                }
            }
        }

        //following last step
        while (stk.size()>0){
            output.add(new Token(stk.pop()));
        }

        //return RPN expression
        return output;
    }
}
