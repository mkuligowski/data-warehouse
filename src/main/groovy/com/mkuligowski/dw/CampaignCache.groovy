package com.mkuligowski.dw

import data.warehouse.Campaign

class CampaignCache {

    Map<String, List<Campaign>> cachedCampaigns = [:]

    CampaignCache(Map<String, List<Campaign>> cachedCampaigns) {
        this.cachedCampaigns = cachedCampaigns
    }

    def put(Campaign campaign) {
        this.cachedCampaigns.merge(campaign.name, [campaign], (a,b) -> a+b)
    }

    def get(String dataSource, String campaign) {
        cachedCampaigns.get(campaign).find{
            it.datasource.name == dataSource
        }
    }

}
