package com.mkuligowski.dw

import data.warehouse.Campaign
import data.warehouse.Datasource
import spock.lang.Specification

class CampaignCacheTest extends Specification {

    def "should add entry to cache"() {
        given:
        CampaignCache campaignCache = new CampaignCache([:])
        Datasource ds = new Datasource(id: 1, name: 'Google Ads')
        Campaign c = new Campaign(id: 1, name: 'some campaign', datasource: ds)

        when:
        campaignCache.put(c)

        then:
        campaignCache.get('Google Ads', 'some campaign') == c
    }

    def "should distinct campaign also by data source name"() {
        given:
        CampaignCache campaignCache = new CampaignCache([:])
        Datasource ds = new Datasource(id: 1, name: 'Google Ads')
        Campaign c = new Campaign(id: 1, name: 'some campaign', datasource: ds)

        when:
        campaignCache.put(c)

        then:
        campaignCache.get('FB Ads', 'some campaign') == null
        campaignCache.get('Google Ads', 'some campaign') == null
    }

}
