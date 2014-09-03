<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fun="java://MyFun"
  xmlns:java="http://xml.apache.org/xslt/java">
  >

  <xsl:template match="/root">
  
  <xsl:output method="text" encoding="UTF-8" />
  
    <xsl:value-of select="fun:fillString(65,'-')" />
    <xsl:text>&#xa;</xsl:text>
    <xsl:value-of select="fun:paddingLeft(20,'Description')" />
    <xsl:value-of select="fun:paddingLeft(15,'Unit Price')" />
    <xsl:value-of select="fun:paddingLeft(15,'Qty')" />
    <xsl:value-of select="fun:paddingLeft(15,'Amount')" />
    <xsl:text>&#xa;</xsl:text>
    <xsl:value-of select="fun:fillString(65,'-')" />
    
    <xsl:for-each select="lines/line">
        <xsl:text>&#xa;</xsl:text>
        <xsl:variable name="name" select="name" />
        <xsl:variable name="price" select="price" />
        <xsl:variable name="qty" select="qty" />
        <xsl:variable name="amount" select="amount" />
        <xsl:value-of select="fun:paddingRight(20,$name)" />        
        <xsl:value-of select="fun:paddingLeft(15,$price)" />        
        <xsl:value-of select="fun:paddingLeft(15,$qty)" />        
        <xsl:value-of select="fun:paddingLeft(15,$amount)" />        
    </xsl:for-each>
    <xsl:text>&#xa;</xsl:text>
    <xsl:value-of select="fun:fillString(65,'-')" />
    <xsl:text>&#xa;</xsl:text>
    <xsl:variable name="total" select="sum(/root/lines/line/amount)"/>
    <xsl:value-of select="fun:paddingLeft(50,'Total')" />    
    <xsl:value-of select="fun:paddingLeft(15,format-number($total,'#.00'))" />        
    
        
  </xsl:template>

</xsl:stylesheet> 