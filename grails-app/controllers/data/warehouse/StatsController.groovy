package data.warehouse

class StatsController {
    static responseFormats = ['json']

    def query(QueryCommand command) {
        def x = false

        List<String> dimensions = command.dimensions.collect{it.mappedColumn}
        List<String> metricsAggregates = command.metrics.collect{it.expression}
        List<String> metricNames = command.metrics.collect{it.mappedColumn}


        def c = StatsView.createCriteria()
        def result = c.list {
            projections {
                sqlGroupProjection "${dimensions.join(',')}, ${metricsAggregates.join(',')}", dimensions.join(','), dimensions + metricNames, dimensions.collect {s -> STRING} + metricNames.collect( i -> INTEGER)
            }
//            sqlRestriction "country='US'"
//            x ? (eq 'country','US' ): (sqlRestriction "1=1")

        }


        respond ([headers: dimensions + metricNames, rows: result]])
    }
}
