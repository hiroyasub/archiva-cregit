begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|//package org.apache.maven.archiva.consumers.lucene;
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
comment|//import org.apache.maven.archiva.database.updater.DatabaseCleanupConsumer;
end_comment

begin_comment
comment|//import org.apache.maven.archiva.model.ArchivaArtifact;
end_comment

begin_comment
comment|//import org.apache.maven.archiva.model.ArchivaArtifactModel;
end_comment

begin_comment
comment|//import org.codehaus.plexus.spring.PlexusInSpringTestCase;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|///**
end_comment

begin_comment
comment|// * LuceneCleanupRemoveIndexedConsumerTest
end_comment

begin_comment
comment|// *
end_comment

begin_comment
comment|// * @version
end_comment

begin_comment
comment|// */
end_comment

begin_comment
comment|//public class LuceneCleanupRemoveIndexedConsumerTest
end_comment

begin_comment
comment|//    extends PlexusInSpringTestCase
end_comment

begin_comment
comment|//{
end_comment

begin_comment
comment|//    private DatabaseCleanupConsumer luceneCleanupRemoveIndexConsumer;
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void setUp()
end_comment

begin_comment
comment|//        throws Exception
end_comment

begin_comment
comment|//    {
end_comment

begin_comment
comment|//        super.setUp();
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        luceneCleanupRemoveIndexConsumer = (DatabaseCleanupConsumer)
end_comment

begin_comment
comment|//            lookup( DatabaseCleanupConsumer.class, "lucene-cleanup" );
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void testIfArtifactExists()
end_comment

begin_comment
comment|//        throws Exception
end_comment

begin_comment
comment|//    {
end_comment

begin_comment
comment|//        ArchivaArtifact artifact = createArtifact(
end_comment

begin_comment
comment|//              "org.apache.maven.archiva", "archiva-lucene-cleanup", "1.0", "jar" );
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        luceneCleanupRemoveIndexConsumer.processArchivaArtifact( artifact );
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    public void testIfArtifactDoesNotExist()
end_comment

begin_comment
comment|//        throws Exception
end_comment

begin_comment
comment|//    {
end_comment

begin_comment
comment|//        ArchivaArtifact artifact = createArtifact(
end_comment

begin_comment
comment|//              "org.apache.maven.archiva", "deleted-artifact", "1.0", "jar" );
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        luceneCleanupRemoveIndexConsumer.processArchivaArtifact( artifact );
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//    private ArchivaArtifact createArtifact( String groupId, String artifactId, String version, String type )
end_comment

begin_comment
comment|//    {
end_comment

begin_comment
comment|//        ArchivaArtifactModel model = new ArchivaArtifactModel();
end_comment

begin_comment
comment|//        model.setGroupId( groupId );
end_comment

begin_comment
comment|//        model.setArtifactId( artifactId );
end_comment

begin_comment
comment|//        model.setVersion( version );
end_comment

begin_comment
comment|//        model.setType( type );
end_comment

begin_comment
comment|//        model.setRepositoryId( "test-repo" );
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//        return new ArchivaArtifact( model );
end_comment

begin_comment
comment|//    }
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//}
end_comment

end_unit

