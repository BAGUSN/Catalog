
<%@ page import="java.util.List"%>
<% String sessionID = (String)session.getAttribute("sessionID");
System.out.println("ss: "+sessionID);
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>Raining Data ePharma - ePedigree Manager - Search</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
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
		<!-- Top Header -->
		<div id="bg">
			<div class="roleIcon-distributor"></div>
			<div class="navIcons">
				<a href="../../landing.html"><img src="../../assets/images/home.gif" width="22" height="27" hspace="10" border="0"></a>
				<img src="../../assets/images/account.gif" width="41" height="27" hspace="10"> <img src="../../assets/images/help.gif" width="21" height="27" hspace="10">
				<img src="../../assets/images/print.gif" width="27" height="27" hspace="10"> <img src="../../assets/images/logout.gif" width="34" height="27" hspace="10">
				<a href="index.html" target="_top"><IMG height="27" hspace="10" src="../../assets/images/inbox.gif" border=0></a>
				<img src="../../assets/images/space.gif" width="20">
			</div>
			<div class="logo"><img src="../../assets/images/logos_combined.jpg"></div>
		</div>
		<!-- Top level nav layer 1 -->
		<div id="menu">
			<table width="100%" height="25" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="middle" background="../../assets/images/bg_menu.jpg"><table width="800" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../admin/index.html" class="menu-link">Admin 
										Console</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../invManagement/index.html" class="menu-link">Inventory 
										Management</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../logistics/index.html" class="menu-link">Logistics</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_On" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="index.html" class="menu-link menuBlack">ePedigree</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../prodRecall/index.html" class="menu-link">Product 
										Recall</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../trackTrace/index.html" class="menu-link">Track 
										and Trace</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../bizIntel/index.html" class="menu-link">Business 
										Intelligence</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<!-- Top level nav layer 2 -->
		<div id="menu1">
			<table width="100%" height="25" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td valign="bottom" background="../../assets/images/bg_menu.jpg"><table width="750" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../tradingPartnerManager/index.html" class="menu-link">Trading 
										Partner Manager</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../diversion/index.html" class="menu-link">Diversion</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<!-- <td align="center" class="primaryNav_Off"
    onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'" onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../clinicalTrials/index.html" class="menu-link">Clinical Trials</a></td>
            <td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td> -->
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../catalog/index.html" class="menu-link">Catalog</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../authentication/index.html" class="menu-link">Authentication</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../productIntegrity/index.html" class="menu-link">Product 
										Integrity</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../reports/index.html" class="menu-link">Reports</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
								<td align="center" class="primaryNav_Off" onmouseover="this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'"
									onmouseout="this.style.backgroundColor='';this.style.color=''"><a href="../kpiDashboard/index.html" class="menu-link">KPI 
										Dashboard</a></td>
								<td><img src="../../assets/images/menu_bg1.jpg" width="3" height="23"></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</div>
		<!-- Left channel -->
		<div id="leftgray">
			<table id="Table5" height="100%" cellSpacing="0" cellPadding="0" width="170" border="0">
				<tr>
					<td class="td-leftred" width="170" colSpan="2">ePedigree Manager</td>
				</tr>
				<tr>
					<td vAlign="top" width="10" bgColor="#dcdcdc"></td>
					<td class="td-left" vAlign="top"><br>
						<A class="typeblue1-link-sub" href="ePedigree_ReceivingManager.html">Receiving 
							Manager<br>
						</A>
						<br>
						<A class="typeblue1-link-sub" href="ePedigree_ShippingManager.html">Shipping 
							Manager<br>
						</A>
						<br>
						<A class="typeblue1-link-sub" href="ePedigree_Commissioning_Repackaging.html">Packaging<br>
						</A><A class="typeblue1-link-sub-sub" href="ePedigree_Commissioning_Repackaging.html">
							&nbsp;&nbsp;&nbsp;Commissioning Station<br>
						</A><A class="typeblue1-link-sub-sub" href="ePedigree_Commissioning_Repackaging.html">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Item</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_Commissioning_Station_Package.html">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Package</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_Commissioning_ProductionRun.html">
							&nbsp;&nbsp;&nbsp;Production Run</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_Commissioning_View.html">&nbsp;&nbsp;&nbsp;View 
							Commissioned Items</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_Commissioning_AggregationStation.html">
							&nbsp;&nbsp;&nbsp;Aggregation Station</A><br>
						<br>
						<A class="typeblue1-link-sub" href="#">Find</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_Manager_Search.html"><FONT color="darkgreen">
								&nbsp;&nbsp;&nbsp;<STRONG>Search ePedigree</STRONG></FONT></A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_APNManager_Search.html">&nbsp;&nbsp;&nbsp;Search 
							APNs</A><br>
						<a href="ePedigree_Orders_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
							Orders</a><br>
						<a href="ePedigree_Invoices_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
							Invoices</a><br>
						<a href="ePedigree_POS_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
							POs</a><br>
						<a href="ePedigree_ASN_Search.html" class="typeblue1-link-sub-sub">&nbsp;&nbsp;&nbsp;Search 
							ASNs</a><br>
						<br>
						<A class="typeblue1-link-sub" href="ePedigree_Reports.html">Reporting</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_Reports.html">&nbsp;&nbsp;&nbsp;ePedigree</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_Reports_APN.html">&nbsp;&nbsp;&nbsp;APN</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_Reports_Repackaging.html">&nbsp;&nbsp;&nbsp;Repackaging</A><br>
						<br>
						<A class="typeblue1-link-sub" href="ePedigree_Workflow.html">Event Workflow</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_CreateWorkflow.html">&nbsp;&nbsp;&nbsp;Create</A><br>
						<A class="typeblue1-link-sub-sub" href="ePedigree_EditWorkflow.html">&nbsp;&nbsp;&nbsp;View/Edit</A>
					</td>
				</tr>
				<tr vAlign="bottom">
					<td class="td-left" colSpan="2" height="80"><IMG height="37" src="../../assets/images/logo_poweredby.gif" width="150"></td>
				</tr>
			</table>
		</div>
		<div id="rightwhite">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="1%" valign="middle" class="td-rightmenu"><img src="../../assets/images/space.gif" width="10" height="10"></td>
					<!-- Messaging -->
					<td width="99%" valign="middle" class="td-rightmenu"><table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="left">
									<!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td class="type-red">[View] </td>
                  <td><a href="#" class="typered-link">Create</a> </td>
                  <td><a href="#" class="typered-link">Delete</a> </td>
                  <td><a href="#" class="typered-link">Duplicate </a></td>
                  <td><a href="#" class="typered-link">Search </a></td>
                  <td><a href="#" class="typered-link">Audit </a></td>
                  <td><a href="#" class="typered-link">Trail</a></td>
                </tr>
              </table> -->
								</td>
								<td align="right">
									<!-- <img src="../../assets/images/3320.jpg" width="200" height="27"> -->
									<img src="../../assets/images/space.gif" width="5"></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td><img src="../../assets/images/space.gif" width="30" height="12"></td>
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
									<form action="Search.do" method="post" ID="Form1">
									<table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white" class="td-menu">
										<tr bgcolor="white">
											<td class="td-typeblack">Simple Search on ePedigree</td>
										</tr>
										<tr>
											<td align="left">
												<TABLE cellSpacing="1" cellPadding="0" border="0" align="center" width="100%">
													<TR class="tableRow_Header">
														<TD class="type-whrite" align="center">
															<STRONG>SEARCH</STRONG>
														</TD>
														<TD class="type-whrite" align="center">
															<STRONG>VALUE</STRONG>
														</TD>
													</TR>
													<TR class="tableRow_On">
														<TD class="td-menu bold" align="center">ePedigree Reference #:</TD>
														<TD class="td-menu" align="center"><INPUT type="text" size="24" name="pedigreeNum"></TD>
													</TR>
													<tr class="tableRow_Header">
														<td colspan="3" align="center">
														<INPUT name="Submit3" type="submit" class="fButton_large" id="Submit2" onClick="MM_goToURL('parent','ePedigree_Manager_AdvancedSearch.html');return document.MM_returnValue"
																value="Advanced Search"> &nbsp;&nbsp;&nbsp;&nbsp;
														<INPUT name="Submit3" type="submit" class="fButton" value="Search">
														</td>
													</tr>
												</TABLE>
											</td>
										</tr>
									</table>
									</form></td>
							</tr>
						</table>
						<DIV></DIV>
						<div id="footer" class="td-menu">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
			
			
<% if ( request.getParameter("pedigreeNum") != null ){
%>			
<TABLE id="Table9" cellSpacing="1" cellPadding="0" width="100%" align="center" border="0">
																<TBODY>
																	
																	<tr class="tableRow_Header">
																		<td class="type-whrite" noWrap align="center">Select</td>
																		<td class="type-whrite" noWrap align="center">Pedigree&nbsp;#</td>
																		<td class="type-whrite" noWrap align="center">Order #</td>
																		<td class="type-whrite" noWrap align="center">Date Received</td>
																		<td class="type-whrite" align="center">Product</td>
																		<td class="type-whrite" noWrap align="center">Quantity</td>
																		<td class="type-whrite" noWrap align="center">Status</td>
																		<td class="type-whrite" noWrap align="center">Created By</td>
																	</tr>
																								
 <%
		
		List list1 =  (List)request.getAttribute("APNResultSet");
		List list2 =  (List)request.getAttribute("OrderResultSet");	
		List list3 =  (List)request.getAttribute("Date ReceivedResultSet");
		List list4 =  (List)request.getAttribute("ProductResultSet");
		List list5 =  (List)request.getAttribute("QuantityResultSet");
								
		if(list1 != null ) {
for(int i=0;i<list1.size();i++){
%>
						<tr class="tableRow_On" >
							
						<td align='center'> <input type="radio" name="opt"></td>
						
						<%
							
							String	temp = (String)list1.get(i);
							
							
							out.println("<td align='center'><a href='ePedigree_Manager_Reconcile.jsp?pedid="+temp+"&pagenm="+pagenm+"&tp_company_nm='>"+temp+"</a></td>");											
							
										
							
							out.println("<td align='center'>"+list2.get(i)+" </td>");
							out.println("<td align='center'>"+list3.get(i)+" </td>");
							out.println("<td align='center'>"+list4.get(i)+" </td>");
							out.println("<td align='center'>"+list5.get(i)+" </td>");
							out.println("<td></td>");
						
						    out.println("<td align='center'>System</td>");
						%>
						
										
						
						
						</tr>
						<%
					
	                   			}		
						}
						
						
						
						
	}
%> 
</TABLE>
			
		</div>
	</body>
</html>