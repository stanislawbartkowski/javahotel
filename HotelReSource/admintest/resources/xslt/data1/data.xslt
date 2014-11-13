<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fun="java://com.jythonui.server.xslt.XSLTFun"
>

<xsl:variable name="lang.subject" select="fun:m('subject')"/>
<xsl:variable name="lang.myname" select="fun:m('myname')"/>
<xsl:variable name="lang.local" select="'English'"/>
<xsl:variable name="wow">
  aaaaa <br/>  bbbbb
</xsl:variable>

<xsl:template match="/">
  <html>
  <body>
    <h1><xsl:value-of select="$lang.local" /></h1>
    <h2>My CD Collection</h2>
    <h1><xsl:value-of select="fun:m('subject')" /> </h1>
    <h1><xsl:value-of select="$lang.subject" /> </h1>
    <h1><xsl:value-of select="$lang.myname" /> </h1>
    <table border="1">
      <tr bgcolor="#9acd32">      
        <th>Title</th>
        <th>Artist</th>
      </tr>
      <xsl:for-each select="catalog/cd">
        <tr>
          <td><xsl:value-of select="title"/></td>
          <td><xsl:value-of select="artist"/></td>
        </tr>
      </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>

</xsl:stylesheet> 