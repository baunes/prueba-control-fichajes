import { Component } from '@angular/core';
import { MessageService } from 'primeng/api';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { RecordsRangeDTO } from 'src/app/model/records/RecordsRangeDTO';
import { RecordsDayDTO } from 'src/app/model/records/RecordsDayDTO';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  title = 'control-fichajes';
  uploadedFiles: any[] = [];
  importUrl: string = environment.BASE_URI + '/records/import';

  employeeId: string;
  rangeDates: Date[];

  records: RecordsDayDTO[] = [];

  private queryUrl = (employeeId, fromDate, toDate) => `${environment.BASE_URI}/records/${employeeId}/${fromDate}/${toDate}`;

  constructor(
    private messageService: MessageService,
    private httpClient: HttpClient
  ) { }

  onUpload(event) {
    for (let file of event.files) {
      this.uploadedFiles.push(file);
    }

    this.messageService.add({ key: 'my-toast', severity: 'info', summary: 'File Imported', detail: '' });
  }

  onError(event) {
    this.messageService.add({ key: 'my-toast', severity: 'error', summary: 'Error importing file', detail: event.error.message });
  }

  listRecords(events) {
    if (!this.employeeId) {
      this.messageService.add({ key: 'my-toast', severity: 'warn', summary: 'Must introduce an Employee Id', detail: '' });
      return;
    }
    if (!this.rangeDates || this.rangeDates.length !== 2 || this.rangeDates[0] == null || this.rangeDates[1] == null) {
      this.messageService.add({ key: 'my-toast', severity: 'warn', summary: 'Must select a valid date range', detail: '' });
      return;
    }
    this.queryRecords(this.employeeId, this.rangeDates[0], this.rangeDates[1]);
  }

  private queryRecords(employeeId: string, fromDate: Date, toDate: Date) {
    this.httpClient.get(this.queryUrl(employeeId, this.parseDate(fromDate), this.parseDate(toDate)))
      .subscribe((response: RecordsRangeDTO) => {
        this.records = response.days;
      });
  }

  private parseDate(date: Date) {
    return new Date(date.getTime() - date.getTimezoneOffset() * 60000).toISOString().substring(0, 10);
  }

  formatDateFromTimestamp(timestamp: number) {
    return new Date(timestamp * 1000);

  }

  formatDateFromArray(values: number[]) {
    if (!!values && values.length === 3) {
      return new Date(values[0], values[1], values[2]);
    }
    return null;
  }

}
