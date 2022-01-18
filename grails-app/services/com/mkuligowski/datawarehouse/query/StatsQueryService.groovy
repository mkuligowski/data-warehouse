package com.mkuligowski.datawarehouse.query

import data.warehouse.Metric
import data.warehouse.QueryCommand
import data.warehouse.SQLRestriction
import data.warehouse.StatsView
import grails.gorm.transactions.Transactional
import grails.orm.HibernateCriteriaBuilder

class StatsQueryService {

//    private static def METRICS_MAPPING = [
//            Metric.CLICKS: [mappedColumn: 'clicks', aggregationExpression: 'sum(clicks) as clicks'],
//            Metric.IMPRESSIONS: [mappedColumn: 'impressions', aggregationExpression: 'sum(impressions) as impressions'],
//            Metric.CTR: [mappedColumn: 'ctr', aggregationExpression: 'sum(clicks) * 1.0 / sum(impressions) as ctr'],
//    ]
//
//    private static def DIMENSIONS_MAPPING = [
//            Metric.CLICKS,
//            Metric.IMPRESSIONS,
//            Metric.CTR,
//    ]

    @Transactional(readOnly = true)
    def query(QueryCommand queryParams) {

        // TODO: validateMetrics


        List<String> dimensions = queryParams.dimensions.collect{it.mappedColumn}
        List<String> metricsAggregates = queryParams.metrics.collect{it.expression}
        List<String> metricNames = queryParams.metrics.collect{it.mappedColumn}

        List<SQLRestriction> restrictions = queryParams.filters.collect {
            new SQLRestriction(field: it.filter.mappedColumn, value: it.value, operator: '=')
        }
        if (queryParams.dateFrom)
            restrictions << new SQLRestriction(field: 'stats_date', value: queryParams.dateFrom, operator: '>=')

        if (queryParams.dateTo)
            restrictions << new SQLRestriction(field: 'stats_date', value: queryParams.dateTo, operator: '<=')

// TODO - round
        String restriction = restrictions.collect {"${it.field}${it.operator}?"}.join(' AND ')

        def c = StatsView.createCriteria()
        def result = c.list {
            projections {
                sqlGroupProjection "${dimensions.join(',')} ${dimensions? ',':''} ${metricsAggregates.join(',')}", dimensions.join(','),
                        dimensions + metricNames,
                        dimensions.collect {s -> HibernateCriteriaBuilder.STRING} + metricNames.collect(i -> HibernateCriteriaBuilder.DOUBLE)
            }
            sqlRestriction restriction, restrictions.collect{it.value}

        }

        return  ([headers: dimensions + metricNames, rows: result])
    }
}
