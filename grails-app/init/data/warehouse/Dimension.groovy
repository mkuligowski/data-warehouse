package data.warehouse

enum Dimension {
    DATASOURCE('datasource_name'), CAMPAIGN('campaign_name')

    private String mappedColumn

    String getMappedColumn() {
        return mappedColumn
    }

    Dimension(String mappedColumn) {
        this.mappedColumn = mappedColumn
    }
}