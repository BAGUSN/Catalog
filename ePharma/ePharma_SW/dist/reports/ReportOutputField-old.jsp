<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Raining Data ePharma - Reports - Advanced</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../../assets/epedigree1.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_goToURL() { //v3.0
  var i, args=MM_goToURL.arguments; document.MM_returnValue = false;
  for (i=0; i<(args.length-1); i+=2) eval(args[i]+".location='"+args[i+1]+"'");
}
//-->
function goToFilterPage()
{
 
 	ReportOutputForm.submit();
}

var checkflag = "false";

function checkAll() 
{

var field = ReportOutputForm.outputField;
if (checkflag == "false") {
for (i = 0; i < field.length; i++) {
field[i].checked = true;}
checkflag = "true";
return "Uncheck All"; }
else {
for (i = 0; i < field.length; i++) {
field[i].checked = false; }
checkflag = "false";
return "Check All"; }
}


</script>
</head>
<%try{%>
<body>
<jsp:include page="../globalIncludes/TopHeader.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer1.jsp" />
<jsp:include page="../globalIncludes/TopLevelLayer2.jsp" />
<jsp:include page="./LeftNav.jsp" />

<form name="ReportOutputForm" action="DisplayReportFilters.do">
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
      </table></td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <input type="hidden" name="cubeName" value="<%=(String)request.getAttribute("cubeName")%>">
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
              <table width="100%" id="Table1" cellSpacing="1" cellPadding="1" align="left" border="0"
			bgcolor="white" class="td-menu">
 <tr bgcolor="white"> 
                  <td class="td-typeblack">Please choose the Report Format Output Fields</td>
                </tr> 
                <tr> 
                  <td align="left"> <table cellpadding="2" cellspacing="2" border="0" width="100%" id="table9">
	<tr class="tableRow_Off">
		<td>&nbsp;
		Select All:</td>
		<td colspan='3' align='left'><input type='checkbox' onClick="javascript:return checkAll();" name='selectAll' value='selectAll' />
		</td>
	</tr>

        <%
        	String str = (String)request.getAttribute("htmlString");
        	System.out.println("-------str is---"+(String)request.getAttribute("cubeName"));
        %>
	<%=str%>
	<tr class="tableRow_Off">
		<td colspan="4">
		<INPUT name="back" type="button" class="fButton_large" id="back" onClick="javascript:history.back();" value="<< Back" >
		&nbsp;<INPUT name="Next" type="button" class="fButton_large" id="next" onClick="javascript:return goToFilterPage();" value="Next >>" ></td>
	</tr>
</table>

</td>
        </tr>
      </table></div>
<div id="footer" class="td-menu" > 
  <table width="100%" height="24" border="0" cellpadding="0" cellspacing="0">
    <tr> 
      <td width="100%" height="24" bgcolor="#D0D0FF" class="td-menu" align="left">&nbsp;&nbsp;Copyright 
        2005. Raining Data.</td>
    </tr>
  </table>
</div>
</form>
</body>
<%}catch(Exception e){e.printStackTrace();}%>
</html>
