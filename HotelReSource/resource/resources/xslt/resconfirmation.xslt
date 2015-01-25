<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fun="java://com.jythonui.server.xslt.XSLTFun"
    xmlns:java="http://xml.apache.org/xslt/java">	
    
    <xsl:import href="lang/var_en.xslt"/>

    <xsl:output method="text" encoding="UTF-8" />
    <xsl:template match="root">
    
<xsl:value-of select="$lang.reservationConfirmation" /> <xsl:text>&#xa;</xsl:text>
<xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.reservationNumber"/> : <xsl:value-of select="elem/resid" /> <xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.bookedOn"/> : <xsl:value-of select="elem/issuedate" /> <xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.Guest"/> : <xsl:value-of select="elem/name1" /> <xsl:value-of select="elem/name2" /><xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.Room"/> : <xsl:value-of select="elem/roomnumber" /> <xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.arrivalDate"/> : <xsl:value-of select="elem/arrivaldate" /> <xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.departureDate"/> : <xsl:value-of select="elem/departuredate" /> <xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.Adults"/>: <xsl:value-of select="elem/nofguests" /> <xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.dailyRate"/> : <xsl:value-of select="elem/dailyrate" /> <xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.Total"/> : <xsl:value-of select="elem/total" /> <xsl:text>&#xa;</xsl:text>
<xsl:text>&#xa;</xsl:text>
<xsl:value-of select="$lang.reservationDetails"/> <xsl:text>&#xa;</xsl:text>
<xsl:value-of select="fun:fillString(50,'-')" /> <xsl:text>&#xa;</xsl:text>
<xsl:value-of select="fun:paddingLeft(10,$lang.Room)" /> 
<xsl:value-of select="fun:paddingLeft(15,$lang.Night)" /> 
<xsl:value-of select="fun:paddingLeft(15,$lang.Total)" /> 
<xsl:text>&#xa;</xsl:text>
<xsl:value-of select="fun:fillString(50,'-')" />
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