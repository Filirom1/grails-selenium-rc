package selenium.headless

import grails.plugins.selenium.SeleniumAware

@Mixin(SeleniumAware)
class GoogleTests extends GroovyTestCase {

	void testHitRemoteServerWithoutStartingGrails() {

		selenium.open("/");
		selenium.type("q", "Grails");
		selenium.fireEvent("q", "keyUp");
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if (selenium.isTextPresent("www.grails.org")) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}

	}

}
