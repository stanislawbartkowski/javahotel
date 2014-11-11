<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fun="java://com.jythonui.server.xslt.XSLTFun"
>

<xsl:variable name="lang.header1">
You have made changes. <br></br>  <br></br> But in order to modify the reservation it is necessary to click
</xsl:variable>
<xsl:variable name="lang.header2">
button firstly. It recalculates the reservation and the changes are visible in the list at the bottom. <br></br> <br></br> Then to make then changes persistent click
</xsl:variable>
<xsl:variable name="lang.header3">
button again.
</xsl:variable>
<xsl:variable name="lang.tabheader">
<th>What has changed</th>
<th>Previous value</th>
<th>New value</th>
</xsl:variable>

<xsl:variable name="lang.makereservation" select="fun:m('makereservation')"/>
<xsl:variable name="lang.refreshreservation" select="fun:m('checkavailability')"/>

<xsl:template match="/">
    <p style="font-size:larger;">
    <xsl:copy-of  select="$lang.header1" />
    <span style="font-weight: bold;"><xsl:value-of select="$lang.refreshreservation"/>&#160;</span> 
    <xsl:copy-of select="$lang.header2"/>
    <span style="font-weight: bold;"> <xsl:value-of select="$lang.makereservation"/>&#160;</span>
    <xsl:copy-of select="$lang.header3"/>
    </p>
    <table border="1">
      <tr bgcolor="#9acd32">
        <xsl:copy-of select="$lang.tabheader"/>
      </tr>
      <xsl:for-each select="root/list/elem">
        <tr>
          <td><xsl:value-of select="descr"/></td>
          <td><xsl:value-of select="prevval"/></td>
          <td><xsl:value-of select="newval"/></td>
        </tr>
      </xsl:for-each>
    </table>
</xsl:template>

</xsl:stylesheet> 