<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fun="java://com.jythonui.server.xslt.XSLTFun"
    xmlns:java="http://xml.apache.org/xslt/java">	
-   <xsl:output method="text" encoding="UTF-8" />  
	<xsl:template match="root">
New Reservation
Reservation Id: <xsl:value-of select="elem/resid" />
Booked on:<xsl:value-of select="elem/resdate" />
Guest: <xsl:value-of select="elem/name1" /> <xsl:value-of select="elem/name2" />
Room : <xsl:value-of select="elem/roomnumber" />
Check-in : <xsl:value-of select="elem/arrivaldate" />
Check-out : <xsl:value-of select="elem/departuredate" />
Adults: <xsl:value-of select="elem/nofguests" />
Daily rate : <xsl:value-of select="elem/dailyrate" />
Total : <xsl:value-of select="elem/total" />
<xsl:text>&#xa;</xsl:text>
<xsl:value-of select="fun:fillString(40,'-')" />
<xsl:text>&#xa;</xsl:text>
Reservation details
<xsl:value-of select="fun:paddingLeft(10,'Room')" /> 
<xsl:value-of select="fun:paddingLeft(15,'Date')" /> 
<xsl:value-of select="fun:paddingLeft(15,'Total')" /> 
<xsl:text>&#xa;</xsl:text>
<xsl:value-of select="fun:fillString(40,'-')" />
<xsl:text>&#xa;</xsl:text>
		<xsl:for-each select="/root/list/elem">
            <xsl:text>&#xa;</xsl:text>
			<xsl:variable name="roomnumber" select="roomnumber" />
			<xsl:variable name="date" select="date" />
			<xsl:variable name="total" select="total" />
			<xsl:value-of select="fun:paddingLeft(10,$roomnumber)" />
			<xsl:value-of select="fun:paddingLeft(15,$date)" />
			<xsl:value-of select="fun:paddingLeft(15,$total)" />
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>  