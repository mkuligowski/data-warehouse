package data.warehouse

import java.time.LocalDate

class CampaignStatistic {

    Campaign campaign
    LocalDate statsDate
    Integer clicks
    Integer impressions

    static constraints = {
        campaign nullable: false
        statsDate nullable: false
        clicks nullable: false
        impressions nullable: false
    }
}
