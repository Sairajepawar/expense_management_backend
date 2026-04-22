<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">

        <fo:root>

            <!-- Page Layout -->
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4"
                                       page-height="29.7cm"
                                       page-width="21cm"
                                       margin="1cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <!-- Page Content -->
            <fo:page-sequence master-reference="A4">
                <fo:flow flow-name="xsl-region-body">

                    <!-- Title -->
                    <fo:block font-size="18pt"
                              font-weight="bold"
                              text-align="center"
                              space-after="10pt">
                        Expense Report
                    </fo:block>

                    <!-- Table -->
                    <fo:table border="1pt solid black"
                              width="100%"
                              table-layout="fixed">

                        <!-- Column Sizes -->
                        <fo:table-column column-width="6cm"/>
                        <fo:table-column column-width="3cm"/>
                        <fo:table-column column-width="3cm"/>
                        <fo:table-column column-width="6cm"/>

                        <!-- Header -->
                        <fo:table-header>
                            <fo:table-row background-color="#d3d3d3">
                                <fo:table-cell border="1pt solid black" padding="5pt">
                                    <fo:block font-weight="bold">Date</fo:block>
                                </fo:table-cell>

                                <fo:table-cell border="1pt solid black" padding="5pt">
                                    <fo:block font-weight="bold">Amount</fo:block>
                                </fo:table-cell>

                                <fo:table-cell border="1pt solid black" padding="5pt">
                                    <fo:block font-weight="bold">Category</fo:block>
                                </fo:table-cell>

                                <fo:table-cell border="1pt solid black" padding="5pt">
                                    <fo:block font-weight="bold">Description</fo:block>
                                </fo:table-cell>

                            </fo:table-row>
                        </fo:table-header>

                        <!-- Data Rows -->
                        <fo:table-body>

                            <xsl:for-each select="expenses/expense">

                                <fo:table-row>

                                    <fo:table-cell border="1pt solid black" padding="5pt">
                                        <fo:block>
                                            <xsl:value-of select="date"/>
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell border="1pt solid black" padding="5pt">
                                        <fo:block>
                                            <xsl:value-of select="amount"/>
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell border="1pt solid black" padding="5pt">
                                        <fo:block>
                                            <xsl:value-of select="category"/>
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell border="1pt solid black" padding="5pt">
                                        <fo:block>
                                            <xsl:value-of select="description"/>
                                        </fo:block>
                                    </fo:table-cell>

                                </fo:table-row>

                            </xsl:for-each>

                        </fo:table-body>

                    </fo:table>

                </fo:flow>
            </fo:page-sequence>

        </fo:root>

    </xsl:template>

</xsl:stylesheet>