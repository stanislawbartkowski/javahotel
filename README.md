 git clone -b bsqlhbase https://github.com/stanislawbartkowski/javahotel.git

Javadoc : https://stanislawbartkowski.github.io/javadoc/bsqlhbase/HBaseBigSql

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

# Prepare jar

Run ant command in javahotel directory

Target: dist/bigsqlhbase.jar 

# Enjoy


# IBM InfoSphere Projects

## Import Eclipse projects into IBM StreamsStudio

### Clone GitHub branch

git clone -b bsqlhbase https://github.com/stanislawbartkowski/javahotel.git

### Import projects

Import two projects from javahotel/Streams directory.

The following projects should be accessible

![](https://github.com/stanislawbartkowski/javahotel/blob/bsqlhbase/wiki/Zrzut%20ekranu%20z%202016-12-28%2023-42-14.png)

### org.ibm.converthbase

Provides single operator JConverter.

JConverter accepts one or more input streams and the same number of output streams. Attributes from input streams are encoded to blob (binary) using format readable by BigSql/HBase.

The number of attributes in input stream(s) should correspond to the number of attributes in output stream(s). All attributes in output streams should be of __blob__ type.

JConverter output stream can be consumed directly by [HBasePut](http://www.ibm.com/support/knowledgecenter/SSCRJU_4.0.1/com.ibm.streams.toolkits.doc/doc/tk$com.ibm.streamsx.hbase/op$com.ibm.streamsx.hbase$HBASEPut.html) operator.

An example:

<pre>
  Input schema
  &lt; int32 id, rstring line, decimal32 balance &gt;
  
  Output schema
  &lt; blob id, blob line, blob balance &gt;
</pre>

The attributes are encoded to blob (binary) by position, not by name, from left to right.

JCoverter operator accepts also nested tuple type. It also should be mirrored in nested tuple in output stream
<pre>
  Input schema
  &lt; int32 id, &lt; rstring line, decimal32 balance &gt; data &gt;
  
  Output schema
  &lt; blob id, &lt; blob line, blob balance &gt; data &gt;

</pre>

### Conversion rules

Stream input attribute types should be reflected in target BigSql/HBase table

| Streams type  | BigSql column type |
| ------------- |:-------------:|
|  boolean      | BOOLEAN 
| int64 uint64  | BIGINT 
| int16 int32 uint16 uint32 | INT 
| int8 uint8 | TINYINT
| rstring | VARCHAR
| float32 | FLOAT
| float64 | DOUBLE
| timestamp | TIMESTAMP
| decimal32 decimal64 decimal128 | DECIMAL

An example

<pre>
Input schema 
&lt; rstring key, int32 val &gt;

Output schema

&lt; blob key, blob val &gt;

BigSql/HBase table

HBasePut operator

		() as HBASEPut_3 = HBASEPut(JConvert_4_out0)
		{
			param
				hbaseSite : dataDirectory() + "/conf/hbase-site.xml" ;
				rowAttrName : "key" ;
				tableName : "sb.testint" ;
				staticColumnFamily : "cf_data" ;
				staticColumnQualifier : "cq_x" ;
				valueAttrName : "val" ;
		}


CREATE HBASE TABLE TESTINT ( K VARCHAR(100), V INT ) COLUMN MAPPING ( key  mapped by (k), cf_data: cq_x mapped by (v));

</pre>

## TestHBaseN project

Contains a good number of tests and usage examples.

All input data are stored in data/test1 ... data/testn directories and corresponding MainTest{N} main composite.

In order to run the example one has to create data/conf/hbase-site.xml property file with connection parameters to BigInsights cluster.

The MainSalesData composite and data/salesdata implements an example described in http://www.ibm.com/developerworks/library/bd-bigsqlhbase1/ article. The data/salesdata directory should contain SLS_SALES_FACT.10p.txt extracted from IBD-1687A_Data.zip sample data file.


