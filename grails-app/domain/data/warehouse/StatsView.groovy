package data.warehouse

import java.time.LocalDate

class StatsView {

    String campaignName
    String datasourceName
    LocalDate statsDate
    Integer clicks
    Integer impressions

    static constraints = {
    }

    static mapping = {
        table 'stats_v'
    }
}
