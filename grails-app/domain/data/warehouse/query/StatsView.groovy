package data.warehouse.query

import java.time.LocalDate

class StatsView {

    String campaignName
    String datasourceName
    LocalDate statsDate
    Integer clicks
    Integer impressions

    static mapping = {
        table 'stats_v'
    }
}
