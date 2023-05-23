#!/usr/bin/env groovy
package org.foo

import groovy.sql.Sql
import groovy.text.SimpleTemplateEngine

@GrabConfig( systemClassLoader=true )
@Grapes(
        @Grab(group='com.mysql', module='mysql-connector-j', version='8.0.33')
)

class AlterSchema {
  def prepareAlterScript() {
    def changeRequestContent = "ALTER TABLE `master_db`.`sy_parameter` \n" +
            "ADD COLUMN `sy_parametercol` VARCHAR(45) NULL AFTER `DB_Version`"
    def dbUrl = 'jdbc:mysql://localhost:3306/master_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
    //DriverManager.registerDriver(new com.mysql.jdbc.Driver())
    sql = Sql.newInstance(dbUrl, 'root', 'Mamata@143123', 'com.mysql.jdbc.Driver')
    def row = sql.firstRow("SELECT DB_Version as oldV FROM master_db.sy_parameter")
    def oldV = row.getProperty("oldV") as BigDecimal
    sql.close()
    def binding = [oldV: oldV, newV: oldV = 0.001, changeRequestContent: changeRequestContent]

    def template = new SimpleTemplateEngine().createTemplate(new File('C:\\Users\\91789\\git\\db-processor\\vars\\alter_schema_template.sql').text)

    def result = template.make(binding).toString()
    println result
  }
}

