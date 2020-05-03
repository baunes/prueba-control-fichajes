import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';

import { FileUploadModule } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';


@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    FileUploadModule,
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
