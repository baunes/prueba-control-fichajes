import { DayType } from './DayType';
import { RecordDTO } from './RecordDTO';
import { DayAlarmDTO } from './DayAlarmDTO';

export interface RecordsDayDTO {
    dayType: DayType;
    date: string;
    records: RecordDTO[];
    workTime: number;
    restTime: number;
    alarms: DayAlarmDTO[];
}