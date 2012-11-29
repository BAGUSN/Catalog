<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html >
 <head>
<title><@s.text name="label.manage.pages"/>- ${title} </title>
 <link rel="icon" type="image/gif" href="${base}/theme/default/favicon.gif"/>

    <style type="text/css" >
        @import "<@s.url value='/theme/default/css/style.css'/>";       
    </style>
   
  
  ${head}
 </head>

 <body >
 <div id="container">

  <#include "header.ftl"/>
  	${body}
  	

	<div id="leftbar">
	    <div class="actions">
	      <h2><@s.text name="label.actions"/></h2>
	      <div class="content">
	        <ul>
	          <li><a <#if actionIndex == 0> class="active"  </#if>  href="<@s.url action='page' ></@s.url>"><@s.text name="label.pages"/></a></li>
	          <li><a <#if actionIndex == 1> class="active"  </#if>  href="<@s.url action='page' method="input"></@s.url>"><@s.text name="label.create.page"/></a></li>          
		  	   <li><a <#if actionIndex == 2> class="active"  </#if>  href="<@s.url action='page.block' method="input"></@s.url>"><@s.text name="label.add.blocks"/></a></li>          
		   
		   </ul>
	       </div>
	    </div>
    </div>
	
<#include "footer.ftl"/>
  
    
 </body>
</html>
