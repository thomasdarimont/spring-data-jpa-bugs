package org.springframework.data.jpa.bugs.datajpa298;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Add:
 * 
 * <pre>
 * -javaagent:/Users/tom/.m2/repository/org/springframework/spring-instrument/3.1.4.RELEASE/spring-instrument-3.1.4.RELEASE.jar
 * </pre>
 * 
 * to jvm args
 * 
 * @author Thomas Darimont
 */
@ActiveProfiles("testing")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WeavingTestConfiguration.class })
public class WeavingTest {

	@Test
	public void testSimple() {
		SimpleUser simpleUser = new SimpleUser("admin");
		assertNotNull("Environment wasn't injected", simpleUser.getEnvironment());

		JpaUser jpaUser = new JpaUser("admin");
		assertNotNull("Environment wasn't injected", jpaUser.getEnvironment());

	}
}
