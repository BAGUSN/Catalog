<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<logic:notPresent name="security_profile">
	<logic:forward name="sessionExpaired" />
</logic:notPresent>
<%@taglib prefix="html-el"  uri="http://struts.apache.org/tags-html-el" %>
<%@page import="com.snipl.ice.utility.UserUtility,java.util.HashMap"%>
<%@page import="com.snipl.ice.config.ICEEnv"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="assets/css/master.css" />
<script src="assets/js/popup.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>ICE Alert</title>
</head>

<body>
<div class="wrapper" id="page">
	<div class="header"></div>
    <div class="body_home">
    <!-- This is the Logo Division -->
    	<div class="logo"><h6>ice logo</h6></div> 
        <div class="remain_header">
        	<div class="alert_message"></div>
	        <div class="logout"><a href="./Logout.ice"><div class="text">Logout</div></a></div>
        </div>
	</div>
    <div class="body_home01">
    	<div class="parent">
        	<div class="margin">
            	<div class="parent_child">
                    <div class="child">
                    
                    <!-- Navigations -->
                        <div class="navigation">
                            <ul>
                                <li><div class="inactive_link_a"><a href="./Home.ice" class="hover">Home</a></div></li>
                                <li class="pipe">|</li>
                                <li class="active_prv"><div class="inactive_link_a"><a href="./ListIce.ice" class="hover">ICE</a></div></li>                               
                                <li class="bg_img_alert"><a href="./ViewReceivedAlertsDB.ice" class="hover"><div class="active_link_alert">Alerts</div></a></li>                                
                                <li class="active_next"><div class="inactive_link_c"><a href="./Community.ice" class="hover">Communities</a></div></li>
                                 <li class="pipe">|</li>
                                <li><div class="inactive_link_d"><a href="./Settings.ice" class="hover">Settings</a></div></li>                                
                                <li class="pipe">|</li>
                                <li><div class="inactive_link_f"><a href="./ICEHelp.ice" class="hover">Help</a></div></li>
                                <logic:present name="security_profile">
                                  	<logic:equal name="security_profile" value="0">
                                		 <li class="pipe">|</li>
                               			 <li><html:link href="./SendAlert.ice"><div class="inactive_link_e">
                                			Admin
                            			 </div></html:link></li>
                                	</logic:equal>
                                </logic:present>
                            </ul> 
                        </div>   
                        
                        <div class="middle_space">
                        	<div class="middle_space_left_corner"></div>
                        </div>
                        <div class="parent_body_content">
                        	<div class="body_content_top_border">
	                        	<div class="body_content">
                                    <div class="padd_left">
                                    	<div class="bound">
                                        	<%
	                                        	if(request.getParameter("action")!=null)
												{
	                                        		out.print("<iframe width='100%' height='100%' frameborder='0' src='./ViewReceivedInviteDB.ice'></iframe>");
												}
												else
												{
													out.print("<iframe width='100%' height='100%' frameborder='0' src='./ViewReceivedAlertsDB_Frame.ice'></iframe>");
												}
                                        	%>
                                        </div>
                                    </div>
                                </div>                            
                            </div>
                        </div>                        
                    </div>
				</div>                
        	</div>            
    	</div>                
        <div class="footer">
	      <div class="text">Copyright � 2007 - All Rights Reserved</div>
	      <div class="footer_links">
	      	<input type="hidden" value="<%=new UserUtility().getUserName(Integer.parseInt(session.getAttribute("security_id").toString()))%>" id="username" name="username"/>
	      	<input type="hidden" value="alert" id="source" name="source"/>
	        <ul>
	          <li><a href="javascript:feedback();" class="text">feedback</a></li>
	          <li>|</li>
	          <li><a href="javascript:reportbug();" class="text">report a bug</a></li>
	          <li>|</li>
	          <li style="padding:0px;"><a href="javascript:contactUs();" class="text">contact us</a></li>
	        </ul>
	      </div>
	    </div>
	</div>       
</div>
</body>
</html>
