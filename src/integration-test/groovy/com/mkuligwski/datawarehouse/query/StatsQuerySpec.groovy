package com.mkuligwski.datawarehouse.query

import data.warehouse.Campaign
import data.warehouse.CampaignStatistic
import data.warehouse.Datasource
import data.warehouse.StatsView
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.*
import spock.lang.Specification

import java.time.LocalDate

@Integration
@Rollback
class StatsQuerySpec extends Specification {

    def setup() {
        Datasource googleDs = new Datasource(name: 'Google Ads').save(flush: true)
        Datasource twitterDs = new Datasource(name: 'Twitter Ads').save(flush: true)

        Campaign someGoogleCampaign = new Campaign(name: 'Google campaign', datasource: googleDs).save(flush: true)
        Campaign someTwitterCampaign = new Campaign(name: 'Twitter campaign', datasource: twitterDs).save(flush: true)

        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
    }

    def cleanup() {
    }

    void "test something"() {
        expect:
        StatsView.findAll().size() == 3
    }
}
