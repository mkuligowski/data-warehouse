package data.warehouse

enum Metric {
    CLICKS('clicks','sum(clicks) as clicks'),
    IMPRESSIONS('impressions','sum(impressions) as impressions'),
    CTR('ctr','sum(clicks) * 1.0 / sum(impressions) as ctr')

    Metric(String mappedColumn, String expression) {
        this.mappedColumn = mappedColumn
        this.expression = expression
    }

    private String mappedColumn
    private String expression

    String getMappedColumn() {
        return mappedColumn
    }

    String getExpression() {
        return expression
    }

}