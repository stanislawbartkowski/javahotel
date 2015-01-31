<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml"
        xmlns:fun="java://com.jythonui.server.xslt.XSLTFun"
        xmlns:java="http://xml.apache.org/xslt/java">	


	<xsl:output method="xml" encoding="UTF-8" />
	
        <xsl:import href="lang/var_en.xslt"/>


	<xsl:template match="/root">
		<html>
			<head>
				<style type="text/css">

					div.left
					{
					margin: 5px;
					padding: 5px;
					border: 1px
					solid #0000ff;
					height: auto;
					width: auto;
					float: left;
					text-align:
					center;
					}
					div.center
					{
					margin: 5px;
					padding: 5px;
					border: 1px solid
					#0000ff;
					height: auto;
					width: auto;
					/* float: left; */
					text-align:
					center;
					overflow: hidden;
					/* display: inline-block; */
					}
					div.right
					{
					margin: 5px;
					padding: 5px;
					border: 1px solid #0000ff;
					height: auto;
					width: auto;
					float: right;
					text-align: center;
					}
				</style>
			</head>
			<body>
				<div>
					<div class="left">
						<xsl:value-of select="elem/name1" />
						<br />
						<xsl:value-of select="elem/name2" />
						<br />
						<xsl:value-of select="elem/address" />
						<br />
						<xsl:value-of select="elem/city" />
						<br />
						<xsl:value-of select="elem/country" />
						<br />
						<xsl:if test="elem/addinfo">
							<xsl:value-of select="elem/addinfo" />
						</xsl:if>
						<br />
					</div>
					<div class="right">
						<xsl:value-of select="$lang.Room"/> : 
						<xsl:value-of select="elem/roomnumber" />
						<br />
						<xsl:value-of select="$lang.dailyRate"/> : 
						<xsl:value-of select="elem/dailyrate" />
						<br />
						<xsl:value-of select="$lang.roomType"/> :
						<xsl:value-of select="elem/roomtype" />
						<br />
						<xsl:value-of select="$lang.numberofGuests"/>:
						<xsl:value-of select="elem/nofguests" />
						<br />
						<xsl:value-of select="$lang.arrivalDate"/>:
						<xsl:value-of select="elem/arrivaldate" />
						<br />
						<xsl:value-of select="$lang.departureDate"/> :
						<xsl:value-of select="elem/departuredate" />
						<br />
					</div>
					<div class="center">
						<H1> Hotel Royal Comfort </H1>
					</div>

				</div>

				<div style="clear:both;"></div>
				<div>
					<table>
						<tr>
							<th><xsl:value-of select="fun:toUpper($lang.Date)"/></th>
							<th><xsl:value-of select="fun:toUpper($lang.Room)"/></th>
							<th><xsl:value-of select="fun:toUpper($lang.roomDescription)"/></th>
							<th><xsl:value-of select="fun:toUpper($lang.dailyRate)"/></th>
							<th><xsl:value-of select="fun:toUpper($lang.Amount)"/></th>
							<th><xsl:value-of select="fun:toUpper($lang.Total)"/></th>
							<th></th>
							<th><xsl:value-of select="fun:toUpper($lang.Tax)"/></th>
						</tr>
						<xsl:for-each select="list/elem">
							<tr>
								<td>
									<xsl:value-of select="date" />
								</td>
								<td>
									<xsl:value-of select="roomnumber" />
								</td>
								<td>
									<xsl:value-of select="description" />
								</td>
								<td>
									<xsl:value-of select="rate" />
								</td>
								<td>
									<xsl:value-of select="amount" />
								</td>
								<td>
									<xsl:value-of select="grossvalue" />
								</td>
								<td>
									<xsl:value-of select="tax" />
								</td>
								<td>
									<xsl:value-of select="taxvalue" />
								</td>
							</tr>

						</xsl:for-each>

					</table>
				</div>

			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>  