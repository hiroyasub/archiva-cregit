-- mysql archiva --user=archiva --password=sa < drop_archiva_tables.sql

DROP TABLE IF EXISTS `ARCHIVAPROJECTMODEL_BUILDEXTENSIONS` ;
DROP TABLE IF EXISTS `ARCHIVAPROJECTMODEL_DEPENDENCIES` ;
DROP TABLE IF EXISTS `ARCHIVAPROJECTMODEL_DEPENDENCYMANAGEMENT` ;
DROP TABLE IF EXISTS `ARCHIVAPROJECTMODEL_INDIVIDUALS` ;
DROP TABLE IF EXISTS `ARCHIVAPROJECTMODEL_LICENSES` ;
DROP TABLE IF EXISTS `ARCHIVAPROJECTMODEL_MAILINGLISTS` ;
DROP TABLE IF EXISTS `ARCHIVAPROJECTMODEL_PLUGINS` ;
DROP TABLE IF EXISTS `ARCHIVAPROJECTMODEL_REPORTS` ;
DROP TABLE IF EXISTS `ARCHIVAPROJECTMODEL_REPOSITORIES` ;
DROP TABLE IF EXISTS `ARCHIVA_ARTIFACT` ;
DROP TABLE IF EXISTS `ARCHIVA_ARTIFACT_REFERENCE` ;
DROP TABLE IF EXISTS `ARCHIVA_CIMANAGEMENT` ;
DROP TABLE IF EXISTS `ARCHIVA_DEPENDENCY` ;
DROP TABLE IF EXISTS `ARCHIVA_EXCLUSIONS` ;
DROP TABLE IF EXISTS `ARCHIVA_INDIVIDUAL` ;
DROP TABLE IF EXISTS `ARCHIVA_ISSUE_MANAGEMENT` ;
DROP TABLE IF EXISTS `ARCHIVA_LICENSES` ;
DROP TABLE IF EXISTS `ARCHIVA_MAILING_LISTS` ;
DROP TABLE IF EXISTS `ARCHIVA_ORGANIZATION` ;
DROP TABLE IF EXISTS `ARCHIVA_PROJECT` ;
DROP TABLE IF EXISTS `ARCHIVA_PROJECT_REPOSITORIES` ;
DROP TABLE IF EXISTS `ARCHIVA_REPOSITORIES` ;
DROP TABLE IF EXISTS `ARCHIVA_REPOSITORY_STATS` ;
DROP TABLE IF EXISTS `ARCHIVA_SCM` ;
DROP TABLE IF EXISTS `ARCHIVA_VERSIONED_REFERENCE` ;
DROP TABLE IF EXISTS `DEPENDENCY_EXCLUSIONS` ;
DROP TABLE IF EXISTS `INDIVIDUAL_ROLES` ;
DROP TABLE IF EXISTS `MAILINGLIST_OTHERARCHIVES` ;
DROP TABLE IF EXISTS `SEQUENCE_TABLE` ;

