/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logicorp.functionPlotter.parser;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hakim
 */
public class ExpressionTest {
    
    public ExpressionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

     @Test
    public void testSomeMethod() {
        
        String expression="cos(x)*cos(x)";
        
        Expression e2;
        e2 = new ExpressionBuilder(expression)
                .variables("x")
                .build();
        
        Parser p=new Parser();
        

       
        for (double i = -16d; i < 0; i+=0.1) {
          e2.setVariable("x",i);
          p.setValue("x", i);
          
          assertEquals(e2.evaluate(),p.eval(expression),0.1d);
        }
    }
}
