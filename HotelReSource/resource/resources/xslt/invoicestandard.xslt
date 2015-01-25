<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml">

	<xsl:output method="xml" encoding="UTF-8" />

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
						Room number :
						<xsl:value-of select="elem/roomnumber" />
						<br />
						Daily rate:
						<xsl:value-of select="elem/dailyrate" />
						<br />
						Room type :
						<xsl:value-of select="elem/roomtype" />
						<br />
						Numb of guests:
						<xsl:value-of select="elem/nofguests" />
						<br />
						Arrival:
						<xsl:value-of select="elem/arrivaldate" />
						<br />
						Departure :
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
							<th>DATE</th>
							<th>ROOM NO</th>
							<th>DESCRIPTION</th>
							<th>RATE</th>
							<th>AMOUNT</th>
							<th>TOTAL</th>
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
									<xsl:value-of select="total" />
								</td>
							</tr>

						</xsl:for-each>

					</table>
				</div>

			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>  