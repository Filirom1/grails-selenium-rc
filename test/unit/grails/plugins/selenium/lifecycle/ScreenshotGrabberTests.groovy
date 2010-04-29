package grails.plugins.selenium.lifecycle

import com.thoughtworks.selenium.SeleniumException
import grails.plugins.selenium.SeleniumWrapper
import org.gmock.WithGMock
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import static org.hamcrest.CoreMatchers.anything
import static org.hamcrest.CoreMatchers.equalTo

@WithGMock
class ScreenshotGrabberTests {

	SeleniumWrapper selenium

	@Before
	void setUp() {
		selenium = mock(SeleniumWrapper)
	}

	@Test
	void capturesScreenshotOnTestFailureEvent() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = true")
		def screenshotGrabber = new ScreenshotGrabber(selenium, config)

		selenium.captureScreenshot pathTo(new File("target/test-reports/test-screenshots/WhateverTests.testWhatever.png"))

		play {
			screenshotGrabber.onTestFailure("some.package.WhateverTests", "testWhatever")
		}
	}

	@Test
	void usesConfiguredDirectory() {
		def config = new ConfigSlurper().parse("""
			selenium.screenshot.onFail = true
			selenium.screenshot.dir = "some/directory/path"
		""")
		def screenshotGrabber = new ScreenshotGrabber(selenium, config)

		selenium.captureScreenshot pathTo(new File("some/directory/path/WhateverTests.testWhatever.png"))

		play {
			screenshotGrabber.onTestFailure("some.package.WhateverTests", "testWhatever")
		}
	}

	@Test
	void doesNotCaptureScreenshotIfDisabledInConfig() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = false")
		def screenshotGrabber = new ScreenshotGrabber(selenium, config)

		play {
			screenshotGrabber.onTestFailure("WhateverTests", "testWhatever")
		}
	}

	@Test
	void handlesExceptionsThrownWhenCapturingScreen() {
		def config = new ConfigSlurper().parse("selenium.screenshot.onFail = true")
		def screenshotGrabber = new ScreenshotGrabber(selenium, config)

		selenium.captureScreenshot(anything()).raises(new SeleniumException("screenshot failed"))
		play {
			screenshotGrabber.onTestFailure("WhateverTests", "testWhatever")
		}
	}

	static Matcher<String> pathTo(File expectedFile) {
		equalTo(expectedFile.canonicalPath)
	}

}