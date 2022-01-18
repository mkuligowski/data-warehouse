package data.warehouse

import grails.validation.Validateable

import java.time.LocalDate

class QueryCommand implements Validateable {
    List<Metric> metrics
    // TODO: may be nullable
    List<Dimension> dimensions
    // TODO: fix filters validation
    List<Filter> filters
    LocalDate dateFrom
    LocalDate dateTo

    static constraints = {
        dateFrom nullable: true
        dateTo nullable: true
        filters nullable: true
        dimensions nullable: true
        metrics nullable: false
    }
}
