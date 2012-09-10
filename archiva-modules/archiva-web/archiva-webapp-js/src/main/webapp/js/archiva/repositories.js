/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
define("archiva.repositories",["jquery","i18n","jquery.tmpl","bootstrap","jquery.validate","knockout","knockout.simpleGrid"],
       function() {

  // FIXME this must be dynamic if we do a plugin mechanism with dynamic repositories types
  // FIXME i18n

  ManagedRepositoryType=function(type,label){
    this.type=type;
    this.label=label;
  }

  window.managedRepositoryTypes = [
            new ManagedRepositoryType("default","Maven 2.x Repository"),
            new ManagedRepositoryType("legacy", "Maven 1.x Repository")
            ];

  ManagedRepository=function(id,name,layout,indexDirectory,location,snapshots,releases,blockRedeployments,cronExpression,
                             scanned,daysOlder,retentionCount,deleteReleasedSnapshots,stageRepoNeeded){

    var self=this;

    //private String id;
    this.id=ko.observable(id);
    this.id.subscribe(function(newValue){self.modified(true)});

    //private String name;
    this.name=ko.observable(name);
    this.name.subscribe(function(newValue){self.modified(true)});

    //private String layout = "default";
    this.layout=ko.observable(layout);
    this.layout.subscribe(function(newValue){self.modified(true)});

    //private String indexDirectory;
    this.indexDirectory=ko.observable(indexDirectory);
    this.indexDirectory.subscribe(function(newValue){self.modified(true)});

    //private String location;
    this.location=ko.observable(location);
    this.location.subscribe(function(newValue){self.modified(true)});

    //private String cronExpression = "0 0 * * * ?";
    this.cronExpression=ko.observable(cronExpression);
    this.cronExpression.subscribe(function(newValue){self.modified(true)});

    //private ManagedRepository stagingRepository;

    //private int daysOlder = 100;
    this.daysOlder=ko.observable(daysOlder);
    this.daysOlder.subscribe(function(newValue){self.modified(true)});

    //private int retentionCount = 2;
    this.retentionCount=ko.observable(retentionCount);
    this.retentionCount.subscribe(function(newValue){self.modified(true)});

    //private boolean scanned = false;
    this.scanned=ko.observable(scanned);
    this.scanned.subscribe(function(newValue){self.modified(true)});

    //private boolean deleteReleasedSnapshots;
    this.deleteReleasedSnapshots=ko.observable(deleteReleasedSnapshots);
    this.deleteReleasedSnapshots.subscribe(function(newValue){self.modified(true)});

    //private boolean stageRepoNeeded;
    this.stageRepoNeeded=ko.observable(stageRepoNeeded);
    this.stageRepoNeeded.subscribe(function(newValue){self.modified(true)});

    //private boolean snapshots = false;
    this.snapshots=ko.observable(snapshots);
    this.snapshots.subscribe(function(newValue){self.modified(true)});

    //private boolean releases = true;
    this.releases=ko.observable(releases);
    this.releases.subscribe(function(newValue){self.modified(true)});

    //private boolean blockRedeployments = false;
    this.blockRedeployments=ko.observable(blockRedeployments);
    this.blockRedeployments.subscribe(function(newValue){self.modified(true)});



    this.getTypeLabel=function(){
      for(var i=0;i<window.managedRepositoryTypes.length;i++){
        if (window.managedRepositoryTypes[i].type==self.layout()){
          return window.managedRepositoryTypes[i].label;
        }
      }
      return "no label";
    }

    this.modified=ko.observable(false);
  }

  ArchivaRepositoryStatistics=function(scanEndTime,scanStartTime,totalArtifactCount,totalArtifactFileSize,totalFileCount,
                                       totalGroupCount,totalProjectCount,newFileCount,duration,managedRepository,lastScanDate){
    //private Date scanEndTime;
    this.scanEndTime = ko.observable(scanEndTime);

    //private Date scanStartTime;
    this.scanStartTime = ko.observable(scanStartTime);

    //private long totalArtifactCount;
    this.totalArtifactCount = ko.observable(totalArtifactCount);

    //private long totalArtifactFileSize;
    this.totalArtifactFileSize = ko.observable(totalArtifactFileSize);

    //private long totalFileCount;
    this.totalFileCount = ko.observable(totalFileCount);

    //private long totalGroupCount;
    this.totalGroupCount = ko.observable(totalGroupCount);

    //private long totalProjectCount;
    this.totalProjectCount = ko.observable(totalProjectCount);

    //private long newFileCount;
    this.newFileCount = ko.observable(newFileCount);

    this.duration = ko.observable(duration);

    this.managedRepository = managedRepository;

    this.lastScanDate=ko.observable(lastScanDate);
  }

  mapManagedRepositories=function(data){
    var mappedManagedRepositories = $.map(data, function(item) {
      return mapManagedRepository(item);
    });
    return mappedManagedRepositories;
  }
  mapManagedRepository=function(data){
    if (data==null){
      return null;
    }
    return new ManagedRepository(data.id,data.name,data.layout,data.indexDirectory,data.location,data.snapshots
                                 ,data.releases,
                                 data.blockRedeployments,data.cronExpression,
                                 data.scanned,data.daysOlder,data.retentionCount,data.deleteReleasedSnapshots,
                                 data.stageRepoNeeded);
  }

  mapArchivaRepositoryStatistics=function(data){
    if (data==null){
      return null;
    }
    return new ArchivaRepositoryStatistics(data.scanEndTime,data.scanStartTime,data.totalArtifactCount,data.totalArtifactFileSize,
                                           data.totalFileCount,data.totalGroupCount,data.totalProjectCount,data.newFileCount,
                                           data.duration,data.managedRepository,data.lastScanDate)
  }

  ManagedRepositoryViewModel=function(managedRepository, update, managedRepositoriesViewModel){
    this.managedRepository=managedRepository;
    this.managedRepositoriesViewModel = managedRepositoriesViewModel;
    this.update = update;

    var self = this;

    this.availableLayouts = window.managedRepositoryTypes;

    this.save=function(){
      var valid = $("#main-content").find("#managed-repository-edit-form").valid();
      if (valid==false) {
          return;
      }
      $.log("save:"+this.managedRepository.name());
      clearUserMessages();
      if (this.update){
        $.ajax("restServices/archivaServices/managedRepositoriesService/updateManagedRepository",
          {
            type: "POST",
            data: ko.toJSON(this.managedRepository),
            contentType: 'application/json',
            dataType: 'json',
            success: function(data) {
              displaySuccessMessage($.i18n.prop('managedrepository.updated',self.managedRepository.id()));
              activateManagedRepositoriesGridTab();
              self.managedRepository.modified(false);
            },
            error: function(data) {
              var res = $.parseJSON(data.responseText);
              displayRestError(res);
            }
          }
        );
      } else {
        var url="restServices/archivaServices/managedRepositoriesService/fileLocationExists";
        url+="?fileLocation="+encodeURIComponent(self.managedRepository.location());
        $.ajax(url,
        {
          type: "GET",
          dataType: 'json',
          success: function(data) {
            if (data){
              var completeCallbackFn = function(){window.modalConfirmDialog.modal('hide')};
              openDialogConfirm(
                  function(){addManagedRepository(self.managedRepository,completeCallbackFn)},
                  $.i18n.prop('ok'), $.i18n.prop('cancel'),
                  $.i18n.prop('managedrepository.add.title'),
                  $("#managed-repository-location-warning-tmpl").tmpl(self.managedRepository));
            }else{
              addManagedRepository(self.managedRepository);
            }
          }
        });
      }
    }

    addManagedRepository=function(managedRepository,completeCallbackFn){
      var curManagedRepository=managedRepository;
      var callbackFn = completeCallbackFn;
      $.ajax("restServices/archivaServices/managedRepositoriesService/addManagedRepository",
        {
          type: "POST",
          contentType: 'application/json',
          data: ko.toJSON(managedRepository),
          dataType: 'json',
          success: function(data) {
            curManagedRepository.location(data.location);
            self.managedRepositoriesViewModel.managedRepositories.push(curManagedRepository);
            displaySuccessMessage($.i18n.prop('managedrepository.added',curManagedRepository.id()));
            curManagedRepository.modified(false);
            activateManagedRepositoriesGridTab();
          },
          error: function(data) {
            var res = $.parseJSON(data.responseText);
            displayRestError(res);
          },
          complete:function(data){
            if(callbackFn){
              callbackFn();
            }
          }
        }
      );
    }

    displayGrid=function(){
      activateManagedRepositoriesGridTab();
    }

  }

  activateManagedRepositoryFormValidation=function(){
    var validator = $("#main-content" ).find("#managed-repository-edit-form").validate({
      rules: {
        daysOlder : {
          digits: true,
          min: 1
        },
        retentionCount : {
          digits: true,
          min: 1,
          max: 100
        },
        cronExpression: {
          required: true,
          remote: {
            url: "restServices/archivaServices/commonServices/validateCronExpression",
            type: "get"
          }
        },
        id: {
          required: true,
          remote: {
            url: "restServices/archivaUiServices/dataValidatorService/managedRepositoryIdNotExists",
            type: "get"
          }
        }
      },
      showErrors: function(validator, errorMap, errorList) {
        customShowError("#main-content #managed-repository-edit-form",validator,errorMap,errorMap);
      }
    });
    validator.settings.messages["cronExpression"]=$.i18n.prop("cronExpression.notvalid");
    validator.settings.messages["id"]=$.i18n.prop("id.required.or.alreadyexists");
  }

  ManagedRepositoriesViewModel=function(){
    this.managedRepositories=ko.observableArray([]);

    this.gridViewModel = null;
    var self = this;

    editManagedRepository=function(managedRepository){
      var mainContent = $("#main-content");
      var viewModel = new ManagedRepositoryViewModel(managedRepository,true,self);
      ko.applyBindings(viewModel,mainContent.find("#managed-repository-edit").get(0));
      activateManagedRepositoryEditTab();
      mainContent.find("#managed-repository-edit-li a").html($.i18n.prop('edit'));
      activateManagedRepositoryFormValidation();
    }

    scanNow=function(managedRepository){
      clearUserMessages();
      openDialogConfirm(
          function(){
            $("#dialog-confirm-modal" ).find("#modal-login-footer").append(smallSpinnerImg());
            var checked = $("#managed-repository-scan-now-all").get(0).checked;
            var url = "restServices/archivaServices/repositoriesService/scanRepositoryNow?";
            url += "repositoryId="+encodeURIComponent(managedRepository.id());
            url += "&fullScan="+(checked==true?"true":"false");
            $.ajax(url,
              {
                type: "GET",
                beforeSend:function(){
                  displayInfoMessage($.i18n.prop("managedrepository.scan.started",managedRepository.id()));
                  closeDialogConfirm();
                },
                success: function(data) {
                  displaySuccessMessage($.i18n.prop("managedrepository.scanned",managedRepository.name()));
                },
                error: function(data) {
                  var res = $.parseJSON(data.responseText);
                  displayRestError(res);
                },
                complete: function(){
                  removeSmallSpinnerImg();
                  closeDialogConfirm();
                }
              }
            );
          },
          $.i18n.prop("ok"),
          $.i18n.prop("cancel"),
          $.i18n.prop("managedrepository.scan.now"),
          $("#managed-repository-scan-now-modal-tmpl").tmpl(managedRepository));

    }

    removeManagedRepository=function(managedRepository){
      clearUserMessages();
      openDialogConfirm(
          function(){
            $("#dialog-confirm-modal").find("#modal-login-footer").append(smallSpinnerImg());
            var url = "restServices/archivaServices/managedRepositoriesService/deleteManagedRepository?";
            url += "repositoryId="+encodeURIComponent(managedRepository.id());

            var checked = $("#managedrepository-deletecontent").get(0).checked;

            url += "&deleteContent="+(checked==true?"true":"false");
            $.ajax(url,
              {
                type: "GET",
                  success: function(data) {
                    self.managedRepositories.remove(managedRepository);
                    displaySuccessMessage($.i18n.prop("managedrepository.deleted",managedRepository.name()));

                  },
                  error: function(data) {
                    var res = $.parseJSON(data.responseText);
                    displayRestError(res);
                  },
                  complete: function(){
                    removeSmallSpinnerImg();
                    closeDialogConfirm();
                  }
              }
            );

          },
          $.i18n.prop("ok"),
          $.i18n.prop("cancel"),
          $.i18n.prop("managedrepository.delete.confirm",managedRepository.name()),
          $("#managed-repository-delete-warning-tmpl").tmpl(managedRepository));
    }

    updateManagedRepository=function(managedRepository){
      var managedRepositoryViewModel = new ManagedRepositoryViewModel(managedRepository,true,this);
      managedRepositoryViewModel.save();
    }

    this.bulkSave=function(){
      $.log("bulkSave");
      return getModifiedManagedRepositories().length>0;
    }

    getModifiedManagedRepositories=function(){
      var prx = $.grep(self.managedRepositories(),
          function (managedRepository,i) {
            return managedRepository.modified();
          });
      return prx;
    }
    updateModifiedManagedRepositories=function(){
      var repos = getModifiedManagedRepositories();

      openDialogConfirm(function(){
                          for (i=0;i<repos.length;i++){
                            updateManagedRepository(repos[i]);
                          }
                          closeDialogConfirm();
                        },
                        $.i18n.prop('ok'),
                        $.i18n.prop('cancel'),
                        $.i18n.prop('managed.repository.bulk.save.confirm.title'),
                        $.i18n.prop('managed.repository.bulk.save.confirm',repos.length));
    }

    directoriesScan=function(managedRepository){
      $.log("directoriesScan:"+managedRepository.id());
      var url = "restServices/archivaServices/repositoriesService/scanRepositoryDirectoriesNow/"+managedRepository.id();
      $.ajax(url,
        {
          type: "GET",
          dataType: 'json',
          beforeSend:function(){
            displayInfoMessage($.i18n.prop("managedrepository.scan.directories.started", managedRepository.id()));
          },
          success: function(data) {
            $.log(" scanRepositoryDirectoriesNow finished ");
            displaySuccessMessage( $.i18n.prop("managedrepository.scan.directories.finished", managedRepository.id()));
          },
          error: function(data) {
            var res = $.parseJSON(data.responseText);
            displayRestError(res);
          },
          complete: function(){
            removeSmallSpinnerImg();
          }

        }
      );
    }

    showStats=function(managedRepository){
      if ($(calculatePopoverId(managedRepository)).html()){
        // we ask stats all the time ? if no uncomment return
        //return;
        $("#managedrepository-stats-"+escapeDot(managedRepository.id())).append(smallSpinnerImg());
      }
      var curRepo=managedRepository;
      var url = "restServices/archivaServices/managedRepositoriesService/getManagedRepositoryStatistics/"+managedRepository.id();
      url+="/"+encodeURIComponent(usedLang());
      $.ajax(url,
        {
          type: "GET",
          dataType: 'json',
          success: function(data) {
            if (data==null){
              return;
            }
            var archivaRepositoryStatistics=mapArchivaRepositoryStatistics(data);
            archivaRepositoryStatistics.managedRepository=curRepo;
            var mainContent = $("#main-content");
            mainContent.find("#managedrepository-stats-"+escapeDot(curRepo.id())).append($("#managed-repository-stats-tmpl").tmpl(archivaRepositoryStatistics));
            mainContent.find("#managedrepository-stats-img-"+escapeDot(curRepo.id())).attr("data-content",$(calculatePopoverId(curRepo)).html());
            mainContent.find("#managedrepository-stats-img-"+escapeDot(curRepo.id())).popover(
                {
                  placement: "left",
                  html: true
                }
            );

            mainContent.find("#managedrepository-stats-img-"+escapeDot(curRepo.id())).popover('show');
            removeSmallSpinnerImg();
          },
          error: function(data) {
            var res = $.parseJSON(data.responseText);
            displayRestError(res);
          },
          complete: function(){
           }
        }
      );
    }

    calculatePopoverId=function(managedRepository){
      return "#main-content #managedrepository-stats-"+escapeDot(managedRepository.id()) + " #managedrepository-stats-"+escapeDot(managedRepository.id())+"-popover";
    }

    hideStats=function(managedRepository){
      $("#body_content" ).find(".popover" ).hide();
    }

    showPomSnippet=function(managedRepository){
      var mainContent = $("#main-content");
      mainContent.find("#managed-repositories-pom-snippet").html(mediumSpinnerImg());
      mainContent.find('#managed-repositories-pom-snippet').show();
      var url = "restServices/archivaServices/managedRepositoriesService/getPomSnippet/"+encodeURIComponent(managedRepository.id());
      $.ajax(url,
        {
          type: "GET",
          dataType: 'text',
          success: function(data) {
            $("#managed-repositories-pom-snippet").html($("#pom-snippet-tmpl").tmpl(data));
          },
          error: function(data) {
            var res = $.parseJSON(data.responseText);
            displayRestError(res);
          },
          complete: function(){
          }
        }
      );

    }



  }

  activateManagedRepositoriesGridTab=function(){
    var mainContent = $("#main-content");
    mainContent.find("#managed-repository-edit-li").removeClass("active");
    mainContent.find("#managed-repository-edit").removeClass("active");

    mainContent.find("#managed-repositories-view-li").addClass("active");
    mainContent.find("#managed-repositories-view").addClass("active");
    mainContent.find("#managed-repository-edit-li a").html($.i18n.prop("add"));
  }

  activateManagedRepositoryEditTab=function(){
    var mainContent = $("#main-content");
    mainContent.find("#managed-repositories-view-li").removeClass("active");
    mainContent.find("#managed-repositories-view").removeClass("active");

    mainContent.find("#managed-repository-edit-li").addClass("active");
    mainContent.find("#managed-repository-edit").addClass("active");
  }


  //---------------------------
  // Remote repositories part
  //---------------------------



  RemoteRepository=function(id,name,layout,indexDirectory,url,userName,password,timeout,downloadRemoteIndex,remoteIndexUrl,
                            remoteDownloadNetworkProxyId,cronExpression,remoteDownloadTimeout,downloadRemoteIndexOnStartup){

    var self=this;

    //private String id;
    this.id=ko.observable(id);
    this.id.subscribe(function(newValue){self.modified(true)});

    //private String name;
    this.name=ko.observable(name);
    this.name.subscribe(function(newValue){self.modified(true)});

    //private String layout = "default";
    this.layout=ko.observable(layout);
    this.layout.subscribe(function(newValue){self.modified(true)});

    //private String indexDirectory;
    this.indexDirectory=ko.observable(indexDirectory);
    this.indexDirectory.subscribe(function(newValue){self.modified(true)});

    //private String url;
    this.url=ko.observable(url);
    this.url.subscribe(function(newValue){self.modified(true)});

    //private String userName;
    this.userName=ko.observable(userName);
    this.userName.subscribe(function(newValue){self.modified(true)});

    //private String password;
    this.password=ko.observable(password);
    this.password.subscribe(function(newValue){self.modified(true)});

    //private int timeout = 60;
    this.timeout=ko.observable(timeout);
    this.timeout.subscribe(function(newValue){self.modified(true)});

    //private boolean downloadRemoteIndex = false;
    this.downloadRemoteIndex=ko.observable(downloadRemoteIndex);
    this.downloadRemoteIndex.subscribe(function(newValue){self.modified(true)});

    //private String remoteIndexUrl = ".index";
    this.remoteIndexUrl=ko.observable(remoteIndexUrl);
    this.remoteIndexUrl.subscribe(function(newValue){self.modified(true)});

    //private String remoteDownloadNetworkProxyId;
    this.remoteDownloadNetworkProxyId=ko.observable(remoteDownloadNetworkProxyId);
    this.remoteDownloadNetworkProxyId.subscribe(function(newValue){self.modified(true)});

    //private String cronExpression = "0 0 08 ? * SUN";
    this.cronExpression=ko.observable(cronExpression);
    this.cronExpression.subscribe(function(newValue){self.modified(true)});

    //private int remoteDownloadTimeout = 300;
    this.remoteDownloadTimeout=ko.observable(remoteDownloadTimeout);
    this.remoteDownloadTimeout.subscribe(function(newValue){self.modified(true)});

    //private boolean downloadRemoteIndexOnStartup = false;
    this.downloadRemoteIndexOnStartup=ko.observable(downloadRemoteIndexOnStartup);
    this.downloadRemoteIndexOnStartup.subscribe(function(newValue){self.modified(true)});

    this.getTypeLabel=function(){
      for(var i=0;i<window.managedRepositoryTypes.length;i++){
        if (window.managedRepositoryTypes[i].type==self.layout()){
          return window.managedRepositoryTypes[i].label;
        }
      }
      return "no label";
    }

    this.modified=ko.observable(false);
  }

  mapRemoteRepository=function(data){
    if (data==null){
      return null;
    }
    return new RemoteRepository(data.id,data.name,data.layout,data.indexDirectory,data.url,data.userName,data.password,
                                data.timeout,data.downloadRemoteIndex,data.remoteIndexUrl,data.remoteDownloadNetworkProxyId,
                                data.cronExpression,data.remoteDownloadTimeout,data.downloadRemoteIndexOnStartup);
  }

  mapRemoteRepositories=function(data){
    var mappedRemoteRepositories = $.map(data, function(item) {
      return mapRemoteRepository(item);
    });
    return mappedRemoteRepositories;
  }

  RemoteRepositoryViewModel=function(remoteRepository, update, remoteRepositoriesViewModel){
    this.remoteRepository=remoteRepository;
    this.remoteRepositoriesViewModel = remoteRepositoriesViewModel;
    this.networkProxies=ko.observableArray([]);
    this.update = update;

    var self = this;

    this.availableLayouts = window.managedRepositoryTypes;

    this.save=function(){
      var valid = $("#main-content" ).find("#remote-repository-edit-form").valid();
      if (valid==false) {
        return;
      }
      clearUserMessages();
      if (update){
        $.ajax("restServices/archivaServices/remoteRepositoriesService/updateRemoteRepository",
          {
            type: "POST",
            data: ko.toJSON(this.remoteRepository),
            contentType: 'application/json',
            dataType: 'json',
            success: function(data) {
              displaySuccessMessage($.i18n.prop('remoterepository.updated',self.remoteRepository.id()));
              activateRemoteRepositoriesGridTab();
              self.remoteRepository.modified(false);
            },
            error: function(data) {
              var res = $.parseJSON(data.responseText);
              displayRestError(res);
            }
          }
        );
      }else {
        $.ajax("restServices/archivaServices/remoteRepositoriesService/addRemoteRepository",
          {
            type: "POST",
            data: ko.toJSON(this.remoteRepository),
            contentType: 'application/json',
            dataType: 'json',
            success: function(data) {
              self.remoteRepository.modified(false);
              self.remoteRepositoriesViewModel.remoteRepositories.push(self.remoteRepository);
              displaySuccessMessage($.i18n.prop('remoterepository.added'));
              activateRemoteRepositoriesGridTab();
            },
            error: function(data) {
              var res = $.parseJSON(data.responseText);
              displayRestError(res);
            }
          }
        );
      }
    }

    displayGrid=function(){
      activateRemoteRepositoriesGridTab();
    }
  }

  RemoteRepositoriesViewModel=function(){
    this.remoteRepositories=ko.observableArray([]);
    this.gridViewModel = null;
    var self = this;

    editRemoteRepository=function(remoteRepository){
      $.log("editRemoteRepository");
      $.ajax("restServices/archivaServices/networkProxyService/getNetworkProxies", {
          type: "GET",
          dataType: 'json',
          success: function(data) {
            var viewModel = new RemoteRepositoryViewModel(remoteRepository,true,self);
            viewModel.networkProxies(mapNetworkProxies(data));
            var mainContent = $("#main-content");
            ko.applyBindings(viewModel,mainContent.find("#remote-repository-edit").get(0));
            activateRemoteRepositoryEditTab();
            mainContent.find("#remote-repository-edit-li a").html($.i18n.prop('edit'));
            activateRemoteRepositoryFormValidation();
          }
      })

    }

    removeRemoteRepository=function(remoteRepository){
      clearUserMessages();
      openDialogConfirm(
          function(){$.ajax("restServices/archivaServices/remoteRepositoriesService/deleteRemoteRepository/"+encodeURIComponent(remoteRepository.id()),
                  {
                    type: "GET",
                    success: function(data) {
                      self.remoteRepositories.remove(remoteRepository);
                      displaySuccessMessage($.i18n.prop('remoterepository.deleted'));
                    },
                    error: function(data) {
                      var res = $.parseJSON(data.responseText);
                      displayRestError(res);
                    },
                    complete:function(){
                      closeDialogConfirm();
                    }
                  }
                )}, $.i18n.prop('ok'),
                $.i18n.prop('cancel'),
                $.i18n.prop('remoterepository.delete.confirm',remoteRepository.id()),
                $("#remote-repository-delete-modal-tmpl").tmpl(remoteRepository));

    }

    this.bulkSave=function(){
      return getModifiedRemoteRepositories().length>0;
    }

    getModifiedRemoteRepositories=function(){
      var prx = $.grep(self.remoteRepositories(),
          function (remoteRepository,i) {
            return remoteRepository.modified();
          });
      return prx;
    }

    updateModifiedRemoteRepositories=function(){
      var modifiedRemoteRepositories = getModifiedRemoteRepositories();

      openDialogConfirm(function(){
                          for(var i=0;i<modifiedRemoteRepositories.length;i++){
                            updateRemoteRepository(modifiedRemoteRepositories[i]);
                          }
                          closeDialogConfirm();
                        },
                        $.i18n.prop('ok'),
                        $.i18n.prop('cancel'),
                        $.i18n.prop('remoterepositories.bulk.save.confirm.title'),
                        $.i18n.prop('remoterepositories.bulk.save.confirm',modifiedRemoteRepositories.length));
    }

    updateRemoteRepository=function(remoteRepository){
      var viewModel = new RemoteRepositoryViewModel(remoteRepository,true,self);
      viewModel.save();
    }

    scheduleDownloadRemoteIndex=function(remoteRepository){
      openDialogConfirm(
        function(){

          var url = "restServices/archivaServices/repositoriesService/scheduleDownloadRemoteIndex?";
          url += "repositoryId="+encodeURIComponent(remoteRepository.id());

          var now = $("#remoterepository-scan-now").get(0).checked;

          var full = $("#remoterepository-scan-full").get(0).checked;

          url += "&now="+(now==true?"true":"false");
          url += "&fullDownload="+(full==true?"true":"false");
          $.ajax(url,
            {
              type: "GET",
                success: function(data) {
                  clearUserMessages();
                  displaySuccessMessage($.i18n.prop("remoterepository.download.remote.scheduled",remoteRepository.name()));
                },
                error: function(data) {
                  var res = $.parseJSON(data.responseText);
                  displayRestError(res);
                },
                complete: function(){
                  closeDialogConfirm();
                }
            }
          );

        },
        $.i18n.prop("ok"),
        $.i18n.prop("cancel"),
        $.i18n.prop("remoterepository.download.remote.confirm",remoteRepository.name()),
        $("#remote-repository-scan-modal-tmpl").tmpl(remoteRepository));
    }
  }

  activateRemoteRepositoryFormValidation=function(){
    // FIXME find a way to activate cronExpression validation only if downloadRemote is activated !
    var validator = $("#main-content" ).find("#remote-repository-edit-form").validate({
      rules: {
        id: {
          required: true,
          remote: {
            url: "restServices/archivaUiServices/dataValidatorService/remoteRepositoryIdNotExists",
            type: "get"
          }
        }
      },
      showErrors: function(validator, errorMap, errorList) {
        customShowError("#main-content #remote-repository-edit-form",validator,errorMap,errorMap);
      }
    });
    validator.settings.messages["cronExpression"]=$.i18n.prop("cronExpression.notvalid");
    validator.settings.messages["id"]=$.i18n.prop("id.required.or.alreadyexists");
  }

  activateRemoteRepositoriesGridTab=function(){
    var mainContent = $("#main-content");
    mainContent.find("#remote-repository-edit-li").removeClass("active");
    mainContent.find("#remote-repository-edit").removeClass("active");

    mainContent.find("#remote-repositories-view-li").addClass("active");
    mainContent.find("#remote-repositories-view").addClass("active");
    mainContent.find("#remote-repository-edit-li a").html($.i18n.prop("add"));
  }

  activateRemoteRepositoryEditTab=function(){
    var mainContent = $("#main-content");
    mainContent.find("#remote-repositories-view-li").removeClass("active");
    mainContent.find("#remote-repositories-view").removeClass("active");

    mainContent.find("#remote-repository-edit-li").addClass("active");
    mainContent.find("#remote-repository-edit").addClass("active");
  }

  //---------------------------
  // Screen loading
  //---------------------------

  displayRepositoriesGrid=function(){
    screenChange();
    var mainContent = $("#main-content");
    mainContent.html(mediumSpinnerImg());
    mainContent.html($("#repositoriesMain").tmpl());
    mainContent.find("#repositories-tabs a:first").tab("show");

    mainContent.find("#managed-repositories-content").append(mediumSpinnerImg());
    mainContent.find("#remote-repositories-content").append(mediumSpinnerImg());

    var managedRepositoriesViewModel = new ManagedRepositoriesViewModel();
    var remoteRepositoriesViewModel = new RemoteRepositoriesViewModel();

    loadManagedRepositories(function(data) {
      managedRepositoriesViewModel.managedRepositories(mapManagedRepositories(data));
      managedRepositoriesViewModel.gridViewModel = new ko.simpleGrid.viewModel({
        data: managedRepositoriesViewModel.managedRepositories,
        columns: [
          {
            headerText: $.i18n.prop('identifier'),
            rowText: "id"
          },
          {
            headerText: $.i18n.prop('name'),
            rowText: "name"
          },
          {
          headerText: $.i18n.prop('type'),
          rowText: "getTypeLabel",
          // FIXME i18n
          title: "Repository type (default is Maven 2)"
          }
        ],
        pageSize: 5,
        gridUpdateCallBack: function(){
          $("#main-content" ).find("#managed-repositories-table").find("[title]").tooltip();
        }
      });
      var mainContent = $("#main-content");
      ko.applyBindings(managedRepositoriesViewModel,mainContent.find("#managed-repositories-view").get(0));

      mainContent.find("#managed-repositories-pills #managed-repositories-view-a").tab('show');
      removeMediumSpinnerImg("#main-content #managed-repositories-content");
      activateManagedRepositoriesGridTab();
    });

    loadRemoteRepositories(function(data) {
      remoteRepositoriesViewModel.remoteRepositories(mapRemoteRepositories(data));
      remoteRepositoriesViewModel.gridViewModel = new ko.simpleGrid.viewModel({
        data: remoteRepositoriesViewModel.remoteRepositories,
        columns: [
          {
            headerText: $.i18n.prop('identifier'),
            rowText: "id"
          },
          {
            headerText: $.i18n.prop('name'),
            rowText: "name"
          },
          {
            headerText: $.i18n.prop('url'),
            rowText: "url"
          },
          {
          headerText: $.i18n.prop('type'),
          rowText: "getTypeLabel",
          // FIXME i18n
          title: "Repository type (default is Maven 2)"
          }
        ],
        pageSize: 5,
        gridUpdateCallBack: function(){
          $("#main-content" ).find("#remote-repositories-table").find("[title]").tooltip();
        }
      });
      var mainContent = $("#main-content");
      ko.applyBindings(remoteRepositoriesViewModel,mainContent.find("#remote-repositories-view").get(0));
      mainContent.find("#remote-repositories-pills #remote-repositories-view-a").tab('show')
      removeMediumSpinnerImg("#main-content #remote-repositories-content");
    });



    mainContent.find("#managed-repositories-pills").on('show', function (e) {
      var mainContent = $("#main-content");
      if ($(e.target).attr("href")=="#managed-repository-edit") {
        var viewModel = new ManagedRepositoryViewModel(new ManagedRepository(),false,managedRepositoriesViewModel);
        ko.applyBindings(viewModel,mainContent.find("#managed-repository-edit").get(0));
        activateManagedRepositoryFormValidation();
      }
      if ($(e.target).attr("href")=="#managed-repositories-view") {
        mainContent.find("#managed-repository-edit-li a").html($.i18n.prop("add"));
      }

    });

    mainContent.find("#remote-repositories-pills").on('show', function (e) {
      if ($(e.target).attr("href")=="#remote-repository-edit") {
        $.ajax("restServices/archivaServices/networkProxyService/getNetworkProxies", {
            type: "GET",
            dataType: 'json',
            success: function(data) {
              var viewModel = new RemoteRepositoryViewModel(new RemoteRepository(),false,remoteRepositoriesViewModel);
              viewModel.networkProxies(mapNetworkProxies(data));
              ko.applyBindings(viewModel,$("#main-content" ).find("#remote-repository-edit").get(0));
              activateRemoteRepositoryFormValidation();
            }
        })
      }
      if ($(e.target).attr("href")=="#remote-repositories-view") {
        $("#main-content" ).find("#remote-repository-edit-li" ).find("a").html($.i18n.prop("add"));
      }

    });

  }

  loadManagedRepositories=function(successCallBackFn,errorCallBackFn){
    $.ajax("restServices/archivaServices/managedRepositoriesService/getManagedRepositories", {
        type: "GET",
        dataType: 'json',
        success: successCallBackFn,
        error: errorCallBackFn
    });
  }

  loadRemoteRepositories=function(successCallBackFn,errorCallBackFn){
    $.ajax("restServices/archivaServices/remoteRepositoriesService/getRemoteRepositories", {
        type: "GET",
        dataType: 'json',
        success: successCallBackFn,
        error: errorCallBackFn
    });
  }

  findManagedRepository=function(id,managedRepositories){
    var managedRepository=$.grep(managedRepositories,
                                    function(repo,idx){
                                      return repo.id()==id;
                                    }
                          );
    return ($.isArray(managedRepository) && managedRepository.length>0) ? managedRepository[0]:new ManagedRepository();
  }

});