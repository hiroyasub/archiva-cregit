/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {merge, Observable, Subject} from "rxjs";
import {debounceTime, distinctUntilChanged, map, mergeMap, pluck, share, startWith} from "rxjs/operators";
import {EntityService} from "../../../model/entity-service";
import {FieldToggle} from "../../../model/field-toggle";


/**
 * This component has a search field and pagination section. Entering data in the search field, or
 * a button click on the pagination triggers a call to a service method, that returns the entity data.
 * The service must implement the {@link EntityService} interface.
 *
 * The content is displayed between the search input and the pagination section. To use the data, you should
 * add an identifier and refer to the item$ variable:
 * ```
 * <app-paginated-entities #parent>
 *   <table>
 *       <tr ngFor="let entity in parent.item$ | async" >
 *           <td>{{entity.id}}</td>
 *       </tr>
 *   </table>
 * </app-paginated-entities>
 * ```
 *
 * @typeparam T The type of the retrieved entity elements.
 */
@Component({
    selector: 'app-paginated-entities',
    templateUrl: './paginated-entities.component.html',
    styleUrls: ['./paginated-entities.component.scss']
})
export class PaginatedEntitiesComponent<T> implements OnInit, FieldToggle, AfterViewInit {

    /**
     * This must be set, if you use the component. This service retrieves the entity data.
     */
    @Input() service: EntityService<T>;

    /**
     * The number of elements per page retrieved
     */
    @Input() pageSize = 10;

    /**
     * Two-Way-Binding attribute for sorting field
     */
    @Input() sortField = [];
    /**
     * Two-Way Binding attribute for sort order
     */
    @Input() sortOrder = "asc";

    /**
     * Pagination controls
     */
    @Input() pagination = {
        maxSize: 5,
        rotate: true,
        boundaryLinks: true,
        ellipses: false
    }

    /**
     * The current page that is selected
     */
    page = 1;
    /**
     * The current search term entered in the search field
     */
    searchTerm: string;

    /**
     * Event thrown, if the page value changes
     */
    @Output() pageChange: EventEmitter<number> = new EventEmitter<number>();
    /**
     * Event thrown, if the search term changes
     */
    @Output() searchTermChange: EventEmitter<string> = new EventEmitter<string>();

    @Output() sortFieldChange: EventEmitter<string[]> = new EventEmitter<string[]>();

    @Output() sortOrderChange: EventEmitter<string> = new EventEmitter<string>();

    /**
     * The total number of elements available for the given search term
     */
    total$: Observable<number>;
    /**
     * The entity items retrieved from the service
     */
    items$: Observable<T[]>;

    private pageStream: Subject<number> = new Subject<number>();
    private searchTermStream: Subject<string> = new Subject<string>();

    constructor() {
    }

    ngOnInit(): void {
        // We combine the sources for the page and the search input field to a observable 'source'
        const pageSource = this.pageStream.pipe(map(pageNumber => {
            return {search: this.searchTerm, page: pageNumber}
        }));
        const searchSource = this.searchTermStream.pipe(
            debounceTime(1000),
            distinctUntilChanged(),
            map(searchTerm => {
                this.searchTerm = searchTerm;
                return {search: searchTerm, page: 1}
            }));
        const source = merge(pageSource, searchSource).pipe(
            startWith({search: this.searchTerm, page: this.page}),
            mergeMap((params: { search: string, page: number }) => {
                return this.service(params.search, (params.page - 1) * this.pageSize, this.pageSize, this.sortField, this.sortOrder);
            }), share());
        this.total$ = source.pipe(pluck('pagination', 'total_count'));
        this.items$ = source.pipe(pluck('data'));
    }

    search(terms: string) {
        // console.log("Keystroke " + terms);
        this.searchTermChange.emit(terms);
        this.searchTermStream.next(terms)
    }

    changePage(pageNumber: number) {
        // console.log("Page change " +pageNumber);
        this.pageChange.emit(pageNumber);
        this.pageStream.next(pageNumber);
    }

    private compareArrays(a1: string[], a2: string[]) {
        let i = a1.length;
        while (i--) {
            if (a1[i] !== a2[i]) return false;
        }
        return true
    }

    toggleSortField(fieldName: string) {
        this.toggleField([fieldName]);
    }

    toggleField(fieldArray: string[]) {
        // console.log("Changing sort field " + fieldArray);
        let sortOrderChanged: boolean = false;
        let sortFieldChanged: boolean = false;
        if (!this.compareArrays(this.sortField, fieldArray)) {
          // console.log("Fields differ: " + this.sortField + " - " + fieldArray);
            this.sortField = fieldArray;
            if (this.sortOrder != 'asc') {
                this.sortOrder = 'asc';
                sortOrderChanged = true;
            }
            sortFieldChanged = true;
        } else {
            if (this.sortOrder == "asc") {
                this.sortOrder = "desc";
            } else {
                this.sortOrder = "asc";
            }
          // console.log("Toggled sort order: " + this.sortOrder);
            sortOrderChanged = true;
        }
        if (sortOrderChanged) {
          //console.log("Sort order changed: "+this.sortOrder)
            this.sortOrderChange.emit(this.sortOrder);
        }
        if (sortFieldChanged) {
            this.sortFieldChange.emit(this.sortField);
        }
        if (sortFieldChanged || sortOrderChanged) {
            this.page = 1;
            this.changePage(1);
        }
    }

    ngAfterViewInit(): void {
        // We emit the current value to push them to the containing reading components
        this.sortOrderChange.emit(this.sortOrder);
        this.sortFieldChange.emit(this.sortField);
    }

}
