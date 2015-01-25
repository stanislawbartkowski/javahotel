<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fun="java://com.jythonui.server.xslt.XSLTFun"
    xmlns:java="http://xml.apache.org/xslt/java">
	<xsl:output method="text" encoding="UTF-8" />
	<xsl:template match="/root">
Invoice pdf
Reservation Id: <xsl:value-of select="elem/resid" />
Booked on:<xsl:value-of select="elem/resdate" />
Guest: <xsl:value-of select="elem/name1" /> <xsl:value-of select="elem/name2" />
Room : <xsl:value-of select="elem/roomnumber" />
Check-in : <xsl:value-of select="elem/arrivaldate" />
Check-out : <xsl:value-of select="elem/departuredate" />
Adults: <xsl:value-of select="elem/nofguests" />
Daily rate : <xsl:value-of select="elem/dailyrate" />
Total : <xsl:value-of select="elem/total" />

</xsl:template>
</xsl:stylesheet>  