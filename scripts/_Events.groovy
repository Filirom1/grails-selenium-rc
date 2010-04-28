includeTargets << new File("$seleniumRcPluginDir/scripts/_Selenium.groovy")

eventCreateWarStart = { warName, stagingDir ->
	ant.delete dir: "${stagingDir}/WEB-INF/classes/grails/plugins/selenium"
	ant.delete dir: "${stagingDir}/plugins/selenium-rc-1.0"
}

eventAllTestsStart = {
	registerSeleniumTestType()
}

eventTestSuiteStart = {String type ->
	if (type =~ /selenium/) {
		startSelenium()
		registerSeleniumTestListeners()
	}
}

eventTestSuiteEnd = {String type ->
	if (type =~ /selenium/) {
		stopSelenium()
	}
}
