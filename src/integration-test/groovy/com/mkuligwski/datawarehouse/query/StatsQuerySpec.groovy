package com.mkuligwski.datawarehouse.query

import com.mkuligowski.datawarehouse.query.StatsQueryService
import data.warehouse.Campaign
import data.warehouse.CampaignStatistic
import data.warehouse.Datasource
import data.warehouse.Dimension
import data.warehouse.Filter
import data.warehouse.Metric
import data.warehouse.QueryCommand
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import java.time.LocalDate
import java.time.Month

@Integration
@Rollback
class StatsQuerySpec extends Specification {

    @Autowired
    StatsQueryService statsQueryService
    Campaign someGoogleCampaign
    Campaign someTwitterCampaign
    Campaign someOtherTwitterCampaign

    def setup() {
        Datasource googleDs = new Datasource(name: 'Google Ads').save(flush: true)
        Datasource twitterDs = new Datasource(name: 'Twitter Ads').save(flush: true)
        someGoogleCampaign = new Campaign(name: 'Google campaign', datasource: googleDs).save(flush: true)
        someTwitterCampaign = new Campaign(name: 'Twitter campaign', datasource: twitterDs).save(flush: true)
        someOtherTwitterCampaign = new Campaign(name: 'Other Twitter campaign', datasource: twitterDs).save(flush: true)
    }

    void "should query without any dimension"() {
        given:
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        when:
        QueryCommand query = new QueryCommand()
        query.metrics = [Metric.CLICKS]
        def result = statsQueryService.query(query)
        then:
        result.headers == ['clicks']
        result.rows.size() == 1
        result.rows[0] == 30 // TODO: check this out
    }

    void "should query with one dimension"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS]
            query.dimensions = [Dimension.DATASOURCE]
            def result = statsQueryService.query(query)
        then:
            result.headers == ['datasource_name', 'clicks']
            result.rows.size() == 2
            result.rows[0] == ['Google Ads',20]
            result.rows[1] == ['Twitter Ads',10]
    }

    void "should query with many dimensions"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 5, impressions: 15, statsDate: LocalDate.now()).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS]
            query.dimensions = [Dimension.DATASOURCE, Dimension.CAMPAIGN]
            def result = statsQueryService.query(query)
        then:
            result.headers == ['datasource_name', 'campaign_name','clicks']
            result.rows.size() == 3
            result.rows[0] == ['Google Ads', 'Google campaign', 20]
            result.rows[1] == ['Twitter Ads', 'Other Twitter campaign', 5]
            result.rows[2] == ['Twitter Ads', 'Twitter campaign', 10]
    }

    void "should calculate clicks without dimensions"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS]
        def result = statsQueryService.query(query)
        then:
            result.headers == ['clicks']
            result.rows.size() == 1
            result.rows[0] == 20
    }

    void "should calculate clicks with datasource dimensions"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 220, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someTwitterCampaign, clicks: 300, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS]
            query.dimensions = [Dimension.DATASOURCE]
            def result = statsQueryService.query(query)
        then:
            result.headers == ['datasource_name', 'clicks']
            result.rows.size() == 2
            result.rows[0] == ['Google Ads', 230]
            result.rows[1] == ['Twitter Ads', 300]
    }

    void "should calculate clicks with campaign dimensions"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 5, impressions: 15, statsDate: LocalDate.now()).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS]
            query.dimensions = [Dimension.CAMPAIGN]
            def result = statsQueryService.query(query)
        then:
            result.headers == ['campaign_name','clicks']
            result.rows.size() == 3
            result.rows[0] == ['Google campaign', 20]
            result.rows[1] == ['Other Twitter campaign', 5]
            result.rows[2] == ['Twitter campaign', 10]
    }

    void "should calculate clicks with datasource and campaign dimensions"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 5, impressions: 15, statsDate: LocalDate.now()).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS]
            query.dimensions = [Dimension.DATASOURCE, Dimension.CAMPAIGN]
        def result = statsQueryService.query(query)
        then:
            result.headers == ['datasource_name', 'campaign_name','clicks']
            result.rows.size() == 3
            result.rows[0] == ['Google Ads', 'Google campaign', 20]
            result.rows[1] == ['Twitter Ads', 'Other Twitter campaign', 5]
            result.rows[2] == ['Twitter Ads', 'Twitter campaign', 10]
    }

    void "should calculate impressions without dimensions"() {
        given:
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        when:
        QueryCommand query = new QueryCommand()
        query.metrics = [Metric.IMPRESSIONS]
        def result = statsQueryService.query(query)
        then:
        result.headers == ['impressions']
        result.rows.size() == 1
        result.rows[0] == 40
    }

    void "should calculate impressions with datasource dimensions"() {
        given:
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 500, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 220, impressions: 2000, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someTwitterCampaign, clicks: 300, impressions: 2000, statsDate: LocalDate.now()).save(flush: true)
        when:
        QueryCommand query = new QueryCommand()
        query.metrics = [Metric.IMPRESSIONS]
        query.dimensions = [Dimension.DATASOURCE]
        def result = statsQueryService.query(query)
        then:
        result.headers == ['datasource_name', 'impressions']
        result.rows.size() == 2
        result.rows[0] == ['Google Ads', 2500]
        result.rows[1] == ['Twitter Ads', 2000]
    }

    void "should calculate impressions with campaign dimensions"() {
        given:
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 5, impressions: 15, statsDate: LocalDate.now()).save(flush: true)
        when:
        QueryCommand query = new QueryCommand()
        query.metrics = [Metric.IMPRESSIONS]
        query.dimensions = [Dimension.CAMPAIGN]
        def result = statsQueryService.query(query)
        then:
        result.headers == ['campaign_name','impressions']
        result.rows.size() == 3
        result.rows[0] == ['Google campaign', 40]
        result.rows[1] == ['Other Twitter campaign', 15]
        result.rows[2] == ['Twitter campaign', 20]
    }

    void "should calculate impressions with datasource and campaign dimensions"() {
        given:
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 5, impressions: 15, statsDate: LocalDate.now()).save(flush: true)
        when:
        QueryCommand query = new QueryCommand()
        query.metrics = [Metric.IMPRESSIONS]
        query.dimensions = [Dimension.DATASOURCE, Dimension.CAMPAIGN]
        def result = statsQueryService.query(query)
        then:
        result.headers == ['datasource_name', 'campaign_name','impressions']
        result.rows.size() == 3
        result.rows[0] == ['Google Ads', 'Google campaign', 40]
        result.rows[1] == ['Twitter Ads', 'Other Twitter campaign', 15]
        result.rows[2] == ['Twitter Ads', 'Twitter campaign', 20]
    }



    void "should calculate ctr with datasource and campaign dimensions"() {
        given:
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 60, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 5, impressions: 50, statsDate: LocalDate.now()).save(flush: true)
        when:
        QueryCommand query = new QueryCommand()
        query.metrics = [Metric.CTR]
        query.dimensions = [Dimension.DATASOURCE, Dimension.CAMPAIGN]
        def result = statsQueryService.query(query)
        then:
        result.headers == ['datasource_name', 'campaign_name','ctr']
        result.rows.size() == 3
        result.rows[0] == ['Google Ads', 'Google campaign', new BigDecimal(0.25)]
        result.rows[1] == ['Twitter Ads', 'Other Twitter campaign', new BigDecimal(0.1)]
        result.rows[2] == ['Twitter Ads', 'Twitter campaign', new BigDecimal(0.5)]
    }


    void "should aggregate many metrics by one dimensions"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 60, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 5, impressions: 50, statsDate: LocalDate.now()).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS, Metric.IMPRESSIONS]
            query.dimensions = [Dimension.CAMPAIGN]
            def result = statsQueryService.query(query)
        then:
            result.headers == ['campaign_name','clicks','impressions']
            result.rows.size() == 3
            result.rows[0] == ['Google campaign', 20, 80]
            result.rows[1] == ['Other Twitter campaign', 5, 50]
            result.rows[2] == ['Twitter campaign', 10, 20]
    }

    void "should filter with dateFrom"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 1)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 2)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 3)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 4)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 5)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 6)).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS, Metric.IMPRESSIONS]
            query.dimensions = [Dimension.CAMPAIGN]
            query.dateFrom = LocalDate.of(2022, Month.JANUARY, 2)
            def result = statsQueryService.query(query)
        then:
            result.headers == ['campaign_name','clicks','impressions']
            result.rows.size() == 1
            result.rows[0] == ['Google campaign', 50, 100]
    }

    void "should filter with dateTo"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 1)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 2)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 3)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 4)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 5)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 6)).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS, Metric.IMPRESSIONS]
            query.dimensions = [Dimension.CAMPAIGN]
            query.dateTo = LocalDate.of(2022, Month.JANUARY, 2)
            def result = statsQueryService.query(query)
        then:
            result.headers == ['campaign_name','clicks','impressions']
            result.rows.size() == 1
            result.rows[0] == ['Google campaign', 20, 40]
    }

    void "should filter with dateFrom and dateTo"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 1)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 2)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 3)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 4)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 5)).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.of(2022, Month.JANUARY, 6)).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS, Metric.IMPRESSIONS]
            query.dimensions = [Dimension.CAMPAIGN]
            query.dateFrom = LocalDate.of(2022, Month.JANUARY, 2)
            query.dateTo = LocalDate.of(2022, Month.JANUARY, 3)
            def result = statsQueryService.query(query)
        then:
            result.headers == ['campaign_name','clicks','impressions']
            result.rows.size() == 1
            result.rows[0] == ['Google campaign', 20, 40]
    }

    void "should filter with filters"() {
        given:
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 60, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
            new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 5, impressions: 50, statsDate: LocalDate.now()).save(flush: true)
        when:
            QueryCommand query = new QueryCommand()
            query.metrics = [Metric.CLICKS, Metric.IMPRESSIONS]
            query.dimensions = [Dimension.CAMPAIGN]
            query.filters = [new Filter(filter: Dimension.CAMPAIGN, value: 'Other Twitter campaign')]
            def result = statsQueryService.query(query)
        then:
            result.headers == ['campaign_name','clicks','impressions']
            result.rows.size() == 1
            result.rows[0] == ['Other Twitter campaign', 5, 50]
    }

    void "should combine filters"() {
        given:
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someGoogleCampaign, clicks: 10, impressions: 60, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someTwitterCampaign, clicks: 10, impressions: 20, statsDate: LocalDate.now()).save(flush: true)
        new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 5, impressions: 50, statsDate: LocalDate.of(2022, Month.JANUARY, 1)).save(flush: true)
        new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 7, impressions: 57, statsDate: LocalDate.of(2022, Month.JANUARY, 2)).save(flush: true)
        new CampaignStatistic(campaign: someOtherTwitterCampaign, clicks: 3, impressions: 13, statsDate: LocalDate.of(2022, Month.JANUARY, 3)).save(flush: true)
        when:
        QueryCommand query = new QueryCommand()
        query.metrics = [Metric.CLICKS, Metric.IMPRESSIONS]
        query.dimensions = [Dimension.CAMPAIGN]
        query.filters = [new Filter(filter: Dimension.CAMPAIGN, value: 'Other Twitter campaign')]
        query.dateFrom = LocalDate.of(2022, Month.JANUARY, 2)
        def result = statsQueryService.query(query)
        then:
        result.headers == ['campaign_name','clicks','impressions']
        result.rows.size() == 1
        result.rows[0] == ['Other Twitter campaign', 10, 70]
    }

    void "should not calculate without any metric"() {
        when:
        QueryCommand query = new QueryCommand()
        query.metrics = []
        query.dimensions = [Dimension.CAMPAIGN]
        query.dateFrom = LocalDate.of(2022, Month.APRIL, 30)
        def result = statsQueryService.query(query)
        then:
        result.headers == ['campaign_name']
        result.rows.isEmpty()
    }
}
