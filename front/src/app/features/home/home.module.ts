import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HomeComponent } from './home.component';


import { ButtonModule } from 'primeng/button';
import { CalendarModule } from 'primeng/calendar';
import { FileUploadModule } from 'primeng/fileupload';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';


@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ButtonModule,
    CalendarModule,
    InputTextModule,
    FileUploadModule,
    TableModule,
    ToastModule
  ],
  providers: [
    MessageService
  ],
  exports: [
    HomeComponent
  ]
})
export class HomeModule { }
