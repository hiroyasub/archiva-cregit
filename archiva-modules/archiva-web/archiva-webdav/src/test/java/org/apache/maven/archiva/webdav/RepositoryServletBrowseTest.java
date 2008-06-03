begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|//package org.apache.maven.archiva.webdav;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///*
end_comment

begin_comment
comment|// * Licensed to the Apache Software Foundation (ASF) under one
end_comment

begin_comment
comment|// * or more contributor license agreements.  See the NOTICE file
end_comment

begin_comment
comment|// * distributed with this work for additional information
end_comment

begin_comment
comment|// * regarding copyright ownership.  The ASF licenses this file
end_comment

begin_comment
comment|// * to you under the Apache License, Version 2.0 (the
end_comment

begin_comment
comment|// * "License"); you may not use this file except in compliance
end_comment

begin_comment
comment|// * with the License.  You may obtain a copy of the License at
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// *  http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// * Unless required by applicable law or agreed to in writing,
end_comment

begin_comment
comment|// * software distributed under the License is distributed on an
end_comment

begin_comment
comment|// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
end_comment

begin_comment
comment|// * KIND, either express or implied.  See the License for the
end_comment

begin_comment
comment|// * specific language governing permissions and limitations
end_comment

begin_comment
comment|// * under the License.
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import com.meterware.httpunit.GetMethodWebRequest;
end_comment

begin_comment
comment|//import com.meterware.httpunit.WebLink;
end_comment

begin_comment
comment|//import com.meterware.httpunit.WebRequest;
end_comment

begin_comment
comment|//import com.meterware.httpunit.WebResponse;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import java.io.File;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//import javax.servlet.http.HttpServletResponse;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// * RepositoryServletBrowseTest
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>
end_comment

begin_comment
comment|// * @version $Id$
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public class RepositoryServletBrowseTest
end_comment

begin_comment
comment|//    extends AbstractRepositoryServletTestCase
end_comment

begin_comment
comment|//{
end_comment

begin_comment
comment|//    public void testBrowse()
end_comment

begin_comment
comment|//        throws Exception
end_comment

begin_comment
comment|//    {
end_comment

begin_comment
comment|//        new File( repoRootInternal, "org/apache/archiva" ).mkdirs();
end_comment

begin_comment
comment|//        new File( repoRootInternal, "net/sourceforge" ).mkdirs();
end_comment

begin_comment
comment|//        new File( repoRootInternal, "commons-lang" ).mkdirs();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        WebRequest request = new GetMethodWebRequest( "http://machine.com/repository/internal/" );
end_comment

begin_comment
comment|//        WebResponse response = sc.getResponse( request );
end_comment

begin_comment
comment|//        assertEquals( "Response", HttpServletResponse.SC_OK, response.getResponseCode() );
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        // dumpResponse( response );
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        WebLink links[] = response.getLinks();
end_comment

begin_comment
comment|//        String expectedLinks[] = new String[] { "./commons-lang/", "./net/", "./org/" };
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        assertEquals( "Links.length", expectedLinks.length, links.length );
end_comment

begin_comment
comment|//        for ( int i = 0; i< links.length; i++ )
end_comment

begin_comment
comment|//        {
end_comment

begin_comment
comment|//            assertEquals( "Link[" + i + "]", expectedLinks[i], links[i].getURLString() );
end_comment

begin_comment
comment|//        }
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//}
end_comment

end_unit

