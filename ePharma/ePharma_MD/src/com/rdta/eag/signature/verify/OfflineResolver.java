/********************************************************************************

* Raining Data Corp.

*

* Copyright (c) Raining Data Corp. All Rights Reserved.

*

* This software is confidential and proprietary information belonging to

* Raining Data Corp. It is the property of Raining Data Corp. and is protected

* under the Copyright Laws of the United States of America. No part of this

* software may be copied or used in any form without the prior

* written permission of Raining Data Corp.

*

*********************************************************************************/

 

/*
 * Copyright  1999-2004 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.rdta.eag.signature.verify;



import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.apache.xml.security.utils.resolver.ResourceResolverSpi;
import org.apache.xml.utils.URI;
import org.w3c.dom.Attr;


/**
 * This class helps us home users to resolve http URIs without a network
 * connection
 *
 * @author $Author: sandhya $
 */
public class OfflineResolver extends ResourceResolverSpi {

   /** {@link org.apache.commons.logging} logging facility */
    static org.apache.commons.logging.Log log = 
        org.apache.commons.logging.LogFactory.getLog(OfflineResolver.class.getName());

   /**
    * Method engineResolve
    *
    * @param uri
    * @param BaseURI
    *
    * @throws ResourceResolverException
    */
   public XMLSignatureInput engineResolve(Attr uri, String BaseURI)
           throws ResourceResolverException {

      try {
         String URI = uri.getNodeValue();

         if (OfflineResolver._uriMap.containsKey(URI)) {
            String newURI = (String) OfflineResolver._uriMap.get(URI);

            log.debug("Mapped " + URI + " to " + newURI);

            InputStream is = new FileInputStream(newURI);

            log.debug("Available bytes = " + is.available());

            XMLSignatureInput result = new XMLSignatureInput(is);

            // XMLSignatureInput result = new XMLSignatureInput(inputStream);
            result.setSourceURI(URI);
            result.setMIMEType((String) OfflineResolver._mimeMap.get(URI));

            return result;
         } else {
            Object exArgs[] = {
               "The URI " + URI + " is not configured for offline work" };

            throw new ResourceResolverException("generic.EmptyMessage", exArgs,
                                                uri, BaseURI);
         }
      } catch (IOException ex) {
         throw new ResourceResolverException("generic.EmptyMessage", ex, uri,
                                             BaseURI);
      }
   }

   /**
    * We resolve http URIs <I>without</I> fragment...
    *
    * @param uri
    * @param BaseURI
    *
    */
   public boolean engineCanResolve(Attr uri, String BaseURI) {

      String uriNodeValue = uri.getNodeValue();

      if (uriNodeValue.equals("") || uriNodeValue.startsWith("#")) {
         return false;
      }

      try {
         URI uriNew = new URI(new URI(BaseURI), uri.getNodeValue());

         if (uriNew.getScheme().equals("http")) {
            log.debug("I state that I can resolve " + uriNew.toString());

            return true;
         }

         log.debug("I state that I can't resolve " + uriNew.toString());
      } catch (URI.MalformedURIException ex) {}

      return false;
   }

   /** Field _uriMap */
   static Map _uriMap = null;

   /** Field _mimeMap */
   static Map _mimeMap = null;

   /**
    * Method register
    *
    * @param URI
    * @param filename
    * @param MIME
    */
   private static void register(String URI, String filename, String MIME) {
       
      OfflineResolver._uriMap.put(URI, filename);
      OfflineResolver._mimeMap.put(URI, MIME);
   }

   static {
      org.apache.xml.security.Init.init();
      

      OfflineResolver._uriMap = new HashMap();
      OfflineResolver._mimeMap = new HashMap();

      OfflineResolver.register("http://www.w3.org/TR/xml-stylesheet",
                               "C:/Security/data/org/w3c/www/TR/xml-stylesheet.html",
                               "text/html");
      OfflineResolver.register("http://www.w3.org/TR/2000/REC-xml-20001006",
                               "C:/Security/data/org/w3c/www/TR/2000/REC-xml-20001006",
                               "text/xml");
      OfflineResolver.register("http://www.nue.et-inf.uni-siegen.de/index.html",
                               "C:/Security/data/org/apache/xml/security/temp/nuehomepage",
                               "text/html");
      OfflineResolver.register(
         "http://www.nue.et-inf.uni-siegen.de/~geuer-pollmann/id2.xml",
         "C:/Security/data/org/apache/xml/security/temp/id2.xml", "text/xml");
      OfflineResolver.register(
         "http://xmldsig.pothole.com/xml-stylesheet.txt",
         "C:/Security/data/com/pothole/xmldsig/xml-stylesheet.txt", "text/xml");
      OfflineResolver.register(
         "http://www.w3.org/Signature/2002/04/xml-stylesheet.b64",
         "C:/Security/data/ie/baltimore/merlin-examples/merlin-xmldsig-twenty-three/xml-stylesheet.b64", "text/plain");
   }
}
