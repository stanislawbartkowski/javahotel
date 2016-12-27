 git clone -b bsqlhbase https://github.com/stanislawbartkowski/javahotel.git

Javadocs: https://stanislawbartkowski.github.io/javadoc/bsqlhbase/HBaseBigSql

HBaseBigSql:  https://stanislawbartkowski.github.io/javadoc/bsqlhbase/HBaseBigSql

HBBasePutGet:  https://stanislawbartkowski.github.io/javadoc/bsqlhbase/HBasePutGet/

# How to create Eclipse project

Prerequisities
* Eclipse, tested with the latest Eclipse Neon
* Ivy plugin : http://www.apache.org/dist/ant/ivyde/updatesite

# Create Eclipse project
9. Import -> Git -> Projects from Git
9. Clone URI: https://github.com/stanislawbartkowski/javahotel.git , **bsqlhbase** branch only, unselect others
9. Local destination -> Browse to workspace directory/javahotel
9. Import using the New Project Wizard -> Project name: javahotel (javahotel is a placeholder for Git clone)

# Import Eclipse projects

Import three projects from javahotel/eprojects directory: hbasebigsql,hbaseputget and testh

![](https://github.com/stanislawbartkowski/javahotel/blob/bsqlhbase/wiki/Zrzut%20ekranu%20z%202016-11-23%2011:44:10.png)

To run the tests, copy your own or modify existing testh/etcb/hase-site.xml

# Enjoy

