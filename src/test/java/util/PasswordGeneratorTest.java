package util;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import util.PasswordGenerator;

public class PasswordGeneratorTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void whenArgumentLessThanOneThenException() {
		thrown.expect(IllegalArgumentException.class);
		
		PasswordGenerator.generate(0);
	}
	
	@Test
	public void returnedStringEqualToArgument() {
		String result = PasswordGenerator.generate(4);
		
		assertEquals(4, result.length());
	}
}
