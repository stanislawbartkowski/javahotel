<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
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
<div class="cell"> What has changed</div>
<div class="cell">Previous value</div>
<div class="cell">New value</div>
</xsl:variable>

<xsl:variable name="lang.makereservation" select="fun:m('makereservation')"/>
<xsl:variable name="lang.refreshreservation" select="fun:m('checkavailability')"/>

<xsl:template match="/">
    <div class="wrapper">
    
    <p style="font-size:larger;">
    <xsl:copy-of  select="$lang.header1" />
    <span style="font-weight: bold;"><xsl:value-of select="$lang.refreshreservation"/>&#160;</span> 
    <xsl:copy-of select="$lang.header2"/>
    <span style="font-weight: bold;"> <xsl:value-of select="$lang.makereservation"/>&#160;</span>
    <xsl:copy-of select="$lang.header3"/>
    </p>
    <div class="table">
      <div class ="row header">
        <xsl:copy-of select="$lang.tabheader"/>
      </div>  
      <xsl:for-each select="root/list/elem">
        <div class="row">
          <div class="cell"><xsl:value-of select="descr"/></div>
          <div class="cell"><xsl:value-of select="prevval"/>&#160;</div>
          <div class="cell"><xsl:value-of select="newval"/>&#160;</div>
        </div>
      </xsl:for-each>
    </div>
    </div>
</xsl:template>

</xsl:stylesheet> 