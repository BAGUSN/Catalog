package org.apache.jsp.dist.epedigree;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.InputStream;
import java.util.List;
import com.rdta.catalog.Constants;
import com.rdta.epharma.epedigree.action.PedigreeTradingPartnerListForm;
import java.sql.*;
import java.util.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.xml.sax.InputSource;
import com.rdta.tlapi.xql.*;
import com.rdta.util.io.StreamHelper;
import com.rdta.tlapi.xql.Connection;
import com.rdta.tlapi.xql.Statement;
import com.rdta.tlapi.xql.DataSourceProperties;
import com.rdta.tlapi.xql.DataSourceFactory;
import java.text.SimpleDateFormat;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;

public final class pedigreeTradingPartnerList_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {



public com.rdta.tlapi.xql.Connection ConnectTL() {
	com.rdta.tlapi.xql.Connection connection = null;
	
	String hostname = "localhost";
	
	try {
		// Create database connection
		DataSourceProperties properties = DataSourceFactory.getDataSourceProperties();
		properties.getProperty(DataSourceProperties.REMOTE_HOST).setValue( hostname );
		properties.getProperty(DataSourceProperties.REMOTE_PORT).setValue( "3408" );
		properties.getProperty(DataSourceProperties.LISTEN_PORT).setValue( "3444" );
	
		DataSource dataSource = DataSourceFactory.getDataSource(properties);
		connection = dataSource.getConnection("admin", "admin");
		return connection;
	}
	catch(com.rdta.tlapi.xql.XQLException ex) { 
		System.out.print(ex); 
		return connection;
	}

}

 
public String getSessionByClientIP(com.rdta.tlapi.xql.Statement statement, String clientIP) {
	
	byte[] xmlResult = null;
	String usrSessionID = "";
	String query = "";
	query = "for $b in collection('tig:///EAGRFID/SysSessions') ";
	query = query + "where  $b/session/userip = '"+clientIP+"' ";
	query = query + "return data($b/session/sessionid)";
	xmlResult = ReadTL(statement, query);
	
	if(xmlResult != null) {
		usrSessionID = new String(xmlResult);
	}
	return usrSessionID;
}



public com.rdta.tlapi.xql.Statement getStatement(com.rdta.tlapi.xql.Connection connection) {

	com.rdta.tlapi.xql.Statement statement = null;
	try {
		statement = connection.createStatement();
		return statement;
	} catch(com.rdta.tlapi.xql.XQLException ex) { 
			System.out.print(ex); 
			return statement;
	}

}



public void CloseConnectionTL(com.rdta.tlapi.xql.Connection connection) {
	try { connection.logoff();
               connection.close(); } catch(XQLException e)   {}
}



public String ValidateUserSession(com.rdta.tlapi.xql.Statement statement, String sessionID, String clientIP) {

String sessStatus = "";
byte[] xmlResults = null;
String strResult = "";

String xQuery = "for $s in collection('tig://root/EAGRFID/SysSessions/') ";
xQuery = xQuery + "where $s/session/sessionid = '"+sessionID+"' and $s/session/userip = '"+clientIP+"' ";
xQuery = xQuery + "return fn:get-minutes-from-dayTimeDuration ";
xQuery = xQuery + "(xs:dateTime(fn:current-dateTime())- xs:dateTime($s/session/lastuse)) ";
xmlResults = ReadTL(statement, xQuery);
if(xmlResults != null) {
	strResult = new String(xmlResults);

	if(Integer.parseInt(strResult)<15) { //IF LAST USED LESS THAN 15 MINUTES SESSION IS OK

		//MAKE SESSION ID
		SimpleDateFormat df = new SimpleDateFormat();

		//SESSION TIME FORMAT: 2000-01-20T12:00:00 
		df.applyPattern("yyyy-MM-dd");
		String screenEnteredDate = df.format(new java.util.Date());
		df.applyPattern("hh:mm:ss");
		String screenEnteredTime = df.format(new java.util.Date());
		String screenEnteredDT = screenEnteredDate+"T"+screenEnteredTime;

		//UPDATE SESSION ENTRY - REFRESH LAST USE ELEMENT
		xQuery = "for $d in collection('tig://root/EAGRFID/SysSessions/') ";
		xQuery = xQuery+"where $d/session[sessionid/text() = '"+sessionID+"'] ";
		xQuery = xQuery+"return ";
		xQuery = xQuery+"let $newDoc := ( ";
		xQuery = xQuery+"<session> ";
		xQuery = xQuery+"  {$d/session/*:*[ . << $d//lastuse]} ";
		xQuery = xQuery+"  <lastuse>"+screenEnteredDT+"</lastuse> ";
		xQuery = xQuery+"  {$d/session/*:*[ . >> $d//lastuse]} ";
		xQuery = xQuery+"</session> ) ";
		xQuery = xQuery+"return tig:replace-document(document-uri( $d ), $newDoc) ";

		xmlResults = ReadTL(statement, xQuery);
		sessStatus = "VALID";
	} else { //SESSION EXPIRED - RELOGIN
		sessStatus = "EXPIRED";
	}
} else { sessStatus = "EXPIRED"; }

return sessStatus;
}



public byte[] ReadTL(com.rdta.tlapi.xql.Statement statement, String xQuery) {

	byte[] data = null;

	try {
		com.rdta.tlapi.xql.ResultSet resultSet;

		   resultSet = statement.execute(xQuery);
		   if ( resultSet.next() )
		   {
		       InputStream resultStream = resultSet.getBinaryStream();
		       
		       data = StreamHelper.copy(resultStream);  // get the data

		       resultStream.close();
		       resultSet.close();

		   }
		   else
		   {
		       System.out.println( "Query returned no result." );
		       System.out.println("Query: "+xQuery);
		   }

	}
        catch(com.rdta.tlapi.xql.XQLException ex) { 
        	System.out.print(ex); 
        	System.out.print(xQuery);
        	//data = ex.toString().getBytes(); 
        }
        
        catch(java.io.IOException ioex) {  
        	System.out.print(ioex); 
        	System.out.print(xQuery);
        	//data = ioex.toString().getBytes();	
        }
 	
 	
 	return data;       
}




public void executeStatementStream(com.rdta.tlapi.xql.Statement statement, String query,  ByteArrayInputStream stream) throws Exception
        {
            com.rdta.tlapi.xql.ResultSet resultSet;
            byte[] data = null;
            InputStream resultStream = null;

            resultSet = statement.execute(query, stream);

            if ( resultSet.next() )
            {
                // get the data
                //data = StreamHelper.copy(resultStream);
                //resultStream.close();
                resultSet.close();
            }
            else
            {
                System.out.println( "Query returned no result." );
                System.out.println("Query: "+query);
            }
            //java.io.InputStream isResult = new ByteArrayInputStream(data);
            
        }
 


public String ParseUsersForViewUserScreen(byte[] xmlResults, String sessionID) {

	String nodeName = "";
	String userID = "";
	String firstName = "";
	String lastName = "";
	String accessLevel = "";
	String Name = "";
	String ACode = "";
	String Department = "";
	String Facility = "";
	String HTMLROW = "";
	String xmlResultString = "";
		
	try {
		java.io.InputStream isResult = new ByteArrayInputStream(xmlResults);

		//build dom
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbb = dbf.newDocumentBuilder();

		org.w3c.dom.Document doc = dbb.parse(isResult);
		NodeList foundusers = doc.getElementsByTagName("user");

		Node usernode;
		Node userchildnode;

		NodeList userchildnodes;
		int numOfResults = foundusers.getLength();

		if(numOfResults>0) {
			for(int i=0;i<numOfResults;i++)  {
				usernode = foundusers.item(i); //get user Node
				nodeName = usernode.getNodeName();

				if(nodeName.equals("user")) {

					userchildnodes=usernode.getChildNodes();
					for(int j=0;j<userchildnodes.getLength();j++)
					{
						userchildnode =  userchildnodes.item(j);
						if (userchildnode != null) {

							nodeName = userchildnode.getNodeName();

							if(nodeName.equals("id")) {
								userID  = userchildnode.getFirstChild().getNodeValue();
							}

							if(nodeName.equals("firstname")) {
								firstName  = userchildnode.getFirstChild().getNodeValue();
							}

							if(nodeName.equals("lastname")) {
								lastName  = userchildnode.getFirstChild().getNodeValue();
							}

							if(nodeName.equals("accesslevel")) {
								accessLevel  = userchildnode.getFirstChild().getNodeValue();
							}

						} // END IF USER CHILD NODE NOT NULL
					} //END FOR USER CHILD NODES
				}  //END IF USER NODE

				//BUILD HTML ROW
				HTMLROW = HTMLROW +"<TR bgcolor=D3E5ED>";
				HTMLROW = HTMLROW + "<TD><A href='NewUser.jsp?userid="+userID+"&sessionID="+sessionID+"'>"+firstName+" "+lastName+"</A></TD>";
				HTMLROW = HTMLROW + "<TD>"+accessLevel+"</TD>";
				HTMLROW = HTMLROW + "</TR>";

			} //END FOR USER NODES

			if( (!userID.equals("")) & (!firstName.equals("")) & (!lastName.equals("")) & (!accessLevel.equals("")) ) {
				// GOT VALID USER INFO, PROCEED TO MAIN PAGE
				xmlResultString = "userID = "+userID+", firstName = "+firstName+", lastName = "+lastName+", accessLevel = "+accessLevel;
			} else {
				// DIDNT GET USER INFO, REDIRECT TO LOGIN FAILED
				xmlResultString = "Parsed but got bad data.";
			}

		} else {
			//redirect URL to LOGIN FAILED
			xmlResultString = "Got not data from TL.";
		}
		
		return HTMLROW;
		
	} catch (javax.xml.parsers.ParserConfigurationException pce) { System.out.print(pce); return HTMLROW; }
		catch (org.xml.sax.SAXException pce) { System.out.print(pce); return HTMLROW; }
		catch (java.io.IOException ioe) { System.out.print(ioe); return HTMLROW; }
	
}


    public boolean writePicToTL(InputStream fileIS, String locID)
    {
        try {

        String insXML = "<root>";
        insXML = insXML +"<document>{binary {$1}}</document>";
        insXML = insXML +"<locationID>"+ locID + "</locationID>";
        insXML = insXML +"</root>";

        String FullQuery = "declare binary-encoding none; tig:insert-document( 'tig://root/EAGRFID/LocationImage/', "+insXML+" )";
        //executeStatementStreamPic(FullQuery, fileIS);

         return true;

        }    catch(Exception e) {
                    System.err.println(e);
                    return false;
        }
    }


        public void executeStatementStreamPic(String query,  InputStream stream) throws Exception
        {
        
            Connection connection = ConnectTL();
        	Statement statement = getStatement(connection);
        	
            com.rdta.tlapi.xql.ResultSet resultSet;
            byte[] data = null;
            InputStream resultStream = null;
            QueryOption qo = QueryOption.XMLSPACE;
            statement.setQueryOption(qo, "preserve");
            resultSet = statement.execute(query, stream);

            if ( resultSet.next() )
            {
                // get the data
                data = StreamHelper.copy(resultStream);
                resultStream.close();
                resultSet.close();
            }
            else
            {
                System.out.println( "Query returned no result." );
            }
        }


      public String replaceString(String s, String one, String another) {
      // In a string replace one substring with another
      if (s.equals("")) return "";
      String res = "";
      int i = s.indexOf(one,0);
      int lastpos = 0;
      while (i != -1) {
        res += s.substring(lastpos,i) + another;
        lastpos = i + one.length();
        i = s.indexOf(one,lastpos);
      }
      res += s.substring(lastpos);  // the rest
      return res;
      }


      public String getUniqueID(String clientIP) {
     		SimpleDateFormat df = new SimpleDateFormat();
     		df.applyPattern("yyyyMMdd");
     		String idDate = df.format(new java.util.Date());
     		df.applyPattern("hhmmss");
     		String idTime = df.format(new java.util.Date());
		String newID = replaceString(clientIP,".","")+idDate+idTime;
      return newID;
      }



		String genId="";

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(5);
    _jspx_dependants.add("/WEB-INF/struts-html.tld");
    _jspx_dependants.add("/WEB-INF/struts-bean.tld");
    _jspx_dependants.add("/WEB-INF/struts-logic.tld");
    _jspx_dependants.add("/dist/epedigree/../../includes/jspinclude.jsp");
    _jspx_dependants.add("/dist/epedigree/../epedigree/topMenu.jsp");
  }

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_rewrite_action_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_html;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_html_hidden_value_property_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_empty_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_notEqual_value_name;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_iterate_offset_name_length_id;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_bean_write_property_name_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_logic_notEmpty_name;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_html_rewrite_action_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_html = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_html_hidden_value_property_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_empty_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_notEqual_value_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_iterate_offset_name_length_id = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_bean_write_property_name_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_logic_notEmpty_name = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_html_rewrite_action_nobody.release();
    _jspx_tagPool_html_html.release();
    _jspx_tagPool_html_hidden_value_property_nobody.release();
    _jspx_tagPool_logic_empty_name.release();
    _jspx_tagPool_logic_notEqual_value_name.release();
    _jspx_tagPool_logic_iterate_offset_name_length_id.release();
    _jspx_tagPool_bean_write_property_name_nobody.release();
    _jspx_tagPool_logic_notEmpty_name.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function callAction(off){\t\r\n");
      out.write("\tvar frm = document.forms[0];\t\t\r\n");
      out.write("\tfrm.action = \"");
      if (_jspx_meth_html_rewrite_0(_jspx_page_context))
        return;
      out.write("\";\r\n");
      out.write("\tdocument.getElementsByName(\"offset\")[0].value = off;\r\n");
      out.write("\tfrm.submit();\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function checkFunction()\r\n");
      out.write("{\r\n");
      out.write(" \r\n");
      out.write("\tvar allchecks = document.getElementsByName('check');\r\n");
      out.write("\tfor(i=0;i<allchecks.length;i++){\r\n");
      out.write("        allchecks[i].checked=\"true\";}\r\n");
      out.write("}\r\n");
      out.write("    \r\n");
      out.write("</script>\r\n");
      out.write("\r\n");


String tp_company_nm=request.getParameter("tp_company_nm");
String pagenm=request.getParameter("pagenm");
String sessionID = (String)session.getAttribute("sessionID");
List list = (List)request.getAttribute("result");
String recs = com.rdta.catalog.Constants.NO_OF_RECORDS;


      out.write("\r\n");
      out.write("\r\n");

		String offset = (String)request.getAttribute("offset");
		int intOffSet = -1;
		if(offset == null){
			offset = "0";
		}else{
			intOffSet = Integer.parseInt(offset);
			System.out.println("intOffSet in jsp is :"+intOffSet);
		}
		
		int totCount=0;
			
			if( list !=  null ){				
				if(list.size()==0){
					totCount = 0;			
				}else{
					totCount = list.size();
					System.out.println("The totCount is :"+totCount);
				}	
			}
			

      out.write("\r\n");
      out.write("\t\r\n");
 
			int c = 0;
			for(int count=0 ; count<totCount; count++){
				 c = totCount/2;
			 	 int extraCount = totCount - c*2;
				 if(extraCount > 0){
			 		c += 1;
			 		System.out.println("c = "+c);
			 	 } 	 
			} 																		
			int intRecs = Integer.parseInt(recs);
			int dispLast = totCount/intRecs;
			if( totCount % intRecs > 0 ) {
				 dispLast++;
			}
			System.out.println("dispLast in jsp is :"+dispLast);

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      //  html:html
      org.apache.struts.taglib.html.HtmlTag _jspx_th_html_html_0 = (org.apache.struts.taglib.html.HtmlTag) _jspx_tagPool_html_html.get(org.apache.struts.taglib.html.HtmlTag.class);
      _jspx_th_html_html_0.setPageContext(_jspx_page_context);
      _jspx_th_html_html_0.setParent(null);
      int _jspx_eval_html_html_0 = _jspx_th_html_html_0.doStartTag();
      if (_jspx_eval_html_html_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("<head>\r\n");
          out.write("<title>Raining Data ePharma - Receiving Manager - Pedigree Trading Partners</title>\r\n");
          out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\r\n");
          out.write("<link href=\"../../assets/epedigree1.css\" rel=\"stylesheet\" type=\"text/css\">\r\n");
          out.write("<style type=\"text/css\">\r\n");
          out.write("</style>\r\n");
          out.write("\t\r\n");
          out.write("<script language=\"JavaScript\" type=\"text/JavaScript\">\r\n");
          out.write("\r\n");
          out.write("function deleteTP(){\r\n");
          out.write("\tvar checkVal='';\r\n");
          out.write("\tvar allchecks = document.getElementsByName('check');\r\n");
          out.write("\tvar checkSel =false;\r\n");
          out.write("\tfor(i=0;i<allchecks.length;i++){\r\n");
          out.write("\t\tif( allchecks[i].checked ){\r\n");
          out.write("\t\t\tcheckSel=true; \r\n");
          out.write("\t\t\tcheckVal = ( checkVal.length == 0 ? '' : '&' ) +  allchecks[i].name + \"=\"  + allchecks[i].value;\r\n");
          out.write("        }\r\n");
          out.write("\t}\r\n");
          out.write("\tif( checkSel == false ){\r\n");
          out.write("\t\talert(\"Please select the Trading Partner!!! \"); \r\n");
          out.write("\t\treturn false;\r\n");
          out.write("\t}else{\r\n");
          out.write(" \t\t\tfrm =  document.forms[0];\r\n");
          out.write("\t\t\talert(\"Hi\")\r\n");
          out.write("\t\t\treturn true;\r\n");
          out.write("\t}\r\n");
          out.write("\t\t\r\n");
          out.write("}\r\n");
          out.write("\r\n");
          out.write("</script>\r\n");
          out.write("\r\n");
          out.write("</head>\r\n");
          out.write("\r\n");
          out.write("<body>\r\n");
          out.write("<form>\r\n");
          out.write("\t");
          out.write('\r');
          out.write('\n');
	
	//GET PATH TO SERVER SO CAN DYNAMICALLY CREATE HREFS
	String servPath = request.getContextPath();
	servPath = "http://"+request.getServerName()+":"+request.getServerPort()+servPath;     
	System.out.println(" tp_company_nm in toMenu.jsp: "+ tp_company_nm);
	String tpList = request.getParameter("tpList");
	if(tpList == null) tpList = "";

          out.write('\r');
          out.write('\n');
          out.write("\r\n");
          out.write("\r\n");
          out.write(" ");
 
   			String linkName =(String) request.getParameter("linkName"); 
   			if(linkName == null )linkName=(String) session.getAttribute("linkName");
   			else session.setAttribute("linkName",linkName);
   			if(linkName == null ) linkName="";
   			System.out.println("LInk Name : "+linkName);
   			
 
          out.write("\r\n");
          out.write("\r\n");
          out.write("<!-- Top Header -->\r\n");
          out.write("<div id=\"bg\" style=\"Z-INDEX: 100\">\r\n");
          out.write("\t<div class=\"roleIcon-southwood\"></div>\r\n");
          out.write("\t<div class=\"navIcons\">\r\n");
          out.write("\t\t<a href=\"");
          out.print(servPath);
          out.write("/logout.jsp\" target=\"_top\"><img src=\"");
          out.print(servPath);
          out.write("/assets/images/logout.gif\" width=\"34\" height=\"27\" hspace=\"10\" border=\"0\"></a>\r\n");
          out.write("<!--\t<a href=\"");
          out.print(servPath);
          out.write("/dist/epedigree/epedigree.do?pagenm=pedigree&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&linkName=eped\" target=\"_top\"><IMG height=\"27\" hspace=\"10\" src=\"");
          out.print(servPath);
          out.write("/assets/images/inbox.gif\" border=\"0\"></a>-->\r\n");
          out.write("\t\t<img src=\"");
          out.print(servPath);
          out.write("/assets/images/space.gif\" width=\"20\">\r\n");
          out.write("\t</div>\r\n");
          out.write("\t\r\n");
          out.write("\t<div class=\"logo\"><img src=\"");
          out.print(servPath);
          out.write("/assets/images/logos_combined.jpg\"></div>\r\n");
          out.write("</div>\r\n");
          out.write("\r\n");
 String SessionID = (String)session.getAttribute("sessionID");
System.out.println("inside topmenu.jsp");

          out.write("\r\n");
          out.write("\t\r\n");
          out.write("\t<div id=\"menu\" style=\"Z-INDEX: 101\">\r\n");
          out.write("\t\t<table width=\"100%\" height=\"25\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table1\">\r\n");
          out.write("\t\t\t<tr>\r\n");
          out.write("\t\t\t\t<td valign=\"middle\" background=\"");
          out.print(servPath);
          out.write("/assets/images/bg_menu.jpg\"><table width=\"400\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table2\">\r\n");
          out.write("\t\t\t\t\t\t<tr>\r\n");
          out.write("\t\t\t\t\t\t\t");
 if(pagenm.equals("epcadmin")) { 
          out.write("\r\n");
          out.write("\t\t\t\t\t\t\t\t<td width=\"200\" align=\"center\" class=\"primaryNav_On\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
          out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
          out.print(servPath);
          out.write("/AdminUser.do?pagenm=epcadmin&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("\"\" class=\"menu-link menuBlack\" target=\"_parent\">Admin Console</a></td>\r\n");
          out.write("\t\t\t\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\t\t");
 } else { 
          out.write("\r\n");
          out.write("\t\t\t\t\t\t\t\t<td  width=\"200\" align=\"center\" class=\"primaryNav_Off\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
          out.write("\t\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
          out.print(servPath);
          out.write("/AdminUser.do?pagenm=epcadmin&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("\"\" class=\"menu-link menuBlack\" target=\"_parent\">Admin Console</a></td>\r\n");
          out.write("\t\t\t\t\t\t\t");
 } 
          out.write("\t\t\t\t\t\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\t\t<td><img src=\"");
          out.print(servPath);
          out.write("/assets/images/menu_bg1.jpg\" width=\"3\" height=\"23\"></td>\r\n");
          out.write("\t\t\t\t\t\t\t");
 if(pagenm.equals("pedigree")) { 
          out.write("\r\n");
          out.write("\t\t\t\t\t\t\t\t<td  width=\"200\" align=\"center\" class=\"primaryNav_On\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
          out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
          out.print(servPath);
          out.write("/dist/epedigree/PedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&accesslevel=PTP&linkName=PTP\" class=\"menu-link menuBlack\" target=\"_parent\">ePedigree</a></td>\r\n");
          out.write("\t\t\t\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\t\t");
 } else { 
          out.write("\r\n");
          out.write("\t\t\t\t\t\t\t\t<td width=\"200\" align=\"center\" class=\"primaryNav_Off\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
          out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
          out.print(servPath);
          out.write("/dist/epedigree/PedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&accesslevel=PTP&linkName=PTP\"  class=\"menu-link menuBlack\" target=\"_parent\">ePedigree</a></td>\r\n");
          out.write("\t\t\t\t\t\t\t");
 } 
          out.write("\t\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\t\t<!--<td><img src=\"");
          out.print(servPath);
          out.write("/assets/images/menu_bg1.jpg\" width=\"3\" height=\"23\"></td>\r\n");
          out.write("\t\t\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\t\t");
 if(pagenm.equals("pedBank")) { 
          out.write("\r\n");
          out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_On\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
          out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
          out.print(servPath);
          out.write("/dist/pedigreeBankResults.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=pedBank\" class=\"menu-link\" target=\"_parent\">Pedigree Bank \r\n");
          out.write("\t\t\t\t\t\t\t\t\t</a></td>\r\n");
          out.write("\t\t\t\t\t\t\t");
 } else { 
          out.write("\r\n");
          out.write("\t\t\t\t\t\t\t\t<td align=\"center\" class=\"primaryNav_Off\" onmouseover=\"this.style.backgroundColor='#FFCC33';this.style.color='#FFFFFF'\"\r\n");
          out.write("\t\t\t\t\t\t\t\tonmouseout=\"this.style.backgroundColor='';this.style.color=''\"><a href=\"");
          out.print(servPath);
          out.write("/dist/pedigreeBankResults.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=pedBank\" class=\"menu-link\" target=\"_parent\">Pedigree Bank \r\n");
          out.write("\t\t\t\t\t\t\t\t</a></td> ");
 } 
          out.write("\r\n");
          out.write("\t\t\t\t\t\t\t-->\r\n");
          out.write("\t\t\t\t\t\t\t<td><img src=\"");
          out.print(servPath);
          out.write("/assets/images/menu_bg1.jpg\" width=\"3\" height=\"23\"></td>\r\n");
          out.write("\t\t\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\t</tr>\r\n");
          out.write("\t\t\t\t\t</table>\r\n");
          out.write("\t\t\t\t</td>\r\n");
          out.write("\t\t\t</tr>\r\n");
          out.write("\t\t</table>\r\n");
          out.write("\t</div>\r\n");
          out.write("\t\r\n");
          out.write("\t");
 if(pagenm.equals("logist")) { 
          out.write("\r\n");
          out.write("\t\r\n");
          out.write("\t<!-- Left channel -->\r\n");
          out.write("\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
          out.write("\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
          out.write("\t    <tr> \r\n");
          out.write("\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Reports</td>\r\n");
          out.write("\t    </tr>\r\n");
          out.write("\t    <tr> \r\n");
          out.write("\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
          out.write("\t      <td valign=\"top\" class=\"td-left\"><p><br>\r\n");
          out.write("\t\t  <a href=\"index.html\" class=\"typeblue1-link\">Reports</a><br>\r\n");
          out.write("\t\t  <!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> --></a> \r\n");
          out.write("\t\t  <a href=\"Reports_Logistics.html\" class=\"typeblue1-link-sub\">Logistics \r\n");
          out.write("\t\t  Reports</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Logistics_ShipmentProduct.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Shipment \r\n");
          out.write("\t\t  by Product</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Logistics_ShipmentDate.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Shipment \r\n");
          out.write("\t\t  by Date Span</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Logistics_ReceivingProduct.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Receiving \r\n");
          out.write("\t\t  by Product</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Logistics_ReceivingDate.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Receiving \r\n");
          out.write("\t\t  by Date Span</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Logistics_ReturnsProduct.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Returns \r\n");
          out.write("\t\t  by Product</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Logistics_ReturnsDate.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Returns \r\n");
          out.write("\t\t  by Date Span</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ePedigree.html\" class=\"typeblue1-link-sub\">ePedigree \r\n");
          out.write("\t\t  Reports</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ePedigree_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ePedigree \r\n");
          out.write("\t\t  per Product</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ePedigree_DateSpan.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ePedigree \r\n");
          out.write("\t\t  by Date Span</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ePedigree_APNTradingPartner.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;APN \r\n");
          out.write("\t\t  by Trading Partner</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ePedigree_APNDateSpan.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;APN \r\n");
          out.write("\t\t  by Date Span</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ePedigree_Commissioning.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Commissioning \r\n");
          out.write("\t\t  by Product</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ePedigree_FailedCommissioning.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Failed \r\n");
          out.write("\t\t  Commissioning by Product</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ProductRecall.html\" class=\"typeblue1-link-sub\">Product \r\n");
          out.write("\t\t  Recall Reports</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ProductRecall_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Recalls \r\n");
          out.write("\t\t  per Product</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ProductRecall_DateSpan.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Recalls \r\n");
          out.write("\t\t  by Date Span</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ProductRecall_ThreatLevel.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Recalls \r\n");
          out.write("\t\t  by Threat Level</a>\r\n");
          out.write("\t\t  <a href=\"Reports_Trading.html\" class=\"typeblue1-link-sub\">Trading Partner Manager Reports</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Trading_SLAFulfillment.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;SLA fulfillment per Trading Product</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Trading_ShipmentTime.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Shipment Time per 3PL carrier</a><br>\r\n");
          out.write("\r\n");
          out.write("\t\t  <a href=\"Reports_Diversion.html\" class=\"typeblue1-link-sub\">Diversion Reports</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Diversion_ASN3PL.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ASN/RM discrepancy per 3PL</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Diversion_ASNLocation.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ASN/RM discrepancy per Location</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Diversion_TheftLocation.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Theft Alerts per Location</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Diversion_TheftProduct.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Theft Alerts per Product</a><br>\r\n");
          out.write("\r\n");
          out.write("\t\t  <a href=\"Reports_ClinicalTrials.html\" class=\"typeblue1-link-sub\">Clinical Trials Reports</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ClinicalTrials_PatientName.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Alerts by Patient Name</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ClinicalTrials_DateSpan.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Alerts by Date Span</a><br>\r\n");
          out.write("\r\n");
          out.write("\t\t  <a href=\"Reports_Authentication.html\" class=\"typeblue1-link-sub\">Authentication Reports</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Authentication_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Authentication Failures per Product</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_Authentication_ReasonCode.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Authentication Failures per Reason Code</a><br>\r\n");
          out.write("\r\n");
          out.write("\t\t  <a href=\"Reports_ProductIntegrity.html\" class=\"typeblue1-link-sub\">Product Integrity Reports<br>\r\n");
          out.write("\t\t  </a> <a href=\"Reports_ProductIntegrity_TradingPartner.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor \r\n");
          out.write("\t\t  Violations per Trading Partner</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ProductIntegrity_3PL.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor \r\n");
          out.write("\t\t  Violations per 3PL</a><br>\r\n");
          out.write("\t\t  <a href=\"Reports_ProductIntegrity_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor \r\n");
          out.write("\t\t  Violations per Product</a><br>\r\n");
          out.write("\t\t</p></td>\r\n");
          out.write("\t    </tr>\r\n");
          out.write("\t    <tr valign=\"bottom\"> \r\n");
          out.write("\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
          out.print(servPath);
          out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("\t    </tr>\r\n");
          out.write("\t  </table>\r\n");
          out.write("\t</div>\r\n");
          out.write("\r\n");
          out.write("\t<div id=\"rightwhite\"> \r\n");
          out.write("\r\n");
          out.write("\t");
 } else {    
	
	if(pagenm.equals("recall")) {     
          out.write("  \r\n");
          out.write("\t\t<!-- Left channel -->\r\n");
          out.write("\t\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
          out.write("\t\t\t\t<table width=\"170\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
          out.write("\t\t\t\t\t<tr>\r\n");
          out.write("\t\t\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\">ePedigree Manager</td>\r\n");
          out.write("\t\t\t\t\t</tr>\r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("\t\t\t\t</table>\r\n");
          out.write("\t\t\t</div>\r\n");
          out.write("\t\t\t<div id=\"rightwhite\">\r\n");
          out.write("\t\r\n");
          out.write("\t");
 } else { 
	
	if(pagenm.equals("busintel")) {     
          out.write("  \r\n");
          out.write("\t\t<!-- Left channel -->\r\n");
          out.write("\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
          out.write("\t\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
          out.write("\t\t    <tr> \r\n");
          out.write("\t\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Business Intelligence</td>\r\n");
          out.write("\t\t    </tr>\r\n");
          out.write("\t\t    <tr> \r\n");
          out.write("\t\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
          out.write("\t\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
          out.write("\t\t\t<a href=\"index.html\" class=\"typeblue1-link\">Business Intelligence</a><br>\r\n");
          out.write("\t\t\t<!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> --></a> \r\n");
          out.write("\t\t\t<a href=\"BizIntel_Alerts.html\" class=\"typeblue1-link-sub\">Alerts</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Alerts_DuplicateTags.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Duplicate Tags</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Alerts_ExchangedTags.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Exchanged Tags</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Alerts_DecommissionedTags.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Decommissioned Tags Observations</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Alerts_UnusualPurchaseOrders.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Unusual Purchase Orders</a><br>\r\n");
          out.write("\t\t\t<a href=\"BizIntel_Alerts_FailedEPedigrees.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Failed \r\n");
          out.write("\t\t\tePedigrees </a><br>\r\n");
          out.write("\t\t\t<a href=\"BizIntel_Statistics.html\" class=\"typeblue1-link-sub\">Statistics</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Statistics_ePedigreeVerification.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ePedigree Verification</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Statistics_ePedigreeFailures.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;ePedigree Failures</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Statistics_Shipment.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Shipment</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Statistics_Revenue.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Revenue</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Statistics_ExpiredProducts.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Expired Products</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Statistics_RecallProducts.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Recall Products</a><br>\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\t\t\t<a href=\"BizIntel_Sales.html\" class=\"typeblue1-link-sub\">Sales<br>\r\n");
          out.write("\t\t\t</a>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Sales_Geography.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales by Geography</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Sales_Period.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales per Period</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_Sales_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales per product</a><br>\r\n");
          out.write("\r\n");
          out.write("\t\t\t    <a href=\"BizIntel_NewProduct.html\" class=\"typeblue1-link-sub\">New Product Introduction<br>\r\n");
          out.write("\t\t\t</a>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_NewProduct_Geography.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales by Geography</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_NewProduct_Period.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales per Period</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"BizIntel_NewProduct_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sales per product</a><br>\r\n");
          out.write("\r\n");
          out.write("\t\t\t</td>\r\n");
          out.write("\t\t    </tr>\r\n");
          out.write("\t\t    <tr valign=\"bottom\"> \r\n");
          out.write("\t\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
          out.print(servPath);
          out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("\t\t    </tr>\r\n");
          out.write("\t\t  </table>\r\n");
          out.write("\t\t</div>\r\n");
          out.write("\t\t<div id=\"rightwhite\">\r\n");
          out.write("\t\r\n");
          out.write("\t");
 } else {  
	
	if(pagenm.equals("diversion")) {     
          out.write("  \r\n");
          out.write("\t\r\n");
          out.write("\t<!-- Left channel -->\r\n");
          out.write("\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
          out.write("\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
          out.write("\t    <tr> \r\n");
          out.write("\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Diversion</td>\r\n");
          out.write("\t    </tr>\r\n");
          out.write("\t    <tr> \r\n");
          out.write("\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
          out.write("\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
          out.write("\t        <a href=\"index.html\" class=\"typeblue1-link\">Diversion</a><br>\r\n");
          out.write("\t        <!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> --></a> \r\n");
          out.write("\t        <a href=\"Diversion_Search.html\" class=\"typeblue1-link-sub\">Search</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_Search_Pedigree.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Pedigree</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_Search_EPC.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;EPC</a><br>\r\n");
          out.write("\t\t\t\r\n");
          out.write("\t        <a href=\"Diversion_Discrepancy.html\" class=\"typeblue1-link-sub\">ASN/RM Discrepancy</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_Discrepancy_3PL.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;3PL Carrier</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_Discrepancy_ShipFrom.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Ship from Location</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_Discrepancy_ShipTo.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Ship to Location</a><br>\r\n");
          out.write("\t\r\n");
          out.write("\t\r\n");
          out.write("\t        <a href=\"Diversion_TheftAlerts.html\" class=\"typeblue1-link-sub\">Theft Alerts<br>\r\n");
          out.write("\t        </a>\r\n");
          out.write("\t\t\t<a href=\"Diversion_TheftAlerts_ViewEPC.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;View EPC</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_TheftAlerts_ViewPedigree.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;View Pedigree</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_TheftAlerts_ViewAgencyReport.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;View Agency Report</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_TheftAlerts_ViewLocations.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;View Locations</a><br>\r\n");
          out.write("\t       \r\n");
          out.write("\t\t    <a href=\"Diversion_PatientInfo.html\" class=\"typeblue1-link-sub\">Patient Information Search<br>\r\n");
          out.write("\t        </a>\r\n");
          out.write("\t\t\t<a href=\"Diversion_PatientInfo_Name.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Patient Name</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_PatientInfo_ID.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Patient ID</a><br>\r\n");
          out.write("\t\t\t<a href=\"Diversion_PatientInfo_Perscriptions.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Perscriptions</a><br>\r\n");
          out.write("\t\r\n");
          out.write("\t\t\t\r\n");
          out.write("\t\t\t</td>\r\n");
          out.write("\t    </tr>\r\n");
          out.write("\t    <tr valign=\"bottom\"> \r\n");
          out.write("\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
          out.print(servPath);
          out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("\t    </tr>\r\n");
          out.write("\t  </table>\r\n");
          out.write("\t</div>\r\n");
          out.write("\t\r\n");
          out.write("\t<div id=\"rightwhite\">\r\n");
          out.write("\t\r\n");
          out.write("\t\r\n");
          out.write("\t");
 } else { 
	if(pagenm.equals("integrity")) {     
          out.write(" \r\n");
          out.write("\t\r\n");
          out.write("\t\t<!-- Left channel -->\r\n");
          out.write("\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
          out.write("\t\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
          out.write("\t\t    <tr> \r\n");
          out.write("\t\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Product Integrity</td>\r\n");
          out.write("\t\t    </tr>\r\n");
          out.write("\t\t    <tr> \r\n");
          out.write("\t\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
          out.write("\t\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
          out.write("\t\t\t<a href=\"index.html\" class=\"typeblue1-link\">Product Integrity</a><br>\r\n");
          out.write("\t\t\t<!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> --></a> \r\n");
          out.write("\t\t\t<a href=\"ProductIntegrity_Search.html\" class=\"typeblue1-link-sub\">Search</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"ProductIntegrity_Search_EPC.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;EPC</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"ProductIntegrity_Search_Pedigree.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Pedigree</a><br>\r\n");
          out.write("\t\t\t<a href=\"ProductIntegrity_SensorViolations.html\" class=\"typeblue1-link-sub\">Sensor \r\n");
          out.write("\t\t\tViolations </a><br>\r\n");
          out.write("\t\t\t\t<a href=\"ProductIntegrity_SensorViolations_Temperature.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Temperature</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"ProductIntegrity_SensorViolations_Vibration.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Vibration</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"ProductIntegrity_SensorViolations_Humidity.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Humidity</a><br>\r\n");
          out.write("\t\t\t<a href=\"ProductIntegrity_Reports.html\" class=\"typeblue1-link-sub\">Reports<br>\r\n");
          out.write("\t\t\t</a>\r\n");
          out.write("\t\t\t\t<a href=\"ProductIntegrity_Reports_TradingPartner.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor Violations per Trading Partner</a><br>\r\n");
          out.write("\t\t\t\t<a href=\"ProductIntegrity_Reports_3PL.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor Violations per 3PL</a><br>\r\n");
          out.write("\t\t\t<a href=\"ProductIntegrity_Reports_Product.html\" class=\"typeblue1-link-sub-sub\">&nbsp;&nbsp;&nbsp;Sensor \r\n");
          out.write("\t\t\tViolations per Product</a><br>\r\n");
          out.write("\t\t\t\t</td>\r\n");
          out.write("\t\t    </tr>\r\n");
          out.write("\t\t    <tr valign=\"bottom\"> \r\n");
          out.write("\t\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
          out.print(servPath);
          out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("\t\t    </tr>\r\n");
          out.write("\t\t  </table>\r\n");
          out.write("\t\t</div>\r\n");
          out.write("\r\n");
          out.write("\t\t<div id=\"rightwhite\">\r\n");
          out.write("\t\r\n");
          out.write("\t");
 } else { 
	
	if(pagenm.equals("reports")) { 
          out.write("\r\n");
          out.write("\t\t<!-- Left channel -->\r\n");
          out.write("\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
          out.write("\t\t\t<table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
          out.write("\t\t\t\t<tr>\r\n");
          out.write("\t\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\">Reports</td>\t\t\t\r\n");
          out.write("\t\t\t\t</tr>\r\n");
          out.write("\t\t\t\t<tr>\r\n");
          out.write("\t\t\t\t\t<td width=\"10\" valign=\"top\" bgcolor=\"#dcdcdc\"></td>\r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("\t\t\t\t</tr>\r\n");
          out.write("\t\t\t\t<tr valign=\"bottom\">\r\n");
          out.write("\t\t\t\t\t<td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
          out.print(servPath);
          out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("\t\t\t\t</tr>\r\n");
          out.write("\t\t\t</table>\r\n");
          out.write("\t\t</div>\r\n");
          out.write("\t\t<div id=\"rightwhite2\">\r\n");
          out.write("\t\t\r\n");
          out.write("\t");
 }  else { 
	
	if(pagenm.equals("pedBank")) { 
          out.write("\r\n");
          out.write("\t\t<!-- Left channel -->\r\n");
          out.write("\t\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
          out.write("\t\t\t\t<table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
          out.write("\t\t\t\t\t<tr>\r\n");
          out.write("\t\t\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\">ePedigree Manager</td>\r\n");
          out.write("\t\t\t\t\t</tr>\r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("              <tr valign=\"bottom\">\r\n");
          out.write("\t\t\t\t\t<td   colspan=\"2\" class=\"td-left\"><img src=\"");
          out.print(servPath);
          out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("\t\t\t\t</tr>\r\n");
          out.write("\t\t\t\t</table>\r\n");
          out.write("\t\t\t</div>\r\n");
          out.write("\t\t\t<div id=\"rightwhite2\">\r\n");
          out.write("\t\r\n");
          out.write("\t");
 } else {  
		if(pagenm.equals("track")) { 
          out.write("\r\n");
          out.write("\t\t<!-- Left channel -->\r\n");
          out.write("\t\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
          out.write("\t\t\t\t<table width=\"170\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
          out.write("\t\t\t\t\t<tr>\r\n");
          out.write("\t\t\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\" align=\"center\">Track and Trace</td>\r\n");
          out.write("\t\t\t\t\t</tr>\r\n");
          out.write("\r\n");
          out.write("\t\t\t\t</table>\r\n");
          out.write("\t\t\t</div>\r\n");
          out.write("\t\t\t<div id=\"rightwhite2\"></div>\r\n");
          out.write("\t");
 }  
		if(pagenm.equals("returns")) { 
          out.write("\r\n");
          out.write("\t\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
          out.write("\t\t\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
          out.write("\t\t\t    <tr> \r\n");
          out.write("\t\t\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Returns</td>\r\n");
          out.write("\t\t\t    </tr>\r\n");
          out.write("\t\t\t    <tr> \r\n");
          out.write("\t\t\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
          out.write("\t\t\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
          out.write("\t\t\t\t  \t<a href=\"../returns/returns.jsp?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=returns\" class=\"typeblue1-link\">ITEM</a><br>\r\n");
          out.write("\t\t\t\t\t<a href=\"../returns/returns_EPC.jsp?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=returns\" class=\"typeblue1-link\">CASE</a><br>\r\n");
          out.write("\t\t\t        <!-- <a href=\"ePedigree.html\" class=\"typeblue1-link-sub\">Dashboard</a><br> -->        </td>\r\n");
          out.write("\t\t\t    </tr>\r\n");
          out.write("\t\t\t    <tr valign=\"bottom\"> \r\n");
          out.write("\t\t\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"../assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("\t\t\t    </tr>\r\n");
          out.write("\t\t\t  </table>\r\n");
          out.write("\t\t\t</div>\r\n");
          out.write("\t\t\t\r\n");
          out.write("\t\t<div id=\"rightwhite2\"> \r\n");
          out.write("\t\t\r\n");
          out.write("\t");
 } else {  
          out.write('\r');
          out.write('\n');
          out.write('	');
 System.out.println("************************************************************Inside Top menu left jsp********");
          out.write('\r');
          out.write('\n');
          out.write('	');
 if(pagenm.equals("TPManager")) { 
          out.write("\r\n");
          out.write("\t\t<!-- Left channel -->\r\n");
          out.write("\t\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\"> \r\n");
          out.write("\t\t  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
          out.write("\t\t    <tr> \r\n");
          out.write("\t\t      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">Vendor Information Manager</td>\r\n");
          out.write("\t\t    </tr>\r\n");
          out.write("\t\t    <tr> \r\n");
          out.write("\t\t      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
          out.write("\t\t      <td valign=\"top\" class=\"td-left\"><br>\r\n");
          out.write("\t\t        <a href=\"TradingPartnerList.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=TPManager&linkName=tpmanager\" class=");
          out.print((linkName.equals("tpmanager"))?"typered4-link":"typeblue3-link");
          out.write("><strong> Vendor Information Manager</strong></a><br> \r\n");
          out.write("\t\t        ");
 if(tpList.equalsIgnoreCase("view")&&tpList != null){
		          String tpGenId1=request.getParameter("tpGenId");
String  tpName1=request.getParameter("tpName");
		        if(tpGenId1==null)tpGenId1="";
		        if(tpName1==null)tpName1="";
		        
		        
		         
          out.write("\r\n");
          out.write("\t\t     &nbsp;&nbsp;&nbsp;&nbsp;   <A href=\"TradingPartnerView.do?operationType=FIND&tpGenId=");
          out.print(tpGenId1);
          out.write("&tpName=");
          out.print(tpName1);
          out.write("&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=");
          out.print(pagenm);
          out.write("&tpList=view&linkName=tpmain\" class=");
          out.print((linkName.equals("tpmain"))?"typered4-link":"typeblue3-link");
          out.write("><strong>Main </strong></A>&nbsp;&nbsp;&nbsp;&nbsp;<br/>\r\n");
          out.write("\t\t\t &nbsp;&nbsp;&nbsp;&nbsp;\t<A href=\"TPLocationList.do?tpGenId=");
          out.print(tpGenId1);
          out.write("&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=");
          out.print(pagenm);
          out.write("&tpList=view&tlList=view&linkName=tpLocation\" class=");
          out.print((linkName.equals("tpLocation"))?"typered4-link":"typeblue3-link");
          out.write(" onClick=\"return LocationReadPrivilage();\"><strong>Locations </strong></A>&nbsp;&nbsp;&nbsp;&nbsp;<br/>\r\n");
          out.write("\t\t\t          ");

			          String tlList1=request.getParameter("tlList");
			          if(tlList1==null)tlList1="";
			          if(tlList1.equalsIgnoreCase("view"))
			          {
			          
          out.write("\r\n");
          out.write("\t\t\t  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;         <a href=\"LocationNew.do?tpGenId=");
          out.print(tpGenId1);
          out.write("&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=");
          out.print(pagenm);
          out.write("&tlList=view&tpList=view&linkName=createLoc\" class=");
          out.print((linkName.equals("createLoc"))?"typered4-link":"typeblue3-link");
          out.write(" >Create New Location</a><br> \r\n");
          out.write("\t\t\t\r\n");
          out.write("\t\t\t          ");
}
          out.write("\r\n");
          out.write("\t\t\t \r\n");
          out.write("\t\t\t ");

			 
			 String tcList1=request.getParameter("tcList");
			 if(tcList1==null)tcList1="";
			 if(tcList1.equalsIgnoreCase("view"))
			 {
			 
          out.write("\r\n");
          out.write("\t\t\t  &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;    <a href=\"CatalogNew.do?tpGenId=");
          out.print(tpGenId1);
          out.write("&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=");
          out.print(pagenm);
          out.write("&tpList=view&tcList=view&linkName=tpNewCatalog\" class=");
          out.print((linkName.equals("tpNewCatalog"))?"typered4-link":"typeblue3-link");
          out.write(" >Create New Catalog</a><br>\r\n");
          out.write("\t\t\t ");
}
			 
          out.write("\r\n");
          out.write("\t\t\t \r\n");
          out.write("\t\t\t ");

			 String catSchema=request.getParameter("catSchema");
			 if(catSchema==null)catSchema="";
			 if(catSchema.equalsIgnoreCase("view"))
			 {
			 
			 genId=request.getParameter("catalogGenId");
			  if(genId==null)genId="";
			 
          out.write("\r\n");
          out.write("\t\t &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; \t <a href=\"OpenCatalogSchemaDef.do?fromModule=TP&catalogGenId=");
          out.print( genId);
          out.write("&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=");
          out.print(pagenm);
          out.write("&tpList=view&tcList=view&linkName=catSchemaDef\" class=");
          out.print((linkName.equals("catSchemaDef"))?"typered4-link":"typeblue3-link");
          out.write(" onClick=\"return CatalogAccessPrivilage()\">\r\n");
          out.write("\t\t\t\t\tSchema Def</a><br>\r\n");
          out.write("\t\t\t\t &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;\t  <a href=\"SelectMasterCatalog.do?leftCatalogGenId=");
          out.print( genId);
          out.write("&tpGenId=");
          out.print(tpGenId1);
          out.write("&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=");
          out.print(pagenm);
          out.write("&tpList=view&tcList=view&linkName=MapToStandard\" class=");
          out.print((linkName.equals("MapToStandard"))?"typered4-link":"typeblue3-link");
          out.write(" onClick=\"return CatalogAccessPrivilage()\">Map To Standard </a> <br>\r\n");
          out.write("\t\t\t \r\n");
          out.write("\t\t\t ");

			 }
			 
          out.write("\r\n");
          out.write("\t\t\t \r\n");
          out.write("\t\t  \t\t");
 } 
          out.write("\r\n");
          out.write("\t\t\t<a href=\"TradingPartnerNew.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=TPManager&linkName=createTP\" class=");
          out.print((linkName.equals("createTP"))?"typered4-link":"typeblue3-link");
          out.write(" >Create New Vendor</a><br>\r\n");
          out.write("\t\t      </td>\r\n");
          out.write("\t\t    </tr>\r\n");
          out.write("\t\t    <tr valign=\"bottom\"> \r\n");
          out.write("\t\t      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"./assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("\t\t    </tr>\r\n");
          out.write("\t\t  </table>\r\n");
          out.write("\t\t</div>\r\n");
          out.write("\t");
 } else if(pagenm.equals("Catalog")) {  
          out.write("\r\n");
          out.write("\t\r\n");
          out.write("\t\r\n");
          out.write("\t<div id=\"leftgray4\"> \r\n");
          out.write("  <table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
          out.write("    <tr> \r\n");
          out.write("      <td width=\"170\" colspan=\"2\" class=\"td-leftred\">GCPIM</td>\r\n");
          out.write("    </tr>\r\n");
          out.write("    <tr> \r\n");
          out.write("      <td width=\"10\" valign=\"top\" bgcolor=\"#DCDCDC\"></td>\r\n");
          out.write("      <td valign=\"top\" class=\"td-left\" ><br>\r\n");
          out.write("  \r\n");
          out.write("        <a href=\"ProductMasterSearchEmpty.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=Catalog&linkName=pSearch\" class=");
          out.print((linkName.equals("pSearch"))?"typered4-link":"typeblue3-link");
          out.write(">Product Search</a><br> \r\n");
          out.write("\t\t<a href=\"ShowMasterCatalogs.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=Catalog&linkName=newProduct\" class=");
          out.print((linkName.equals("newProduct"))?"typered4-link":"typeblue3-link");
          out.write(">Create New Product</a><br>\r\n");
          out.write("\t\t<a href=\"KitNew.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=Catalog&linkName=newKit\" class=");
          out.print((linkName.equals("newKit"))?"typered4-link":"typeblue3-link");
          out.write(">Create New Kit</a><br>\r\n");
          out.write("\t\t<a href=\"GCPIMOpenCatalogSchemaDef.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=Catalog&linkName=editMaster\" class=");
          out.print((linkName.equals("editMaster"))?"typered4-link":"typeblue3-link");
          out.write(">Edit Master Product Schema</a><br>\r\n");
          out.write("     \t\r\n");
          out.write("     \t");
 if(linkName.equals("manageStandard")||linkName.equals("newCatalog")){ 
          out.write("\r\n");
          out.write("\t\t\t\t<a href=\"GCPIMStandardCatalogList.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=Catalog&linkName=manageStandard\" class=");
          out.print((linkName.equals("manageStandard"))?"typered4-link":"typeblue3-link");
          out.write(">Manage Standard Catalogs</a><br>\r\n");
          out.write("\t\t\r\n");
          out.write("\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"GCPIMStandardCatalogNew.do?operationType=ADD&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=Catalog&linkName=newCatalog\" class=");
          out.print((linkName.equals("newCatalog"))?"typered4-link":"typeblue3-link");
          out.write(">Create New Catalog</a>\r\n");
          out.write("\t\t\t");
}else{
          out.write("\r\n");
          out.write("\t\t\t\r\n");
          out.write("\t\t\t\t<a href=\"GCPIMStandardCatalogList.do?tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&pagenm=Catalog&linkName=manageStandard\" class=");
          out.print((linkName.equals("manageStandard"))?"typered4-link":"typeblue3-link");
          out.write(">Manage Standard Catalogs</a><br>\r\n");
          out.write("\t\t\t");
}
          out.write("\r\n");
          out.write("\t  </td>\r\n");
          out.write("    </tr>\r\n");
          out.write("    <tr valign=\"bottom\"> \r\n");
          out.write("      <td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"./assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("    </tr>\r\n");
          out.write("  </table>\r\n");
          out.write("</div>\r\n");
          out.write("\t\r\n");
          out.write("\t\r\n");
          out.write("\t\r\n");
          out.write("\t");
}else{ 
          out.write("\r\n");
          out.write("\t\r\n");
          out.write("\t\r\n");
          out.write("\t");
 if(pagenm.equals("pedigree")) { 
          out.write("\r\n");
          out.write("\t<!-- Left channel -->\r\n");
          out.write("\t<div id=\"leftgray2\" style=\"Z-INDEX: 104\">\r\n");
          out.write("\t\t<table width=\"170\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" ID=\"Table5\">\r\n");
          out.write("\t\t\t<tr>\r\n");
          out.write("\t\t\t\t<td width=\"170\" colspan=\"2\" class=\"td-leftred\">ePedigree Manager</td>\r\n");
          out.write("\t\t\t</tr>\r\n");
          out.write("\t\t\t<tr>\r\n");
          out.write("\t\t\t\t<td width=\"10\" valign=\"top\" bgcolor=\"#dcdcdc\"></td>\r\n");
          out.write("\t\t\t\t");
 //if (accessLevel.equals("true")){
          out.write("\r\n");
          out.write("\t\t\t\t<td valign=\"top\" class=\"td-left\"><br>\r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("\t\t\t\t<!--\t<a href=\"");
          out.print(servPath);
          out.write("/dist/epedigree/ShippingManagerSearchEmpty.do?pagenm=pedigree&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&linkName=shippingmang\" class=");
          out.print((linkName.equals("shippingmang"))?"typered4-link":"typeblue3-link");
          out.write(">Shipping \r\n");
          out.write("\t\t\t\t\t\tManager<br>\r\n");
          out.write("\t\t\t\t\t</a>\r\n");
          out.write("\t\t\t\t\t<br>\r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t <a href=\"#\" class=\"typeblue3-link\">Find</a><br> \r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t<a href=\"../epedigree/PedigreeSearch.do?pagenm=pedigree&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&accesslevel=apnsearch&linkName=apnsear\" class=");
          out.print((linkName.equals("apnsear"))?"typered4-link":"typeblue3-link");
          out.write(">Search \r\n");
          out.write("\t\t\t\t\t\tPedigrees</a><br>\r\n");
          out.write("\t\t\t\t\t\t<br>-->\r\n");
          out.write("\t\t\t\t\t<a href=\"../epedigree/PedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&accesslevel=PTP&linkName=PTP\" class=");
          out.print((linkName.equals("PTP"))?"typered4-link":"typeblue3-link");
          out.write(">Add Pedigree Trading Partner \r\n");
          out.write("\t\t\t\t\t\t</a><br><br>\r\n");
          out.write("\t\t\t\t\t\t<a href=\"../epedigree/ListPedigreeTradingPartner.do?pagenm=pedigree&tp_company_nm=");
          out.print(tp_company_nm);
          out.write("&accesslevel=PTP&linkName=ListPTP\" class=");
          out.print((linkName.equals("ListPTP"))?"typered4-link":"typeblue3-link");
          out.write(">List Pedigree Trading Partner \r\n");
          out.write("\t\t\t\t\t\t</a><br>\r\n");
          out.write("\t\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t\r\n");
          out.write("\t\t\t\t\t");
//}
          out.write("\r\n");
          out.write("\t\t\t</tr>\r\n");
          out.write("\t\t\t<tr valign=\"bottom\">\r\n");
          out.write("\t\t\t\t<td height=\"80\" colspan=\"2\" class=\"td-left\"><img src=\"");
          out.print(servPath);
          out.write("/assets/images/logo_poweredby.gif\" width=\"150\" height=\"37\"></td>\r\n");
          out.write("\t\t\t</tr>\r\n");
          out.write("\t\t\t");
 System.out.println("************************************************************After Top menu left jsp********");
          out.write("\r\n");
          out.write("\t\t</table>\r\n");
          out.write("\t</div>\r\n");
          out.write("\t");
 } 
          out.write("\r\n");
          out.write("\t<div id=\"rightwhite2\">\r\n");
          out.write("\r\n");
          out.write("\t");
 } } } } } } } } }
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write("\r\n");
          out.write('\r');
          out.write('\n');
          out.write('	');
          //  html:hidden
          org.apache.struts.taglib.html.HiddenTag _jspx_th_html_hidden_0 = (org.apache.struts.taglib.html.HiddenTag) _jspx_tagPool_html_hidden_value_property_nobody.get(org.apache.struts.taglib.html.HiddenTag.class);
          _jspx_th_html_hidden_0.setPageContext(_jspx_page_context);
          _jspx_th_html_hidden_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_html_hidden_0.setProperty("pagenm");
          _jspx_th_html_hidden_0.setValue(pagenm);
          int _jspx_eval_html_hidden_0 = _jspx_th_html_hidden_0.doStartTag();
          if (_jspx_th_html_hidden_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_html_hidden_value_property_nobody.reuse(_jspx_th_html_hidden_0);
          out.write('\r');
          out.write('\n');
          out.write('	');
          //  html:hidden
          org.apache.struts.taglib.html.HiddenTag _jspx_th_html_hidden_1 = (org.apache.struts.taglib.html.HiddenTag) _jspx_tagPool_html_hidden_value_property_nobody.get(org.apache.struts.taglib.html.HiddenTag.class);
          _jspx_th_html_hidden_1.setPageContext(_jspx_page_context);
          _jspx_th_html_hidden_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_html_hidden_1.setProperty("offset");
          _jspx_th_html_hidden_1.setValue(pagenm);
          int _jspx_eval_html_hidden_1 = _jspx_th_html_hidden_1.doStartTag();
          if (_jspx_th_html_hidden_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_html_hidden_value_property_nobody.reuse(_jspx_th_html_hidden_1);
          out.write("\t\r\n");
          out.write("\t<table width=\"100%\" id=\"Table1\" cellSpacing=\"1\" cellPadding=\"1\" align=\"left\" border=\"0\" bgcolor=\"white\">\r\n");
          out.write("\t<tr>\r\n");
          out.write("\t\t<td class=\"td-rightmenu\" vAlign=\"middle\" width=\"1%\"><IMG height=\"10\" src=\"../../assets/images/space.gif\" width=\"10\"></td>\r\n");
          out.write("\t\t<td class=\"td-rightmenu\" vAlign=\"middle\" width=\"99%\">\r\n");
          out.write("\t\t\t<table id=\"Table7\" cellSpacing=\"0\" cellPadding=\"0\" width=\"100%\" border=\"0\">\r\n");
          out.write("\t\t\t\t<tr>\r\n");
          out.write("\t\t\t\t\t<td align=\"left\"></td>\r\n");
          out.write("\t\t\t\t\t<td align=\"right\"><!-- <img src=\"../../assets/images/3320.jpg\" width=\"200\" height=\"27\"> --><IMG src=\"../../assets/images/space.gif\" width=\"5\"></td>\r\n");
          out.write("\t\t\t\t</tr>\r\n");
          out.write("\t\t\t</table>\r\n");
          out.write("\t\t</td>\r\n");
          out.write("\t</tr>\r\n");
          out.write("\t<tr bgcolor=\"white\">\r\n");
          out.write("\t\t<td class=\"type-red\" colspan=\"2\">LIST OF PEDIGREE TRADING PARTNERS </td>\r\n");
          out.write("\t</tr>\r\n");
          out.write("\t<tr > <td height=\"15\">&nbsp;</td> </tr>\r\n");
          out.write("\t<tr>\r\n");
          out.write("\t\t<td colspan=\"2\">\r\n");
          out.write("\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"1\" width=\"100%\">\r\n");
          out.write("\t\t\t<tr class=\"tableRow_Header\">\r\n");
          out.write("\t\t\t\t<td class=\"title\" align=\"center\" height=\"15\" width=\"10%\">Select</td>\r\n");
          out.write("\t\t\t\t<td class=\"title\" align=\"center\" height=\"15\" width=\"25%\">Pedigree Trading Partner Name:</td>\r\n");
          out.write("\t\t\t\t<td class=\"title\" align=\"center\" height=\"15\" width=\"25%\">DEA Number:</td>\t\r\n");
          out.write("\t\t\t</tr>\r\n");
          out.write("\t\t\t");
          if (_jspx_meth_logic_empty_0(_jspx_th_html_html_0, _jspx_page_context))
            return;
          out.write("\r\n");
          out.write("\t\r\n");
          out.write("\t\t \t");
          //  logic:notEqual
          org.apache.struts.taglib.logic.NotEqualTag _jspx_th_logic_notEqual_0 = (org.apache.struts.taglib.logic.NotEqualTag) _jspx_tagPool_logic_notEqual_value_name.get(org.apache.struts.taglib.logic.NotEqualTag.class);
          _jspx_th_logic_notEqual_0.setPageContext(_jspx_page_context);
          _jspx_th_logic_notEqual_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_logic_notEqual_0.setName("result");
          _jspx_th_logic_notEqual_0.setValue("0");
          int _jspx_eval_logic_notEqual_0 = _jspx_th_logic_notEqual_0.doStartTag();
          if (_jspx_eval_logic_notEqual_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("\t\t\t\t");
              //  logic:iterate
              org.apache.struts.taglib.logic.IterateTag _jspx_th_logic_iterate_0 = (org.apache.struts.taglib.logic.IterateTag) _jspx_tagPool_logic_iterate_offset_name_length_id.get(org.apache.struts.taglib.logic.IterateTag.class);
              _jspx_th_logic_iterate_0.setPageContext(_jspx_page_context);
              _jspx_th_logic_iterate_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_notEqual_0);
              _jspx_th_logic_iterate_0.setName(Constants.PTP_DETAILS);
              _jspx_th_logic_iterate_0.setId("ptp");
              _jspx_th_logic_iterate_0.setLength(recs);
              _jspx_th_logic_iterate_0.setOffset(offset);
              int _jspx_eval_logic_iterate_0 = _jspx_th_logic_iterate_0.doStartTag();
              if (_jspx_eval_logic_iterate_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
                java.lang.Object ptp = null;
                if (_jspx_eval_logic_iterate_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
                  out = _jspx_page_context.pushBody();
                  _jspx_th_logic_iterate_0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
                  _jspx_th_logic_iterate_0.doInitBody();
                }
                ptp = (java.lang.Object) _jspx_page_context.findAttribute("ptp");
                do {
                  out.write("\r\n");
                  out.write("\t\t\t\t<tr class=\"tableRow_Off\" >\r\n");
                  out.write("\t\t\t\t \t<td align=\"center\" height=\"15\" width=\"5%\">\r\n");
                  out.write("\t\t\t\t\t\t<input type='checkbox' name='check' value='");
                  if (_jspx_meth_bean_write_0(_jspx_th_logic_iterate_0, _jspx_page_context))
                    return;
                  out.write("' />\r\n");
                  out.write("\t\t\t\t\t</td>\r\n");
                  out.write("\t\t\t\t\t<td align=\"center\" height=\"15\" width=\"25%\"><a href='/ePharma/dist/epedigree/PedigreeTradingPartnerDetails.do?deaNumber=");
                  if (_jspx_meth_bean_write_1(_jspx_th_logic_iterate_0, _jspx_page_context))
                    return;
                  out.write('\'');
                  out.write('>');
                  out.write(' ');
                  if (_jspx_meth_bean_write_2(_jspx_th_logic_iterate_0, _jspx_page_context))
                    return;
                  out.write("</td>\r\n");
                  out.write("\t\t\t \t\t<td align=\"center\" height=\"15\" width=\"25%\">");
                  if (_jspx_meth_bean_write_3(_jspx_th_logic_iterate_0, _jspx_page_context))
                    return;
                  out.write("</td> \t\r\n");
                  out.write("\t\t\t \t</tr>\r\n");
                  out.write("\t\t \t");
                  int evalDoAfterBody = _jspx_th_logic_iterate_0.doAfterBody();
                  ptp = (java.lang.Object) _jspx_page_context.findAttribute("ptp");
                  if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                    break;
                } while (true);
                if (_jspx_eval_logic_iterate_0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE)
                  out = _jspx_page_context.popBody();
              }
              if (_jspx_th_logic_iterate_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
                return;
              _jspx_tagPool_logic_iterate_offset_name_length_id.reuse(_jspx_th_logic_iterate_0);
              out.write("\r\n");
              out.write(" \t\r\n");
              out.write("\t\t \t<tr class=\"tableRow_On\">\r\n");
              out.write("\t\t\t</tr>\r\n");
              out.write("\t\t \t");
 if(totCount > Integer.parseInt(recs)) {
              out.write("\r\n");
              out.write("\t\t\t\t<a href=\"javascript:callAction(0)\">First</a>\r\n");
              out.write("\t\t\t\t<a href=\"javascript:callAction(");
              out.print((dispLast-1)*intRecs);
              out.write(")\">Last</a>\r\n");
              out.write("\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
              out.write("\t\t\t\t");
 if(intOffSet+intRecs < totCount){	
              out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t \r\n");
              out.write("\t\t\t\t\t<a href=\"javascript:callAction(");
              out.print(intOffSet+intRecs);
              out.write(")\">Next</a>\r\n");
              out.write("\t\t\t\t");
 } 
              out.write("\t\r\n");
              out.write("\t\t\t\t");
 if(intOffSet > 0) { 
              out.write("\r\n");
              out.write("\t\t\t\t\t<a href=\"javascript:callAction(");
              out.print(intOffSet-intRecs);
              out.write(")\">Previous</a>\r\n");
              out.write("\t\t\t\t");
 } 
              out.write("\r\n");
              out.write("\t\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
              out.write("\t\t\t\t(Displaying records ");
              out.print(intOffSet+1);
              out.write(" to ");
              out.print( (intOffSet+intRecs) > totCount ? totCount : (intOffSet+intRecs));
              out.write(" of ");
              out.print(totCount);
              out.write(" matching the search criteria)\r\n");
              out.write("\t\t\t\t<br>\r\n");
              out.write("\t\t\t\t<br>\r\n");
              out.write("\t\t\t");
 } 
              out.write("\t\t\r\n");
              out.write("\t\t \t");
              int evalDoAfterBody = _jspx_th_logic_notEqual_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_logic_notEqual_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_logic_notEqual_value_name.reuse(_jspx_th_logic_notEqual_0);
          out.write("\r\n");
          out.write(" \t\r\n");
          out.write("\t\r\n");
          out.write("\t\t</td>\r\n");
          out.write("\t</tr>  \r\n");
          out.write(" </table>\r\n");
          out.write(" <br>\r\n");
          out.write(" <br>\r\n");
          out.write("  ");
 if(totCount > Integer.parseInt(recs)) {
          out.write("\r\n");
          out.write("\t\t<a href=\"javascript:callAction(0)\">First</a>\r\n");
          out.write("\t\t<a href=\"javascript:callAction(");
          out.print((dispLast-1)*intRecs);
          out.write(")\">Last</a>\r\n");
          out.write("\t\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
          out.write("\t\t\t");
 if(intOffSet+intRecs < totCount){	
          out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t \r\n");
          out.write("\t\t\t\t<a href=\"javascript:callAction(");
          out.print(intOffSet+intRecs);
          out.write(")\">Next</a>\r\n");
          out.write("\t\t\t");
 } 
          out.write("\t\r\n");
          out.write("\t\t\t");
 if(intOffSet > 0) { 
          out.write("\r\n");
          out.write("\t\t\t\t<a href=\"javascript:callAction(");
          out.print(intOffSet-intRecs);
          out.write(")\">Previous</a>\r\n");
          out.write("\t\t\t");
 } 
          out.write("\r\n");
          out.write("\t\t\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n");
          out.write("\t\t\t(Displaying records ");
          out.print(intOffSet+1);
          out.write(" to ");
          out.print( (intOffSet+intRecs) > totCount ? totCount : (intOffSet+intRecs));
          out.write(" of ");
          out.print(totCount);
          out.write(" matching the search criteria)\r\n");
          out.write("\t");
 } 
          out.write("\t\r\n");
          out.write("\r\n");
          out.write("\r\n");
          //  logic:notEmpty
          org.apache.struts.taglib.logic.NotEmptyTag _jspx_th_logic_notEmpty_0 = (org.apache.struts.taglib.logic.NotEmptyTag) _jspx_tagPool_logic_notEmpty_name.get(org.apache.struts.taglib.logic.NotEmptyTag.class);
          _jspx_th_logic_notEmpty_0.setPageContext(_jspx_page_context);
          _jspx_th_logic_notEmpty_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
          _jspx_th_logic_notEmpty_0.setName(Constants.PTP_DETAILS);
          int _jspx_eval_logic_notEmpty_0 = _jspx_th_logic_notEmpty_0.doStartTag();
          if (_jspx_eval_logic_notEmpty_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            do {
              out.write("\r\n");
              out.write("  <table>\r\n");
              out.write("      <tr></tr><tr></tr><tr></tr><tr></tr>\r\n");
              out.write("      <tr>\r\n");
              out.write("      <!--   <td class=\"type-whrite\" align=\"left\" >\r\n");
              out.write("\t  \t     <input type=\"button\" value=\"SelectAll\"   onclick=\"checkFunction();\"/>\r\n");
              out.write("\t    \t </td>\r\n");
              out.write("\t   -->\r\n");
              out.write("             <td class=\"type-whrite\" align=\"left\" >\r\n");
              out.write("\t   \t\t      <input type=\"button\" value=\"Delete\"  onclick=\"deleteTP();\"/>\r\n");
              out.write("\t         </td>\r\n");
              out.write("\t   </tr>\t\r\n");
              out.write("  </table  \r\n");
              out.write("\r\n");
              out.write("\r\n");
              out.write(" </table>\r\n");
              out.write(" ");
              int evalDoAfterBody = _jspx_th_logic_notEmpty_0.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
          }
          if (_jspx_th_logic_notEmpty_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
            return;
          _jspx_tagPool_logic_notEmpty_name.reuse(_jspx_th_logic_notEmpty_0);
          out.write("\r\n");
          out.write("<div id=\"footer\"> \r\n");
          out.write("  <table width=\"100%\" height=\"24\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\r\n");
          out.write("    <tr> \r\n");
          out.write("      <td width=\"100%\" height=\"24\" bgcolor=\"#D0D0FF\" class=\"td-menu\" align=\"left\">&nbsp;&nbsp;Copyright 2005. \r\n");
          out.write("        Raining Data.</td>\r\n");
          out.write("    </tr>\r\n");
          out.write("  </table>\r\n");
          out.write("</div>\r\n");
          out.write("</form>\r\n");
          out.write("</body>\r\n");
          out.write("\r\n");
          int evalDoAfterBody = _jspx_th_html_html_0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_html_html_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
        return;
      _jspx_tagPool_html_html.reuse(_jspx_th_html_html_0);
      out.write('\r');
      out.write('\n');
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_html_rewrite_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  html:rewrite
    org.apache.struts.taglib.html.RewriteTag _jspx_th_html_rewrite_0 = (org.apache.struts.taglib.html.RewriteTag) _jspx_tagPool_html_rewrite_action_nobody.get(org.apache.struts.taglib.html.RewriteTag.class);
    _jspx_th_html_rewrite_0.setPageContext(_jspx_page_context);
    _jspx_th_html_rewrite_0.setParent(null);
    _jspx_th_html_rewrite_0.setAction("./dist/epedigree/ProcessedFaxes.do");
    int _jspx_eval_html_rewrite_0 = _jspx_th_html_rewrite_0.doStartTag();
    if (_jspx_th_html_rewrite_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_html_rewrite_action_nobody.reuse(_jspx_th_html_rewrite_0);
    return false;
  }

  private boolean _jspx_meth_logic_empty_0(javax.servlet.jsp.tagext.JspTag _jspx_th_html_html_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  logic:empty
    org.apache.struts.taglib.logic.EmptyTag _jspx_th_logic_empty_0 = (org.apache.struts.taglib.logic.EmptyTag) _jspx_tagPool_logic_empty_name.get(org.apache.struts.taglib.logic.EmptyTag.class);
    _jspx_th_logic_empty_0.setPageContext(_jspx_page_context);
    _jspx_th_logic_empty_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_html_html_0);
    _jspx_th_logic_empty_0.setName("result");
    int _jspx_eval_logic_empty_0 = _jspx_th_logic_empty_0.doStartTag();
    if (_jspx_eval_logic_empty_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\r\n");
        out.write("`\t\t\t\t   <tr class='tableRow_Off'><td class='td-content' colspan='9' align='center'>There are no Pedigree Trading Partners...</td></tr>\r\n");
        out.write("\t\t\t");
        int evalDoAfterBody = _jspx_th_logic_empty_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_logic_empty_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_logic_empty_name.reuse(_jspx_th_logic_empty_0);
    return false;
  }

  private boolean _jspx_meth_bean_write_0(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_iterate_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_0 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_0.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_iterate_0);
    _jspx_th_bean_write_0.setName("ptp");
    _jspx_th_bean_write_0.setProperty("deaNumber");
    int _jspx_eval_bean_write_0 = _jspx_th_bean_write_0.doStartTag();
    if (_jspx_th_bean_write_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_0);
    return false;
  }

  private boolean _jspx_meth_bean_write_1(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_iterate_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_1 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_1.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_iterate_0);
    _jspx_th_bean_write_1.setName("ptp");
    _jspx_th_bean_write_1.setProperty("deaNumber");
    int _jspx_eval_bean_write_1 = _jspx_th_bean_write_1.doStartTag();
    if (_jspx_th_bean_write_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_1);
    return false;
  }

  private boolean _jspx_meth_bean_write_2(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_iterate_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_2 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_2.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_iterate_0);
    _jspx_th_bean_write_2.setName("ptp");
    _jspx_th_bean_write_2.setProperty("tradingPartnerName");
    int _jspx_eval_bean_write_2 = _jspx_th_bean_write_2.doStartTag();
    if (_jspx_th_bean_write_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_2);
    return false;
  }

  private boolean _jspx_meth_bean_write_3(javax.servlet.jsp.tagext.JspTag _jspx_th_logic_iterate_0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  bean:write
    org.apache.struts.taglib.bean.WriteTag _jspx_th_bean_write_3 = (org.apache.struts.taglib.bean.WriteTag) _jspx_tagPool_bean_write_property_name_nobody.get(org.apache.struts.taglib.bean.WriteTag.class);
    _jspx_th_bean_write_3.setPageContext(_jspx_page_context);
    _jspx_th_bean_write_3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_logic_iterate_0);
    _jspx_th_bean_write_3.setName("ptp");
    _jspx_th_bean_write_3.setProperty("deaNumber");
    int _jspx_eval_bean_write_3 = _jspx_th_bean_write_3.doStartTag();
    if (_jspx_th_bean_write_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE)
      return true;
    _jspx_tagPool_bean_write_property_name_nobody.reuse(_jspx_th_bean_write_3);
    return false;
  }
}
