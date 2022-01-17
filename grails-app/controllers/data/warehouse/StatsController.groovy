package data.warehouse

import grails.orm.HibernateCriteriaBuilder

class StatsController {
    static responseFormats = ['json']

    def query(QueryCommand command) {
        def x = false

        if (!command.validate()) {
            response.status = 400
            respond( [errors: command.errors.allErrors])
        }

        List<String> dimensions = command.dimensions.collect{it.mappedColumn}
        List<String> metricsAggregates = command.metrics.collect{it.expression}
        List<String> metricNames = command.metrics.collect{it.mappedColumn}
        String restriction = command.filters.collect {"${it.filter.mappedColumn}=?"}.join(' AND ')

        def c = StatsView.createCriteria()
        def result = c.list {
            projections {
                sqlGroupProjection "${dimensions.join(',')}, ${metricsAggregates.join(',')}", dimensions.join(','),
                        dimensions + metricNames,
                        dimensions.collect {s -> HibernateCriteriaBuilder.STRING} + metricNames.collect( i -> HibernateCriteriaBuilder.BIG_DECIMAL)
            }
            sqlRestriction restriction, command.filters.collect{it.value}
//            x ? (eq 'country','US' ): (sqlRestriction "1=1")

        }

        respond ([headers: dimensions + metricNames, rows: result])
    }
}
