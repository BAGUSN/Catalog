<%@include file='../../includes/jspinclude.jsp'%>
<%@page import="com.rdta.tlapi.xql.Connection"%>
<%@page import="com.rdta.tlapi.xql.Statement"%>
<%@page import="com.rdta.Admin.Utility.Helper"%>

<SCRIPT LANGUAGE="javascript">

function newWindow(  htmFile  ) {
   msgWindow = window.open( htmFile, "WindowName", "left=20,top=20,width= 700,height=500,toolbar=0,scrollbar=1,resizable=1,location=0,status=0");
}

function checkAll(field)
{
alert(myform.list.value)
for (i = 0; i < field.length; i++)
	field[i].checked = true ;
}
</SCRIPT>
<%
String servIP = request.getServerName();
String clientIP = request.getRemoteAddr();
String APNID = request.getParameter("pedid");
//String sessionID = request.getParameter("sessionID");
String sessionID = (String)session.getAttribute("sessionID");
String pagenm = request.getParameter("pagenm");
String tp_company_nm = request.getParameter("tp_company_nm");
String PedigreeOrder = "x";

Connection conn; 
Statement stmt;
Helper helper = new Helper();

//Validating Session
			HttpSession sess = request.getSession();
			clientIP = request.getRemoteAddr();		
			conn = helper.ConnectTL(); 
			stmt = helper.getStatement(conn);
			System.out.println("Validating The Session");
			
			//Validating Session
				
			String validateResult = helper.ValidateUserSession(stmt, sessionID, clientIP);        
		
			if ( !validateResult.equals("VALID")){
			    //return a forward to invalid .
			    
			}
if(request.getParameter("selpedid") != null) {
	PedigreeOrder = request.getParameter("selpedid");
} 


String HTMLROW = "";
String PedDocDT = "";
String FromAddr = "";
String ToAddr = "";
String SHIPROW = "";
String PRODS = "";
String MANUFACT = "";
String TOPLINE = "";
String CUSTODY = "";
String totProducts = "";
String totManufacturers = "";
String totCustodians = "";
byte[] xmlResults;

com.rdta.tlapi.xql.Connection connection = ConnectTL();
com.rdta.tlapi.xql.Statement statement = getStatement(connection);

String xQuery = "";

if(PedigreeOrder.equals("x")) {

	xQuery = "for $b in collection('tig:///ePharma/APN')/APN  ";
	xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
	xQuery = xQuery + "return  max($b/Pedigrees/Pedigree/@order) ";

	xmlResults = ReadTL(statement, xQuery);
	if(xmlResults != null) {
		PedigreeOrder = new String(xmlResults);
		PedigreeOrder = replaceString(PedigreeOrder, ".0E0", "");
	}
}


xQuery = "for $b in collection('tig:///ePharma/APN')/APN  ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"' ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "<tr class='tableRow_Header'>    ";
xQuery = xQuery + "<td class='type-whrite'>Pedigree ID : <STRONG><FONT color='#ffff00'>"+APNID+"</FONT></STRONG></td> ";
xQuery = xQuery + "<td class='type-whrite'>Issue Date : <FONT color='#ffff00'><STRONG>{data($b/DateTime)}</STRONG></FONT></td>     ";
xQuery = xQuery + "<td class='type-whrite'>Transaction Type: <FONT color='#ffff00'><STRONG>{data($b/To/TransactionType)}</STRONG></FONT></td> ";
xQuery = xQuery + "<td class='type-whrite'>Transaction # : <STRONG><FONT color='#ffff00'>{data($b/To/TransactionNumber)}</FONT></STRONG></td>   ";
xQuery = xQuery + "<td class='type=whrite' bgcolor='#ccffff'> ";
xQuery = xQuery + "{  if(data($b/To/TransactionType) = 'Order') then  ";
xQuery = xQuery + "(  ";
xQuery = xQuery + " <A HREF =\"javascript:newWindow('ePedigree_ViewOrder.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm=" + tp_company_nm + "&amp;pagenm=" + pagenm + "')\" > ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> </A> ";
xQuery = xQuery + ") ";
xQuery = xQuery + "else (  if(data($b/To/TransactionType) = 'DespatchAdvice') then ";
xQuery = xQuery + " <A HREF = \" javascript:newWindow('ASN_Details.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm=" + tp_company_nm + "&amp;pagenm=" + pagenm + "')\" > ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> </A> ";
xQuery = xQuery + "else  ";
xQuery = xQuery + " <A HREF = \" javascript:newWindow('Invoice_Details.jsp?trType={data($b/To/TransactionType)}&amp;trNum={data($b/To/TransactionNumber)}&amp;tp_company_nm=" + tp_company_nm + "&amp;pagenm=" + pagenm + "')\" > ";
xQuery = xQuery + "<FONT color='#000099'><STRONG>View</STRONG></FONT> </A> ";
xQuery = xQuery + " ) } ";
xQuery = xQuery + "</td>  ";
xQuery = xQuery + "</tr> ";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	TOPLINE = new String(xmlResults);
}

//GET Total Custodians
xQuery = "for $b in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $b/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return  ";
xQuery = xQuery + "count($b/Pedigrees/Pedigree/Custody)";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	totCustodians = new String(xmlResults);
}

xQuery = "for $p in collection('tig:///ePharma/APN')/APN ";
xQuery = xQuery + "where $p/DocumentId = '"+APNID+"'  ";
xQuery = xQuery + "return ";
xQuery = xQuery + "<tr class='tableRow_On'> ";
xQuery = xQuery + "<td align = 'center' class='tp-menu'><input type='checkbox' name='{data($p/Pedigrees/Pedigree/Custody/Name)}'/></td> ";
xQuery = xQuery + "<td class='td-menu'><A href='ViewCustodianDetail.jsp?name={data($p/Pedigrees/Pedigree/Custody/Name)}'>{data($p/Pedigrees/Pedigree/Custody/Name)}</A></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/Address)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/Contact)}</td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/AuthenticatorPhone)}</td> ";
xQuery = xQuery + "<td class='td-menu'><a href='mailto:{data($p/Pedigrees/Pedigree/Custody/AuthenticatorEmail)}'>{data($p/Pedigrees/Pedigree/Custody/AuthenticatorEmail)}</a></td> ";
xQuery = xQuery + "<td class='td-menu'>{data($p/Pedigrees/Pedigree/Custody/InCustodyFromDate)} - {data($p/Pedigrees/Pedigree/Custody/InCustodyToDate)}</td> ";
xQuery = xQuery + "</tr>";

xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	CUSTODY = new String(xmlResults);
}

CloseConnectionTL(connection);
%>

<html>
	<head>
		<title>Raining Data ePharma - ePedigree - APN Manager - Create APN</title>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
			<style type="text/css"></style>
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
			</script>
			<script language="JavaScript" type="text/JavaScript">
<!--
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);

function MM_popupMsg(msg) { //v1.0
  alert(msg);
}
//-->
			</script>
	</head>
	<body>
		<%@include file='topMenu.jsp'%>
		
		<form name="myform" action="abc.do" method="post">
			<table border="0" cellspacing="0" cellpadding="0" width="100%" ID="Table1">
				<tr>
					<td></td>
					<td rowspan="2">&nbsp;</td>
				</tr>
				<tr>
					<td>
						<!-- info goes here -->
						<table width="100%" id="Table2" cellSpacing="1" cellPadding="1" align="left" border="0"
							bgcolor="white">
							<tr bgcolor="white">
								<td class="td-typeblack">Pedigree Detail</td>
								<td class="td-typeblack" align="right"><FONT color="#0000cc">Anti Counterfeit Measures</FONT>
								</td>
							</tr>
							<tr class="tableRow_Header">
								<td colspan="2">
									<table width="100%" id="Table3" cellSpacing="1" cellPadding="1" align="left" border="0"
										bgcolor="white">
										<tr>
											<td colspan="5">
												<table border="1" cellpadding="1" cellspacing="1" width="100%" ID="Table4">
								                       <TR bgcolor="#ccffff">			
									                    <TD class="type-whrite"  align="center"><STRONG><A href="ePedigree_Manager_Reconcile.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Summary</FONT></A></STRONG></TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_ReconcileFromTo.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">From-To</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ProductList.do?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Products</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Manufacturer.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Manufacturer(s)</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite"   bgcolor="gold" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Custody.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Custody</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Status.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Status 
																		History</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Returns.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree&selpedid=<%=(String)session.getAttribute("PedigreeOrder")%>"><FONT color="#000099">Returns</FONT></a></STRONG>
														</TD>
														<TD class="type-whrite" align="center"><STRONG><a href="ePedigree_Manager_Reconcile_Trail.jsp?pedid=<%=APNID%>&tp_company_nm=<%=tp_company_nm%>&pagenm=pedigree"><FONT color="#000099">Audit Trail</FONT></a></STRONG>
														</TD>
													</TR>
												</table>
											</td>
										</tr>
										<%=TOPLINE%>
									</table>
								</td>
							</tr>
							<tr>
								<Td colspan="2"><br>
									<table border="0" cellpadding="0" cellspacing="1" width="100%" ID="Table9">
									</table>
									<TABLE id="Table10" cellSpacing="1" cellPadding="0" width="100%" border="0">
										<TR>
											<TD class="td-typeblack" colSpan="7">Drug Sales &amp; Distribution - <FONT color="#336600">
													Total: <%=totCustodians%></FONT></TD>
										</TR>
										<TR class="tableRow_Header">
											<td class="type-whrite" align=center>Select</td>
											<TD class="type-whrite" height="13">Name</TD>
											<TD class="type-whrite" height="13">Address</TD>
											<TD class="type-whrite" height="13">Contact</TD>
											<TD class="type-whrite" height="13">Phone</TD>
											<TD class="type-whrite" height="13">Email</TD>
											<TD class="type-whrite" height="13">Dates in Custody</TD>
										</TR>
										<%=CUSTODY%>
										<TR class="tableRow_Header">
											<TD class="td-menu" colspan="1" align="center"><a href="javascript:checkAll(document.myform.list)"><FONT color="#ffff66"><STRONG>Check 
															All</STRONG></FONT></a></TD>
											<TD class="td-menu" colspan="2" align="center"><a href=""><FONT color="#ffff66"><STRONG>Uncheck 
															All</STRONG></FONT></a></TD>
											<TD class="td-menu" colspan="4" align="right">&nbsp;<INPUT id="Submit7" type="submit" value="Validate/Authenticate" name="Submit4"></TD>
										</TR>
									</TABLE>
								</Td>
							</tr>
							<TR>
								<TD colSpan="2"><BR>
									<A onclick="MM_openBrWindow('PrintPedigree.jsp?apnId=<%=APNID%>','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										href="#"><IMG height="27" hspace="10" src="../../assets/images/print.gif" width="27" border="0"></A>EXPORT 
									AS: <INPUT id="Submit1" type="submit" value="XML" name="Submit1">&nbsp;&nbsp; <INPUT id="Submit2" type="submit" value="CSV" name="Submit1">&nbsp;&nbsp;&nbsp;<INPUT id="Submit3" type="submit" value="EDI" name="Submit1">&nbsp;&nbsp;
									<INPUT id="Button1" onclick="MM_openBrWindow('DH2129_PedigreeForm.pdf','APN','scrollbars=yes,resizable=yes,width=800,height=600')"
										type="button" value="PDF" name="Button1">&nbsp;&nbsp;
								</TD>
							</TR>
						</table></form>
						<DIV></DIV>
						<div id="footer">
							<table width="100%" height="24" border="0" cellpadding="0" cellspacing="0" ID="Table19">
								<tr>
									<td width="100%" height="24" bgcolor="#d0d0ff" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
										2005. Raining Data.</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>
