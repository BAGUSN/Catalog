

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.Constants"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
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


<div id="rightwhite">
<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
  <tr> 
    <td width="1%" valign="middle" class="td-rightmenu"><img src="./assets/images/space.gif" width="10" height="10"></td>
    <!-- Messaging -->
    <td width="99%" valign="middle" class="td-rightmenu">
	
	<jsp:include page="./TPHeader.jsp" />
	  
	  
	  </td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><table border="0" cellspacing="0" cellpadding="0" width="100%">
        <tr> 
          <td><img src="./assets/images/space.gif" width="30" height="12"></td>
          <td rowspan="2">&nbsp;</td>
        </tr>
        <!-- Breadcrumb -->
        <!-- <tr> 
            <td><a href="#" class="typegray1-link">Home</a><span class="td-typegray"> 
              - </span><a href="#" class="typegray1-link">ePedigree</a></td>
          </tr> -->
        <tr> 


		<%

    TradingPartnerContext context = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
    String tpName = context.getTpName();
    String tpGenId = context.getTpGenId();
%>

          <td> 
            <!-- info goes here -->
              <table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white"> 
                  <td class="td-typeblack">List of Locations of the Trading Partner :<%=tpName%> </td>

				  <td><a href="LocationNew.do?tpGenId=<%=session.getAttribute("tpGenId_sessionValue")%>" class="typered-bold-link">Create New Location</a></td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">				
					<tr class="tableRow_Header">
					<td class="type-whrite">Name</td>
					<td class="type-whrite">Type</td>
					<td class="type-whrite">Address</td>
					<td class="type-whrite">InFormat</td>
					<td class="type-whrite">OutFormat</td>
					<td class="type-whrite">Phone</td>
					<td class="type-whrite">Fax</td>
				

				</tr>


				<%
					List list =  (List)request.getAttribute("LocationListInfo");

					String name = "";
					String genId = "";
					String address = "";
					String type = "";
					String phone =""; 
					String fax = "";
					String inFormat = "";
					String outFormat = "";

					for(int i=0; i < list.size(); i++) {
					
						Node locationNode = XMLUtil.parse((InputStream) list.get(i) );

						genId = CommonUtil.jspDisplayValue(locationNode,"genId");

						name = CommonUtil.jspDisplayValue(locationNode,"name");
						type = CommonUtil.jspDisplayValue(locationNode,"type");

						phone = CommonUtil.jspDisplayValue(locationNode,"phone");
						fax = CommonUtil.jspDisplayValue(locationNode,"fax");

						inFormat = CommonUtil.jspDisplayValue(locationNode,"inFormat");
						outFormat = CommonUtil.jspDisplayValue(locationNode,"outFormat");

						String line1 = CommonUtil.jspDisplayValue(locationNode,"address/line1");
						String line2 = CommonUtil.jspDisplayValue(locationNode,"address/line2");
						String city = CommonUtil.jspDisplayValue(locationNode,"address/city");
						String state = CommonUtil.jspDisplayValue(locationNode,"address/state");
						String country = CommonUtil.jspDisplayValue(locationNode,"address/country");
						String zip = CommonUtil.jspDisplayValue(locationNode,"address/zip");

						address = line1 + " "  + line2 + " " + city + " " + state + " "   + country + " " + zip; 
			   %>


				<tr class="tableRow_On">
					<td><a href="Location.do?operationType=FIND&locationGenId=<%= genId%>" class="typered-bold-link">
					<%= name %></a></td>
					<td><%= type %></td>
					<td><%= address %></td>
					<td><%= inFormat %></td>
					<td><%= outFormat %></td>
					<td><%= phone %></td>
					<td><%= fax %></td>
					
					
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
</html>
