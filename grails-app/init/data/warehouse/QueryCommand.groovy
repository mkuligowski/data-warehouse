package data.warehouse

import java.time.LocalDate

class QueryCommand {
    List<Metric> metrics
    List<Dimension> dimensions
    List<Filter> filter
    LocalDate dateFrom
    LocalDate dateTo
}
