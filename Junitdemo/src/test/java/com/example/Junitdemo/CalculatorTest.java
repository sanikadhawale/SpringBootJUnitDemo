package com.example.Junitdemo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CalculatorTest {
	
	Calculator calculator;
	
	
	@Test
	public void testMultiply() {
		calculator = new Calculator();
		assertEquals(20, calculator.multiply(4, 5));
		assertEquals(30, calculator.multiply(5, 6));
	}
	
	@Test
	public void testDivide() {
		calculator = new Calculator();
		assertEquals(2, calculator.divide(10, 5));
	}

}
