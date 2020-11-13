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
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateLoader, TranslateModule, TranslateCompiler } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateMessageFormatCompiler, MESSAGE_FORMAT_CONFIG } from 'ngx-translate-messageformat-compiler';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './modules/general/home/home.component';
import { ContactComponent } from './modules/general/contact/contact.component';
import { AboutComponent } from './modules/general/about/about.component';
import { NotFoundComponent } from './modules/general/not-found/not-found.component';
import { SidemenuComponent } from './modules/general/sidemenu/sidemenu.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { LoginComponent } from './modules/general/login/login.component';
import { ViewPermissionDirective } from './directives/view-permission.directive';
import { NavSubgroupDirective } from './directives/nav-subgroup.directive';
import { SearchComponent } from './modules/repo/search/search.component';
import { BrowseComponent } from './modules/repo/browse/browse.component';
import { UploadComponent } from './modules/repo/upload/upload.component';
import { ManageUsersComponent } from './modules/user/manage-users/manage-users.component';
import { ManageRolesComponent } from './modules/user/manage-roles/manage-roles.component';
import { SecurityConfigurationComponent } from './modules/user/security-configuration/security-configuration.component';
import { ManageUsersListComponent } from './modules/user/users/manage-users-list/manage-users-list.component';
import { ManageUsersAddComponent } from './modules/user/users/manage-users-add/manage-users-add.component';
import { NgbPaginationModule, NgbTooltipModule} from "@ng-bootstrap/ng-bootstrap";
import { PaginatedEntitiesComponent } from './modules/general/paginated-entities/paginated-entities.component';
import { SortedTableHeaderComponent } from './modules/general/sorted-table-header/sorted-table-header.component';
import { SortedTableHeaderRowComponent } from './modules/general/sorted-table-header-row/sorted-table-header-row.component';
import { ManageUsersEditComponent } from './modules/user/users/manage-users-edit/manage-users-edit.component';
import {ErrorHandlerModule} from "./modules/core/errors/error-handler.module";
import {CoreModule} from "./modules/core/core.module";


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ContactComponent,
    AboutComponent,
    NotFoundComponent,
    SidemenuComponent,
    LoginComponent,
    ViewPermissionDirective,
    NavSubgroupDirective,
    SearchComponent,
    BrowseComponent,
    UploadComponent,
    ManageUsersComponent,
    ManageRolesComponent,
    SecurityConfigurationComponent,
    ManageUsersListComponent,
    ManageUsersAddComponent,
    PaginatedEntitiesComponent,
    SortedTableHeaderComponent,
    SortedTableHeaderRowComponent,
    ManageUsersEditComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    TranslateModule.forRoot({
      compiler: {
        provide: TranslateCompiler,
        useClass: TranslateMessageFormatCompiler
      },
      loader: {
        provide: TranslateLoader,
        useFactory: httpTranslateLoader,
        deps: [HttpClient]
      }
    }),
      NgbPaginationModule,
      NgbTooltipModule,
      CoreModule
  ],
  providers: [
    { provide: MESSAGE_FORMAT_CONFIG, useValue: { locales: ['en', 'de'] }}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function httpTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http);
}