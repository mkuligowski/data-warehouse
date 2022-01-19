package com.mkuligwski.datawarehouse.query

import data.warehouse.Campaign
import data.warehouse.CampaignStatistic
import data.warehouse.Datasource
import data.warehouse.StatsLoadingService
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

@Integration
@Rollback
class StatsLoadingServiceSpec extends Specification{

    @Autowired
    StatsLoadingService service

    void setup() {
        Datasource.deleteAll()
        Campaign.deleteAll()
        CampaignStatistic.deleteAll()
    }

    void "should load CSV file with headers"() {
        given:
            def inputCsv =
'''Datasource,Campaign,Daily,Clicks,Impressions
Google Ads,Adventmarkt Touristik,11/12/19,7,22425
Google Ads,Adventmarkt Touristik,11/13/19,16,45452
Google Ads,Adventmarkt Touristik,11/14/19,147,80351
Twitter Ads,DE|SN|Snowboards|Brands,01/03/19,7,243
Twitter Ads,PL|SN|Snowboards|Brands,01/04/19,14,273
Twitter Ads,DE|SN|Snowboards|Brands,01/05/19,9,258'''

            InputStream stream = new ByteArrayInputStream(inputCsv.getBytes());
        when:
            service.loadFromStream(stream)

        then:
            Datasource.count() == 2
            Datasource.findByName('Google Ads') != null
            Datasource.findByName('Twitter Ads') != null

            Campaign.count() == 3
            Campaign c1 = Campaign.findByName('Adventmarkt Touristik')
            c1 != null
            Campaign c2 = Campaign.findByName('DE|SN|Snowboards|Brands')
            c2 != null
            Campaign c3 = Campaign.findByName('PL|SN|Snowboards|Brands')
            c3 != null

            CampaignStatistic.count() == 6
            CampaignStatistic cs1 = CampaignStatistic.findByCampaignAndStatsDate(c1, LocalDate.of(2019, Month.NOVEMBER,12))
            cs1.clicks == 7
            cs1.impressions == 22425

            CampaignStatistic cs2 = CampaignStatistic.findByCampaignAndStatsDate(c1, LocalDate.of(2019, Month.NOVEMBER,13))
            cs2.clicks == 16
            cs2.impressions == 45452

            CampaignStatistic cs3 = CampaignStatistic.findByCampaignAndStatsDate(c1, LocalDate.of(2019, Month.NOVEMBER,14))
            cs3.clicks == 147
            cs3.impressions == 80351

            CampaignStatistic cs4 = CampaignStatistic.findByCampaignAndStatsDate(c2, LocalDate.of(2019, Month.JANUARY,3))
            cs4.clicks == 7
            cs4.impressions == 243

            CampaignStatistic cs5 = CampaignStatistic.findByCampaignAndStatsDate(c3, LocalDate.of(2019, Month.JANUARY,4))
            cs5.clicks == 14
            cs5.impressions == 273

            CampaignStatistic cs6 = CampaignStatistic.findByCampaignAndStatsDate(c2, LocalDate.of(2019, Month.JANUARY,5))
            cs6.clicks == 9
            cs6.impressions == 258
    }

    void "should load CSV file without headers"() {
        given:
        def inputCsv =
                '''Google Ads,Adventmarkt Touristik,11/13/19,16,45452
Twitter Ads,DE|SN|Snowboards|Brands,01/05/19,9,258'''

        InputStream stream = new ByteArrayInputStream(inputCsv.getBytes());
        when:
        service.loadFromStream(stream)

        then:
        Datasource.count() == 2
        Datasource.findByName('Google Ads') != null
        Datasource.findByName('Twitter Ads') != null

        Campaign.count() == 2
        Campaign c1 = Campaign.findByName('Adventmarkt Touristik')
        c1 != null
        Campaign c2 = Campaign.findByName('DE|SN|Snowboards|Brands')
        c2 != null

        CampaignStatistic.count() == 2
        CampaignStatistic cs1 = CampaignStatistic.findByCampaignAndStatsDate(c1, LocalDate.of(2019, Month.NOVEMBER,13))
        cs1.clicks == 16
        cs1.impressions == 45452

        CampaignStatistic cs2 = CampaignStatistic.findByCampaignAndStatsDate(c2, LocalDate.of(2019, Month.JANUARY,5))
        cs2.clicks == 9
        cs2.impressions == 258
    }


    void "should not load statistics for same campaign in the same day"() {
        given:
        def inputCsv =
                '''Google Ads,Adventmarkt Touristik,11/13/19,16,45452
Google Ads,Adventmarkt Touristik,11/13/19,22,11111'''

        InputStream stream = new ByteArrayInputStream(inputCsv.getBytes());
        when:
        service.loadFromStream(stream)

        then:
            thrown DuplicateKeyException
    }
}
