<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
    <h2>You made some chganges.</h2>
    <table border="1">
      <tr bgcolor="#9acd32">
        <th>What has changed</th>
        <th>Previous value</th>
        <th>New value</th>
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