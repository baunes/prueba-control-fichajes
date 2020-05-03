import { RecordType } from './RecordType';
import { ServiceType } from './ServiceType';

export interface RecordDTO {
    businessId: string;
    date: string;
    employeeId: string;
    recordType: RecordType;
    serviceId: string;
    type: ServiceType;
}
