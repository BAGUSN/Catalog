<%@ taglib uri="/struts-html" prefix="html" %>

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html:html>
<head>
<title>Raining Data ePharma - Trading Partner Manager</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}


//-->
</script>
</head>
<body>

<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />


<%

    TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    String tpName = context.getTpName();
    String tpGenId = context.getTpGenId();
%>

<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><html:img src="./assets/images/space.gif" width="10" height="10"/></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
	<jsp:include page="./TPHeader.jsp" />
	  
	  
	  </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><html:img src="./assets/images/space.gif" width="30" height="12"/></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
        <!-- Breadcrumb -->
        <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
        <tr> 
          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Catalogs of the Trading Partner :<%=tpName%> </td>

				  <td><a href="CatalogNew.do?tpGenId=<%=tpGenId%>" class="typered-bold-link">Create New Catalog</a></td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Description</td>
					<td class="type-whrite">Updated Date</td>
					<td class="type-whrite"></td>
							

				</tr>


				<%
					List list =  (List)request.getAttribute("CatalogListInfo");

					String name = "";
					String genId = "";
					String description = "";
					String updatedDate = "";
				

					for(int i=0; i < list.size(); i++) {
					
						Node catalogNode = XMLUtil.parse((InputStream) list.get(i) );

						genId = CommonUtil.jspDisplayValue(catalogNode,"catalogID");

						name = CommonUtil.jspDisplayValue(catalogNode,"catalogName");
						description = CommonUtil.jspDisplayValue(catalogNode,"description");

						updatedDate = CommonUtil.jspDisplayValue(catalogNode,"updatedDate");

			   %>


				<tr class="tableRow_On">
					<td><a href="Catalog.do?operationType=FIND&catalogGenId=<%= genId%>" class="typered-bold-link">
					<%= name %></a></td>
					<td><%= description %></td>
					<td><%= updatedDate %></td>

					<td><a href="OpenCatalogSchemaDef.do?fromModule=TP&catalogGenId=<%= genId%>" class="typered-bold-link">
					Schema Def</a>  &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
					<!--<a href="MapCatalogSchemas.do?leftCatalogGenId=<%= genId%>&rightCatalogGenId=StandardGenId1000
					">Map Attributes To Standard Catalog</a>-->
					
					
					<!--Here Iam making changes--> 
                          
                         <a href="SelectMasterCatalog.do?leftCatalogGenId=<%= genId%>&tpGenId=<%=tpGenId%>
                         ">Map Attributes To Standard Catalog</a> 
                        
					</td>

					
	
				</tr>

                <%

					}
                 %>
			
				</TABLE>
            </td>
        </tr>
      </table></div>



<jsp:include page="../includes/Footer.jsp" />


</body>
</html:html>
