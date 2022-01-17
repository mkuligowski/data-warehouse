package com.mkuligowski.dw

import data.warehouse.Campaign
import data.warehouse.CampaignStatistic
import data.warehouse.Datasource

import javax.transaction.Transactional
import java.time.LocalDate

class DataLoader {

    @Transactional
    def loadFile(InputStream is) {

        CampaignCache campaignCache = new CampaignCache(Campaign.findAll().groupBy {it.name})

        def lineNumber = 1
        is.splitEachLine(','){ csvLine ->
            println('PROCESSING ' + lineNumber)
            if (lineNumber++ == 1)
                return

            if (lineNumber > 1){
                String datasource = csvLine[0]
                String campaign = csvLine[1]
                String date = csvLine[2]
                String clicks = csvLine[3]
                String impressions = csvLine[4]
                if (campaignCache.get(datasource, campaign)){
                    println("Using cached ${campaign}")

                    Campaign c = campaignCache.get(datasource, campaign)
                    new CampaignStatistic(campaign: c,
                            statsDate: LocalDate.parse(date, 'M/d/yy') ,
                            clicks: clicks as Integer,
                            impressions: impressions as Integer).save()
                }else {
                    println("Saving new ${campaign}")
                    Datasource ds = new Datasource(name: datasource).save()
                    Campaign c = new Campaign(datasource: ds, name: campaign).save()
                    campaignCache.put(c)

                    new CampaignStatistic(campaign: c,
                            statsDate: LocalDate.parse(date, 'M/d/yy') ,
                            clicks: clicks as Integer,
                            impressions: impressions as Integer).save()
                }
            }

        }
    }
}
