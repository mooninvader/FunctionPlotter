package com.logicorp.functionPlotter.parser

/**
 *
 * @author hakim
 */
class FunctionsRepository {

    public static def fcts =[
                     'sin' : new Function(core :{Math.sin(it[0])}, nbArgs :1),
                     'cos'   : new Function(core :{ Math.cos(it[0])}, nbArgs :1),
                     'tan'   : new Function(core :{ Math.tan(it[0])}, nbArgs :1,dod:{it[0]%(Math.PI/2)!=0}),
                     'log'   : new Function(core :{ Math.log(it[0])}, nbArgs :1,dod :{it[0]>0}),
                     'log10' : new Function(core :{Math.log10(it[0])}, nbArgs :1,dod :{it[0]>0}),
                     'sqrt'  : new Function(core :{ Math.sqrt(it[0])}, nbArgs :1,dod :{it[0]>=0}),
                     'abs'   : new Function(core :{Math.abs(it[0])}, nbArgs :1),
                     'exp'   : new Function(core:{ Math.pow(it[0],it[1])},nbArgs:2) ,
                     'max'   : new Function(core:{ Math.max(it[0],it[1])},nbArgs:2),
                     'min'   : new Function(core:{Math.min(it[0],it[1])},nbArgs:2) 
                    ];

   public static ops =['+': new Operator(core : {double p1,double p2 -> p1+p2} ,precedence :1,associativity :Associativity.LEFT),
                       '-': new Operator(core : {double p1,double p2 -> p1-p2} ,precedence :1,associativity :Associativity.LEFT), 
                       '*': new Operator(core : {double p1,double p2 -> p2*p1} ,precedence:2,associativity :Associativity.LEFT), 
                       '/': new Operator(core : {double p1,double p2 -> p1/p2} ,precedence :2,dod :{p1,p2 ->  p2!=0},associativity :Associativity.LEFT), 
                       '^': new Operator(core : {double p1,double p2 -> Math.pow(p1,p2)} ,precedence :3,associativity :Associativity.RIGHT, dod : {p1,p2 -> ! Double.isNaN(Math.pow(p1,p2))}), 
                      ] 

}
/*TODO  find the dod of tan*/
                    
