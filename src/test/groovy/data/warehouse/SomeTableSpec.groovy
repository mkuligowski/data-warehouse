package data.warehouse

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class SomeTableSpec extends Specification implements DomainUnitTest<SomeTable> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
