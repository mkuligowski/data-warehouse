package data.warehouse.query

import data.warehouse.Dimension
import data.warehouse.Filter
import data.warehouse.Metric
import grails.validation.Validateable

import java.time.LocalDate

class StatsQueryParams implements Validateable {
    List<Metric> metrics
    List<Dimension> dimensions
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
