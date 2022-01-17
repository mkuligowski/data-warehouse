databaseChangeLog = {

    changeSet(author: "mkuligowski (generated)", id: "1642371805740-1") {
        createTable(tableName: "CAMPAIGN") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "CONSTRAINT_2")
            }

            column(name: "VERSION", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "DATASOURCE_ID", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "NAME", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "mkuligowski (generated)", id: "1642371805740-2") {
        createTable(tableName: "CAMPAIGN_STATISTIC") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "CONSTRAINT_8")
            }

            column(name: "VERSION", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "STATS_DATE", type: "date") {
                constraints(nullable: "false")
            }

            column(name: "CLICKS", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "CAMPAIGN_ID", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "IMPRESSIONS", type: "INT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "mkuligowski (generated)", id: "1642371805740-3") {
        createTable(tableName: "DATASOURCE") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "CONSTRAINT_9")
            }

            column(name: "VERSION", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "NAME", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "mkuligowski (generated)", id: "1642371805740-4") {
        createTable(tableName: "SOME_TABLE") {
            column(autoIncrement: "true", name: "ID", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "CONSTRAINT_1")
            }

            column(name: "VERSION", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "CLICKS", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "CITY", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "IMPRESSIONS", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "COUNTRY", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "mkuligowski (generated)", id: "1642371805740-5") {
        createIndex(indexName: "FK6046KVC58J9NBMKPH354AAJDX_INDEX_8", tableName: "CAMPAIGN_STATISTIC") {
            column(name: "CAMPAIGN_ID")
        }
    }

    changeSet(author: "mkuligowski (generated)", id: "1642371805740-6") {
        createIndex(indexName: "FKKFI4V8VE3PKDLLNYELS9X1F8R_INDEX_2", tableName: "CAMPAIGN") {
            column(name: "DATASOURCE_ID")
        }
    }

    changeSet(author: "mkuligowski (generated)", id: "1642371805740-7") {
        addForeignKeyConstraint(baseColumnNames: "CAMPAIGN_ID", baseTableName: "CAMPAIGN_STATISTIC", constraintName: "FK6046KVC58J9NBMKPH354AAJDX", deferrable: "false", initiallyDeferred: "false", onDelete: "RESTRICT", onUpdate: "RESTRICT", referencedColumnNames: "ID", referencedTableName: "CAMPAIGN", validate: "true")
    }

    changeSet(author: "mkuligowski (generated)", id: "1642371805740-8") {
        addForeignKeyConstraint(baseColumnNames: "DATASOURCE_ID", baseTableName: "CAMPAIGN", constraintName: "FKKFI4V8VE3PKDLLNYELS9X1F8R", deferrable: "false", initiallyDeferred: "false", onDelete: "RESTRICT", onUpdate: "RESTRICT", referencedColumnNames: "ID", referencedTableName: "DATASOURCE", validate: "true")
    }

    changeSet(author: "mkuligowski (generated)", id: "1642371805740-9") {
        createView(viewName: "stats_v", selectQuery: "select cs.*, c.name campaign_name, d.name datasource_name from campaign_statistic cs join campaign c on c.id = cs.campaign_id join datasource d on d.id = c.datasource_id")
    }
}
