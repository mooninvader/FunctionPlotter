
package com.logicorp.functionPlotter.parser

Parser p =new Parser()
def tokens =p.tokenize("(1-x)*(x-3)/(2-x)")
println p.converToPostFixExpression(tokens);

println p.evaluatePostfixExpression(p.converToPostFixExpression(tokens))
println 'ert'