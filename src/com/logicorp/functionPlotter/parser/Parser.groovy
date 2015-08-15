package com.logicorp.functionPlotter.parser

import groovy.util.logging.*;
/**
 *
 * @author hakim
 */

@Log
public class Parser  {

    String eoe                      = '$' //end of expression
    def operators                   =  FunctionsRepository.ops
    def variables                   = ['x':0] 
    def rightParenthesis            = [')',']']
    def leftParenthesis             = ['(','[']  
    def functionArgumentSeparator   = [',']
    def pi                          = ['PI']
    def e                           = ['E']
    def functions                   = FunctionsRepository.fcts
    def delimiters                  = operators.keySet() + rightParenthesis +  leftParenthesis + functionArgumentSeparator+eoe  
 
    String expression='';
    def finalStack;
    
    
    Type type(String token){       
        switch (token){
        case {it in leftParenthesis}           :Type.LEFT_PARENTISIS
                                                break
        case {it in rightParenthesis}          :Type.RIGHT_PARENTIISIS
                                                break       
        case {operators.containsKey(it)}       :Type.OPERATOR
                                                break  
        case {variables.containsKey(it)}       :Type.VARIABLE
                                                break             
        case {functions.containsKey(it)}       :Type.FUNCTION
                                                break   
        case  {it==~'\\d+(\\.\\d+)?'}          :Type.CONSTANT
                                                break
        case {it.toUpperCase() in pi}          :Type.PI
                                                break
        case {it.toUpperCase() in e }          :Type.E
                                                break                                                 
        default                                :Type.ERROR           
            
        } 
    }
    
    def tokenize(String _str){           
        def tokens=[]
        def token =''
        
        (_str+'$').each {car  ->
            if (!car.isAllWhitespace()  ){
                if (!delimiters.contains(car) ){                    
                    token +=car
                }
                else {                    
                    if (token) {tokens << token;token =''}          
                    if (car!=eoe) tokens << car 
                }  
            }
        }    
        tokens
    }

    def converToPostFixExpression(tokens){        
        def output =[]
        Stack<String> stack  = new Stack<>();
        tokens.each{ token->
            switch(type(token)) {              
            case  Type.CONSTANT             :
            case  Type.VARIABLE             : output << token
                                              break
            case  Type.PI                   : output << Math.PI.toString()
                                              break
            case  Type.E                    : output << Math.E.toString()
                                              break                              
            case  Type.FUNCTION             :                     
            case  Type.LEFT_PARENTISIS      : stack.push(token);
                                              break
            case  Type.OPERATOR             : while (!stack.isEmpty()){                      
                                               if((type(stack.peek())==Type.OPERATOR)
                                                       && (operators[stack.peek()].precedence  > operators[token].precedence 
                                                          || 
                                                          (operators[stack.peek()].precedence==operators[token].precedence 
                                                       && operators[token].associativity!=Associativity.RIGHT)))
                                               output << stack.pop(); else break; 
                                              }
                                              stack.push(token);
                                              break
            case  Type.RIGHT_PARENTIISIS    :  while (!stack.isEmpty() && type(stack.peek()) != Type.LEFT_PARENTISIS){
                                               output << stack.pop()
                                              }
                                              if (stack.isEmpty() || type(stack.peek())!=Type.LEFT_PARENTISIS) { 
                                                throw new Exception('mismatched parentheses')
                                              }else{
                                                 stack.pop() 
                                              }  
                                              if (!stack.isEmpty() && type(stack.peek())==Type.FUNCTION){
                                               output << stack.pop()  
                                              }
                                              break 
            case Type.FUNCTION_ARGUMENT_SEPARATOR :
                                           break
            }
            
        }
        while(!stack.isEmpty()){
            if(type(stack.peek())==Type.RIGHT_PARENTIISIS && type(stack.peek())==Type.LEFT_PARENTIISIS)
              throw new Exception('mismatched parentheses')
            output << stack.pop()  
        }
        output
    }
    
    def evaluatePostfixExpression(tokens)throws Exception{        
        Stack<Double> stack  = new Stack<>();
        tokens.each{ token->
            switch(type(token)){
            case  Type.CONSTANT          :stack.push(Double.parseDouble(token))
                                          break
                
            case  Type.VARIABLE          :stack.push(variables[token])
                                          break
             
            case  Type.OPERATOR          :def (op2,op1) =[stack.pop(),stack.pop()] 
                                          if(operators[token].dod!=null && operators[token].dod.call(op1,op2)==false){                  
                                               throw new Exception( ' argument is not in the domain of definition')  
                                               log.info 'an exception occurred'
                                          } else{
                                                stack.push(operators[token].core.call(op1,op2))
                                                
                                          }
                                          break 
                                            
            case  Type.FUNCTION          :def args=[]                                            
                                             functions[token].nbArgs.downto(1){args <<  stack.pop()}                                             
                                             
                                          if(functions[token].dod!=null && functions[token].dod.call(args)==false){                
                                              throw new Exception( ' argument is not in the domain of definition') 
                                              log.info 'an exception occurred'
                                          } else{                                                
                                                stack.push(functions[token].core.call(args))                                
                                          }                                             
                                          break              
            }           
        }
        
        stack.pop()
    }    
    
    void setValue(String var,double value){
            variables[var]=value; 
    }
            
    Double eval(String exp) {
        
        if (!this.expression.equalsIgnoreCase(exp)){
            finalStack=converToPostFixExpression(tokenize(exp))
            this.expression=exp;            
        }
        evaluatePostfixExpression(finalStack)
    }                                
}
