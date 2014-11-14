<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fun="java://com.jythonui.server.xslt.XSLTFun"
>

<xsl:output method="html"
            encoding="UTF-8"
            indent="no"/>

<xsl:variable name="lang.activity" select="fun:m('activity')" />
<xsl:variable name="lang.numberof" select="fun:m('numberof')" />
<xsl:variable name="lang.ascustomer" select="fun:m('ascustomer')" />
<xsl:variable name="lang.asguest" select="fun:m('asguest')" />
<xsl:variable name="lang.aspayer" select="fun:m('aspayer')" />
<xsl:variable name="lang.mails" select="fun:m('mails')" />

<xsl:template match="/root/elem">
    <div class="wrapper">
    
    <div class="table">
      <div class ="row header green">
        <div class="cell"><xsl:value-of select="$lang.activity"/></div>
        <div class="cell"><xsl:value-of select="$lang.numberof"/></div>
      </div>  
        <div class="row">
          <div class="cell"><xsl:value-of select="$lang.ascustomer"/></div>
          <div class="cell"><xsl:value-of select="nofcustomer"/></div>
        </div>
        <div class="row">
          <div class="cell"><xsl:value-of select="$lang.asguest"/></div>          
          <div class="cell"><xsl:value-of select="nofguest"/></div>
        </div>
        <div class="row">
          <div class="cell"><xsl:value-of select="$lang.aspayer"/></div>          
          <div class="cell"><xsl:value-of select="nofpayer"/></div>
        </div>
        <div class="row">
          <div class="cell"><xsl:value-of select="$lang.mails"/></div>          
          <div class="cell"><xsl:value-of select="nomails"/></div>
        </div>
    </div>
    </div>
</xsl:template>

</xsl:stylesheet> 