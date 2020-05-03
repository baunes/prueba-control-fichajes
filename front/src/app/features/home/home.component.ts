import { Component } from '@angular/core';
import { MessageService } from 'primeng/api';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  title = 'control-fichajes';
  uploadedFiles: any[] = [];
  importUrl: string = environment.BASE_URI + '/records/import';

  constructor(private messageService: MessageService) { }



  onUpload(event) {
    for (let file of event.files) {
      this.uploadedFiles.push(file);
    }

    this.messageService.add({ key: 'my-toast', severity: 'info', summary: 'File Imported', detail: '' });
  }

  onError(event) {
    this.messageService.add({ key: 'my-toast', severity: 'error', summary: 'Error importing file', detail: event.error.message });
  }

}
