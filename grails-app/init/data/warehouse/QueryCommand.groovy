package data.warehouse

import grails.validation.Validateable

import java.time.LocalDate

class QueryCommand implements Validateable {
    List<Metric> metrics
    List<Dimension> dimensions
    List<Filter> filters
    LocalDate dateFrom
    LocalDate dateTo
}
