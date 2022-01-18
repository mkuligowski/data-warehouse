package com.mkuligowski.datawarehouse.query

import data.warehouse.Dimension
import data.warehouse.Metric
import data.warehouse.QueryCommand
import data.warehouse.SQLRestriction
import data.warehouse.StatsView
import grails.gorm.transactions.Transactional
import grails.orm.HibernateCriteriaBuilder
import org.hibernate.type.Type

class StatsQueryService {

    private static def METRICS_MAPPING = [
            (Metric.CLICKS): [mappedColumn: 'clicks', aggregationExpression: 'sum(clicks) as clicks'],
            (Metric.IMPRESSIONS): [mappedColumn: 'impressions', aggregationExpression: 'sum(impressions) as impressions'],
            (Metric.CTR): [mappedColumn: 'ctr', aggregationExpression: 'ROUND(sum(clicks) * 1.0 / sum(impressions),5) as ctr'],
    ]

    private static def DIMENSIONS_MAPPING = [
            (Dimension.CAMPAIGN):[mappedColumn: 'campaign_name'],
            (Dimension.DATASOURCE):[mappedColumn: 'datasource_name']
    ]

    @Transactional(readOnly = true)
    def query(QueryCommand queryParams) {

        if (!queryParams.metrics)
            return  ([headers: [], rows: []])


        List<String> dimensions = queryParams.dimensions.collect{DIMENSIONS_MAPPING[it].mappedColumn}
        List<String> metricsAggregates = queryParams.metrics.collect{METRICS_MAPPING[it].aggregationExpression}
        List<String> metricNames = queryParams.metrics.collect{METRICS_MAPPING[it].mappedColumn}

        def (String restrictionSQLStatement, List<Object> restrictionBindings) = prepareRestrictionsStatement(queryParams)

        def criteria = StatsView.createCriteria()
        def result = criteria.list {
            projections {
                sqlGroupProjection prepareProjectionSQLStatement(dimensions, metricsAggregates),
                        prepareGroupingSQLStatement(dimensions),
                        prepareColumnList(dimensions, metricNames),
                        prepareColumnTypes(dimensions, metricNames)
            }
            sqlRestriction restrictionSQLStatement, restrictionBindings

        }

        return  ([headers: dimensions + metricNames, rows: result])
    }

    private static List<Type> prepareColumnTypes(List<String> dimensions, List<String> metricNames) {
        dimensions.collect { s -> HibernateCriteriaBuilder.STRING } + metricNames.collect(i -> HibernateCriteriaBuilder.BIG_DECIMAL)
    }

    private static List<String> prepareColumnList(List<String> dimensions, List<String> metricNames) {
        dimensions + metricNames
    }

    private static String prepareGroupingSQLStatement(List<String> dimensions) {
        dimensions.join(',')
    }

    private static String prepareProjectionSQLStatement(List<String> dimensions, List<String> metricsAggregates) {
        return "${dimensions.join(',')} ${dimensions ? ',' : ''} ${metricsAggregates.join(',')}"
    }

    private static List prepareRestrictionsStatement(QueryCommand queryParams) {
        List<SQLRestriction> restrictions = queryParams.filters.collect {
            new SQLRestriction(field: DIMENSIONS_MAPPING[it.filter].mappedColumn, value: it.value, operator: '=')
        }
        if (queryParams.dateFrom)
            restrictions << new SQLRestriction(field: 'stats_date', value: queryParams.dateFrom, operator: '>=')

        if (queryParams.dateTo)
            restrictions << new SQLRestriction(field: 'stats_date', value: queryParams.dateTo, operator: '<=')

        String restrictionSqlStatement = restrictions.collect { "${it.field}${it.operator}?" }.join(' AND ')
        return [restrictionSqlStatement, restrictions.collect{it.value}]
    }
}
