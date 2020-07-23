package Restaruner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)

@CucumberOptions(
		features = "RestaFeatures/Restafeaturetestcase/RestaTestCase.feature",
		glue = {"Restapagestepdefination","Classpath/Restapagestepdefination"}
		
		)

public class Restruner {
	
	

}
