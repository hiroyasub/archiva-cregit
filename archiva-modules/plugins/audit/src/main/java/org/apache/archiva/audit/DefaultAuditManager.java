begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|audit
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|model
operator|.
name|facets
operator|.
name|AuditEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepositoryException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"auditManager#default"
argument_list|)
specifier|public
class|class
name|DefaultAuditManager
implements|implements
name|AuditManager
block|{
specifier|private
specifier|static
specifier|final
name|int
name|NUM_RECENT_EVENTS
init|=
literal|10
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultAuditManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|TimeZone
name|UTC_TIME_ZONE
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"UTC"
argument_list|)
decl_stmt|;
annotation|@
name|Inject
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|AuditEvent
argument_list|>
name|getMostRecentAuditEvents
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
init|)
block|{
comment|// TODO: consider a more efficient implementation that directly gets the last ten from the content repository
name|List
argument_list|<
name|AuditRecord
argument_list|>
name|records
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repositoryId
range|:
name|repositoryIds
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
name|metadataRepository
operator|.
name|getMetadataFacets
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|AuditEvent
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|records
operator|.
name|add
argument_list|(
operator|new
name|AuditRecord
argument_list|(
name|repositoryId
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|records
argument_list|)
expr_stmt|;
name|records
operator|=
name|records
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|records
operator|.
name|size
argument_list|()
operator|<
name|NUM_RECENT_EVENTS
condition|?
name|records
operator|.
name|size
argument_list|()
else|:
name|NUM_RECENT_EVENTS
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AuditEvent
argument_list|>
name|events
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|records
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|AuditRecord
name|record
range|:
name|records
control|)
block|{
name|AuditEvent
name|auditEvent
init|=
operator|(
name|AuditEvent
operator|)
name|metadataRepository
operator|.
name|getMetadataFacet
argument_list|(
name|session
argument_list|,
name|record
operator|.
name|repositoryId
argument_list|,
name|AuditEvent
operator|.
name|FACET_ID
argument_list|,
name|record
operator|.
name|name
argument_list|)
decl_stmt|;
name|events
operator|.
name|add
argument_list|(
name|auditEvent
argument_list|)
expr_stmt|;
block|}
return|return
name|events
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addAuditEvent
parameter_list|(
name|MetadataRepository
name|repository
parameter_list|,
name|AuditEvent
name|event
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
init|)
block|{
comment|// ignore those with no repository - they will still be logged to the textual audit log
if|if
condition|(
name|event
operator|.
name|getRepositoryId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|.
name|addMetadataFacet
argument_list|(
name|session
argument_list|,
name|event
operator|.
name|getRepositoryId
argument_list|()
argument_list|,
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteAuditEvents
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
init|)
block|{
name|metadataRepository
operator|.
name|removeMetadataFacets
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|AuditEvent
operator|.
name|FACET_ID
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|AuditEvent
argument_list|>
name|getAuditEventsInRange
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|,
name|Date
name|startTime
parameter_list|,
name|Date
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
return|return
name|getAuditEventsInRange
argument_list|(
name|metadataRepository
argument_list|,
name|repositoryIds
argument_list|,
literal|null
argument_list|,
name|startTime
argument_list|,
name|endTime
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|AuditEvent
argument_list|>
name|getAuditEventsInRange
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|,
name|String
name|resource
parameter_list|,
name|Date
name|startTime
parameter_list|,
name|Date
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
init|)
block|{
name|List
argument_list|<
name|AuditEvent
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repositoryId
range|:
name|repositoryIds
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|metadataRepository
operator|.
name|getMetadataFacets
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|AuditEvent
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|list
control|)
block|{
try|try
block|{
name|Date
name|date
init|=
name|createNameFormat
argument_list|()
operator|.
name|parse
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|startTime
operator|==
literal|null
operator|||
operator|!
name|date
operator|.
name|before
argument_list|(
name|startTime
argument_list|)
operator|)
operator|&&
operator|(
name|endTime
operator|==
literal|null
operator|||
operator|!
name|date
operator|.
name|after
argument_list|(
name|endTime
argument_list|)
operator|)
condition|)
block|{
name|AuditEvent
name|event
init|=
operator|(
name|AuditEvent
operator|)
name|metadataRepository
operator|.
name|getMetadataFacet
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|AuditEvent
operator|.
name|FACET_ID
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
operator|||
name|event
operator|.
name|getResource
argument_list|()
operator|.
name|startsWith
argument_list|(
name|resource
argument_list|)
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Invalid audit event found in the metadata repository: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
comment|// continue and ignore this one
block|}
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|results
argument_list|,
operator|new
name|Comparator
argument_list|<
name|AuditEvent
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|AuditEvent
name|o1
parameter_list|,
name|AuditEvent
name|o2
parameter_list|)
block|{
return|return
name|o2
operator|.
name|getTimestamp
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o1
operator|.
name|getTimestamp
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
block|}
specifier|private
specifier|static
name|SimpleDateFormat
name|createNameFormat
parameter_list|()
block|{
name|SimpleDateFormat
name|fmt
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|AuditEvent
operator|.
name|TIMESTAMP_FORMAT
argument_list|)
decl_stmt|;
name|fmt
operator|.
name|setTimeZone
argument_list|(
name|UTC_TIME_ZONE
argument_list|)
expr_stmt|;
return|return
name|fmt
return|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|AuditRecord
implements|implements
name|Comparable
argument_list|<
name|AuditRecord
argument_list|>
block|{
specifier|private
name|String
name|repositoryId
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|public
name|AuditRecord
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compareTo
parameter_list|(
name|AuditRecord
name|other
parameter_list|)
block|{
comment|// reverse ordering
return|return
name|other
operator|.
name|name
operator|.
name|compareTo
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
specifier|public
name|RepositorySessionFactory
name|getRepositorySessionFactory
parameter_list|( )
block|{
return|return
name|repositorySessionFactory
return|;
block|}
specifier|public
name|void
name|setRepositorySessionFactory
parameter_list|(
name|RepositorySessionFactory
name|repositorySessionFactory
parameter_list|)
block|{
name|this
operator|.
name|repositorySessionFactory
operator|=
name|repositorySessionFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

