begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|key
package|;
end_package

begin_class
specifier|public
class|class
name|MetadataKey
block|{
specifier|private
name|String
name|groupId
decl_stmt|;
specifier|private
name|String
name|artifactId
decl_stmt|;
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|int
name|metadataKey
decl_stmt|;
specifier|public
name|MetadataKey
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|MetadataKey
parameter_list|()
block|{
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifactId
return|;
block|}
specifier|public
name|void
name|setArtifactId
parameter_list|(
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|int
name|getMetadataKey
parameter_list|()
block|{
return|return
name|metadataKey
return|;
block|}
specifier|public
name|void
name|setMetadataKey
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|metadataKey
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
block|}
end_class

end_unit

