begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
operator|.
name|web
operator|.
name|actionmapper
operator|.
name|test
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|webwork
operator|.
name|dispatcher
operator|.
name|mapper
operator|.
name|ActionMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
operator|.
name|web
operator|.
name|action
operator|.
name|test
operator|.
name|stub
operator|.
name|HttpServletRequestStub
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
operator|.
name|web
operator|.
name|actionmapper
operator|.
name|RepositoryProxyActionMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusTestCase
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryProxyActionMapperTest
extends|extends
name|PlexusTestCase
block|{
name|RepositoryProxyActionMapper
name|actionMapper
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|actionMapper
operator|=
operator|new
name|RepositoryProxyActionMapper
argument_list|()
expr_stmt|;
block|}
comment|// TODO: uncomment once we know how to make the default action mapper work using stubs
comment|//    public void testDefaultActionMapping()
comment|//    throws Exception
comment|//    {
comment|//        ActionMapping mapping = actionMapper.getMapping( new DefaultActionMapperRequestStub() );
comment|//
comment|//        String expectedNamespace = "test";
comment|//        String expectedName = "test";
comment|//
comment|//        assertNotNull( "ActionMapping is null", mapping );
comment|//        assertNotNull( "namespace is null", mapping.getNamespace() );
comment|//        assertNotNull( "name is null", mapping.getName() );
comment|//        assertTrue( "invalid namespace: " + mapping.getNamespace(), mapping.getNamespace().equals( expectedNamespace ) );
comment|//        assertTrue( "invalid name: " + mapping.getName(), mapping.getName().equals( expectedName ) );
comment|//    }
specifier|public
name|void
name|testRepositoryProxyActionMapping
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testDir
init|=
name|getBasedir
argument_list|()
operator|+
literal|"/target/test-classes/unit/proxy-test"
decl_stmt|;
name|actionMapper
operator|.
name|setConfigfile
argument_list|(
name|testDir
operator|+
literal|"/maven-proxy-complete.conf"
argument_list|)
expr_stmt|;
name|ActionMapping
name|mapping
init|=
name|actionMapper
operator|.
name|getMapping
argument_list|(
operator|new
name|HttpServletRequestStub
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|expectedName
init|=
literal|"proxy"
decl_stmt|;
name|String
name|expectedFile
init|=
literal|"org/sometest/artifact-0.0.jar"
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"ActionMapping is null"
argument_list|,
name|mapping
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"name is null"
argument_list|,
name|mapping
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|mappingName
init|=
name|mapping
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|requestedFile
init|=
operator|(
name|String
operator|)
name|mapping
operator|.
name|getParams
argument_list|()
operator|.
name|get
argument_list|(
literal|"requestedFile"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"invalid name: "
operator|+
name|mappingName
argument_list|,
name|mappingName
operator|.
name|equals
argument_list|(
name|expectedName
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"invalid parameter: "
operator|+
name|requestedFile
argument_list|,
name|requestedFile
operator|.
name|equals
argument_list|(
name|expectedFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// do nothing
block|}
block|}
end_class

end_unit

