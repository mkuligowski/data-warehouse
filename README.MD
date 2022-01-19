# Data warehouse

Simple Data Warehouse (extract, transform, load, query) using Grails 5.1.1
## Usage

app exposes one endpoint for querying statistics:

```
HTTP POST -> /campaign-statistics
```

### Parameters
* metrics [REQUIRED] - an array of the metrics you want to get, possible values: CLICKS, CTR, IMPRESSIONS
* dimensions [OPTIONAL] - the dimensions you want to group on, possible values DATASOURCE, CAMPAIGN
* filters [OPTIONAL] - needed to filter by dimensions. Arrays of objects with filter and value attributes
* dateFrom [OPTIONAL] - dateFrom filter
* dateTo [OPTIONAL] - dateTo filter
### examples of usage

```
request:
{
    "metrics":["CLICKS","CTR", "IMPRESSIONS"],
    "dimensions":["DATASOURCE"],
    "filters":[{"filter":"DATASOURCE", "value":"Twitter Ads"}],
    "dateFrom":"2020-01-12",
    "dateTo":"2020-03-12"
}

response

{
    "headers": [
        "datasource_name",
        "clicks",
        "ctr",
        "impressions"
    ],
    "rows": [
        [
            "Twitter Ads",
            20994,
            0.14942,
            140502
        ]
    ]
}
```

```
request:
{
    "metrics":["CLICKS","CTR"],
    "dimensions":["DATASOURCE","CAMPAIGN"],
    "filters":[{"filter":"DATASOURCE", "value":"Twitter Ads"}]
}

response
{
    "headers": [
        "datasource_name",
        "campaign_name",
        "clicks",
        "ctr"
    ],
    "rows": [
        [
            "Twitter Ads",
            "AT | PLA",
            1088,
            0.01921
        ],
        [
            "Twitter Ads",
            "AT|SN|Brand",
            4861,
            0.41847
        ],
        [
            "Twitter Ads",
            "AT|SN|Longboard|Generisch",
            201,
            0.08831
        ],
        [
            "Twitter Ads",
            "AT|SN|Skate|Generisch",
            195,
            0.03637
        ],
        [
            "Twitter Ads",
            "AT|SN|Skullcandy",
            105,
            0.05597
        ],
        [
            "Twitter Ads",
            "AT|SN|Snow Bindungen|Brands",
            7,
            0.04217
        ],
        [
            "Twitter Ads",
            "AT|SN|Snow Helme|Brands",
            2,
            0.02703
        ],
        [
            "Twitter Ads",
            "AT|SN|Snow Hosen|Brands",
            3,
            0.09091
        ],
        [
            "Twitter Ads",
            "AT|SN|Snow Jacken|Brands",
            6,
            0.09677
        ],
        [
            "Twitter Ads",
            "AT|SN|Snow Schuhe|Brands",
            6,
            0.02174
        ],
        [
            "Twitter Ads",
            "AT|SN|Snowboards|Brands",
            190,
            0.05855
        ],
        [
            "Twitter Ads",
            "AT|SN|Snowboard|Generisch",
            1401,
            0.08629
        ],
        [
            "Twitter Ads",
            "AT|SN|Splitboard|Brands",
            71,
            0.02741
        ],
        [
            "Twitter Ads",
            "Adventmarkt Touristik",
            3298,
            0.01255
        ],
        [
            "Twitter Ads",
            "Bonus Kasko",
            302,
            0.03204
        ],
        [
            "Twitter Ads",
            "Carfinder",
            1028,
            0.03050
        ],
        [
            "Twitter Ads",
            "DE | PLA",
            32053,
            0.01917
        ],
        [
            "Twitter Ads",
            "DE | SP Gadgets",
            7250,
            0.06032
        ],
        [
            "Twitter Ads",
            "DE|SN|Brand",
            2484,
            0.40227
        ],
        [
            "Twitter Ads",
            "DE|SN|Longboard|Generisch",
            960,
            0.08172
        ],
        [
            "Twitter Ads",
            "DE|SN|Skate|Generisch",
            1616,
            0.05241
        ],
        [
            "Twitter Ads",
            "DE|SN|Skullcandy",
            200,
            0.07524
        ],
        [
            "Twitter Ads",
            "DE|SN|Snow Bindungen|Brands",
            14,
            0.01918
        ],
        [
            "Twitter Ads",
            "DE|SN|Snow Helme|Brands",
            33,
            0.10927
        ],
        [
            "Twitter Ads",
            "DE|SN|Snow Hosen|Brands",
            3,
            0.01840
        ],
        [
            "Twitter Ads",
            "DE|SN|Snow Jacken",
            27,
            0.05114
        ],
        [
            "Twitter Ads",
            "DE|SN|Snow Schuhe|Brands",
            22,
            0.01677
        ],
        [
            "Twitter Ads",
            "DE|SN|Snowboards|Brands",
            503,
            0.04013
        ],
        [
            "Twitter Ads",
            "DE|SN|Snowboard|Generisch",
            3249,
            0.07963
        ],
        [
            "Twitter Ads",
            "DE|SN|Splitboard|Brands",
            0,
            0.00000
        ],
        [
            "Twitter Ads",
            "DE|SN|Wear|Brands",
            240,
            0.08239
        ],
        [
            "Twitter Ads",
            "EN | SP Gadgets",
            2578,
            0.03916
        ],
        [
            "Twitter Ads",
            "Freundschaftswerbung",
            6853,
            0.07674
        ],
        [
            "Twitter Ads",
            "Führerschein-Test",
            24724,
            0.07891
        ],
        [
            "Twitter Ads",
            "Google Shopping",
            13061,
            0.03267
        ],
        [
            "Twitter Ads",
            "Mitgliedschaft KiMi",
            21712,
            0.04147
        ],
        [
            "Twitter Ads",
            "Motorrad Mitgliedschaft",
            1633,
            0.02193
        ],
        [
            "Twitter Ads",
            "NT ÖCC",
            244,
            0.09156
        ],
        [
            "Twitter Ads",
            "Nothilfe",
            8364,
            0.05174
        ],
        [
            "Twitter Ads",
            "Nothilfe|Smartphones Android",
            30,
            0.05263
        ],
        [
            "Twitter Ads",
            "Nothilfe|Smartphones iOS",
            25,
            0.04817
        ],
        [
            "Twitter Ads",
            "Nothilfe|Tablets Android",
            15,
            0.06073
        ],
        [
            "Twitter Ads",
            "Nothilfe|Tablets iOS",
            68,
            0.05965
        ],
        [
            "Twitter Ads",
            "Nulltarif",
            42742,
            0.07655
        ],
        [
            "Twitter Ads",
            "Nulltarif-Q4",
            170777,
            0.11676
        ],
        [
            "Twitter Ads",
            "Online-Special_Trendwizzard Fahrrad",
            239,
            0.08066
        ],
        [
            "Twitter Ads",
            "Pickerl-Erinnerung",
            22169,
            0.04019
        ],
        [
            "Twitter Ads",
            "Rechtsschutz",
            5010,
            0.02854
        ],
        [
            "Twitter Ads",
            "Rechtsschutz - MOBIL",
            278,
            0.05036
        ],
        [
            "Twitter Ads",
            "SN_Brand",
            1272346,
            0.45573
        ],
        [
            "Twitter Ads",
            "SN_Brand Produkte",
            22503,
            0.41177
        ],
        [
            "Twitter Ads",
            "SN_Essen",
            6454,
            0.03526
        ],
        [
            "Twitter Ads",
            "SN_Hüttengaudi_Felicity Filztaschen",
            840,
            0.05657
        ],
        [
            "Twitter Ads",
            "SN_Kistenkonkurrenz",
            86,
            0.01531
        ],
        [
            "Twitter Ads",
            "SN_KochAbo",
            77631,
            0.48248
        ],
        [
            "Twitter Ads",
            "SN_Lebensmittel",
            5033,
            0.06322
        ],
        [
            "Twitter Ads",
            "SN_Obstbox",
            550,
            0.11846
        ],
        [
            "Twitter Ads",
            "SN_Online-Only_Kochtopfset",
            529,
            0.12325
        ],
        [
            "Twitter Ads",
            "SN_Online-Special_Dyson Staubsauger",
            191,
            0.05725
        ],
        [
            "Twitter Ads",
            "SN_Online-Special_Esprit Buggy",
            277,
            0.10290
        ],
        [
            "Twitter Ads",
            "SN_Online-Special_LG Fernseher",
            2,
            0.06897
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_AEG_Staubsauger",
            109,
            0.05044
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Gaggia_Espressosiebträger",
            348,
            0.02531
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Gorenje Mikrowelle",
            26,
            0.13472
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Kaffeepadmaschine",
            105,
            0.06418
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Krups Toaster",
            867,
            0.08765
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Krups Wasserkocher",
            290,
            0.03618
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_LG Heimkinoanlage",
            380,
            0.06987
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Leuchtenwald",
            852,
            0.06809
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Philips Küchenmaschine",
            1047,
            0.06622
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Philips Schallzahnbürste",
            1757,
            0.02477
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Philips_Airfryer",
            3594,
            0.04385
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Philips_Tischgrill",
            114,
            0.04536
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Saeco_Kaffeevollautomat",
            383,
            0.03723
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Tefal Raclette-Grill",
            38,
            0.03100
        ],
        [
            "Twitter Ads",
            "SN_OnlineSchnäppchen_Trendline Kaltschaummatratze",
            274,
            0.07546
        ],
        [
            "Twitter Ads",
            "SN_Partner",
            268,
            0.05160
        ],
        [
            "Twitter Ads",
            "SN_Specials",
            270,
            0.01918
        ],
        [
            "Twitter Ads",
            "SN_Wettbewerber",
            1009,
            0.01038
        ],
        [
            "Twitter Ads",
            "Schutzbrief",
            1909,
            0.05970
        ],
        [
            "Twitter Ads",
            "Schutzbrief Image|SN",
            32356,
            0.11442
        ],
        [
            "Twitter Ads",
            "Touring-Set",
            1022,
            0.02061
        ],
        [
            "Twitter Ads",
            "Touristik City Guide",
            60537,
            0.03697
        ],
        [
            "Twitter Ads",
            "Touristik City Guide_Android",
            2059,
            0.03109
        ],
        [
            "Twitter Ads",
            "Touristik City Guide_iOS",
            16,
            0.00772
        ],
        [
            "Twitter Ads",
            "Touristik Routenplaner",
            661060,
            0.19466
        ],
        [
            "Twitter Ads",
            "US|SN|Amazon",
            11031,
            0.01353
        ],
        [
            "Twitter Ads",
            "Versicherungen",
            13948,
            0.05945
        ],
        [
            "Twitter Ads",
            "Vorteilspartner",
            27,
            0.00244
        ],
        [
            "Twitter Ads",
            "WRKS",
            22234,
            0.08751
        ],
        [
            "Twitter Ads",
            "ÖAMTC ADAM-MANIA Gewinnspiel",
            194,
            0.02221
        ],
        [
            "Twitter Ads",
            "ÖAMTC App",
            19926,
            0.09864
        ],
        [
            "Twitter Ads",
            "ÖAMTC App|Smartphones Android",
            8831,
            0.11219
        ],
        [
            "Twitter Ads",
            "ÖAMTC App|Smartphones iOS",
            2547,
            0.11848
        ],
        [
            "Twitter Ads",
            "ÖAMTC App|Tablets Android",
            10694,
            0.10477
        ],
        [
            "Twitter Ads",
            "ÖAMTC App|Tablets iOS",
            1104,
            0.11632
        ],
        [
            "Twitter Ads",
            "ÖAMTC Autopreis Marcus 2014",
            1986,
            0.05470
        ],
        [
            "Twitter Ads",
            "ÖCC",
            13277,
            0.04771
        ]
    ]
}
```

## License
[MIT](https://choosealicense.com/licenses/mit/)