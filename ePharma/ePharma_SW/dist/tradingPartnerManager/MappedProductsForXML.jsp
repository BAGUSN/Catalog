

<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.*"%>




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



<table width="100%" cellSpacing="0" cellPadding="0" align="left" border="0"
			bgcolor="white">
                <tr bgcolor="white" align="center"> 
                  <td class="td-typeblack">Mapped Products Details</td>
                </tr>
                <tr> 
                  <td align="left">
					<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">	
					<tr ><td><font size="2"><STRONG>Master Catalog Products:</font></STRONG></td></tr>			
					<tr class="tableRow_Header">
					<td class="type-whrite">NDC #</td>
					<td class="type-whrite">Product Name</td>
					<td class="type-whrite">Description</td>
					<td class="type-whrite">Manufacturer Name</td>
				</tr>
<% 
String masterString=new String("");
List mlist=(List)request.getAttribute("MasterList");
Iterator iterator1=mlist.iterator();
while(iterator1.hasNext())
{
masterString=(String)iterator1.next();
String str1[]=masterString.split(",");

%>

                <tr>
                
                <td><%=str1[0]%> </td>
                <td><%=str1[1]%> </td>
                <td><%=str1[3]%> </td>
                <td><%=str1[2]%> </td>
                </tr>
              
               
                
<%      
}


%>

</table>
<TABLE cellSpacing="1" cellPadding="3" border="0" width="100%">	
					<tr ><td><font size="2"><STRONG>Trading Partner Catalog Products:</font></STRONG></td></tr>			
					<tr class="tableRow_Header">
					<td class="type-whrite" >NDC #</td>
					<td class="type-whrite">Product Name</td>
					<td class="type-whrite">Description</td>
					<td class="type-whrite">Manufacturer Name</td>
				</tr>
  
<% 
Node n;
List list=(List)session.getAttribute("mappedList");
Iterator iterator=list.iterator();
while(iterator.hasNext())
{

n=(Node)iterator.next();


%>
 

                <tr>
                
                <td><%=XMLUtil.getValue(n,"NDC") %> </td>
                <td><%=XMLUtil.getValue(n,"ProductName") %> </td>
                <td><%=XMLUtil.getValue(n,"Description") %> </td>
                <td><%=XMLUtil.getValue(n,"ManufacturerName") %></td>
                </tr>
              
               
      
<%      
}


%>

</table>



<jsp:include page="../includes/Footer.jsp" />


</body>
</html>
