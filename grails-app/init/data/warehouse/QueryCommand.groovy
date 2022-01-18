package data.warehouse

import grails.validation.Validateable

import java.time.LocalDate

class QueryCommand implements Validateable {
    // TODO: at least one metric
    List<Metric> metrics
    // TODO: may be nullable
    List<Dimension> dimensions
    // TODO: fix filters validation
    List<Filter> filters
    LocalDate dateFrom
    LocalDate dateTo
}
