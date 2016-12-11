
# Purpose

This is just a quick demo of running sql joins between elasticsearch and mysql using spark.

# Running

    brew update
    brew install elasticsearch mysql
    elasticsearch &
    mysql.server start &
    
    git clone https://github.com/datacharmer/test_db/
    mysql -u root < test_db/employees.sql
    
    export SBT_OPTS="-Xmx6G -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled "
    sbt run

:)

# TODO

move mysql jdbc dependency into build.sbt and out of lib
