package com.googlecode.openbox.testu.tester;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD,ElementType.TYPE,ElementType.PACKAGE })
@Inherited
@Documented
/**
 * if you don't mark this annotation , it treat as true
 *
 */
public @interface TestCaseAutomated {
	
	public boolean value();

}
