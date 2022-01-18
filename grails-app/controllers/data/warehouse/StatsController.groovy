package data.warehouse

import grails.orm.HibernateCriteriaBuilder

class StatsController {
    static responseFormats = ['json']

    def query(QueryCommand command) {

        if (!command.validate()) {
            response.status = 400
            respond( [errors: command.errors.allErrors])
        }

        List<String> dimensions = command.dimensions.collect{it.mappedColumn}
        List<String> metricsAggregates = command.metrics.collect{it.expression}
        List<String> metricNames = command.metrics.collect{it.mappedColumn}

        List<SQLRestriction> restrictions = command.filters.collect {
            new SQLRestriction(field: it.filter.mappedColumn, value: it.value, operator: '=')
        }
        if (command.dateFrom)
            restrictions << new SQLRestriction(field: 'stats_date', value: command.dateFrom, operator: '>')

        if (command.dateTo)
            restrictions << new SQLRestriction(field: 'stats_date', value: command.dateTo, operator: '<')


        String restriction = restrictions.collect {"${it.field}${it.operator}?"}.join(' AND ')

        def c = StatsView.createCriteria()
        def result = c.list {
            projections {
                // TODO - fix when empty dimensions
                sqlGroupProjection "${dimensions.join(',')}, ${metricsAggregates.join(',')}", dimensions.join(','),
                        dimensions + metricNames,
                        dimensions.collect {s -> HibernateCriteriaBuilder.STRING} + metricNames.collect( i -> HibernateCriteriaBuilder.BIG_DECIMAL)
            }
            sqlRestriction restriction, restrictions.collect{it.value}

        }

        respond ([headers: dimensions + metricNames, rows: result])
    }
}
