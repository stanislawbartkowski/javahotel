<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fun="java://com.jythonui.server.xslt.XSLTFun"
    xmlns:java="http://xml.apache.org/xslt/java">
	<xsl:output method="text" encoding="UTF-8" />
	<xsl:template match="/reservation">
Invoice pdf
Reservation Id: <xsl:value-of select="resid" />
Booked on:<xsl:value-of select="resdate" />
Guest: <xsl:value-of select="name1" /> <xsl:value-of select="name2" />
Room : <xsl:value-of select="roomnumber" />
Check-in : <xsl:value-of select="arrivaldate" />
Check-out : <xsl:value-of select="departuredate" />
Adults: <xsl:value-of select="nofguests" />
Daily rate : <xsl:value-of select="dailyrate" />
Total : <xsl:value-of select="total" />
</xsl:template>
</xsl:stylesheet>  