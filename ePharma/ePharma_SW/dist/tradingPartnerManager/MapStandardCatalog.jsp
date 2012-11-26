
<%@ page import="com.rdta.commons.CommonUtil"%>
<%@ page import="com.rdta.catalog.OperationType"%>
<%@ page import="org.w3c.dom.Node"%>
<%@ page import="com.rdta.commons.xml.XMLUtil"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="java.util.List"%>
<%@ page import="com.rdta.catalog.session.TradingPartnerContext"%>
<%@ page import="com.rdta.catalog.session.CatalogContext"%>
<%@ page import="com.rdta.catalog.Constants"%>
<%@ page import="com.rdta.catalog.MappingNodeObject"%>
<%@ page import="com.rdta.catalog.JavaScriptTree"%>

<%@ page import="com.rdta.catalog.DataElementNode"%>



<TITLE>R & D - eList</TITLE>
<HEAD>
<title>Raining Data ePharma - Trading Partner Manager -  Detail</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="./assets/epedigree1.css" rel="stylesheet" type="text/css">

<SCRIPT>
	function redirect() {
		window.location = 'ManageElement.do?catalogGenID=""';
	}



function submitform(operation)
{
	document.mappingsEdit.operationType.value = operation;
	document.mappingsEdit.submit();

	return true;
}

function showValues(selSourceValue, selTargetValue) {

	document.mappingsEdit.selectedSourceName.value = selSourceValue;
	document.mappingsEdit.selectedTargetName.value = selTargetValue;
	document.mappingsEdit.submit();

	return true;

}

function goback()
{

	document.mappingsEdit.operationType.value =  "FIND";
    document.mappingsEdit.action = "TPCatalogList.do";
	document.mappingsEdit.target="_top";
	document.mappingsEdit.submit();

	return true;
}


	
</SCRIPT>
</HEAD>


<%

		MappingNodeObject mappingNodeObject = null;


		String tpName = "";
		String tpGenId  = "";
		String genId = "";
		if(session != null) {
			 mappingNodeObject = (MappingNodeObject) session.getAttribute(Constants.SESSION_CATALOG_MAPPING_CONTEXT);
			 Node mappingNode = mappingNodeObject.getNode();
			 genId = XMLUtil.getValue(mappingNode,"genId");



			TradingPartnerContext tpContext = (TradingPartnerContext)session.getAttribute(Constants.SESSION_TP_CONTEXT); 
			if(tpContext != null) {
				tpName = tpContext.getTpName();
				tpGenId = tpContext.getTpGenId();
			}
		} 

		System.out.println(" tpGenId Id" + tpGenId );

%>


<body>
	<table align="center" border="1" cellspacing="1" cellpadding="1" bgColor="459EBE" width="90%">
		
		<tr bgcolor="white">
			<td align="center"><FONT face="Arial" color="#cc0033" size="2"><STRONG>Selected Attributes</STRONG></FONT></td>
		</tr>
		<tr bgcolor="D3E5ED">
			<td>
				<br>
				
				<form name= "mappingsEdit" action="CatalogMappingEdit.do" method="post"  >

					<input type="hidden" name="mappingGenId" value="<%= genId %>">
					<input type="hidden" name="operationType" value="">

					<INPUT id="hidden10" type="hidden" name="tpGenId" value="<%=tpGenId%>">

					<input type="hidden" name="selectedSourceName" value="">
					<input type="hidden" name="selectedTargetName" value="">



				<table cellSpacing="1" cellPadding="3" align=center>
							<tr  class="titleBlack">
								<td > <b> Select </b></td>
								<td > <b> Source Attribute </b></td>
								<td > <b> Target Attribute </b></td>
								<td > </td>
							</tr>
	
					<%
						System.out.println(" Raju is here");

						List dataElements = mappingNodeObject.getDataElementsList();

						String sourceName = "";
						String targetName = "";
						String checkBoxName = "";

						for(int i=0; i< dataElements.size(); i++)  {

							DataElementNode dataNode = (DataElementNode) dataElements.get(i);
							sourceName = dataNode.getAbsoluteSourceEleName();
							targetName = dataNode.getAbsoluteTargetEleName();
		
							if(targetName != null && targetName.equalsIgnoreCase("NEED_TO_BE_DEFINED_RAJU") ){
								targetName = "";
							}

							checkBoxName = "check" + i;

					%>
							<tr>

								<td> <input type="checkbox" name="<%=checkBoxName%>" value="<%=sourceName%>" /> </td>
								<td > <b> <%= sourceName%> </b></td>

								<td > <b> <%=targetName %> </b></td>

								<%

									if(dataNode.hasValueMap()) {
								%>

									<td> <input type="button"  value="Show Values" onClick="return showValues('<%= sourceName%>',  '<%=targetName %>')">
									
									</td>
								<%
									} else {

								%>

										<td> </td>

								<%
									}
								%>

							</tr>

						<% } %>
					
						<tr>
							<td  colspan="3"><input type="button"  value="Go Back" onClick="return goback()">
							<input type="button" value="Delete" onClick="return submitform('DELETE')">

							<input type="button"  value="Save Mapping" onClick="return submitform('SAVE')"></td>
							
							
						</tr>

					</table>



					<%
						
						String selectedSourceName = request.getParameter("selectedSourceName");
						String selectedTargetName = request.getParameter("selectedTargetName");

						if(selectedSourceName != null && !selectedSourceName.trim().equals("") && selectedTargetName != null && !selectedTargetName.trim().equals("") ) {

							System.out.println(" selectedSourceName " + selectedSourceName );

						Node dataElementNode = mappingNodeObject.getDataElementNodeFromAbsoluteSourceName(selectedSourceName).getNode();

						List valueNodes = XMLUtil.executeQuery(dataElementNode,"values/value");
							

					%>

						<br> 
						<table align="center" cellSpacing="1" cellPadding="3" >

						 	<tr class="titleBlack">
							
								<td align="center" colspan="3"> <%= selectedSourceName %> : <%= selectedTargetName %>  </td>
								
							</tr>
							<tr>
								<td >  Serial. No </td>
								<td >  Source Value </td>
								<td >  Target Value </td>
							</tr>

							
							<%
								Node valueNode ;
								for(int k=0;k<valueNodes.size();k++) {

									valueNode = (Node)valueNodes.get(k);
							%>

							<tr>
								<td> <%= k + 1 %> </td>
								<td > <%= XMLUtil.getValue(valueNode, "@sourceValue")%> </td>
								<td >  <%= XMLUtil.getValue(valueNode, "@targetValue")%></td>
							</tr>

					<%
							}//end of for loop

						}//end of if loop
					%>

						</table>

				</form>
			</td>
			
			


		</tr>
		
	</table>

	
</body>

